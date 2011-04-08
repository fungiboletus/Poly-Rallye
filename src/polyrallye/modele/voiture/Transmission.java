package polyrallye.modele.voiture;

import java.util.Arrays;

import org.jdom.Element;

import polyrallye.ouie.liseuse.Liseuse;
import polyrallye.utilitaires.GestionXML;

/**
 * @author Antoine Pultier
 * 
 *         Les informations sur les différentes boites de vitesses étant
 *         difficiles à trouver, et comme le jeu ne propose quand même pas un
 *         aspect de simulation poussé à son maximum, les spécificités des
 *         boites de vitesses sont générées uniquement à partir du nombre de
 *         rapports, et de la vitesse maximale.
 */
public class Transmission {
	/**
	 * Nombre de vitesses fournit par la voiture.
	 */
	protected int nbVitesses;

	/**
	 * Le rapport de vitesse sélectionné.
	 * 
	 * 0 correspond au point mort.
	 * 
	 * Les rapports vont de 0 au nombre de vitesses.
	 */
	protected int vitesseCourante;

	/**
	 * Type de transmission (4x4, traction, etc…)
	 */
	protected TypeTransmission type;

	/**
	 * Vitesse maximale de la voiture lorsque le moteur fournit sa puissance
	 * maximale.
	 */
	protected double vitessePuissanceMaximale;

	/**
	 * La transmission a un lien étroit avec le moteur.
	 * 
	 * Ça fait une référence croisée aussi.
	 */
	protected Moteur moteur;

	/**
	 * Rendement de transmission élevé, car on part du principe qu'il s'agit
	 * (pour la plupart des voitures) de pièces de compétition
	 */
	protected static double RENDEMENT = 0.90;

	/**
	 * Toutes les voitures ont la même boîte de vitesses, donc pourquoi pas les
	 * mêmes roues ?
	 * 
	 * Donnée en m
	 */
	protected static double RAYON_ROUE = 0.42;

	/**
	 * Vitesse maximale en première.
	 * 
	 * m/s
	 */
	protected static double VITESSE_MAX_PREMIERE = 10;

	/**
	 * Tableau des rapports de vitesses.
	 */
	protected double coefsRapports[];

	/**
	 * Constructeur de base.
	 * 
	 * La méthode calculerRapports doit être appelée suite à l'appel de ce
	 * constructeur.
	 * 
	 * @param moteur
	 *            Moteur lié à la transmission.
	 */
	public Transmission(Moteur moteur) {
		this.moteur = moteur;

		vitesseCourante = 0;

		// 200 Km/h de base, c'est pas mal non ?
		vitessePuissanceMaximale = 200.0;
	}

	/**
	 * Constructeur à partir d'un noeud XML.
	 * 
	 * @param moteur
	 *            Moteur lié à la transmission
	 * @param noeud
	 *            Noeud XML contenant les spécifications de la transmission
	 */
	public Transmission(Moteur moteur, Element noeud) {
		this(moteur);

		nbVitesses = GestionXML.getInt(noeud.getChildText("nb_rapports"));

		String sTypeTransmission = noeud.getChildText("type").toUpperCase();

		if (sTypeTransmission.equals("TRACTION")) {
			type = TypeTransmission.TRACTION;
		} else if (sTypeTransmission.equals("PROPULSION")) {
			type = TypeTransmission.PROPULSION;
		} else {
			type = TypeTransmission.QUATTRO;
		}

		Element vitesse = noeud.getChild("vitesse_puissance_max");

		if (vitesse != null) {
			vitessePuissanceMaximale = GestionXML.getInt(vitesse.getText());
		}
		
		calculerRapports();
	}

