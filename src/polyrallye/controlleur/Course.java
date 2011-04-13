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
import sun.management.Sensor;
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
	protected Sound ok;

	protected int devonsNousTourner = 10000000;
	
	protected boolean virageDroite;
	
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
		
		
		
		final Thread canard = new Thread() {
			public void run() {
				terrain.play();
			}
		};
		canard.start();

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
				if (entreesCourse.echap) {
					canard.stop();
					environnement.stop();
					terrain.stop();
					sMoteur.stop();
					timerOrganisateur.cancel();
					Main.changerGestionEntrees(GestionEntreesMenu.getInstance());
				}
				
				// Gestion du temps
				org.lwjgl.util.Timer.tick();
				float tempsTmp = timerCompteur.getTime();
				float deltaTemps = tempsTmp - temps;
				temps = tempsTmp;

				Transmission t = voiture.getTransmission();

				if (entreesCourse.isAccelere()) {
					regime += t.getCoefCourant() * 1.25;

				} else {
					regime -= 27.0f;
				}

				if (entreesCourse.isFreine()) {
					regime -= 50.0f;
					
					/*if (devonsNousTourner > 1) {
						if (virageDroite == false) {
							gauche.play();
						} else {
							droite.play();
						}
					}*/
				}

				if (entreesCourse.isGauche() || entreesCourse.isDroite()) {
					regime -= 12.0f;
					terrain.playTourne();
					/*// Si ça fait trop longtemps que l'on freine
					if (--devonsNousTourner < 0) {
						
						if (devonsNousTourner < 80) {
							// CRASH
							regime = 8000; // Pour avoir un retour sonore hein							
						}
					}
					else {
						if ((entreesCourse.isGauche() && virageDroite == false) || entreesCourse.isDroite() && virageDroite) {
							if (devonsNousTourner == 0) {
								ok.play();
								devonsNousTourner = 10000000;
							}
						} else {
							// CRASH
							regime = 8000;
						}
					}*/
				} else {
					terrain.stopTourne();
				}

				if (entreesCourse.isRapportInf()) {
					if (t.retrograder()) {
						if (t.getCoefCourant() < 0.01) {
							t.passerVitesse();
						} else {
							regime *= 1.2f;
							sMoteur.passageRapport();							
						}
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
		
		// TODO
		ok = new Sound("Sons/divers/freine.wav");

		new Thread() {
			public void run() {

				while (true) {

					long d = Random.unsignedDelta(4, 10) * 1000;

					Main.log("" + d);

					Multithreading.dormir(d);
					
					freine.play();

					virageDroite = Random.unsignedDelta(1, 2) == 1;
					
					devonsNousTourner = Random.unsignedDelta(120, 400);

					Main.log("salut");
				}
			}
		};//.start();

	}
}
