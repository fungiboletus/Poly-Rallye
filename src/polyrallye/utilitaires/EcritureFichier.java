package polyrallye.utilitaires;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class EcritureFichier {

	private String chemin;

	public EcritureFichier(String chemin) {
		this.chemin = chemin;
	}

	/**
	 * lit la premiere ligne d'un fichier
	 * 
	 * @param fichier
	 * @param arg
	 * @return
	 */
	public String ecriturePremiereLigne(String fichier, String line) {
		String result = null;

		BufferedWriter mani = null;
		// On lit le fichier

		try {
			mani = new BufferedWriter(new FileWriter(chemin + fichier));
		} catch (IOException e1) {
			System.err.println("Erreur ouverture ecriture fichier");
			e1.printStackTrace();
		}
		try {
			mani.append(line);
		} catch (IOException e1) {
			System.err.println("Erreur ecriture fihcier");
			e1.printStackTrace();
		}

		try {
			mani.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

}
