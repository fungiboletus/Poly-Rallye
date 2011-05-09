package polyrallye.ouie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import polyrallye.ouie.environnement.Sfx;
import polyrallye.ouie.utilitaires.Sound;
import polyrallye.ouie.utilitaires.Sound.SoundException;

public class Copilote {
	
	private int id;
	private Sound gauche;
	private Sound droite;
	private Sound freine;
	private Sfx bullshit;
	private boolean isPipelette;
	
	private int[] nombres;
	
	public Copilote() {
		String rep = "Sons/copilote/";
		int nb = (new File(rep).list()).length;
		Random random = new Random();
		id = random.nextInt(nb)+1;
		
		isPipelette = false;
		nombres = new int[4];
		//On évite les faux manifeste
		for (int i = 0; i < nombres.length; i++) {
			nombres[i]=1;
			if(i==3)
				nombres[i]=0;
		}
		
		rep+=id+"/";
		
		BufferedReader mani = null;
		// On lit le fichier comme d'ab
		try {
			mani = new BufferedReader(new FileReader(rep + "manifeste.cfg"));
			String line = null;
			try {
				while ((line = mani.readLine()) != null) {
					if (line.contains("gauche")) {
						nombres[0] = Integer.valueOf(line.substring(line
								.indexOf(" ") + 1));
					} else if (line.contains("sfx")) {
						nombres[4] = Integer.valueOf(line.substring(line
								.indexOf(" ") + 1));
					} else if (line.contains("droite")) {
						nombres[1] = Integer.valueOf(line.substring(line
								.indexOf(" ") + 1));
					} else if (line.contains("freine")) {
						nombres[2] = Integer.valueOf(line.substring(line
								.indexOf(" ") + 1));
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

		gauche = new Sound(rep+"gauche_"+random.nextInt(nombres[0])+".wav");
		droite = new Sound(rep+"droite_"+random.nextInt(nombres[1])+".wav");
		freine = new Sound(rep+"freine_"+random.nextInt(nombres[2])+".wav");
		
		if (nombres[3]!=0)
		bullshit = new Sfx(rep+"sfx/", nombres[3], 5, true);
		else
			bullshit = new Sfx(); 

		bullshit.pause(true);
	}
	
	public void togglePipelette() {
		String rep = "Sons/copilote/"+id+"/";
		if (isPipelette) {
			isPipelette = false;
			bullshit.pause(true);

			if (new File(rep+"stfu.wav").exists()) {
				Sound stfu = new Sound(rep+"stfu.wav");
				stfu.playAndWait();
				stfu.delete();
			}
		}
		else {
			isPipelette=true;
			bullshit.pause(false);
		}
	}
	
	public void playGauche() {
		String rep = "Sons/copilote/"+id+"/";
		Random random = new Random();
		if (isPipelette)
			bullshit.pause(true);
		gauche.playAndWait();
		if (isPipelette)
			bullshit.pause(false);
		gauche.delete();
		try {
			gauche.charger(rep+"gauche_"+random.nextInt(nombres[0])+".wav");
		} catch (SoundException e) {
			System.err.println("Erreur chargement son gauche");
		}
	}
	
	public void playDroite() {
		String rep = "Sons/copilote/"+id+"/";
		Random random = new Random();
		if (isPipelette)
			bullshit.pause(true);
		droite.playAndWait();
		if (isPipelette)
			bullshit.pause(false);
		droite.delete();
		try {
			droite.charger(rep+"droite_"+random.nextInt(nombres[1])+".wav");
		} catch (SoundException e) {
			System.err.println("Erreur chargement son droite");
		}
	}
	
	public void playFreine() {
		String rep = "Sons/copilote/"+id+"/";
		Random random = new Random();
		if (isPipelette)
			bullshit.pause(true);
		freine.playAndWait();
		if (isPipelette)
			bullshit.pause(false);
		freine.delete();
		try {
			freine.charger(rep+"freine_"+random.nextInt(nombres[2])+".wav");
		} catch (SoundException e) {
			System.err.println("Erreur chargement son freine");
		}
	}
		

}
