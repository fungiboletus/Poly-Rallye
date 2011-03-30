package polyrallye.modele.voiture;

import org.jdom.Element;

import polyrallye.ouie.Liseuse;
import polyrallye.utilitaires.GestionXML;

public class Voiture
{
	protected String nom;
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

	public Voiture()
	{
		super();
	}

	public Voiture(Element noeud)
	{
		super();
		
		nom = noeud.getChildText("nom");
		
		Element presentation = noeud.getChild("presentation");
		constructeur = presentation.getChildText("constructeur");
		
		Element economie = noeud.getChild("economie");
		prix = GestionXML.getInt(economie.getChildText("prix"));
		rarete = GestionXML.getInt(economie.getChildText("rarete"));
		
		Element periode = presentation.getChild("periode");
		debutDiffusion = GestionXML.getInt(periode.getAttributeValue("debut"));
		finDiffusion = GestionXML.getInt(periode.getAttributeValue("fin"));
		
		moteur = new Moteur(noeud.getChild("moteur"));
		transmission = new Transmission(moteur, noeud.getChild("transmission"));
		chassis = new Chassis(noeud.getChild("chassis"));
		sources = new Sources(noeud.getChild("sources"));
	}

	public String getNom()
	{
		return nom;
	}

	public String getConstructeur()
	{
		return constructeur;
	}

	public int getRarete()
	{
		return rarete;
	}

	public int getPrix()
	{
		return prix;
	}

	public int getDebutDiffusion()
	{
		return debutDiffusion;
	}

	public int getFinDiffusion()
	{
		return finDiffusion;
	}

	public Moteur getMoteur()
	{
		return moteur;
	}

	public Transmission getTransmission()
	{
		return transmission;
	}

	public Chassis getChassis()
	{
		return chassis;
	}

	public Sources getSources()
	{
		return sources;
	}

	public double getRapportPuissance()
	{
		return moteur.getCoupleMax()/chassis.poids;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Voiture [ ");
		if (constructeur != null) {
			builder.append(constructeur);
			builder.append(", ");
		}
		if (nom != null) {
			builder.append(nom);
			builder.append(", ");
		}
		builder.append(prix);
		builder.append("€ ]");
		return builder.toString();
	}
	
	public void lireSpecifications()
	{
		Liseuse.lire(nom);
		Liseuse.lire("Construite par ");
		Liseuse.lire(constructeur);
		Liseuse.lire(" entre ");
		Liseuse.lire(debutDiffusion);
		Liseuse.lire(" et ");
		Liseuse.lire(finDiffusion);
		Liseuse.lire("Valeur de ");
		Liseuse.lire(prix);
		Liseuse.lire("pour une rareté de ");
		Liseuse.lire(rarete);
		Liseuse.marquerPause();
		moteur.lireSpecifications();
	}

    /**
     * @return the conduite
     */
    public Conduite getConduite() {
        return conduite;
    }

    /**
     * @param conduite the conduite to set
     */
    public void setConduite(Conduite conduite) {
        this.conduite = conduite;
    }
}
