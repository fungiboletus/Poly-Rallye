package polyrallye.ouie;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import t2s.son.LecteurTexte;

import polyrallye.controlleur.Main;

public abstract class Liseuse {

	protected static LecteurTexte lt;
	
	static {
		lt = new LecteurTexte();
	}
	
	public static void lire(String texte) {
		System.out.println(texte);
		
		
		Main.changerTexteFenetre(texte);

		// Efficace et bourrin (pas de gestion de colisions, on verra si on en
		// rencontre un jour…)
		int clef = texte.hashCode();

		File f = new File("Paroles/" + clef + ".ogg");

		if (f.exists()) {
			// TODO Lecture du son
		} else {
			
			f = new File("Paroles/"+clef+".wav");
			
			if (!f.exists())
			{
				lt.setTexte(texte);
				lt.play();
				
				FileChannel in = null; // canal d'entrée
				FileChannel out = null; // canal de sortie
				 
				try {
				  // Init
				  in = new FileInputStream("VocalyzeSIVOX/donneesMbrola/pho_wav/phrase.wav").getChannel();
				  out = new FileOutputStream(f.getPath()).getChannel();
				 
				  // Copie depuis le in vers le out
				  in.transferTo(0, in.size(), out);
				} catch (Exception e) {
				  e.printStackTrace(); // n'importe quelle exception
				} finally { // finalement on ferme
				  if(in != null) {
				  	try {
					  in.close();
					} catch (Exception e) {}
				  }
				  if(out != null) {
				  	try {
					  out.close();
					} catch (Exception e) {}
				  }
				}
			}
			
			Sound s = new Sound(f.getPath(),null);
			s.play();

			// Si on n'a pas le son, on fait un fichier texte qui contient le
			// texte a énoncer

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

	public static void lire(int valeur) {
		if (valeur >= 1000000) {
			lire(valeur / 1000000);
			Liseuse.lire(" millions ");

			int reste = valeur % 1000000;

			if (reste > 0) {
				lire(reste);
			}
		} else if (valeur >= 1000) {
			if (valeur / 1000 > 1) {
				lire(valeur / 1000);
			}
			Liseuse.lire(" milles ");

			int reste = valeur % 1000;

			if (reste > 0) {
				lire(reste);
			}
		} else if (valeur >= 100) {
			if (valeur / 100 > 1) {
				lire(valeur / 100);
			}

			Liseuse.lire(" cent ");

			int reste = valeur % 100;

			if (reste > 0) {
				lire(reste);
			}
		} else if (valeur >= 10) {
			int dizaine = valeur / 10;
			int reste = valeur % 10;

			switch (dizaine) {
			case 9:
				Liseuse.lire(" quatre vingt ");
				Liseuse.lire(valeur-80);
				reste = 0;
				break;
			case 8:
				Liseuse.lire(" quatre vingt");
				break;
			case 7:
				Liseuse.lire(" soixante ");
				if (reste == 1)
				{
					Liseuse.lire(" et ");
				}
				Liseuse.lire(valeur-60);
				reste = 0;
				break;
			case 6:
				Liseuse.lire(" soixante ");
				if (reste == 1)
				{
					Liseuse.lire(" et ");
				}
				break;
			case 5:
				Liseuse.lire(" cinquante ");
				if (reste == 1)
				{
					Liseuse.lire(" et ");
				}

				break;
			case 4:
				Liseuse.lire(" quarante ");
				if (reste == 1)
				{
					Liseuse.lire(" et ");
				}

				break;
			case 3:
				Liseuse.lire(" trente ");
				if (reste == 1)
				{
					Liseuse.lire(" et ");
				}

				break;
			case 2:
				Liseuse.lire(" vingt ");
				if (reste == 1)
				{
					Liseuse.lire(" et ");
				}

				break;
			case 1:
				switch (reste) {
				case 9:
					Liseuse.lire(" dix-neuf ");
					break;
				case 8:
					Liseuse.lire(" dix huit");
					break;
				case 7:
					Liseuse.lire(" dix sept ");
					break;
				case 6:
					Liseuse.lire(" seize ");
					break;
				case 5:
					Liseuse.lire(" quinze ");
					break;
				case 4:
					Liseuse.lire(" quatorze ");
					break;
				case 3:
					Liseuse.lire(" treize ");
					break;
				case 2:
					Liseuse.lire(" douze ");
					break;
				case 1:
					Liseuse.lire(" onze ");
					break;
				case 0:
					Liseuse.lire(" dix ");
					break;
				}
				reste = 0;
			}

			if (reste > 0) {
				lire(reste);
			}
		} else {
			switch (valeur) {
			case 9:
				Liseuse.lire(" neuf ");
				break;
			case 8:
				Liseuse.lire(" huit ");
				break;
			case 7:
				Liseuse.lire(" sept  ");
				break;
			case 6:
				Liseuse.lire(" six ");
				break;
			case 5:
				Liseuse.lire(" cinq ");
				break;
			case 4:
				Liseuse.lire(" quatre ");
				break;
			case 3:
				Liseuse.lire(" trois ");
				break;
			case 2:
				Liseuse.lire(" deux ");
				break;
			case 1:
				Liseuse.lire(" un ");
				break;
			case 0:
				Liseuse.lire(" zéro ");
				break;
			}
		}
	}

	public static void marquerPause() {

	}

}
