package polyrallye.ouie.environnement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import polyrallye.ouie.Sound;

public class Environnement {

	protected String type;
	protected String temps;
	protected Meteo meteo;

	protected int randAmb;
	protected int randSfx;

	protected Sound ambiance;
	protected Sfx sfx;

	public Environnement() {
		this("foret", "jour", "vent");
	}

	public Environnement(String type, String temps, String meteo) {
		super();
		this.type = type;
		this.temps = temps;
		this.meteo = new Meteo(meteo, type);

		// On va charger dans le fichier les config
		String rep = "Sons/" + type + "/";
		BufferedReader mani = null;
		// On lit le fichier
		try {
			mani = new BufferedReader(new FileReader(rep + "manifeste.cfg"));
			String line = null;
			try {
				while ((line = mani.readLine()) != null) {
					if (line.contains(temps)) {
						this.randAmb = Integer.valueOf(line.substring(line
								.indexOf(" ") + 1));
					} else if (line.contains("sfx")) {
						this.randSfx = Integer.valueOf(line.substring(line
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

		// On prend un son au pif parmi ceux disponibles
		Random random = new Random();

		ambiance = new Sound(rep + temps + "_" + (random.nextInt(randAmb) + 1)
				+ ".wav");
		ambiance.setLoop(true);
		ambiance.setGain(0.4f);
		ambiance.setPosition(0, 0, 0);

		// Création du sfx
		if (temps.equals(temps) && new File(rep + "sfx_"+temps).exists()) {
			randSfx = new File(rep + "sfx_nuit").listFiles().length;
			rep += "sfx_nuit/";
		} else
			rep += "sfx/";

		sfx = new Sfx(rep, randSfx);

	}

	public void play() {
		ambiance.play();
		sfx.start();
		meteo.play();
	}

	public void fade() {
		ambiance.setVelocity(0, 0, 100f);
	}

	public void stop() {
		ambiance.stop();
		sfx.stop();
		meteo.stop();
	}

	public static void main(String[] args) {
		Environnement test = new Environnement();
		test.play();
		Scanner sc = new Scanner(System.in);

		while (!sc.next().equals("e")) {

		}
		test.fade();
		while (!sc.next().equals("e")) {

		}
		test.stop();

	}

}