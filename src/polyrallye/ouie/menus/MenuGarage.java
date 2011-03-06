package polyrallye.ouie.menus;

import polyrallye.ouie.Liseuse;
import polyrallye.ouie.Menu;

public class MenuGarage extends Menu {

	public MenuGarage()
	{
		super();
		
		Liseuse.lire("Vive les canards \\_o<");
		
		ajouterElement("cool", null);
	}

	@Override
	public void annuler() {
		new MenuPrincipal().lancer();
	}
}
