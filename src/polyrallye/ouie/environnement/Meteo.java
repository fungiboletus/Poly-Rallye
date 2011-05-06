package polyrallye.ouie.environnement;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import polyrallye.ouie.utilitaires.Sound;


public class Meteo {

	protected String etat;
	protected String environnement;
	protected int env;
	protected int randSfx;

	protected Sound meteo;
	protected Sfx sfx;

	public Meteo() {
		super();
		etat = "clair";
		environnement = "defaut";

		env = -1;
		randSfx = -1;

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

		if (!etat.equals("clair")) {
		// On va charger dans le fichier les config
		String rep = "Sons/meteo" + "/" + etat + "/";
		BufferedReader mani = null;
		// On lit le fichier
		try {
			mani = new BufferedReader(new FileReader(rep + "manifeste.cfg"));
			String line = null;
			try {
				while ((line = mani.readLine()) != null) {
					if (line.contains(environnement)) {
						this.env = Integer.valueOf(line.substring(line
								.indexOf(" ") + 1));
					} else if (line.contains("sfx")) {
						this.randSfx = Integer.valueOf(line.substring(line
								.indexOf(" ") + 1));
					}
				}
				// On remet le terrain a defaut si on a pas de sons specifiques
				if (env == -1) {
					environnement = "defaut";
					mani.close();
					mani = new BufferedReader(new FileReader(rep + "manifeste.cfg"));
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
			System.out.println("Erreur fermeture fichier");
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
		
		//Création du sfx
		rep+="sfx/";
		sfx = new Sfx(rep,randSfx);
		sfx.start();
		
		}

	}

	public void delete() {
		if (meteo != null) {
			meteo.delete();			
		}
	}

}
