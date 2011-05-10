package polyrallye.ouie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import polyrallye.ouie.utilitaires.Sound;
import polyrallye.ouie.utilitaires.Sound.SoundException;
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
		String rep = "Sons/radio/";
		nbRadio = (new File(rep).list()).length - 1;
		Random random = new Random();
		id = random.nextInt(nbRadio) + 1;

		level = 1.0f;

		inter = new Sound(rep + "radio.wav");
		inter.setLoop(true);

		com = new Sound();
		musique = new Sound();

		readManifeste();
		chargerCom();
		chargerZike();
		setToLevel();
		
		isPaused=true;
		isAlive=true;
		
		inter.play();
		inter.pause(true);
		
	}
	
	public void run() {
		while (isAlive) {
			if(!isPaused)
				com.playAndWait();
			if(!isPaused) {
				com.delete();
				chargerCom();
				musique.playAndWait();
			}
			if(!isPaused) {
			musique.delete();
			chargerZike();
			}
			setToLevel();
			if(isPaused)
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
		
		readManifeste();
		
		chargerCom();
		chargerZike();
		Multithreading.dormir(1000);
		inter.pause(true);
		
		}
	}
	
	public void toggleRadio() {
		if (isPaused)
			isPaused=false;
		else {
			isPaused=true;
			com.stop();
			musique.stop();
		}
	}

	public void diminuerSon() {
		if (level > 0.2f) {
			level -= 0.2;
			setToLevel();
		}
	}

	public void augmenterSon() {
		if (level < 3.0f) {
			level += 0.2;
			setToLevel();
		}
	}

	public void delete() {
		isAlive=false;
		musique.delete();
		com.delete();
		inter.delete();
	}

	private void chargerCom() {
		String rep = "Sons/radio/" + id + "/";
		Random random = new Random();
		try {
			com.charger(rep + "com_" + (random.nextInt(nbCom) + 1) + ".wav");
		} catch (SoundException e) {
			System.err.println("Erreur chargement Commentaires radio");
		}
	}
	
	private void chargerZike() {
		String rep = "Sons/radio/" + id + "/";
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
		String rep = "Sons/radio/" + id + "/";

		BufferedReader mani = null;
		// On lit le fichier comme d'ab
		try {
			mani = new BufferedReader(new FileReader(rep + "manifeste.cfg"));
			String line = null;
			try {
				while ((line = mani.readLine()) != null) {
					if (line.contains("com")) {
						nbCom = Integer.valueOf(line.substring(line
								.indexOf(" ") + 1));
					} else if (line.contains("zike")) {
						nbZike = Integer.valueOf(line.substring(line
								.indexOf(" ") + 1));
					}
				}
			} catch (IOException e) {
				System.out.println("Erreur lecture fichier");
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			System.out.println("Erreur chargement fichier");
			e.printStackTrace();
		}

		try {
			mani.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
