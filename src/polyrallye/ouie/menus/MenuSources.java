package polyrallye.ouie.menus;

import java.net.URI;

import polyrallye.modele.voiture.Sources;
import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Menu;
import polyrallye.ouie.liseuse.Liseuse;

public class MenuSources extends Menu implements ActionMenu {

	protected Sources sources;

	public MenuSources(Menu menuPrecedent, final Sources s) {
		super(menuPrecedent);

		sources = s;
	}

	@Override
	public void actionMenu() {
		switch (sources.getSources().size()) {
		case 0:
			Liseuse.lire("Désolé il n'y a pas de sources pour cette voiture");
			break;
		case 1:
			sources.naviguer(sources.getSources().get(0));
			break;
		default:
			Liseuse.lire("Différentes sources sont disponibles");
			lancer();

		}
	}

	@Override
	public void remplir() {
		for (final URI u : sources.getSources()) {
			ajouterElement(u.getHost(), new ActionMenu() {

				@Override
				public void actionMenu() {
					sources.naviguer(u);
				}
			});
		}
	}
}
