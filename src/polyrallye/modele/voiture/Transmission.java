package polyrallye.modele.voiture;

import java.util.Arrays;

import org.jdom.Element;

import polyrallye.controlleur.Main;
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
	protected double vitessePuissanceMaximale = 0;

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
	protected static double RENDEMENT = 0.95;

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
	}

	// Définition d'un score
	public void etablirVitesseMaximale(double scoreVoiture) {
		// Interpolation linéaire, comme d'habitude
		
		final double xa = 120;
		final double xb = 900;
		
		final double ya = 160;
		final double yb = 300;
		
		vitessePuissanceMaximale = ya + (scoreVoiture - xa)*((yb-ya)/(xb-xa));
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
		final double vitesseMax = vitessePuissanceMaximale * 5 / 18;

		// Périmètre de roue :-) (en m)
		final double perimetreRoue = 2 * RAYON_ROUE * Math.PI;

		// Combien la roue doit faire de tours par seconde pour attendre la
		// vitesse maximale ? (en hertzs)
		final double nbToursMax = vitesseMax / perimetreRoue;

		// Pareil pour le minimum
		final double nbToursMin = VITESSE_MAX_PREMIERE / perimetreRoue;

		// Régime moteur en hertzs.
		final double regimeMoteur = moteur.getRegimePuissanceMax() / 60;

		// Calcul super compliqué des rapports
		final double rapportMin = regimeMoteur / nbToursMin;
		final double rapportMax = regimeMoteur / nbToursMax;

		// La différence de rapport suit une loi exponentielle
		// Le calcul se fait donc avec une propriété exponentielle, et bien évidemment, avec une interpolation linéaire
		
		// Coefficient déterminé à la main à partir de véritables boites de vitesses
		final double coeff = -0.4;
		
		final double expRapportMin = Math.exp(coeff);
		final double expRapportMax = Math.exp(nbVitesses*coeff);

		for (int i = 1; i <= nbVitesses; ++i) {
			coefsRapports[i] = rapportMin + (Math.exp(coeff*i) - expRapportMin)*((rapportMax-rapportMin)/(expRapportMax-expRapportMin));
		}
	}

	/**
	 * Passage à la vitesse supèrieure.
	 * 
	 * Pas de protection anti-noob là. Il veut passer la vitesse, il la passe.
	 */
	public boolean passerVitesse() {
		if (vitesseCourante < nbVitesses) {
			++vitesseCourante;
			return true;
		}
		return false;
	}

	/**
	 * Passage à la vitesse inférieure.
	 * 
	 * Pas de protectection anti-noob non plus.
	 */
	public boolean retrograder() {
		if (vitesseCourante > 1) {
			--vitesseCourante;
			return true;
		}
		return false;
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
		//Main.logLiseuse("couple moteur: "+moteur.getCouple());
		// Gestion d'une fausse vitesse en prise directe (plus rendement plus élevé)
		double rendement = (vitesseCourante+1) == nbVitesses/2 ? RENDEMENT : RENDEMENT*RENDEMENT;
		
		switch (type) {
		case PROPULSION:
			switch (pr) {
			case ARRIERE:
				return moteur.getCouple() * getCoefCourant() * rendement;
			}
			break;
		case TRACTION:
			switch (pr) {
			case AVANT:
				return moteur.getCouple() * getCoefCourant() * rendement;
			}
			break;
		case QUATTRO:
			// Une transmission 4x4 a un rendement plus faible.
			return moteur.getCouple() * getCoefCourant() * rendement * RENDEMENT * 0.5;
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

	public int getRapportCourant() {
		return vitesseCourante;
	}

	public double getVitessePuissanceMaximale() {
		return vitessePuissanceMaximale;
	}

	public void setPremiere() {
		vitesseCourante = 1;
	}
}
