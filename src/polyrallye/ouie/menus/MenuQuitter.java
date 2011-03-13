package polyrallye.ouie.menus;

import polyrallye.controlleur.Main;
import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Liseuse;
import polyrallye.ouie.Menu;

public class MenuQuitter extends Menu  {

	public MenuQuitter() {
		super(null);

		ajouterElement("Non", new ActionMenu() {
			
			@Override
			public void actionMenu() {
				Main.getMenuPrincipal().lancer();
			}
		});
		
		ajouterElement("Oui", new ActionMenu() {
			
			@Override
			public void actionMenu() {
				annuler();
			}
		});
		
	}

	@Override
	public void lancer() {
		Liseuse.lire("Voulez vous quitter le jeu ?");
		super.lancer();
	}
	
	@Override
	public void annuler()
	{
		Main.demanderAQuitter();
	}
}
