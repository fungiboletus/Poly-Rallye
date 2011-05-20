package polyrallye.ouie.environnement;

import java.util.Random;

import polyrallye.ouie.utilitaires.Sound;
import polyrallye.utilitaires.LectureFichier;


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

	public Meteo(String et, String en) {
		this();
		etat = et;
		environnement = en;
	}

	public void play() {

		if (!etat.equals("clair")) {
		// On va charger dans le fichier les config
		String rep = "Sons/meteo" + "/" + etat + "/";

		// On lit le fichier
		String[] lectureManifeste = new String[2];
		lectureManifeste[0] = environnement;
		lectureManifeste[1] = "sfx";


		lectureManifeste = new LectureFichier(rep).lire("manifeste.cfg",
				lectureManifeste);
		if (lectureManifeste[0]!=null)
			this.env = Integer.valueOf(lectureManifeste[0].substring(lectureManifeste[0]
					.indexOf(" ") + 1));

		if (lectureManifeste[1]!=null) {
			this.randSfx = Integer.valueOf(lectureManifeste[1].substring(lectureManifeste[1]
					.indexOf(" ") + 1));
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
		if (sfx != null) sfx.tuer();
	}

}
