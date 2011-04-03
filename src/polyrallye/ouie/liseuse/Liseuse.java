package polyrallye.ouie.liseuse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

import org.lwjgl.Sys;

import t2s.son.LecteurTexte;

import polyrallye.ouie.CallbackArretSon;
import polyrallye.ouie.utilitaires.Sound;
import polyrallye.utilitaires.Multithreading;

public abstract class Liseuse {

	protected static LecteurTexte lt;
	protected static Sound sonParoles_A;
	protected static Sound sonParoles_B;
	
	protected static int source = 0;

	protected static Map<String, Parole> paroles;

	protected static Queue<String> fileParoles;

	protected static Thread thread;

	protected static boolean lancee;

	protected static boolean interrompre;
	
	protected static long delai = 100;

	static {
		fileParoles = new LinkedList<String>();

		lt = new LecteurTexte();
		sonParoles_A = new Sound("Paroles/paroles.ogg");
		sonParoles_A.setGain(0.90f);
		sonParoles_B = new Sound(sonParoles_A);
		sonParoles_B.setGain(0.90f);

		paroles = new HashMap<String, Parole>();

		try {
			BufferedReader brMarqueurs = new BufferedReader(new FileReader(
					"Paroles/marqueurs.txt"));

			String ligneMarqueurs;
			while ((ligneMarqueurs = brMarqueurs.readLine()) != null) {

				Scanner sc = new Scanner(ligneMarqueurs);

				float debut = sc.nextFloat();
				float fin = sc.nextFloat();

				try {
					String clef = sc.nextLine().trim().toLowerCase();

					Parole p = new Parole(debut, fin, clef);
					//System.out.println(p);
					paroles.put(clef, p);
				} catch (Exception ee) {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		thread = new Thread() {
			public void run() {
				while (lancee) {
					// System.out.println("canard");
					interrompre = false;
					if (!lireSuivant()) {
						// Mise en attente
						// 100 millisecondes n'est pas énorme, car normalement,
						// les éléments sont ajoutés à la suite
						// il ne peut pas avoir des pauses de 100 millisecondes
						// pendant
						// une même phrase
						Multithreading.dormir(100);
					}
				}
			}
		};
	}

	public static void lancer() {
		lancee = true;
		thread.start();
	}

	public static void arreter() {
		lancee = false;
	}

	public static void interrompre() {
		fileParoles.clear();
		interrompre = true;
	}

	public static boolean lireSuivant() {
		String texte = fileParoles.poll();

		if (texte == null) {
			Multithreading.dormir(delai);
			sonParoles_A.stop();
			sonParoles_B.stop();
			return false;
		}

		System.out.println(texte);

		//Main.changerTexteFenetre(texte);

		// Efficace et bourrin (pas de gestion de colisions, on verra si on en
		// rencontre un jour…)
		int clef = texte.hashCode();

		Parole p = paroles.get(texte.trim().toLowerCase());
		if (p != null) {
			// System.out.println("alélouilla");
			// System.out.println(p);
			long time = Sys.getTime();

			if (source == 0) {
				sonParoles_A.setOffset(p.getDebut());
				sonParoles_A.play();
				Multithreading.dormir(delai);
				sonParoles_B.stop();
				source = 1;
			} else {
				sonParoles_B.setOffset(p.getDebut());
				sonParoles_B.play();
				Multithreading.dormir(delai);
				sonParoles_A.stop();
				source = 0;
			}

			long t = (long) ((p.getFin() - p.getDebut()) * 1000)-delai;

			while (!interrompre) {
				
				if ((Sys.getTime() - time) > t) {
					interrompre = true;
				}
				Multithreading.dormir(50);
			}
		} else {

			sonParoles_A.stop();
			sonParoles_B.stop();
			
			File d = new File("Paroles/Generated");
			
			if (!d.exists()) {
				d.mkdir();
			}
			
			File f = new File("Paroles/Generated/" + clef + ".wav");

			if (!f.exists()) {

				try {
					lt.setTexte(texte);
					lt.setVoix(2);
					lt.play();

					new File("VocalyzeSIVOX/donneesMbrola/pho_wav/phrase.wav")
							.renameTo(f);

				} catch (Exception e) {
					// TODO La phrase d'en dessous doit obligatoirement 
					// être enregistrée :D
					Liseuse.lire("Erreur de synthèse vocale");
					System.err.println("VocalizeSIVOX s'est planté…");
					e.printStackTrace();
					return true;
				}
			}

			// VocalizeSIVOX peut se planter…
			if (f.exists()) {
				Sound s = new Sound(f.getPath());
				s.playAndWaitWithCallback(new CallbackArretSon() {
					
					@Override
					public boolean continuerLecture() {
						return !interrompre;
					}
				});
				s.delete();
			}

			// Si on n'a pas le son, on fait un fichier texte qui contient le
			// texte a énoncer

			try {
				PrintWriter pw = new PrintWriter(new FileWriter(
						"Paroles/a_enregistrer.txt", true));
				pw.println(texte);
				pw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return true;
	}

	public static void lire(String texte) {
		fileParoles.add(texte);
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
				Liseuse.lire(" quatre-vingt ");
				Liseuse.lire(valeur - 80);
				reste = 0;
				break;
			case 8:
				Liseuse.lire(" quatre-vingt");
				break;
			case 7:
				Liseuse.lire(" soixante ");
				if (reste == 1) {
					Liseuse.lire(" et ");
				}
				Liseuse.lire(valeur - 60);
				reste = 0;
				break;
			case 6:
				Liseuse.lire(" soixante ");
				if (reste == 1) {
					Liseuse.lire(" et ");
				}
				break;
			case 5:
				Liseuse.lire(" cinquante ");
				if (reste == 1) {
					Liseuse.lire(" et ");
				}

				break;
			case 4:
				Liseuse.lire(" quarante ");
				if (reste == 1) {
					Liseuse.lire(" et ");
				}

				break;
			case 3:
				Liseuse.lire(" trente ");
				if (reste == 1) {
					Liseuse.lire(" et ");
				}

				break;
			case 2:
				if (reste == 0) {
					Liseuse.lire(" vingt ");
				} else {
					Liseuse.lire("vinte");
				}
				if (reste == 1) {
					Liseuse.lire(" et ");
				}

				break;
			case 1:
				switch (reste) {
				case 9:
					Liseuse.lire(" dix-neuf ");
					break;
				case 8:
					Liseuse.lire(" dix-huit");
					break;
				case 7:
					Liseuse.lire(" dix-sept ");
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
		// va faire deux pause de «delai»
		fileParoles.add(null);
	}

}
