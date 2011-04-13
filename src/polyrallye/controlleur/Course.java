package polyrallye.controlleur;

import java.util.TimerTask;

import polyrallye.modele.voiture.Transmission;
import polyrallye.modele.voiture.Voiture;
import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.SonMoteur;
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

	// Démonstration, ça ne restera pas
	protected float regime;

	protected Sound gauche;
	protected Sound droite;

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
					regime -= 70.0f;
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

				sMoteur.setRegime(regime, entreesCourse.isAccelere());
				// terrain.setVitesse(regime / 3.0f);
				// TODO mettre le code de abdoul (oui monsieur)
			}
		};

		// À 50Hz, comme le courant EDF
		timerOrganisateur.schedule(tt, 0, 20);

		gauche = new Sound("Sons/divers/gauche.wav");
		gauche.setGain(18.0f);
		droite = new Sound("Sons/divers/droite.wav");
		droite.setGain(18.0f);

		new Thread() {
			public void run() {

				while (true) {

					long d = Random.unsignedDelta(4, 22) * 1000;

					Main.log("" + d);

					Multithreading.dormir(d);

					Main.log("salut");

					if (Random.unsignedDelta(1, 2) == 1) {
						gauche.play();
					} else {
						droite.play();
					}
				}
			}
		}.start();

	}
}
