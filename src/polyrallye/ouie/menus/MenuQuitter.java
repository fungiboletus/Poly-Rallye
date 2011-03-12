package polyrallye.ouie.menus;

import polyrallye.controlleur.Main;
import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Liseuse;
import polyrallye.ouie.Menu;

public class MenuQuitter extends Menu implements ActionMenu  {

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
	public void actionMenu() {
		Liseuse.lire("Voulez vous quitter le jeu ?");
		lancer();

	}
	
	@Override
	public void annuler()
	{
		Main.demanderAQuitter();
	}
}
