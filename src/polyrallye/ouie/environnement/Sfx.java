package polyrallye.ouie.environnement;

import java.util.Random;

import polyrallye.ouie.Sound;
import polyrallye.ouie.Sound.SoundException;

public class Sfx extends Thread {

	protected String rep;
	protected int nombre;
	protected long intervalle;
	
	public Sfx(String rep, int nombre) {
		this(rep,nombre,10);
	}
	
	public Sfx(String rep, int nombre, long intervalle) {
		super();
		this.rep = rep;
		this.nombre = nombre;
		this.intervalle = intervalle;
	}
	
	public void run() {
		Sound temp = new Sound();
		
		Random random = new Random();
		
		while (this.isAlive())
		{
			System.out.println("playing "+rep+"sfx_"+(random.nextInt(nombre)+1)+".wav"+"?");
			try {
				temp.charger(rep+"sfx_"+(random.nextInt(nombre)+1)+".wav");
			} catch (SoundException e1) {
			}
			temp.setGain(2f);
			temp.setPosition(random.nextInt(10), random.nextInt(10), random.nextInt(10));
			temp.play();
			while(temp.isPlaying());
			temp.delete();
			try {
				Thread.sleep(intervalle*1000);
			} catch (InterruptedException e) {}	
		}
	
	}
}
