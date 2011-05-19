package polyrallye.ouie.environnement;

import java.util.Random;

import polyrallye.ouie.utilitaires.Sound;
import polyrallye.ouie.utilitaires.Sound.SoundException;
import polyrallye.utilitaires.LectureFichier;

public class Crash {
	protected String environnement;

	protected Sound son;

	public Crash(String env) {

		environnement = null;
		son  = null;
		changeEnvironnement(env);

	}

	public void changeEnvironnement(String env) {
		if (son !=null && son.isAlive())
			son.delete();
		// On va charger dans le fichier les config
		String rep = "Sons/Crash" + "/";
		// On lit le fichier
		String[] lectureManifeste = new String[1];
		lectureManifeste[0] = env;


		lectureManifeste = new LectureFichier(rep).lire("manifeste.cfg",
				lectureManifeste);
		if (lectureManifeste[0]!=null)
			this.environnement = (lectureManifeste[0]
					.substring(lectureManifeste[0].indexOf(" ") + 1));
		else
			environnement = "vehicule";


		// On prend un son au pif parmi ceux disponibles
		Random random = new Random();
		son = new Sound(rep + environnement + "_" + (random.nextInt(5) + 1)
				+ ".wav");

		// Configuration du son
		son.setGain(2f);
		son.setPosition(0, 0, 0);
	}

	public void play() {
		new Thread() {
			public void run() {
				son.play();
				son.playAndWait();
				repick();
				son.setGain(2f);
				son.setPosition(0, 0, 0);				
			}
		}.start();
	}

	private void repick() {
		Random random = new Random();
		try {
			son.charger("Sons/Crash/" + environnement + "_"
					+ (random.nextInt(5) + 1) + ".wav");
		} catch (SoundException e) {
			System.out.println("Erreur changement son crash");
		}
	}

	public void delete() {
		son.delete();
	}

}
