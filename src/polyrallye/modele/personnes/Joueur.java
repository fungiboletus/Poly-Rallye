package polyrallye.modele.personnes;

import java.io.File;

import org.jdom.Element;

import polyrallye.modele.Garage;
import polyrallye.modele.Permis;
import polyrallye.modele.voiture.Voiture;
import polyrallye.ouie.liseuse.Liseuse;
import polyrallye.utilitaires.GestionXML;

public class Joueur extends Personne
{
	public static Joueur session;

	protected Garage garage;
	protected int argent = 0;
	
	protected Permis permis;
	
	public Joueur(String nom)
	{
		super(nom);
		
		garage = new Garage(null);
		
		// Au début, on a beaucoup d'argent
		argent = 20000;
		
		permis = new Permis();
	}
	
	public Joueur(Element noeud)
	{
		super(noeud.getChildText("nom"));
		
		garage = new Garage(noeud.getChild("garage"));
		
		argent = GestionXML.getInt(noeud.getChildText("argent"));
		
		permis = new Permis(noeud.getChild("permis"));
	}

	public Garage getGarage() {
		return garage;
	}
	
	public int getArgent() {
		return argent;
	}
	
	public int ajouterArgent(int somme) {
            return (argent += somme);
        }

	public Permis getPermis() {
		return permis;
	}

	public void acheterVoiture(Voiture v) throws Exception{
		int prix = v.getPrix();
		
		if (prix > argent){
			throw new Exception("Vous n'avez pas assez d'argent pour acheter la voiture");
		}
		garage.ajouter(v);
		argent -= prix;
	}
	
	public void vendreVoiture(Voiture v) throws Exception{
		argent += garage.vendre(v);
	}
	
	public Element toXML(){
		Element noeud = new Element("joueur");
		
		noeud.addContent(new Element("nom").setText(nom));
		noeud.addContent(new Element("argent").setText(""+argent));
		
		noeud.addContent(garage.toXML());
				
		return noeud;
	}
	
	public void setSessionCourante()
	{
		session = this;
	}
	
	public static Joueur chargerJoueur(String nom)
	{
		File f = new File("Comptes/"+nom.hashCode()+".xml");
		
		if (f.exists())
		{
			Element n;
			try {
				n = GestionXML.chargerNoeudRacine(f);
				return new Joueur(n);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return new Joueur(nom);
	}
	
	public static void EnregistrerJoueur(Joueur j)
	{
		try {
			File d = new File("Comptes");
			
			if (!d.exists()) {
				d.mkdir();
			}
			
			GestionXML.enregistrerRacine("Comptes/"+j.getNom().hashCode()+".xml", j.toXML());
		} catch (Exception e) {
			e.printStackTrace();
			Liseuse.lire("Impossible de sauvegarder la progression.");
		}
	}
}
