package polyrallye.controlleur;

import java.util.TimerTask;

import polyrallye.modele.voiture.Transmission;
import polyrallye.modele.voiture.Voiture;
import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Klaxon;
import polyrallye.ouie.SonMoteur;
import polyrallye.ouie.environnement.Crash;
import polyrallye.ouie.environnement.Environnement;
import polyrallye.ouie.environnement.Terrain;
import polyrallye.ouie.utilitaires.Sound;
import polyrallye.utilitaires.Multithreading;
import t2s.util.Random;

public class Course implements ActionMenu {

	/**
	 * Le timer qui excécute la course.
	 */
	protected java.util.Timer timerOrganisateur;
	protected org.lwjgl.util.Timer timerCompteur;

	protected float temps;

	protected GestionEntreesCourse entreesCourse;

	protected SonMoteur sMoteur;
	protected Environnement environnement;
	protected Terrain terrain;

	protected Voiture voiture;
	
	protected Crash crash;
	protected Klaxon klaxon;

	// Démonstration, ça ne restera pas
	protected float regime;

	protected Sound gauche;
	protected Sound droite;
	protected Sound freine;

	public Course(Voiture voiture) {
		this.voiture = voiture;
	}

	@Override
	public void actionMenu() {
		entreesCourse = new GestionEntreesCourse();

		Main.changerGestionEntrees(entreesCourse);

		environnement = new Environnement("village", "jour", "clair");
		terrain = new Terrain("terre");
		sMoteur = new SonMoteur(voiture);
		
		crash = new Crash(environnement.getType());
		klaxon = new Klaxon(voiture.getNomComplet());
		
		
		
		new Thread() {
			public void run() {
				terrain.play();
			}
		}.start();

		System.out.println("ok");
		environnement.play();
		sMoteur.play();

		regime = 850.0f;
		voiture.getTransmission().passerVitesse();

		timerOrganisateur = new java.util.Timer();
		timerCompteur = new org.lwjgl.util.Timer();

		org.lwjgl.util.Timer.tick();
		temps = timerCompteur.getTime();

		TimerTask tt = new TimerTask() {

			@Override
			public void run() {
				// Gestion du temps
				org.lwjgl.util.Timer.tick();
				float tempsTmp = timerCompteur.getTime();
				float deltaTemps = tempsTmp - temps;
				temps = tempsTmp;

				Transmission t = voiture.getTransmission();

				if (entreesCourse.isAccelere()) {
					regime += t.getCoefCourant() * 1.25;

				} else {
					regime -= 30.0f;
				}

				if (entreesCourse.isFreine()) {
					regime -= 50.0f;
				}

				if (entreesCourse.isGauche() || entreesCourse.isDroite()) {
					regime -= 17.0f;
					terrain.playTourne();
				} else {
					terrain.stopTourne();
				}

				if (entreesCourse.isRapportInf()) {
					if (t.retrograder()) {
						regime *= 1.2f;
						sMoteur.passageRapport();
					}
				}

				if (entreesCourse.isRapportSup()) {
					if (t.passerVitesse()) {
						regime *= 0.625f;
						sMoteur.passageRapport();
					}
				}

				if (regime < 850) {
					regime = 850;
				} else if (regime > 9300) {
					regime = 9000;
				}
				
				if(entreesCourse.klaxon) {
					klaxon.play();
				}
				else
					klaxon.pause();

				sMoteur.setRegime(regime, entreesCourse.isAccelere());
				// terrain.setVitesse(regime / 3.0f);
				// TODO mettre le code de abdoul (oui monsieur)
			}
		};

		// À 50Hz, comme le courant EDF
		timerOrganisateur.schedule(tt, 0, 20);

		gauche = new Sound("Sons/divers/gauche.wav");
		//gauche.setGain(18.0f);
		droite = new Sound("Sons/divers/droite.wav");
		//droite.setGain(18.0f);
		
		freine = new Sound("Sons/divers/freine.wav");

		new Thread() {
			public void run() {

				while (true) {

					long d = Random.unsignedDelta(4, 10) * 1000;

					Main.log("" + d);

					Multithreading.dormir((long) (d*0.9));
					
					freine.play();

					Multithreading.dormir((long) (d*0.1));
					
					Main.log("salut");

					if (Random.unsignedDelta(1, 2) == 1) {
						gauche.play();
						if (!hasTourned("gauche")) {
							megaCrash();	
							
						}
					} else {
						droite.play();
						if (!hasTourned("droite")) {
							megaCrash();
						}
					}
				}
			}
			protected boolean hasTourned(String sens) {
				double time = 0;
				while (time<2) {
					if ((sens.equals("gauche") && entreesCourse.isGauche()) || (sens.equals("droite") && entreesCourse.isDroite()))
						return true;
					time+=0.1;
					Multithreading.dormir(100);
				}
				return false;
			}
			protected void megaCrash() {
				regime = 10;
				sMoteur.setRegime(10, false);
				sMoteur.passageRapport();
				crash.play();

			}
		}.start();

	}
}
