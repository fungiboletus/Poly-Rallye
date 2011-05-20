package polyrallye.ouie.environnement;

import java.util.Random;

import polyrallye.ouie.utilitaires.Sound;
import polyrallye.utilitaires.Multithreading;

public class Terrain {
	protected String terrain;

	protected Sound son;
	protected Sound tournant;
	
	protected double distance;

	protected Sfx sfx;

	public Terrain(String t) {
		terrain = t;
		Random random = new Random();
		String rep = "Sons/terrain/" + terrain + "/";
		son = new Sound(rep + terrain + "_" + (random.nextInt(3) + 1) + ".wav");
		son.setLoop(true);
		son.setGain(1.5f);


		tournant = new Sound(rep + "derapage.wav");
		tournant.setLoop(true);

		sfx = new Sfx("Sons/terrain/sfx/", 12, 20, true);

	}

	public void play() {
		son.play();
		tournant.play();
		tournant.pause(true);
		sfx.start();
		sfx.pause(true);
	}

	public void setVitesse(double vitesse) {
		if (vitesse < 50 * 1000 / 3600)
			sfx.pause(true);
		else
			sfx.pause(false);
		float pitch = (float) vitesse / (50 * 1000 / 3600);

		son.setPitch(pitch);
	}

	public void change(String t) {
		son.stop();
		son.delete();
		
		
		terrain = t;
		Sound sonTemp = new Sound();
		Thread ttemp = new Fade(sonTemp) {
			@Override
			public void run() {
				Random random = new Random();
				String rep = "Sons/terrain/" + terrain + "/";
				String temp = rep + terrain + "_" + (random.nextInt(3) + 1) + ".wav";

				sonTemp = new Sound(temp);
				sonTemp.setLoop(true);
				sonTemp.setGain(1.5f);
				sonTemp.setPosition(0, 0, 0);
				float positionX =0;
				float positionYi = 1000 - random.nextInt(50);
				float positionYo = 0;
				float positionZ = 5 - random.nextInt(10);

				sonTemp.setPosition(positionX, positionYi, positionZ);
				sonTemp.setReferenceDistance(200);
				double realDistance = distance;
				sonTemp.play();
				while (positionYo>-2000) {
					if (distance != realDistance) {
						if(positionYi>0)
						positionYi -= distance - realDistance;
						positionYo -= distance - realDistance;
						realDistance = distance;
						sonTemp.setPosition(positionX, positionYi, positionZ);
						son.setPosition(positionX, positionYo, positionZ);
					}
					Multithreading.dormir(20);
				}
				son.stop();
				son.delete();
				son = sonTemp;
				
				tournant.delete();
				tournant = new Sound(rep + "derapage.wav");
				tournant.setLoop(true);

			}
		};
		ttemp.start();
		
	}
	
	public void playFrottement(float gainFrottement) {
		tournant.pause(false);
		tournant.setGain(gainFrottement);
	}

	public void stopFrottement() {

		float gain = tournant.getGain();
		gain -= 0.05f;

		if (gain < 0.0) {
			tournant.pause(true);
		} else {
			tournant.setGain(gain);
		}
	}

	public void setDistance(double d) {
		distance = d * 100;
	}

	public void delete() {
		sfx.tuer();
		son.delete();
		tournant.delete();
	}

	// public static void main(String[] args) {
	// Terrain t = new Terrain("terre");
	// t.play();
	// Scanner sc = new Scanner(System.in);
	// boolean b = true;
	// while(b) {
	// System.out.print("Vitesse ? ");
	// float temp = sc.nextFloat();
	// if (temp==-1)
	// b=false;
	// else
	// t.setVitesse(temp);
	// }
	// }
	private class Fade extends Thread {
		Sound sonTemp;
		
		public Fade(Sound sonTemp) {
			this.sonTemp=sonTemp;
		}
	
	}

}
