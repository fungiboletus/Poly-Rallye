package polyrallye.ouie.menus;

import polyrallye.ouie.Menu;
import polyrallye.ouie.liseuse.Liseuse;

public class Libre extends SelectionVoiture {

	public Libre(Menu menuPrecedent) {
		super(menuPrecedent);
	}

	@Override
	public void actionMenu() {
		Liseuse.lire("Sélectionnez la voiture");
		lancer();
	}

}
