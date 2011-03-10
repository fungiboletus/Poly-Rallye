package polyrallye.modele;

import org.jdom.Element;

public class Moteur {
	protected String nom;

	protected int cylindree;

	protected int nbCylindres;
	protected DispositionMoteur disposition;
	protected CompressionMoteur compression;

	protected int nbSoupapes;

	protected int puissanceMax;
	protected int regimePuissanceMax;

	protected int coupleMax;
	protected int regimeCoupleMax;

	protected int regimeRupteur;

	public Moteur() {

	}

	public Moteur(Element noeud) {
		nom = noeud.getAttributeValue("nom");

		Element configuration = noeud.getChild("configuration");
		Element puissance = noeud.getChild("puissance");
		Element couple_max = noeud.getChild("couple_max");

		cylindree = GestionXML.getInt(noeud.getChildText("cylindree"));

		nbCylindres = GestionXML.getInt(configuration
				.getAttributeValue("nb_cylindres"));

		nbSoupapes = GestionXML.getInt(configuration
				.getAttributeValue("nb_soupapes"));

		puissanceMax = GestionXML.getInt(puissance.getText());
		regimePuissanceMax = GestionXML.getInt(puissance
				.getAttributeValue("regime"));

		coupleMax = GestionXML.getInt(couple_max.getText());
		regimeCoupleMax = GestionXML.getInt(couple_max
				.getAttributeValue("regime"));

		regimeRupteur = GestionXML.getInt(noeud.getChildText("rupteur"));

		String sSisposition = configuration.getAttributeValue("disposition")
				.toUpperCase();

		if (sSisposition.equals("V")) {
			disposition = DispositionMoteur.V;
		} else if (sSisposition.equals("PLAT")) {
			disposition = DispositionMoteur.PLAT;
		} else {
			disposition = DispositionMoteur.LIGNE;
		}
		
		String sCompression = configuration.getAttributeValue("compression")
				.toUpperCase();

		if (sCompression.equals("TURBO")) {
			compression = CompressionMoteur.TURBO;
		} else if (sSisposition.equals("COMPRESSEUR")) {
			compression = CompressionMoteur.COMPRESSEUR;
		} else {
			compression = CompressionMoteur.NON;
		}
	}

	public String getNom() {
		return nom;
	}

	public int getCylindree() {
		return cylindree;
	}

	public int getNbCylindres() {
		return nbCylindres;
	}

	public DispositionMoteur getDisposition() {
		return disposition;
	}

	public CompressionMoteur getCompression() {
		return compression;
	}

	public int getNbSoupapes() {
		return nbSoupapes;
	}

	public int getPuissanceMax() {
		return puissanceMax;
	}

	public int getRegimePuissanceMax() {
		return regimePuissanceMax;
	}

	public int getCoupleMax() {
		return coupleMax;
	}

	public int getRegimeCoupleMax() {
		return regimeCoupleMax;
	}

	public int getRegimeRupteur() {
		return regimeRupteur;
	}

	public double getCouple()
	{
		return 0.0;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Moteur [");
		if (nom != null) {
			builder.append("nom=");
			builder.append(nom);
			builder.append(", ");
		}
		builder.append("cylindree=");
		builder.append(cylindree);
		builder.append(", nbCylindres=");
		builder.append(nbCylindres);
		builder.append(", ");
		if (disposition != null) {
			builder.append("disposition=");
			builder.append(disposition);
			builder.append(", ");
		}
		if (compression != null) {
			builder.append("compression=");
			builder.append(compression);
			builder.append(", ");
		}
		builder.append("nbSoupapes=");
		builder.append(nbSoupapes);
		builder.append(", puissanceMax=");
		builder.append(puissanceMax);
		builder.append(", regimePuissanceMax=");
		builder.append(regimePuissanceMax);
		builder.append(", coupleMax=");
		builder.append(coupleMax);
		builder.append(", regimeCoupleMax=");
		builder.append(regimeCoupleMax);
		builder.append(", regimeRupteur=");
		builder.append(regimeRupteur);
		builder.append("]");
		return builder.toString();
	}
}
