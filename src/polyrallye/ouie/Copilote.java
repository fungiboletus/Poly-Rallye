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
	private Sound ok;
	private boolean isPipelette;
	
	private int[] nombres;
	
	public Copilote() {
		String rep = "Sons/copilote/";
		int nb = (new File(rep).list()).length;
		Random random = new Random();
		id = random.nextInt(nb)+1;
		
		isPipelette = false;
		nombres = new int[5];
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
						nombres[3] = Integer.valueOf(line.substring(line
								.indexOf(" ") + 1));
					} else if (line.contains("droite")) {
						nombres[1] = Integer.valueOf(line.substring(line
								.indexOf(" ") + 1));
					} else if (line.contains("ok")) {
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

		gauche = new Sound(rep+"gauche_"+(random.nextInt(nombres[0])+1)+".wav");
		droite = new Sound(rep+"droite_"+(random.nextInt(nombres[1])+1)+".wav");
		freine = new Sound(rep+"freine_"+(random.nextInt(nombres[2])+1)+".wav");
		ok = new Sound(rep+"ok_"+(random.nextInt(nombres[4])+1)+".wav");
		
		if (nombres[3]!=0)
		bullshit = new Sfx(rep+"sfx/", nombres[3], 3, true,3.0f);
		else
			bullshit = new Sfx(); 
		bullshit.start();

		bullshit.pause(true);
	}
	
	public void togglePipelette() {
		String rep = "Sons/copilote/"+id+"/";
		if (isPipelette) {
			isPipelette = false;
			bullshit.pause(true);

			if (new File(rep+"stfu.wav").exists()) {
				Sound stfu = new Sound(rep+"stfu.wav");
				stfu.playAndDelete();
			}
		}
		else {
			isPipelette=true;
			bullshit.pause(false);
		}
	}
	
	public void playOk() {
		String rep = "Sons/copilote/"+id+"/";
		Random random = new Random();
		if (isPipelette)
			bullshit.pause(true);
		ok.play();
		if (isPipelette)
			bullshit.pause(false);
		ok.delete();
		try {
			ok.charger(rep+"ok_"+(random.nextInt(nombres[4])+1)+".wav");
		} catch (SoundException e) {
			System.err.println("Erreur chargement son ok (copilote "+id+")");
		}
	}
	
	public void playCrash() {
		String rep = "Sons/copilote/"+id+"/";
		if (new File(rep+"crash.wav").exists()) {
			Sound crash = new Sound(rep+"crash.wav");
			crash.playAndWait();
			crash.delete();
		}
			
	}
	
	public void playGauche() {
		String rep = "Sons/copilote/"+id+"/";
		Random random = new Random();
		if (isPipelette)
			bullshit.pause(true);
		gauche.play();
		if (isPipelette)
			bullshit.pause(false);
		gauche.delete();
		try {
			gauche.charger(rep+"gauche_"+(random.nextInt(nombres[0])+1)+".wav");
		} catch (SoundException e) {
			System.err.println("Erreur chargement son gauche(copilote "+id+")");
		}
	}
	
	public void playDroite() {
		String rep = "Sons/copilote/"+id+"/";
		Random random = new Random();
		if (isPipelette)
			bullshit.pause(true);
		droite.play();
		if (isPipelette)
			bullshit.pause(false);
		droite.delete();
		try {
			droite.charger(rep+"droite_"+(random.nextInt(nombres[1])+1)+".wav");
		} catch (SoundException e) {
			System.err.println("Erreur chargement son droite(copilote "+id+")");
		}
	}
	
	public void playFreine() {
		String rep = "Sons/copilote/"+id+"/";
		Random random = new Random();
		if (isPipelette)
			bullshit.pause(true);
		freine.play();
		if (isPipelette)
			bullshit.pause(false);
		freine.delete();
		try {
			freine.charger(rep+"freine_"+(random.nextInt(nombres[2])+1)+".wav");
		} catch (SoundException e) {
			System.err.println("Erreur chargement son freine(copilote "+id+")");
		}
	}
	
	public void delete() {
		gauche.delete();
		droite.delete();
		freine.delete();
		bullshit.tuer();
	}
		

}
