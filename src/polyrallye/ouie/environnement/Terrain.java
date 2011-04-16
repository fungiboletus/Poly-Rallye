package polyrallye.ouie.environnement;

import java.util.Random;
import java.util.Scanner;

import polyrallye.ouie.utilitaires.Sound;
import polyrallye.ouie.utilitaires.Sound.SoundException;

public class Terrain {
	protected String terrain;
	
	protected Sound son;
	protected Sound tournant;
	
	protected Double frottement;
	
	protected Sfx sfx;
	
	public Terrain(String t) {
		terrain=t;
		Random random = new Random();
			String rep = "Sons/terrain/"+terrain+"/";
			System.out.println(rep);
			son = new Sound(rep+terrain+"_"+(random.nextInt(3)+1)+".wav");
			son.setLoop(true);
			son.setGain(1.0f);
			
			tournant = new Sound(rep+"derapage.wav");
			tournant.setLoop(true);
			
			sfx = new Sfx("Sons/terrain/sfx/",12,20,true);
		
	}
	
	public void play() {
		son.play();
		tournant.play();
		tournant.pause(true);
		sfx.run();
	}
	
	public void setVitesse(double vitesse) {
		float pitch = (float)vitesse / 50;
		son.setPitch(pitch);
	}
	
	public void change(String t) {
		
	}
	
	public void playTourne() {
		tournant.pause(false);
		tournant.setGain(1.2f);
	}
	
	public void stopTourne() {
		
		float gain = tournant.getGain();
		gain -= 0.05f;
		
		if (gain < 0.0) {
			tournant.pause(true);
		}
		else {
			tournant.setGain(gain);
		}
	}
	
	public void stop() {
		sfx.tuer();
		son.stop();
	}
	
	public static void main(String[] args) {
		Terrain t = new Terrain("terre");
		t.play();
		Scanner sc = new Scanner(System.in);
		boolean b = true;
		while(b) {
			System.out.print("Vitesse ? ");
			float temp = sc.nextFloat();
			if (temp==-1)
				b=false;
			else if (temp==0)
				t.playTourne();
			else if (temp==1)
				t.stopTourne();
				else
			t.setVitesse(temp);
			}
		}
		
	
	

}
