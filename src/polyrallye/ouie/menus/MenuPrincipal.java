package polyrallye.ouie.menus;

import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Liseuse;
import polyrallye.ouie.Menu;

public class MenuPrincipal extends Menu {

	public MenuPrincipal() {
		super();
		
		Liseuse.lire("Menu principal.");
		
		ajouterElement("Garage", new ActionMenu() {
			
			@Override
			public void actionMenu() {
				new MenuGarage().lancer();
			}
		});
		ajouterElement("Permis", null);
		ajouterElement("Sortir", null);
		ajouterElement("Manger", null);
		ajouterElement("Garage", null);
	}
	
	@Override
	public void annuler()
	{
		System.out.println("Quittons");
	}

}
