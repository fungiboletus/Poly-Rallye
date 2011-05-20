package polyrallye.ouie.environnement;

import java.io.File;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import polyrallye.controlleur.Main;
import polyrallye.ouie.utilitaires.Sound;
import polyrallye.ouie.utilitaires.Sound.SoundException;
import polyrallye.utilitaires.LectureFichier;
import polyrallye.utilitaires.Multithreading;

public class Environnement {

	protected String type;
	protected String temps;
	protected Meteo meteo;
	protected Crash crash;

	protected int randAmb;
	protected int randSfx;
	protected int intervalle;
	protected double distance;

	protected Sound ambiance;
	protected Sfx sfx;

	public Environnement(String type, String temps, String meteo) {
		super();

		if (type == null)
			type = "plaine";
		if (temps == null)
			temps = "jour";
		if (meteo == null)
			meteo = "clair";

		this.type = type;
		this.temps = temps;
		this.meteo = new Meteo(meteo, type);

		// Par defaut
		intervalle = 10;
		String extSfx = null;

		// On va charger dans le fichier les config
		String rep = "Sons/" + type + "/";

		// On lit le fichier
		String[] lectureManifeste = new String[4];
		lectureManifeste[0] = temps;
		lectureManifeste[1] = "sfx";
		lectureManifeste[2] = "out";
		lectureManifeste[3] = "random";

		lectureManifeste = new LectureFichier(rep).lire("manifeste.cfg",
				lectureManifeste);
		if (lectureManifeste[0]!=null)
			this.randAmb = Integer.valueOf(lectureManifeste[0]
					.substring(lectureManifeste[0].indexOf(" ") + 1));

		if (lectureManifeste[1]!=null) {
			this.randSfx = Integer.valueOf(lectureManifeste[1]
					.substring(lectureManifeste[1].indexOf(" ") + 1));
		}
		if (lectureManifeste[2]!=null) {
			extSfx = lectureManifeste[2].substring(lectureManifeste[2]
					.indexOf(" ") + 1);
		}
		if (lectureManifeste[3]!=null) {
			this.intervalle = Integer.valueOf(lectureManifeste[3]
					.substring(lectureManifeste[3].indexOf(" ") + 1));
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

		// Pas de sfx pour certains niveau
		if (randSfx == 0 || (extSfx != null && extSfx.equals("null")))
			sfx = new Sfx();
		else
			sfx = new Sfx(rep, randSfx, intervalle);

		// Création Crash
		crash = new Crash(type);

	}

	public void change(String env) {
		Main.logInfo("Changement environnement de " + type + " vers " + env);
		sfx.tuer();

		type = env;

		// intervalle par defaut (secondes ?)
		intervalle = 10;
		String extSfx = null;

		// On va charger dans le fichier les config
		String rep = "Sons/" + type + "/";

		// On lit le fichier
		String[] lectureManifeste = new String[4];
		lectureManifeste[0] = temps;
		lectureManifeste[1] = "sfx";
		lectureManifeste[2] = "out";
		lectureManifeste[3] = "random";

		lectureManifeste = new LectureFichier(rep).lire("manifeste.cfg",
				lectureManifeste);
		if (lectureManifeste[0]!=null)
			this.randAmb = Integer.valueOf(lectureManifeste[0]
					.substring(lectureManifeste[0].indexOf(" ") + 1));

		if (lectureManifeste[1]!=null) {
			this.randSfx = Integer.valueOf(lectureManifeste[1]
					.substring(lectureManifeste[1].indexOf(" ") + 1));
		}
		if (lectureManifeste[2]!=null) {
			extSfx = lectureManifeste[2].substring(lectureManifeste[2]
					.indexOf(" ") + 1);
		}
		if (lectureManifeste[3]!=null) {
			this.intervalle = Integer.valueOf(lectureManifeste[3]
					.substring(lectureManifeste[3].indexOf(" ") + 1));
		}

		// On prend un son au pif parmi ceux disponibles

		Random random = new Random();
		String temp = rep + temps + "_" + (random.nextInt(randAmb) + 1)
				+ ".wav";
		System.out.println(temp);

		// Création du sfx
		// Si le sfx est extérieur au dossier
		if (extSfx != null)
			rep = "Sons/" + extSfx + "/";
		if (temps.equals(temps) && new File(rep + "sfx_" + temps).exists()) {
			randSfx = new File(rep + "sfx_nuit").listFiles().length;
			rep += "sfx_nuit/";
		} else
			rep += "sfx/";

		if (randSfx == 0)
			sfx = new Sfx();
		else
			sfx = new Sfx(rep, randSfx, intervalle);

		// On fade
		// sonTemp.play();
		// ambiance.fadeOut(100);
		// while (ambiance.isPlaying()) {
		// System.out.println("Ambiance is playing !");
		// }
		// ambiance.delete();
		// ambiance = sonTemp;
		Sound sonTemp = new Sound();
		Thread ttemp = new Fade(sonTemp) {
			@Override
			public void run() {
				Random random = new Random();
				String temp = "Sons/" + type + "/" + temps + "_"
						+ (random.nextInt(randAmb) + 1) + ".wav";
				try {
					sonTemp.charger(temp);
				} catch (SoundException e) {

				}
				sonTemp.setLoop(true);
				sonTemp.setGain(0.4f);
				sonTemp.setPosition(0, 0, 0);
				float positionX = 0;
				float positionYi = 1000 - random.nextInt(50);
				float positionYo = 0;
				float positionZ = 5 - random.nextInt(10);
				sonTemp.setPosition(positionX, positionYi, positionZ);
				sonTemp.setReferenceDistance(200);
				double realDistance = distance;
				sonTemp.play();
				while (positionYo > -2000) {
					if (distance != realDistance) {
						if (positionYi > 0)
							positionYi -= distance - realDistance;
						positionYo -= distance - realDistance;
						realDistance = distance;
						sonTemp.setPosition(positionX, positionYi, positionZ);
						ambiance.setPosition(positionX, positionYo, positionZ);
					}
					Multithreading.dormir(20);
				}
				ambiance.stop();
				ambiance.delete();
				ambiance = sonTemp;
				ambiance.setPosition(0, 0, 0);

			}
		};
		ttemp.start();
		// Creation crash
		crash.changeEnvironnement(type);

		// play
		sfx.start();

	}

	public void play() {
		ambiance.play();
		sfx.start();
		meteo.play();
	}

	public void setDistance(double d) {
		sfx.setDistance(d);
		distance = d * 100;
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
		final Environnement test = new Environnement("devSFX", "jour", "clair");
		test.setVitesse(300f);
		test.play();
		Scanner sc = new Scanner(System.in);

		while (!sc.next().equals("e")) {
			test.change("foret");
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

	private class Fade extends Thread {
		Sound sonTemp;

		public Fade(Sound sonTemp) {
			this.sonTemp = sonTemp;
		}

	}

}
