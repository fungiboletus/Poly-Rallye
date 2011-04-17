package polyrallye.controlleur;

import java.util.TimerTask;

import polyrallye.modele.voiture.Moteur;
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
	
	// Temporaire hein
	protected Thread canard2;
	
	public Course(Voiture voiture) {
		this.voiture = voiture;
	}

	@Override
	public void actionMenu() {
		entreesCourse = new GestionEntreesCourse();

		Main.changerGestionEntrees(entreesCourse);

		environnement = new Environnement("village", "jour", "clair");
		terrain = new Terrain("asphalt");
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
		
		final double score = voiture.getMoteur().getPuissanceMax();

		TimerTask tt = new TimerTask() {

			@Override
			public void run() {
				if (entreesCourse.echap) {
					canard.stop();
					canard2.stop();
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
					double xa = 20;
					double xb = 1000;
					double ya = 1.5;
					double yb = 2.5;
					
					double plus = t.getCoefCourant() * (ya + (score - xa)*((yb-ya)/(xb-xa)));
					
					
					regime += plus;
					
					//System.out.println(plus);
					

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
					regime -= 35.0f;
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
					if (t.retrograder() && t.getRapportCourant() > 1) {
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

				Moteur m = voiture.getMoteur();
				if (regime < 850) {
					regime = 850;
				} else if (regime > m.getRegimeRupteur()) {
					boolean rupteur = true;
					if (entreesCourse.automatique) {
						if (t.passerVitesse()) {
							rupteur = false;
							regime *= 0.625f;
							sMoteur.passageRapport();
							System.out.println("CANARD DE MERDE");
						}
					}
					
					if (rupteur) {
						System.out.println(m.getRegimeRupteur());
						regime = m.getRegimeRupteur() - 250;						
					}
				}
				
				if (entreesCourse.automatique && regime < m.getRegimeRupteur()*0.625) {
					if (t.getRapportCourant() > 1) {
						t.retrograder();
						regime *= 1.2f;
						sMoteur.passageRapport();
						System.out.println("CANARD DE MERDE 2");
					}
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

		canard2 = new Thread() {
			public void run() {

				while (true) {

					long d = Random.unsignedDelta(4, 10) * 1000;

					Main.log("" + d);

					Multithreading.dormir(d);
					
					freine.play();

					virageDroite = Random.unsignedDelta(1, 2) == 1;
					
					devonsNousTourner = Random.unsignedDelta(120, 400);

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
				
				Transmission t = voiture.getTransmission();
				while (t.getRapportCourant() > 1)
				{
					t.retrograder();
				}
				sMoteur.passageRapport();
				crash.play();

			}
		};
		
		canard2.start();

	}
}
