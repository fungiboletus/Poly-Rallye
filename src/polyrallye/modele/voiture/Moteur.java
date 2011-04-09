package polyrallye.modele.voiture;

import org.jdom.Element;

import polyrallye.ouie.liseuse.Liseuse;
import polyrallye.utilitaires.GestionXML;

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

    private double regimeCourant;
    protected double coeff = 0.7;
    
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
        
        if (regimeRupteur == 0) {
        	regimeRupteur = regimePuissanceMax + 500;
        }

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
    
    /**
     * Renvoie le couple de la voiture en fonction de son régime actuel
     * @param regime
     * @return
     */
    public double getCouple() {
        double res = getCoupleMax()+ (getRegimePuissanceMax() - getRegimeCoupleMax())
                * ((1500 - getCoupleMax()) / (getRegimePuissanceMax()-getRegimeCoupleMax()));
//        System.out.println("couple "+res);
        return res;
    }


    /**
     * @param regimeCourant the regimeCourant to set
     */
    public void setRegimeCourant() {
        regimeCourant = coeff * regimeCoupleMax;
    }

    /**
     * @return the regimeCourant
     */
    public double getRegimeCourant() {
        return regimeCourant;
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

    public void lireSpecifications() {
    	StringBuilder sb = new StringBuilder();
		
        sb.append("Moteur de ");

        sb.append((int)Math.round(cylindree/1000.0));
        sb.append(" litres pour ");
        sb.append(nbCylindres);
        sb.append(" cylindres, ");
        sb.append(nbSoupapes);
        sb.append(" soupapes");
        
        Liseuse.lire(sb.toString());
        Liseuse.marquerPause();
        sb = new StringBuilder();
        
        sb.append("Disposés ");

        switch (disposition) {
        case LIGNE:
            sb.append("en ligne");
            break;
        case PLAT:
            sb.append("à plat");
            break;
        case V:
            sb.append("en V");
            break;
        }

        switch (compression) {
        case COMPRESSEUR:
            sb.append(" avec compresseur mécanique");
            break;
        case TURBO:
            sb.append(" avec turbo compresseur");
            break;
        }

        Liseuse.lire(sb.toString());
        Liseuse.marquerPause();
        sb = new StringBuilder();
        
        sb.append("Couple max de ");
        sb.append(coupleMax);
        sb.append(" nioutown mètres à ");
        sb.append(regimeCoupleMax);
        sb.append(" tours par minutes");
        
        Liseuse.lire(sb.toString());
        sb = new StringBuilder();
        
        sb.append("Puissance max de ");
        sb.append(puissanceMax);
        sb.append(" chevaux à ");
        sb.append(regimePuissanceMax);
        sb.append(" tours minute");
        
        Liseuse.lire(sb.toString());
    }
}
