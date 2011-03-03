package polyrallye.modele;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

import polyrallye.modele.*;

public class ChargerVoitures {

	protected List<Voiture> voitures;

	public ChargerVoitures() throws Exception {
		voitures = new ArrayList<Voiture>();

		File dossier = new File("Voitures");

		if (!dossier.isDirectory()) {
			throw new Exception(
					"Le jeu ne contient pas de dossier de voitures.");
		}
		
		chargerDossier(dossier);
		
		for(Voiture v : voitures)
		{
			System.out.println(v);
		}
	}
	
	protected void chargerDossier(File dossier)
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

	protected void chargerFichierVoiture(File fichier) throws Exception
	{
		FileInputStream lecteurFichier = new FileInputStream(fichier);
		Element noeud = GestionXML.chargerElementFlux(GestionXML.creerDocument(lecteurFichier));
		
		voitures.add(new Voiture(noeud));
	}
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		new ChargerVoitures();
	}

}
