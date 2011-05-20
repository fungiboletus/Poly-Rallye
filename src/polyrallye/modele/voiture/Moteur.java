package polyrallye.modele.voiture;

import org.jdom.Element;

import polyrallye.controlleur.Main;
import polyrallye.ouie.liseuse.Liseuse;
import polyrallye.utilitaires.GestionXML;

/**
 * Gestion du moteur de la voiture.
 *
 */
public class Moteur {
    /**
     * Nom du moteur.
     * 
     * Cela permet de repérer les moteurs identiques
     * en fonction des voitures, comme le V6 PRV
     */
    protected String nom;

    /**
     * Cylindrée du moteur
     */
    protected int cylindree;

    /**
     * Nombre de cylindres du moteur
     */
    protected int nbCylindres;
    
    /**
     * Disposition du moteur (ligne, V, canard)
     */
    protected DispositionMoteur disposition;
    
    /**
     * Type de compression du moteur (turbo, compresseur, etc)
     */
    protected CompressionMoteur compression;

    /**
     * Nombre de soupapes du moteur.
     */
    protected int nbSoupapes;

    /**
     * Puissance maximale du moteur.
     */
    protected double puissanceMax;
    
    /**
     * Régime associé à la puissance maximale du moteur. 
     */
    protected double regimePuissanceMax;

    /**
     * Couple maximal du moteur.
     */
    protected double coupleMax;
    
    /**
     * Régime associé au couple maximal du moteur. 
     */
    protected double regimeCoupleMax;
    
    /**
     * Couple correspondant à la puissance maximale.
     */
    protected double couplePuissanceMax;
    
    /**
     * Régime auquel le rupteur se déclanche.
     */
    protected double regimeRupteur;

    /**
     * Est-ce que le rupteur est enclanché ?
     * 
     * Il se déclanche au regimeRupteur, et s'arrête au regimePuissanceMax
     */
    protected boolean rupteurEnclanche;
    
    /**
     * Regime actuel du moteur, est utilisé pour calculer 
     * le couple en fonction de la puissance.
     */
    private double regimeCourant = 800.0;
    
    /**
     * Est-ce que la voiture est en train d'accélérer ?
     * 
     * Dans une version ultérieure, il pourrait être intéressant
     * de permettre d'accélérer plus ou moins fort. Mais avec un clavier,
     * c'est tout ou rien.
     */
    private boolean accelere = false;
    
    /**
     * Est-ce que le moteur est en panne ?
     */
    private boolean panne = false;
    
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
        
        couplePuissanceMax = (puissanceMax*736)/((regimePuissanceMax/60.0)*2*Math.PI);

        regimeRupteur = GestionXML.getInt(noeud.getChildText("rupteur"));

        if (regimeRupteur == 0) {
            regimeRupteur = regimePuissanceMax + 350;
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

    public double getPuissanceMax() {
        return puissanceMax;
    }

    public double getRegimePuissanceMax() {
        return regimePuissanceMax;
    }

    public double getCoupleMax() {
        return coupleMax;
    }

    public double getRegimeCoupleMax() {
        return regimeCoupleMax;
    }

    public double getRegimeRupteur() {
        return regimeRupteur;
    }

    /**
     * Renvoie le couple de la voiture en fonction de son régime actuel
     * 
     * @param regime
     * @return
     */

    public double getCouple() {
    	// Si le moteur est en panne, il n'avance plus
    	if (panne) return 0.0;
    	
    	// Entre couple max et puissance max (l'idéal)
    	double xa = regimeCoupleMax;
    	double xb = regimePuissanceMax;
    	
    	double ya = coupleMax;
    	double yb = couplePuissanceMax;

    	if (!accelere || rupteurEnclanche) {
    		xa = 800.0;
    		xb = regimeRupteur;
    		
    		ya = couplePuissanceMax*-0.57;
    		yb = coupleMax*-0.63;
    	} else if (regimeCourant < regimeCoupleMax) {
    		xb = xa;
    		xa = 800.0;
    		
    		yb = ya;
    		ya = yb*0.79;
    	} else if (regimeCourant > regimePuissanceMax) {
    		xa = xb;
    		xb = regimeRupteur;
    		
    		ya = yb;
    		yb = ya*0.73;
    	}
    	
		double couple = ya + (regimeCourant - xa)*((yb-ya)/(xb-xa));
    	
		Main.logDebug("Couple calculé: "+couple, 8);
    	
    	return couple;
    }
    
    /**
     * @param regimeCourant
     *            the regimeCourant to set
     */
    public void setRegimeCourant(double regime) {
    	
        regimeCourant = regime > 800.0 ? regime : 800.0;
        
        if (regimeCourant > regimeRupteur) {
        	// Si on a un gros sur-régime
        	/*if (regimeCourant*0.7 > regimeRupteur && !panne) {
        		Liseuse.lire("Panne du moteur");
        		Liseuse.lire("Relancez la course");
        		Sound s = new Sound("Sons/Crash/vehicule_1.wav");
        		s.playAndDelete();
        		panne = true;
        	} else {        		*/
        		rupteurEnclanche = true;
        	//}
        } else if (rupteurEnclanche && regimeCourant < regimeRupteur - 400) {
        	rupteurEnclanche = false;
        }
    }

    /**
     * @return the regimeCourant
     */
    public double getRegimeCourant() {
    	if (panne) return 0.0;
        return regimeCourant;
    }

    public boolean isAccelere() {
		return accelere;
	}

	public void setAccelere(boolean accelere) {
		this.accelere = accelere;
	}

	public boolean isRupteurEnclanche() {
		return rupteurEnclanche;
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

        sb.append((int) Math.round(cylindree / 1000.0));
        sb.append(" litres pour ");
        sb.append(nbCylindres);
        sb.append(" cylindres et ");
        sb.append(nbSoupapes);
        sb.append(" soupapes");

        Liseuse.lire(sb.toString());
        Liseuse.marquerPause();
        sb = new StringBuilder();
        
        sb.append("Disposition ");

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
        sb.append((int)coupleMax);
        sb.append(" nioutown mètres à ");
        sb.append((int)regimeCoupleMax);
        sb.append(" tours par minutes");

        Liseuse.lire(sb.toString());
        sb = new StringBuilder();

        sb.append("Puissance max de ");
        sb.append((int)puissanceMax);
        sb.append(" chevaux à ");
        sb.append((int)regimePuissanceMax);
        sb.append(" tours minute");

        Liseuse.lire(sb.toString());
    }
    
    public void reset() {
    	panne = false;
    }
}
