package polyrallye.ouie.environnement;

import java.util.Random;

import polyrallye.ouie.utilitaires.Sound;
import polyrallye.ouie.utilitaires.Sound.SoundException;
import polyrallye.utilitaires.Multithreading;

public class Sfx extends Thread {

	protected String rep;
	protected int nombre;
	protected long intervalle;

	protected float vitesse;
	protected Sound temp;

	protected float positionX;
	protected float positionY;
	protected float positionZ;

	public float distance;
	protected float realDistance;

	protected boolean isAlive;

	public Sfx(String rep, int nombre) {
		this(rep, nombre, 10);
	}

	public Sfx(String rep, int nombre, long intervalle) {
		super();
		this.rep = rep;
		this.nombre = nombre;
		this.intervalle = intervalle;
		isAlive = true;
		vitesse = 0;
		temp = new Sound();
		positionX = 0;
		positionY = 0;
		positionZ = 0;
		distance = 0;
		realDistance = 0;
	}

	public void run() {

		Random random = new Random();
		int position = 0;

		while (isAlive) {
			System.out.println("playing " + rep + "sfx_"
					+ (random.nextInt(nombre) + 1) + ".wav" + "?");
			try {
				temp.charger(rep + "sfx_" + (random.nextInt(nombre) + 1)
						+ ".wav");
			} catch (SoundException e1) {
			}
			// Volume
			temp.setGain(1f);
			// Position initiale du son
			positionX = 5 - random.nextInt(10);
			positionY = 5 - random.nextInt(10);
			positionZ = 5 - random.nextInt(10);
			temp.setPosition(positionX, positionY, positionZ);
			// Distance d'eloignement ?
			temp.setReferenceDistance(200);

			// On joue le son
			temp.play();
			realDistance = distance;
			// On attend que le son se termine
			while (temp.isPlaying()) {
				if (distance != realDistance) {
					positionY -= distance - realDistance;
					realDistance=distance;
				temp.setPosition(positionX, positionY, positionZ);
				}
			}
			// On le supprimme
			temp.delete();
			Multithreading.dormir(intervalle * 1000);
		}

	}

	public void setVitesse(float t) {
		vitesse = t;
	}

	public void setDistance(float d) {
		distance = d;
	}

	public void tuer() {
		isAlive = false;
	}
}
