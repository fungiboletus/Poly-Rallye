package polyrallye.utilitaires;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LectureFichier {
	
	private String chemin;
	
	public LectureFichier(String chemin) {
		this.chemin=chemin;
	}
	
	/**
	 * Cherche dans fichier les arguments arg et renvoit leur valeur
	 * @param fichier
	 * @param arg
	 * @return
	 */
	public String[] lire(String fichier,String[] arg) {
		String[] result = new String[arg.length];
		
		BufferedReader mani = null;
		// On lit le fichier
		try {
			mani = new BufferedReader(new FileReader(chemin + fichier));
			String line = null;
			try {
				while ((line = mani.readLine()) != null) {
					//On met le resultat de chaque champ recherche
					for (int i = 0; i < arg.length; i++) {
						if(line.contains(arg[i]))
							result[i]=line.substring(line.indexOf(" ") + 1);
					}

				}
			} catch (IOException e) {
				System.err.println("Erreur lecture fichier");
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			System.err.println("Erreur chargement fichier");
			e.printStackTrace();
		}

		try {
			mani.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * lit la premiere ligne d'un fichier
	 * @param fichier
	 * @param arg
	 * @return
	 */
	public String lirePremiereLigne(String fichier) {
		String result=null;
		
		BufferedReader mani = null;
		// On lit le fichier
		try {
			mani = new BufferedReader(new FileReader(chemin + fichier));
			try {
				result = mani.readLine();
				
			} catch (IOException e) {
				System.err.println("Erreur lecture fichier");
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			System.err.println("Erreur chargement fichier");
			e.printStackTrace();
		}

		try {
			mani.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}

}
