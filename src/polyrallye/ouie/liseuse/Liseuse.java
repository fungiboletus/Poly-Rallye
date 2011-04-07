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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lwjgl.Sys;

import t2s.prosodie.ListePhonemes;
import t2s.son.SynthetiseurMbrola;
import t2s.traitement.Arbre;
import t2s.traitement.Phrase;
import t2s.traitement.Pretraitement;
import t2s.util.ConfigFile;

import polyrallye.controlleur.Main;
import polyrallye.ouie.CallbackArretSon;
import polyrallye.ouie.utilitaires.Sound;
import polyrallye.utilitaires.Multithreading;

/**
 * @author Antoine Pultier
 * 
 *         Liseuse de texte et de nombres.
 */
public abstract class Liseuse {

	// Il y a deux sons pour pouvoir enchaîner rapidement
	protected static Sound sonParoles_A;
	protected static Sound sonParoles_B;

	/**
	 * Identifieur entre sonParoles_A et sonParoles_B
	 */
	protected static int source = 0;

	/**
	 * Dictionnaire des paroles enregistrées.
	 */
	protected static Map<String, Parole> paroles;

	/**
	 * File des paroles.
	 */
	protected static Queue<String> fileParoles;

	/**
	 * Thread qui énumère les paroles.
	 */
	protected static Thread thread;

	/**
	 * Est-ce que la liseuse est lancée ?
	 */
	protected static boolean lancee;

	/**
	 * Condition de continuation du thread.
	 */
	protected static boolean interrompre;

	/**
	 * Délai entre deux vérifications d'interruption.
	 */
	protected static long delai = 100;

	/**
	 * Expression régulière permettant de reconnaître les acronymes.
	 */
	protected static Pattern regexAbreviations;

	/**
	 * Voix utilisée par VocalyzeSIVOX
	 */
	protected static String voixVocalyzeSIVOX;

