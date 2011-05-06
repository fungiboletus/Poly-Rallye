package polyrallye.ouie.environnement;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import polyrallye.ouie.utilitaires.Sound;
import polyrallye.ouie.utilitaires.Sound.SoundException;

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
		BufferedReader mani = null;
		// On lit le fichier
		try {
			mani = new BufferedReader(new FileReader(rep + "manifeste.cfg"));
			String line = null;
			try {
				while ((line = mani.readLine()) != null) {
					if (line.contains(env)) {
						this.environnement = (line
								.substring(line.indexOf(" ") + 1));
					}
				}
				// On remet le terrain a defaut si on a pas de sons specifiques
				if (environnement == null) {
					environnement = "vehicule";
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
		son = new Sound(rep + environnement + "_" + (random.nextInt(5) + 1)
				+ ".wav");

		// Configuration du son
		son.setGain(2f);
		son.setPosition(0, 0, 0);
	}

	public void play() {
		son.play();
		while (son.isPlaying());
		repick();
		son.setGain(2f);
		son.setPosition(0, 0, 0);
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