	/**
	 * Calcule des spécifités de chaque rapport de la boite de vitesse, à partir
	 * des informations contenues dans les données membres.
	 */
	public void calculerRapports() {
		coefsRapports = new double[nbVitesses + 1];

		// Au point mort, on a rien
		coefsRapports[0] = 0.0;

		// Conversion de la vitesse en km/h en m/s
		double vitesseMax = vitessePuissanceMaximale * 5 / 18;

		// Périmètre de roue :-) (en m)
		double perimetreRoue = 2 * RAYON_ROUE * Math.PI;

		// Combien la roue doit faire de tours par seconde pour attendre la
		// vitesse maximale ? (en hertzs)
		double nbToursMax = vitesseMax / perimetreRoue;

		// Pareil pour le minimum
		double nbToursMin = VITESSE_MAX_PREMIERE / perimetreRoue;

		// Régime moteur en hertzs.
		double regimeMoteur = moteur.getRegimePuissanceMax() / 60;

		// Calcul super compliqué des rapports
		double rapportMax = regimeMoteur / nbToursMax;
		double rapportMin = regimeMoteur / nbToursMin;

		// Pour l'instant, on sépare chaque rapport de manière linéaire
		// TODO: Penser à faire un truc non linéaire

		double intervale = (rapportMin - rapportMax) / (nbVitesses - 1);

		for (int i = 0; i < nbVitesses; ++i) {
			coefsRapports[i + 1] = rapportMin - intervale * i;
		}
	}

	/**
	 * Passage à la vitesse supèrieure.
	 * 
	 * Pas de protection anti-noob là. Il veut passer la vitesse, il la passe.
	 */
	public void passerVitesse() {
		if (vitesseCourante < nbVitesses) {
			++vitesseCourante;
		}
	}

	/**
	 * Passage à la vitesse inférieure.
	 * 
	 * Pas de protectection anti-noob non plus.
	 */
	public void retrograder() {
		if (vitesseCourante > 0) {
			--vitesseCourante;
		}
	}

	/**
	 * Récupère le coefficient de la vitesse courante
	 * 
	 * @return Coefficient de la vitesse courante
	 */
	public double getCoefCourant() {
		return coefsRapports[vitesseCourante];
	}

	/**
	 * Récupère le couple à la roue en fonction de la roue et du type de
	 * transmission.
	 * 
	 * @param pr
	 *            Roue
	 * @return Couple à la roue
	 */
	public double getCoupleParRoue(PositionRoue pr) {
		switch (type) {
		case PROPULSION:
			switch (pr) {
			case ARRIERE_DROITE:
			case ARRIERE_GAUCHE:
				return moteur.getCouple() * getCoefCourant() * RENDEMENT * 0.5;
			}
			break;
		case TRACTION:
			switch (pr) {
			case AVANT_DROITE:
			case AVANT_GAUCHE:
				return moteur.getCouple() * getCoefCourant() * RENDEMENT * 0.5;
			}
			break;
		case QUATTRO:
			// Une transmission 4x4 a un rendement plus faible.
			return moteur.getCouple() * getCoefCourant() * RENDEMENT
					* RENDEMENT * 0.25;
		}

		// Si on est là, c'est que la roue n'est pas motrice.
		return 0.0;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Transmission [nbVitesses=");
		builder.append(nbVitesses);
		builder.append(", vitesseCourante=");
		builder.append(vitesseCourante);
		builder.append(", ");
		if (type != null) {
			builder.append("type=");
			builder.append(type);
			builder.append(", ");
		}
		builder.append("vitessePuissanceMaximale=");
		builder.append(vitessePuissanceMaximale);
		builder.append(", ");
		if (coefsRapports != null) {
			builder.append("coefsRapports=");
			builder.append(Arrays.toString(coefsRapports));
		}
		builder.append("]");
		return builder.toString();
	}
	
	public void lireSpecifications() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Transmission aux ");
		switch (type) {
		case PROPULSION:
			sb.append("roues arrières");
			break;
		case TRACTION:
			sb.append("roues avant");
			break;
		case QUATTRO:
			sb.append("4 roues");
			break;
		}
		
		Liseuse.lire(sb.toString());
        Liseuse.marquerPause();
        sb = new StringBuilder();
        
		sb.append("Avec une boite de ");
		sb.append(nbVitesses);
		sb.append(" vitesses");
		
		Liseuse.lire(sb.toString());
	}

}
