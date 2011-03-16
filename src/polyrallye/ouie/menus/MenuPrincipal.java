package polyrallye.ouie.menus;

import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Liseuse;
import polyrallye.ouie.Menu;
import polyrallye.ouie.environnement.Environnement;

public class MenuPrincipal extends Menu {

	public MenuPrincipal() {
		super(new MenuQuitter());


		ajouterElement("Course rapide", new ActionMenu() {
			
			@Override
			public void actionMenu() {
				Environnement e = new Environnement("foret", "nuit", "vent");
				
				e.play();
			}
		});
		ajouterElement("Garage", new MenuGarage(this));
		ajouterElement("Magasins", new MenuMagasins(this));
		ajouterElement("Permis", new MenuPermis(this));
	}
	
	public void lancer()
	{
		Liseuse.lire("Menu principal");
		super.lancer();
	}

}
