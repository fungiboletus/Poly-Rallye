package polyrallye.ouie.menus;

import polyrallye.controlleur.Main;
import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Menu;
import polyrallye.ouie.liseuse.Liseuse;

public class Quitter extends Menu {

	public Quitter() {
		super(null);
	}

	@Override
	public void lancer() {
		Liseuse.lire("Voulez vous quitter le jeu ?");
		super.lancer();
	}

	@Override
	public void annuler() {
		Main.demanderAQuitter();
	}

	@Override
	public void remplir() {
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
}
