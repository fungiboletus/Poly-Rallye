package polyrallye.ouie.environnement;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import org.lwjgl.Sys;

import polyrallye.ouie.Sound;
import polyrallye.ouie.SoundScape;
import polyrallye.ouie.WaveData;

public class Meteo {

	protected String etat;
	protected String environnement;
	protected int env;
	protected int sfx;

	protected Sound meteo;

	public Meteo() {
		super();
		etat = "clair";
		environnement = "defaut";

		env = -1;
		sfx = -1;

	}

	public Meteo(String et) {
		this();
		etat = et;
	}

	public Meteo(String et, String en) {
		this();
		etat = et;
		environnement = en;
	}

	public void play() {

		// On va charger dans le fichier les config
		String rep = "Sons/meteo" + "/" + etat + "/";
		BufferedReader mani = null;
		// On lit le fichie
		try {
			mani = new BufferedReader(new FileReader(rep + "manifeste.cfg"));
			String line = null;
			try {
				while ((line = mani.readLine()) != null) {
					if (line.contains(environnement)) {
						this.env = Integer.valueOf(line.substring(line
								.indexOf(" ") + 1));
					}
				}
				// On remet le terrain a defaut si on a pas de sons specifiques
				if (env == -1) {
					environnement = "defaut";
					line = null;
					while ((line = mani.readLine()) != null) {
						if (line.contains(environnement)) {
							this.env = Integer.valueOf(line.substring(line
									.indexOf(" ") + 1));
						}
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

		meteo = new Sound(rep + environnement + "_" + (random.nextInt(env) + 1)
				+ ".wav");

		// Configuration du son
		meteo.setLoop(true);
		meteo.setGain(0.3f);
		meteo.setPosition(0, 0, 0);

		meteo.play();

	}

	public void stop() {

	}

}