	static {

		// On regarde quelles sont les abréviations (lettres en majuscules d'au
		// moins 2 lettres de long)
		regexAbreviations = Pattern.compile("[A-Z][A-Z]*[A-Z0-9]");

		fileParoles = new LinkedList<String>();

		// lt = new LecteurTexte();
		sonParoles_A = new Sound("Paroles/paroles.ogg");
		// sonParoles_A.setGain(0.90f);
		sonParoles_B = new Sound(sonParoles_A);
		// sonParoles_B.setGain(0.90f);

		voixVocalyzeSIVOX = ConfigFile.rechercher("VOIX_4");

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
					// System.out.println(p);
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

		Main.log(texte);

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

			long t = (long) ((p.getFin() - p.getDebut()) * 1000) - delai;

			while (!interrompre) {

				if ((Sys.getTime() - time) > t) {
					interrompre = true;
				}
				Multithreading.dormir(50);
			}
		} else {

			sonParoles_A.stop();
			sonParoles_B.stop();

			// VocalizeSIVOX a un peu de mal avec les abréviations, il faut
			// rajouter
			// des espaces

			// Nouveau texte à lire
			StringBuffer sb = new StringBuffer();

			// On en profite pour remplacer les points par « points » pour
			// éviter
			// que la synthèse vocale s'arrête au milieu d'une cylindrée
			Matcher regexMatcher = regexAbreviations.matcher(texte.replace(".",
					" point "));

			// Tant qu'il y a des abréviations
			while (regexMatcher.find()) {

				// Construction de la nouvelle abréviation
				StringBuilder remplacement = new StringBuilder();
				remplacement.append(' ');

				for (char c : regexMatcher.toMatchResult().group()
						.toCharArray()) {
					remplacement.append(remplacerLettre(c));
					remplacement.append(' ');
				}

				// Remplacement de l'abréviation par la nouvelle
				regexMatcher.appendReplacement(sb, remplacement.toString());
			}

			// La suite
			regexMatcher.appendTail(sb);

			// Utilisation de VocalyzeSIVOX
			String nouveautexte = sb.toString();
			
			if (!texte.equals(nouveautexte)) {
				Main.log(nouveautexte);
			}
			
			Pretraitement traitement = new Pretraitement(nouveautexte);
			Phrase phrase = null;
			try {
				phrase = traitement.nouvellePhrase();

				if (phrase != null) {
					Arbre arbre = new Arbre("");

					ListePhonemes listePhonemes = new ListePhonemes(
							arbre.trouverPhoneme(phrase.getPhrase()),
							phrase.getProsodie());

					listePhonemes.ecrirePhonemes("Paroles/VocalyzeSIVOX.pho");

					SynthetiseurMbrola synthe = new SynthetiseurMbrola(
							voixVocalyzeSIVOX, "Paroles/", "VocalyzeSIVOX");

					synthe.muet();
				} else {
					Main.log("Traitement de la phrase raté…");
				}

			} catch (Exception e1) {
				e1.printStackTrace();
			}

			// De temps en temps, il n'y a aucun fichier à lire
			File son = new File("Paroles/VocalyzeSIVOX.wav");
			if (son.exists()) {

				// Récupération du son, qui a pour fichier son, et pour
				// identifiant dans le cache texte
				Sound s = new Sound(son, texte);

				s.playAndWaitWithCallback(new CallbackArretSon() {

					@Override
					public boolean continuerLecture() {
						return !interrompre;
					}
				});
				s.delete();

				// Suppression du fichier automatique, pour détecter les erreurs
				// quand il y en a
				son.delete();

			} else {

				// TODO La phrase d'en dessous doit obligatoirement
				// être enregistrée :D
				Liseuse.lire("Erreur de synthèse vocale");
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
		fileParoles.add(""+valeur);/*
		if (valeur >= 1000000) {
			lire(valeur / 1000000);
			fileParoles.add(" millions ");

			int reste = valeur % 1000000;

			if (reste > 0) {
				lire(reste);
			}
		} else if (valeur >= 1000) {
			if (valeur / 1000 > 1) {
				lire(valeur / 1000);
			}
			fileParoles.add(" milles ");

			int reste = valeur % 1000;

			if (reste > 0) {
				lire(reste);
			}
		} else if (valeur >= 100) {
			if (valeur / 100 > 1) {
				lire(valeur / 100);
			}

			fileParoles.add(" cent ");

			int reste = valeur % 100;

			if (reste > 0) {
				lire(reste);
			}
		} else if (valeur >= 10) {
			int dizaine = valeur / 10;
			int reste = valeur % 10;

			switch (dizaine) {
			case 9:
				fileParoles.add(" quatre-vingt ");
				Liseuse.lire(valeur - 80);
				reste = 0;
				break;
			case 8:
				fileParoles.add(" quatre-vingt");
				break;
			case 7:
				fileParoles.add(" soixante ");
				if (reste == 1) {
					fileParoles.add(" et ");
				}
				Liseuse.lire(valeur - 60);
				reste = 0;
				break;
			case 6:
				fileParoles.add(" soixante ");
				if (reste == 1) {
					fileParoles.add(" et ");
				}
				break;
			case 5:
				fileParoles.add(" cinquante ");
				if (reste == 1) {
					fileParoles.add(" et ");
				}

				break;
			case 4:
				fileParoles.add(" quarante ");
				if (reste == 1) {
					fileParoles.add(" et ");
				}

				break;
			case 3:
				fileParoles.add(" trente ");
				if (reste == 1) {
					fileParoles.add(" et ");
				}

				break;
			case 2:
				if (reste == 0) {
					fileParoles.add(" vingt ");
				} else {
					fileParoles.add("vinte");
				}
				if (reste == 1) {
					fileParoles.add(" et ");
				}

				break;
			case 1:
				switch (reste) {
				case 9:
					fileParoles.add(" dix-neuf ");
					break;
				case 8:
					fileParoles.add(" dix-huit");
					break;
				case 7:
					fileParoles.add(" dix-sept ");
					break;
				case 6:
					fileParoles.add(" seize ");
					break;
				case 5:
					fileParoles.add(" quinze ");
					break;
				case 4:
					fileParoles.add(" quatorze ");
					break;
				case 3:
					fileParoles.add(" treize ");
					break;
				case 2:
					fileParoles.add(" douze ");
					break;
				case 1:
					fileParoles.add(" onze ");
					break;
				case 0:
					fileParoles.add(" dix ");
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
				fileParoles.add(" neuf ");
				break;
			case 8:
				fileParoles.add(" huit ");
				break;
			case 7:
				fileParoles.add(" sept  ");
				break;
			case 6:
				fileParoles.add(" six ");
				break;
			case 5:
				fileParoles.add(" cinq ");
				break;
			case 4:
				fileParoles.add(" quatre ");
				break;
			case 3:
				fileParoles.add(" trois ");
				break;
			case 2:
				fileParoles.add(" deux ");
				break;
			case 1:
				fileParoles.add(" un ");
				break;
			case 0:
				fileParoles.add(" zéro ");
				break;
			}
		}*/
	}
	
	/**
	 * VocalyseSIVOX est très sympathique, mais il a du mal avec les lettres. Cette fonction permet de lui rendre une prononciation correcte.
	 * 
	 * @param c Lettre à énnoncer
	 * @return Prononciation de la lettre
	 */
	public static String remplacerLettre(char c) {
		String upper = (""+c).toUpperCase();
		switch (upper.charAt(0)) {
		case 'X':
			return "iksse";
		case 'S':
			return "aisse";
		case 'R':
			return "aire";
		case 'T':
			return "té";
		case '-':
			return " ";
		default:
			return upper;
		}
	}

	public static void marquerPause() {
		// va faire deux pause de «delai»
		fileParoles.add(null);
	}

}
