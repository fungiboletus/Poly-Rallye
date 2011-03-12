package polyrallye.ouie.environnement;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import org.lwjgl.Sys;

import polyrallye.ouie.SoundScape;
import polyrallye.ouie.WaveData;


public class Meteo {

	protected String etat;
	protected String environnement;
	protected int env;
	protected int sfx;
	protected int sfx_rate;
	protected float[] velocity;
	protected boolean isPlaying;

	public Meteo() {
		super();
		etat = "Clair";
		environnement = "defaut";
		
		SoundScape.create();
		SoundScape.setListenerPosition(0f, 0f, 0f);

		
		velocity = new float[3];
		velocity[0]=0f;
		velocity[1]=0f;
		velocity[2]=0f;
		
		isPlaying = false;
		env = -1;
		sfx = -1;
		sfx_rate = -1;
		
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

	private void sfx(String rep) {
		String path = rep+"sfx/";
		
		int sfx_temp;
		long time;
		
		Random random = new Random();
		System.out.println("SFX ; env : "+env+" "+SoundScape.isPlaying(env));
		while (isPlaying)
		{
			time = System.nanoTime();
			long temp=System.nanoTime() - time;
			while (temp<10000000)
			{
				System.out.println(temp);
				temp = System.nanoTime() - time;
			}
			if (isPlaying)
			{
				System.out.println(path+"sfx_"+(random.nextInt(sfx)+1)+".wav");
				sfx_temp = SoundScape.makeSound(path+"sfx_"+(random.nextInt(sfx)+1)+".wav");
				
				SoundScape.setSoundPosition(sfx_temp, 0, 0, 0);
				
				SoundScape.play(sfx_temp);
				//while(SoundScape.isPlaying(sfx_temp));
				//SoundScape.deleteSoundSource(sfx_temp);
				
				Scanner sc = new Scanner(System.in);
				sc.next();
			}
		}
		
		System.out.println("SFX OUT");
		
		
	}
	
	public void play() {

		String rep = "Sons/Meteo/" + etat+"/";
		BufferedReader mani = null;		
		//On lit le fichier
		try {
			mani = new BufferedReader(new FileReader(rep + "manifeste.cfg"));
			String line = null;
			try {
				while ((line = mani.readLine()) != null) {
					if (line.contains(environnement)) {
						env = Integer.valueOf(line
								.substring(line.indexOf(" ") + 1));
					} else if (line.contains("random")) {
						sfx_rate = Integer.valueOf(line
								.substring(line.indexOf(" ") + 1));
					} else if (line.contains("sfx")) {
						sfx  = Integer.valueOf(line
								.substring(line.indexOf(" ") + 1));
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
		Random random = new Random();
		//On charge un son au pif
		if(env>-1)
		{

			System.out.println(rep+environnement+"_"+(random.nextInt(env)+1)+".wav");
			env = SoundScape.makeSound(rep+environnement+"_"+(random.nextInt(env)+1)+".wav");
		}
		//position (middle)
		SoundScape.setSoundPosition(env,  0f, 0f, -8f);
		//loop
		SoundScape.setLoop(env, true);
		//Son
		SoundScape.setGain(env, 0.4f);
		//On le joue pour la vie
		isPlaying = true;
		SoundScape.play(env);
		
		//On active les sfx
		//sfx(rep);
		

	}

	
	public void stop() {
		isPlaying=false;
		SoundScape.stop(env);
		

	}

}
