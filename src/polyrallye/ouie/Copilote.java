package polyrallye.ouie;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import polyrallye.controlleur.Main;
import polyrallye.ouie.environnement.Sfx;
import polyrallye.ouie.utilitaires.Sound;
import polyrallye.utilitaires.GestionXML;
import polyrallye.utilitaires.LectureFichier;

public class Copilote {

	// Id du copilote
	private int id;

	private List<Sound> gauche;
	private List<Sound> droite;
	private List<Sound> freine;
	private List<Sound> ok;

	private Sfx bullshit;
	private boolean isPipelette;

	private Random random;

	public Copilote() {

		random = new Random();

		// Sélection d'un copilote
		String rep = "Ressources/Sons/copilote/";
		int nb = (new File(rep).list()).length;
		id = random.nextInt(nb) + 1;
		rep += id + "/";

		isPipelette = false;

		// Manifeste…
		int nbGauche = 0;
		int nbDroite = 0;
		int nbFreine = 0;
		int nbSfx = 0;
		int nbOk = 0;

		// On lit le fichier comme d'ab
		String[] lectureManifeste = new String[5];
		lectureManifeste[0] = "gauche";
		lectureManifeste[1] = "sfx";
		lectureManifeste[2] = "ok";
		lectureManifeste[3] = "droite";
		lectureManifeste[4] = "freine";

		lectureManifeste = new LectureFichier(rep).lire("manifeste.cfg",
				lectureManifeste);
		if (lectureManifeste[0] != null)
			nbGauche = Integer.valueOf(lectureManifeste[0]
					.substring(lectureManifeste[0].indexOf(" ") + 1));

		if (lectureManifeste[1] != null) {
			nbSfx = Integer.valueOf(lectureManifeste[1]
					.substring(lectureManifeste[1].indexOf(" ")+1));
		}
		if (lectureManifeste[2] != null) {
			nbOk = Integer.valueOf(lectureManifeste[2]
					.substring(lectureManifeste[2].indexOf(" ") + 1));
		}
		if (lectureManifeste[3] != null) {
			nbDroite = Integer.valueOf(lectureManifeste[3]
					.substring(lectureManifeste[3].indexOf(" ")+1));
		}
		if (lectureManifeste[4] != null)
			nbFreine = Integer.valueOf(lectureManifeste[4]
					.substring(lectureManifeste[4].indexOf(" ")+1));

		if (nbGauche == -1)
			nbGauche = 0;
		if (nbDroite == -1)
			nbDroite = 0;
		if (nbOk == -1)
			nbOk = 0;
		if (nbSfx == -1)
			nbSfx = 0;
		if (nbFreine == -1)
			nbFreine = 0;

		gauche = new ArrayList<Sound>(nbGauche);
		droite = new ArrayList<Sound>(nbDroite);
		freine = new ArrayList<Sound>(nbFreine);

		ok = new ArrayList<Sound>(nbOk);
		Main.logImportant("" + nbDroite);
		for (int i = 1; i <= nbGauche; ++i) {
			gauche.add(new Sound(rep + "gauche_" + i + ".wav"));
		}

		for (int i = 1; i <= nbDroite; ++i) {
			droite.add(new Sound(rep + "droite_" + i + ".wav"));
		}

		for (int i = 1; i <= nbFreine; ++i) {
			freine.add(new Sound(rep + "freine_" + i + ".wav"));
		}

		for (int i = 1; i <= nbOk; ++i) {
			ok.add(new Sound(rep + "ok_" + i + ".wav"));
		}

		if (nbSfx != 0) {
			bullshit = new Sfx(rep + "sfx/", nbSfx, 3, true, 3.0f);
		} else {
			bullshit = new Sfx();
		}
		bullshit.start();
		bullshit.pause(true);

	}

	public void togglePipelette() {
		String rep = "Ressources/Sons/copilote/" + id + "/";
		if (isPipelette) {
			isPipelette = false;
			bullshit.pause(true);
			Main.logInfo("Le copilote reste professionnel");
			if (new File(rep + "stfu.wav").exists()) {
				Sound stfu = new Sound(rep + "stfu.wav");
				stfu.playAndDelete();
			}
		} else {
			Main.logInfo("Le copilote raconte sa vie");
			isPipelette = true;
			bullshit.pause(false);
		}
	}

	public void playOk() {
		Main.logInfo("OK");
		ok.get(random.nextInt(ok.size())).play();
	}

	public void playCrash() {
		Main.logImportant("CRASH CRASH CRASH !");
		String rep = "Ressources/Sons/copilote/" + id + "/";
		if (isPipelette) {
			isPipelette = false;
			bullshit.pause(true);
		}
		if (new File(rep + "crash.wav").exists()) {
			Sound crash = new Sound(rep + "crash.wav");
			crash.playAndDelete();
		}

	}

	public void playGauche() {

		Main.logInfo("Gauche…");

		/*
		 * if (isPipelette) bullshit.pause(true);*
		 */

		gauche.get(random.nextInt(gauche.size())).play();

		/*
		 * if (isPipelette) bullshit.pause(false);
		 */
	}

	public void playDroite() {

		Main.logInfo("Droite…");

		/*
		 * if (isPipelette) bullshit.pause(true);
		 */

		droite.get(random.nextInt(droite.size())).play();

		/*
		 * if (isPipelette) bullshit.pause(false);
		 */

	}

	public void playFreine() {

		Main.logInfo("Freine !");

		if (isPipelette)
			bullshit.pause(true);

		freine.get(random.nextInt(freine.size())).play();

		if (isPipelette)
			bullshit.pause(false);

	}

	public void delete() {
		for (Sound s : gauche) {
			s.delete();
		}
		for (Sound s : droite) {
			s.delete();
		}
		for (Sound s : freine) {
			s.delete();
		}
		for (Sound s : ok) {
			s.delete();
		}
		bullshit.tuer();
	}

}
