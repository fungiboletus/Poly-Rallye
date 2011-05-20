package polyrallye.ouie.menus.carriere;

import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Menu;
import polyrallye.ouie.liseuse.Liseuse;
import polyrallye.ouie.menus.SelectionVoitureMagasin;

public class Magasin extends Menu implements ActionMenu {

	public Magasin(Menu menuPrecedent) {
		super(menuPrecedent);
	}

	@Override
	public void actionMenu() {
		Liseuse.lire("Comment voulez vous afficher les voitures ?");
		lancer();
	}

	@Override
	public void remplir() {
		
	ajouterElement("Afficher uniquement les voitures que je peux acheter", new SelectionVoitureMagasin(this, false,true));
	ajouterElement("Afficher toutes les voitures", new SelectionVoitureMagasin(this, false));

	}

}
