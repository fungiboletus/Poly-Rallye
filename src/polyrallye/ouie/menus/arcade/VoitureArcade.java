package polyrallye.ouie.menus.arcade;

import polyrallye.modele.voiture.Voiture;
import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Menu;
import polyrallye.ouie.liseuse.Liseuse;
import polyrallye.ouie.menus.MenuSources;
import polyrallye.ouie.menus.Specifications;

public class VoitureArcade extends Menu implements ActionMenu {
	
	protected Voiture voiture;

	public VoitureArcade(Menu menuPrecedent, Voiture v) {
		super(menuPrecedent);
		
		voiture = v;
	}

	@Override
	public void actionMenu() {
		voiture.ennoncerCategoriePerformances();
		
		Liseuse.marquerPause();
		
		lancer();

		System.out.println(voiture.getTransmission());
	}

	@Override
	public void remplir() {

		ajouterElement("Lancer la course avec cette voiture", new SelectionCircuit(this,voiture));

		ajouterElement("Écouter les spécifications", new Specifications(this, voiture));
		
		ajouterElement("Voir plus d'informations sur le web", new MenuSources(this, voiture.getSources()));

	}

}