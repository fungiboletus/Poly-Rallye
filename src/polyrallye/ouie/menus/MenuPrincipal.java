package polyrallye.ouie.menus;

import polyrallye.controlleur.Main;
import polyrallye.ouie.Liseuse;
import polyrallye.ouie.Menu;

public class MenuPrincipal extends Menu {

	public MenuPrincipal() {
		super(null);

		Liseuse.lire("Menu principal.");


		ajouterElement("Garage", new MenuGarage(this));
		ajouterElement("Magasins", new MenuMagasins(this));
		ajouterElement("Permis", null);
		ajouterElement("Manger", null);
	}

	@Override
	public void annuler() {
		Main.demanderAQuitter();
	}

}
