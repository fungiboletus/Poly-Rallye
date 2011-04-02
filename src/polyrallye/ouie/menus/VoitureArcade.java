package polyrallye.ouie.menus;

import polyrallye.modele.voiture.Voiture;
import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Course;
import polyrallye.ouie.Menu;
import polyrallye.ouie.liseuse.Liseuse;

public class VoitureArcade extends Menu implements ActionMenu {
	
	protected Voiture voiture;

	public VoitureArcade(Menu menuPrecedent, Voiture v) {
		super(menuPrecedent);
		
		voiture = v;
	}

	@Override
	public void actionMenu() {
		Liseuse.lire(voiture.getNomComplet());
		
		voiture.ennoncerCategoriePerformances();
		
		Liseuse.marquerPause();
		
		lancer();

	}

	@Override
	public void remplir() {

		ajouterElement("Sélectionner cette voiture", new Course());

		ajouterElement("Écouter les spécifications complètes", new ActionMenu() {
			
			@Override
			public void actionMenu() {
				voiture.lireSpecifications();
			}
		});
		
		ajouterElement("Voir plus d'informations sur le web", new MenuSources(this, voiture.getSources()));

	}

}