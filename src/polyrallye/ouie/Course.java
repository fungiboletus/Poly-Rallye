package polyrallye.ouie;

import java.util.Timer;
import java.util.TimerTask;

import polyrallye.controlleur.Main;
import polyrallye.ouie.environnement.Environnement;

public class Course implements ActionMenu {

	/**
	 * Le timer qui excécute la course.
	 */
	protected Timer timer;
	
	@Override
	public void actionMenu() {
		timer = new Timer();

		TimerTask tt = new TimerTask() {

			@Override
			public void run() {
				// TODO mettre le code de abdoul (oui monsieur)
			}
		};

		// À 50Hz, comme le courant EDF
		timer.schedule(tt, 0, 20);
		
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
