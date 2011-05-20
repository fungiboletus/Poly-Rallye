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
import polyrallye.ouie.utilitaires.Sound;
import polyrallye.utilitaires.CallbackArretSon;
import polyrallye.utilitaires.Multithreading;

/**
 * @author Antoine Pultier
 * 
 *         Liseuse de texte et de nombres.
 */
public abstract class Liseuse {

	/**
	 * Son contenant toute les paroles enregistrées à la suite.
	 */
	protected static Sound sonParoles;

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
		sonParoles = new Sound("Paroles/paroles.ogg");
		// sonParoles_A.setGain(0.90f);
		
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
			return false;
		}

		Main.logLiseuse(texte);

		Parole p = paroles.get(texte.trim().toLowerCase());
		if (p != null) {
			// System.out.println("alélouilla");
			// System.out.println(p);
			long time = Sys.getTime();
			sonParoles.setOffset(p.getDebut());
			sonParoles.play();

			long t = (long) ((p.getFin() - p.getDebut()) * 1000) - delai;

			while (!interrompre) {

				if ((Sys.getTime() - time) > t) {
					interrompre = true;
				}
				Multithreading.dormir(50);
			}
			sonParoles.stop();
		} else {

			// VocalizeSIVOX gère mal les virgules
			for (String te : texte.split(",")) {

				// La première chose à faire est d'aller voir le cache
				Sound son = Sound.depuisCache(te);

				if (son == null) {

					// VocalizeSIVOX a un peu de mal avec les abréviations, il
					// faut
					// rajouter
					// des espaces

					// Nouveau texte à lire
					StringBuffer sb = new StringBuffer();

					// On en profite pour remplacer les points par « points »
					// pour
					// éviter
					// que la synthèse vocale s'arrête au milieu d'une cylindrée
					Matcher regexMatcher = regexAbreviations.matcher(te
							.replace(".", " point "));

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
						regexMatcher.appendReplacement(sb,
								remplacement.toString());
					}

					// La suite
					regexMatcher.appendTail(sb);

					// Utilisation de VocalyzeSIVOX
					String nouveautexte = sb.toString();

					if (!texte.equals(nouveautexte)) {
						Main.logDebug(nouveautexte, 19);
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

							listePhonemes
									.ecrirePhonemes("Paroles/VocalyzeSIVOX.pho");

							SynthetiseurMbrola synthe = new SynthetiseurMbrola(
									voixVocalyzeSIVOX, "Paroles/",
									"VocalyzeSIVOX");

							synthe.muet();
						} else {
							Main.logImportant("Traitement de la phrase raté…");
						}

					} catch (Exception e1) {
						e1.printStackTrace();
					}

					// De temps en temps, il n'y a aucun fichier à lire
					File fson = new File("Paroles/VocalyzeSIVOX.wav");
					if (fson.exists()) {

						// Récupération du son, qui a pour fichier son, et pour
						// identifiant dans le cache texte
						son = new Sound(fson, te);

						// Suppression du fichier automatique, pour détecter les
						// erreurs
						// quand il y en a
						fson.delete();
					}

				}
				if (son != null) {
					son.playAndWaitWithCallback(new CallbackArretSon() {

						@Override
						public boolean continuerLecture() {
							return !interrompre;
						}
					});

					son.delete();

				} else {

					// TODO La phrase d'en dessous doit obligatoirement
					// être enregistrée :D
					//Liseuse.lire("Polyrallye");
					Main.logImportant("Erreur de syntèse vocale…");
					Main.logImportant(ConfigFile.rechercher("CONFIGURE"));
				}
				// Si on n'a pas le son, on fait un fichier texte qui contient
				// le
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
		}
		return true;
	}

	public static void lire(String texte) {

		fileParoles.add(texte);
	}

	/**
	 * VocalyseSIVOX est très sympathique, mais il a du mal avec les lettres.
	 * Cette fonction permet de lui rendre une prononciation correcte.
	 * 
	 * @param c
	 *            Lettre à énnoncer
	 * @return Prononciation de la lettre
	 */
	public static String remplacerLettre(char c) {
		String upper = ("" + c).toUpperCase();
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
		case '0':
			return "zéro";
		case '1':
			return "un";
		case '2':
			return "deu";
		case '3':
			return "troi";
		case '4':
			return "quatre";
		case '5':
			return "cinq";
		case '6':
			return "six";
		case '7':
			return "sept";
		case '8':
			return "huite";
		case '9':
			return "neuf";
		default:
			return upper;
		}
	}

	public static void marquerPause() {
		// va faire deux pause de «delai»
		fileParoles.add(null);
	}

}
