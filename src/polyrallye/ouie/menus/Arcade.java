package polyrallye.ouie.menus;

import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Menu;
import polyrallye.ouie.liseuse.Liseuse;

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
		ajouterElement("En faisant appel à un expert", null);
		ajouterElement("En parcourant les concessions", new SelectionVoiture(this, true));
	}

}
