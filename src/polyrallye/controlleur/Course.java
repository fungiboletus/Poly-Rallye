package polyrallye.controlleur;

import java.util.TimerTask;

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

		environnement = new Environnement("foret", "nuit", "vent");
		terrain = new Terrain("asphalt"); 
		sMoteur = new SonMoteur(voiture);
		
		terrain.play();
		environnement.play();
		sMoteur.play();

		regime = 850.0f;
		
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
					regime += 
				}
				
				// TODO mettre le code de abdoul (oui monsieur)
			}
		};

		// À 50Hz, comme le courant EDF
		timerOrganisateur.schedule(tt, 0, 20);
		

		
	}
}
