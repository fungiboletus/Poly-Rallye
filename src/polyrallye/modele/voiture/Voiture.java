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
			debutDiffusion = GestionXML.getInt(periode.getAttributeValue("annee"));
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

	public double getScore() {
		return ((moteur.getPuissanceMax() * 1.75 + moteur.getCoupleMax() * moteur.getCoupleMax()) / ((double) chassis
				.getPoids() * 1.75))
				* ((transmission.type == TypeTransmission.QUATTRO) ? 1.2 : 1.0);
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
		Liseuse.lire(nom);
		Liseuse.lire("Construite par ");
		Liseuse.lire(constructeur);

		if (debutDiffusion == finDiffusion) {
			Liseuse.lire("en");
			Liseuse.lire(debutDiffusion);
		} else {
			Liseuse.lire(" entre ");
			Liseuse.lire(debutDiffusion);
			Liseuse.lire(" et ");
		}
		Liseuse.lire(finDiffusion);
		Liseuse.lire("Valeur de ");
		Liseuse.lire(prix);
		Liseuse.lire("pour une rareté de ");
		Liseuse.lire(rarete);
		Liseuse.marquerPause();
		moteur.lireSpecifications();
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
