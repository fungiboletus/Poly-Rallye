package polyrallye.ouie;

import java.io.File;
import java.util.Random;

import polyrallye.controlleur.Main;
import polyrallye.ouie.utilitaires.Sound;
import polyrallye.ouie.utilitaires.Sound.SoundException;
import polyrallye.utilitaires.GestionXML;
import polyrallye.utilitaires.LectureFichier;
import polyrallye.utilitaires.Multithreading;

public class Radio extends Thread {
	private int id;
	private Sound inter;
	private Sound com;
	private Sound musique;

	private int nbCom;
	private int nbZike;
	private int nbRadio;
	private float level;

	private boolean isPaused;
	private boolean isAlive;

	public Radio() {
		String rep = "Ressources/Sons/radio/";
		nbRadio = (new File(rep).list()).length - 1;
		Random random = new Random();
		id = random.nextInt(nbRadio) + 1;

		level = 1.0f;

		inter = new Sound(rep + "radio.wav");
		inter.setLoop(true);

		com = new Sound();
		musique = new Sound();

		readManifeste();
		// chargerCom();
		// chargerZike();
		setToLevel();

		isPaused = true;
		isAlive = true;

		inter.play();
		inter.pause(true);

	}

	public void run() {
		while (isAlive) {
			if (!isPaused)
				com.playAndWait();
			if (!isPaused) {
				com.delete();
				chargerCom();
				musique.playAndWait();
			}
			if (!isPaused) {
				musique.delete();
				chargerZike();
			}
			setToLevel();
			if (isPaused)
				Multithreading.dormir(1000);
		}
	}

	public void changeStation() {
		if (!isPaused) {
			inter.pause(false);
			com.stop();
			musique.stop();
			com.delete();
			musique.delete();

			Random random = new Random();
			id = random.nextInt(nbRadio) + 1;
			switch (id) {
			case 1:
				Main.logInfo("Radio Jazz");
				break;
			case 2:
				Main.logInfo("Radio Rock");
				break;
			case 3:
				Main.logInfo("Radio Rap");
				break;
			case 4:
				Main.logInfo("Radio Disco");
				break;
			}

			readManifeste();

			chargerCom();
			chargerZike();
			Multithreading.dormir(1000);
			inter.pause(true);

		}
	}

	public void toggleRadio() {
		if (isPaused) {
			Main.logInfo("Radio On");
			inter.play();
			chargerCom();
			chargerZike();
			inter.stop();
			isPaused = false;
		} else {
			Main.logInfo("Radio Off");
			isPaused = true;
			com.stop();
			musique.stop();
			com.delete();
			musique.delete();
		}
	}

	public void diminuerSon() {
		if (level > 0.2f) {
			level -= 0.2;
			setToLevel();
			Main.logInfo("Volume -");
		} else
			Main.logInfo("Volume au minimum");
	}

	public void augmenterSon() {
		if (level < 3.0f) {
			level += 0.2;
			setToLevel();
			Main.logInfo("Volume +");
		} else
			Main.logInfo("Volume au maximum");
	}

	public void delete() {
		isAlive = false;
		musique.delete();
		com.delete();
		inter.delete();
	}

	private void chargerCom() {
		String rep = "Ressources/Sons/radio/" + id + "/";
		Random random = new Random();
		try {
			com.charger(rep + "com_" + (random.nextInt(nbCom) + 1) + ".wav");
		} catch (SoundException e) {
			System.err.println("Erreur chargement Commentaires radio");
		}
	}

	private void chargerZike() {
		String rep = "Ressources/Sons/radio/" + id + "/";
		Random random = new Random();
		try {
			musique.charger(rep + "musique_" + (random.nextInt(nbZike) + 1)
					+ ".ogg");
		} catch (SoundException e) {
			System.err.println("Erreur chargement musique radio");
		}
	}

	private void setToLevel() {
		inter.setGain(level);
		com.setGain(level);
		musique.setGain(level);
		inter.setPosition(0, 0, 0);
		com.setPosition(0, 0, 0);
		musique.setPosition(0, 0, 0);
	}

	private void readManifeste() {
		String rep = "Ressources/Sons/radio/" + id + "/";

		// On lit le fichier comme d'ab
		String[] lectureManifeste = new String[2];
		lectureManifeste[0] = "com";
		lectureManifeste[1] = "zike";

		lectureManifeste = new LectureFichier(rep).lire("manifeste.cfg",
				lectureManifeste);
		if (lectureManifeste[0] != null)
			nbCom = Integer.valueOf(lectureManifeste[0]
					.substring(lectureManifeste[0].indexOf(" ")+1));

		if (lectureManifeste[1] != null) {
			nbZike = Integer.valueOf(lectureManifeste[1]
					.substring(lectureManifeste[1].indexOf(" ")+1));

		}

	}

}
