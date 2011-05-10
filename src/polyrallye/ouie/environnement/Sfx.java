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

	public double distance;
	protected double realDistance;

	protected boolean isAlive;
	protected boolean isFixe;
	protected boolean isPaused;
	protected boolean isNull;

	private float gain;

	public Sfx() {
		isNull = true;
		isFixe = false;
		isPaused = true;
	}

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
		isFixe = false;
		isPaused = false;
		isNull = false;
		gain = 0.7f;
	}

	public Sfx(String rep, int nombre, long intervalle, boolean tt) {
		this(rep, nombre, intervalle);
		isFixe = true;
	}

	public Sfx(String rep, int nombre, long intervalle, boolean tt, float g) {
		this(rep, nombre, intervalle, tt);
		gain = g;
	}

	public void run() {
		if (!isNull) {

			Random random = new Random();
			int position = 0;

			while (isAlive) {
				try {
					if (!isPaused)
						temp.charger(rep + "sfx_"
								+ (random.nextInt(nombre) + 1) + ".wav");
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

				if (isFixe) {
					temp.setPosition(0, 0, 0);
					temp.setGain(gain);
				}

				// On joue le son
				if (!isPaused)
					temp.play();
				realDistance = distance;
				// On attend que le son se termine
				while (temp.isPlaying()) {
					if (!isFixe && distance != realDistance) {
						positionY -= distance - realDistance;
						realDistance = distance;
						temp.setPosition(positionX, positionY, positionZ);
					}
					Multithreading.dormir(20);
				}
				// On le supprimme
				if (!isPaused)
					temp.delete();
				Multithreading.dormir(intervalle * 1000);
			}
		}
	}

	public void setVitesse(float t) {
		vitesse = t;
		if (isFixe && t < 18)
			isPaused = true;
		else if (isFixe)
			isPaused = false;
	}

	public void setDistance(double d) {
		distance = d;
	}

	public void tuer() {
		if (!isNull)
			temp.delete();
		isAlive = false;
	}

	public void pause(boolean b) {
		isPaused = b;
		if(b)
			temp.stop();
	}
}
