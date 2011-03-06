package polyrallye.modele;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import org.jdom.Element;

import polyrallye.ouie.Liseuse;

public abstract class StockVoitures {

	protected static Map<String, Voiture> voitures;

	static {
		voitures = new TreeMap<String, Voiture>();
		
		File dossier = new File("Voitures");
		
		if (!dossier.isDirectory()) {
			Liseuse.lire("Le jeu ne contient pas de dossier de voitures.");
		}
		
		chargerDossier(dossier);		
	}
	
	protected static void chargerDossier(File dossier)
	{
		for (File fichier : dossier.listFiles()) {
			if (fichier.isDirectory())
			{
				chargerDossier(fichier);
			}else if (fichier.isFile() && !fichier.isHidden()){
				try {
					chargerFichierVoiture(fichier);
				} catch (Exception e) {
					System.out.println("Problème avec le fichier "+fichier);
					e.printStackTrace();
				}
			}
		}
	}

	protected static void chargerFichierVoiture(File fichier) throws Exception
	{
		Element noeud = GestionXML.chargerNoeudRacine(fichier);
		Voiture v = new Voiture(noeud);
		voitures.put(v.getNom(), v);
	}
	
	public static Voiture getVoitureParNom(String nom)
	{
		return voitures.get(nom);
	}
	
	public static Map<String, Voiture> getVoitures()
	{
		return voitures;
	}

}
