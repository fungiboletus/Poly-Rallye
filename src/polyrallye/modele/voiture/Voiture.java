package polyrallye.modele.voiture;

import org.jdom.Element;

import polyrallye.ouie.liseuse.Liseuse;
import polyrallye.utilitaires.GestionXML;

public class Voiture {
	protected String nom;
	protected String version;
	protected String constructeur;

	protected int rarete;
	protected int prix;

	protected int debutDiffusion;
	protected int finDiffusion;

	protected Moteur moteur;

	protected Transmission transmission;

	protected Conduite conduite;

	protected Chassis chassis;

	protected Sources sources;

	public Voiture() {
		super();
	}

	public Voiture(Element noeud) {
		super();

		Element presentation = noeud.getChild("presentation");
		nom = presentation.getChildText("nom");
		version = presentation.getChildText("version");
		if (version == null) {
			version = "serie";
		}
		constructeur = presentation.getChildText("constructeur");

		Element economie = noeud.getChild("economie");
		prix = GestionXML.getInt(economie.getChildText("prix"));
		rarete = GestionXML.getInt(economie.getChildText("rarete"));

		Element periode = presentation.getChild("periode");
		debutDiffusion = GestionXML.getInt(periode.getAttributeValue("debut"));

		if (debutDiffusion == 0) {
			debutDiffusion = GestionXML.getInt(periode
					.getAttributeValue("annee"));
		}

		finDiffusion = GestionXML.getInt(periode.getAttributeValue("fin"));

		moteur = new Moteur(noeud.getChild("moteur"));
		transmission = new Transmission(moteur, noeud.getChild("transmission"));
		chassis = new Chassis(noeud.getChild("chassis"));
		sources = new Sources(noeud.getChild("sources"));
	}

	public String getNomComplet() {
		StringBuilder sb = new StringBuilder(constructeur);
		sb.append(" ");
		sb.append(nom);
		if (version != null) {
			sb.append(" ");
			sb.append(version);
		}
		return sb.toString();
	}

	public String getNom() {
		return nom;
	}

	public String getVersion() {
		return version;
	}

	public String getConstructeur() {
		return constructeur;
	}

	public int getRarete() {
		return rarete;
	}

	public int getPrix() {
		return prix;
	}

	public int getDebutDiffusion() {
		return debutDiffusion;
	}

	public int getFinDiffusion() {
		return finDiffusion;
	}

	public Moteur getMoteur() {
		return moteur;
	}

	public Transmission getTransmission() {
		return transmission;
	}

	public Chassis getChassis() {
		return chassis;
	}

	public Sources getSources() {
		return sources;
	}

	/**
	 * Calcule un score de la voiture.
	 * 
	 * Le score est calculé en fonction de certains paramètres de la voiture. La
	 * formule n'a rien de scientifique, le but est seulement d'obtenir un moyen
	 * fiable et pertinent pour classer les différentes voitures en fonction de
	 * leurs performances.
	 * 
	 * Une voiture 4x4 a un meilleur score qu'une voiture propulsion, qui a un
	 * meilleur score qu'une voiture traction. Plus la plage d'utilisation du
	 * moteur est élevée, plus le score est élevé. Plus le couple (important
	 * dans la formule), et la puissance (moins important) sont élevés, plus le
	 * score est élevé. Plus le poids de la voiture est élevé, plus le score est
	 * faible.
	 * 
	 * Le résultat est interpolé
	 * 
	 * @return Le score de la voiture
	 */
	public double getScore() {
		double score = (((transmission.type == TypeTransmission.QUATTRO) ? 1.2
				: ((transmission.type == TypeTransmission.TRACTION) ? 0.9 : 1.0))
				* (0.6 + (Math.abs(moteur.getRegimePuissanceMax()
						- moteur.getRegimeCoupleMax()) / 5000.0) * 0.42) * (moteur
				.getPuissanceMax() * 4.2 + Math.pow(moteur.getCoupleMax(), 1.3)))
				/ Math.pow((double) chassis.getPoids(), 1.2);
		
		// Maintenant, il faut faire une interpolation

		double xa = 0.01;
		double xb = 1.25;
		double ya = 20;
		double yb = 700;

		if (score >= xb) {
			xa = xb;
			xb = 2.5;
			ya = yb;
			yb = 900;
		}

		return ya + (score - xa)*((yb-ya)/(xb-xa));
		
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Voiture [ ");
		builder.append(getNomComplet());
		builder.append(" score : ");
		builder.append(getScore());
		builder.append(" prix : ");
		builder.append(prix);
		builder.append("€ ]");
		return builder.toString();
	}

	public void lireSpecifications() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Construite");

		if (debutDiffusion == finDiffusion || finDiffusion == 0) {
			sb.append(" en ");
			sb.append(debutDiffusion);
		} else {
			sb.append(" entre ");
			sb.append(debutDiffusion);
			sb.append(" et ");
			sb.append(finDiffusion);
		}
		
		Liseuse.lire(sb.toString());
		
		moteur.lireSpecifications();
		Liseuse.marquerPause();
		
		chassis.lireSpecifications();
		Liseuse.marquerPause();

		transmission.lireSpecifications();
		
	}

	public void ennoncerCategoriePerformances() {
		double score = getScore();

		if (score <= 0.2) {
			Liseuse.lire("Faibles performances");
		} else if (score <= 0.35) {
			Liseuse.lire("Performances moyennes");
		} else if (score <= 0.5) {
			Liseuse.lire("Bonnes performances");
		} else if (score <= 0.8) {
			Liseuse.lire("Très bonnes performances");
		} else {
			Liseuse.lire("Performances exceptionnelles");
		}
	}

	/**
	 * @return the conduite
	 */
	public Conduite getConduite() {
		return conduite;
	}

	/**
	 * @param conduite
	 *            the conduite to set
	 */
	public void setConduite(Conduite conduite) {
		this.conduite = conduite;
	}
}
