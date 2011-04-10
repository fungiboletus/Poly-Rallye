package polyrallye.controlleur;

import java.util.TimerTask;

import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.environnement.Environnement;

public class Course implements ActionMenu {

	/**
	 * Le timer qui excécute la course.
	 */
	protected java.util.Timer timerOrganisateur;
	protected org.lwjgl.util.Timer timerCompteur;
	
	protected float temps;
	
	protected GestionEntreesCourse entreesCourse;
	
	@Override
	public void actionMenu() {
		entreesCourse = new GestionEntreesCourse();
		
		Main.changerGestionEntrees(entreesCourse);
		
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
				float deltaTemps = tempsTmp-temps;
				temps = tempsTmp;
				
				if (entreesCourse.isAccelere()) {
					System.out.println("accelere");
				}
				
				// TODO mettre le code de abdoul (oui monsieur)
			}
		};

		// À 50Hz, comme le courant EDF
		timerOrganisateur.schedule(tt, 0, 20);
		
		/*Environnement e = new Environnement("foret", "nuit", "vent");

		e.play();

		// final SonMoteur sm = new SonMoteur();

		EcouteurEntrees ee = new EcouteurEntrees() {

			@Override
			public void selectionner() {
				// TODO Auto-generated method stub

			}

			@Override
			public void haut() {
				SonMoteur.accelere = true;
			}

			@Override
			public void gauche() {
				SonMoteur.regime *= 1.3;
			}

			@Override
			public void droite() {
				SonMoteur.regime *= 0.73;
			}

			@Override
			public void bas() {
				SonMoteur.accelere = false;
			}

			@Override
			public void annuler() {
				Main.demanderAQuitter();
			}

			@Override
			public void aide() {
				// TODO Auto-generated method stub

			}
		};

		GestionEntrees.getInstance().setEcouteur(ee);

		new Thread() {
			public void run() {
				SonMoteur.lancer();
			}
		}.start();*/
	}
}
