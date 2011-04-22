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
		Liseuse.lire("Voulez-vous vraiment quitter le jeu ?");
		super.lancer();
	}

	@Override
	public void annuler() {
		Main.demanderAQuitter();
	}

	@Override
	public void remplir() {
		ajouterElement("Revenir au menu principal", new ActionMenu() {

			@Override
			public void actionMenu() {
				Main.getMenuPrincipal().lancer();
			}
		});

		ajouterElement("Quitter le jeu", new ActionMenu() {

			@Override
			public void actionMenu() {
				annuler();
			}
		});
	}
}
