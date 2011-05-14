package polyrallye.ouie.environnement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import polyrallye.ouie.utilitaires.Sound;

public class Environnement {

	protected String type;
	protected String temps;
	protected Meteo meteo;
	protected Crash crash;

	protected int randAmb;
	protected int randSfx;
	protected int intervalle;

	protected Sound ambiance;
	protected Sfx sfx;

	public Environnement(String type, String temps, String meteo) {
		super();
		
		if (type == null) type = "plaine";
		if (temps == null) temps = "jour";
		if (meteo == null) meteo = "clair";
		
		this.type = type;
		this.temps = temps;
		this.meteo = new Meteo(meteo, type);

		// Par defaut
		intervalle = 10;
		String extSfx = null;

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
					} else if (line.contains("sfxout")) {
						extSfx = line.substring(line.indexOf(" ") + 1);
					} else if (line.contains("random")) {
						this.intervalle = Integer.valueOf(line.substring(line
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
		String temp = rep + temps + "_" + (random.nextInt(randAmb) + 1)
				+ ".wav";
		System.out.println(temp);
		ambiance = new Sound(temp);
		ambiance.setLoop(true);
		ambiance.setGain(0.4f);
		ambiance.setPosition(0, 0, 0);

		// Création du sfx
		// Si le sfx est extérieur au dossier
		if (extSfx != null && !extSfx.equals("null"))
			rep = "Sons/" + extSfx + "/";
		// Procédure normale
		if (extSfx != null && !extSfx.equals("null") && temps.equals(temps)
				&& new File(rep + "sfx_" + temps).exists()) {
			randSfx = new File(rep + "sfx_nuit").listFiles().length;
			rep += "sfx_nuit/";
		} else
			rep += "sfx/";
		
		//Pas de sfx pour certains niveau
		if (randSfx==0 || (extSfx != null && extSfx.equals("null")))
			sfx = new Sfx();
		else
			sfx = new Sfx(rep, randSfx, intervalle);

		// Création Crash
		crash = new Crash(type);

	}

	public void change(String env) {
		sfx.tuer();

		type = env;

		// intervalle par defaut (secondes ?)
		intervalle = 10;
		String extSfx = null;

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
					} else if (line.contains("sfxout")) {
						extSfx = line.substring(line.indexOf(" ") + 1);
					} else if (line.contains("random")) {
						this.intervalle = Integer.valueOf(line.substring(line
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
		Sound sonTemp = null;
		Random random = new Random();
		String temp = rep + temps + "_" + (random.nextInt(randAmb) + 1)
				+ ".wav";
		System.out.println(temp);
		sonTemp = new Sound(temp);
		sonTemp.setLoop(true);
		sonTemp.setGain(0.4f);
		sonTemp.setPosition(0, 0, 0);

		// Création du sfx
		// Si le sfx est extérieur au dossier
		if (extSfx != null)
			rep = "Sons/" + extSfx + "/";
		if (temps.equals(temps) && new File(rep + "sfx_" + temps).exists()) {
			randSfx = new File(rep + "sfx_nuit").listFiles().length;
			rep += "sfx_nuit/";
		} else
			rep += "sfx/";

		if (randSfx==0)
			sfx = new Sfx();
		else
			sfx = new Sfx(rep, randSfx, intervalle);

		// On fade
		sonTemp.fadeIn(100, 0.4f);
		ambiance.fadeOut(100);
		while (ambiance.isPlaying()) {
			System.out.println("Ambiance is playing !");
		}
		ambiance.delete();
		ambiance = sonTemp;

		// Creation crash
		crash.changeEnvironnement(type);

	}

	public void play() {
		ambiance.play();
		sfx.start();
		meteo.play();
	}

	public void fade() {
		ambiance.setVelocity(0, 0, 100f);
	}

	public void setDistance(double d) {
		sfx.setDistance(d);
	}

	public void delete() {
		ambiance.delete();
		sfx.tuer();
		meteo.delete();
		crash.delete();
	}

	public void setVitesse(double v) {
		sfx.setVitesse((float) v);
	}

	public String getType() {
		return type;
	}

	public void playCrash() {
		crash.play();
	}

	public static void main(String[] args) {
		final Environnement test = new Environnement("mer", "jour", "pluie");
		test.setVitesse(300f);
		test.play();
		Scanner sc = new Scanner(System.in);

		while (!sc.next().equals("e")) {

		}
		Timer t = new Timer();

		TimerTask tt = new TimerTask() {

			@Override
			public void run() {
				// System.out.println(SonMoteur.accelere);
				System.out.println("acc");
				double dis = test.sfx.distance;
				dis += 10;
				test.sfx.setDistance(dis);

			}
		};

		t.schedule(tt, 0, 10);

	}

}
