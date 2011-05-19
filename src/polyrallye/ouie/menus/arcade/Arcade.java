package polyrallye.ouie.menus.arcade;

import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Menu;
import polyrallye.ouie.liseuse.Liseuse;
import polyrallye.ouie.menus.SelectionVoitureMagasin;
import polyrallye.ouie.menus.SelectionVoiturePerformances;

public class Arcade extends Menu implements ActionMenu {

	
	public Arcade(Menu menuPrecedent) {
		super(menuPrecedent);
	}
	
	@Override
	public void actionMenu() {
		Liseuse.lire("Comment voulez-vous choisir votre voiture ?");
		lancer();
	}
	
	@Override
	public void remplir() {
		ajouterElement("En fonction des performances", new SelectionVoiturePerformances(this));
		ajouterElement("En parcourant les concessions", new SelectionVoitureMagasin(this, true));
	}

}
