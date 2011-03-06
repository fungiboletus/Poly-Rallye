package polyrallye.ouie;

import java.io.File;
import java.io.FileOutputStream;

public abstract class Liseuse {

	public static void lire(String texte) {
		System.out.println(texte);

		// Efficace et bourrin (pas de gestion de colisions, on verra si on en
		// rencontre un jour…)
		int clef = texte.hashCode();

		File f = new File("Paroles/" + clef + ".ogg");

		if (f.exists()) {
			// TODO Lecture du son
		} else {
			// Si on n'a pas le son, on fait un fichier texte qui contient le texte a énoncer

			File fi = new File("Paroles/" + clef + ".txt");

			if (!fi.exists()) {
				try {
					FileOutputStream instructions = new FileOutputStream(fi);

					instructions.write(texte.getBytes());

					instructions.close();
				} catch (Exception e) {
					// ON NE FAIT RIEN !
				}
			}

		}
	}
	
	public static void lire(int valeur){
		System.out.println(""+valeur);
	}
	
	public static void marquerPause(){
		
	}

}
