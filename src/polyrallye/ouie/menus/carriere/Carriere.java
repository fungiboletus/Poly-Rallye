package polyrallye.ouie.menus.carriere;

import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Menu;
import polyrallye.ouie.liseuse.Liseuse;
import polyrallye.ouie.menus.SelectionVoitureMagasin;

public class Carriere extends Menu implements ActionMenu {

	public Carriere(Menu menuPrecedent) {
		super(menuPrecedent);
	}

	@Override
	public void actionMenu() {
		Liseuse.lire("Le mode carrière n'est pas du tout terminé.");
		lancer();
	}

	@Override
	public void remplir() {
		
	ajouterElement("Garage", new Garage(this));
	ajouterElement("Magasins", new SelectionVoitureMagasin(this, false));
	ajouterElement("Permis", new Permis(this));
	}

}
