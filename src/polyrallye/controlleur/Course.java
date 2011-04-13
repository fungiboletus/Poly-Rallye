package polyrallye.controlleur;

import java.util.TimerTask;

import polyrallye.modele.voiture.Transmission;
import polyrallye.modele.voiture.Voiture;
import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.SonMoteur;
import polyrallye.ouie.environnement.Environnement;
import polyrallye.ouie.environnement.Terrain;

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
					regime += t.getCoefCourant()*2.5;

				} else {
					regime -= 30.0f;
				}

				if (entreesCourse.isFreine()) {
					regime -= 150.0f;
				}

				if (entreesCourse.isGauche() || entreesCourse.isDroite()) {
					regime -= 29.0f;
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
						regime *= 0.65f;
						sMoteur.passageRapport();
					}
				}

				if (regime < 850) {
					regime = 850;
				} else if (regime > 11000) {
					regime = 10500;
				}

				sMoteur.setRegime(regime, entreesCourse.isAccelere());
				terrain.setVitesse(regime / 30.0f);
				// TODO mettre le code de abdoul (oui monsieur)
			}
		};

		// À 50Hz, comme le courant EDF
		timerOrganisateur.schedule(tt, 0, 20);

	}
}
