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

		for (final URI u : s.getSources()) {
			ajouterElement(u.getHost(), new ActionMenu() {

				@Override
				public void actionMenu() {
					s.naviguer(u);
				}
			});
		}

	}

	@Override
	public void actionMenu() {
		Liseuse.lire("Différentes sources disponibles");
		lancer();

	}

    @Override
    public void remplir() {
        // TODO Auto-generated method stub
        
    }
}
