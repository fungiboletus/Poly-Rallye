package polyrallye.ouie.menus;

import polyrallye.ouie.Liseuse;
import polyrallye.ouie.Menu;

public class MenuPrincipal extends Menu {

	public MenuPrincipal() {
		super(new MenuQuitter());

		Liseuse.lire("Menu principal.");

		ajouterElement("Garage", new MenuGarage(this));
		ajouterElement("Magasins", new MenuMagasins(this));
		ajouterElement("Permis", new MenuPermis(this));
		ajouterElement("Manger", null);
	}

}
