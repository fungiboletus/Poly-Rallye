package polyrallye.ouie.menus;

import java.util.Map.Entry;

import polyrallye.modele.StockVoitures;
import polyrallye.modele.Voiture;
import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Liseuse;
import polyrallye.ouie.Menu;

public class MenuMagasins extends Menu implements ActionMenu {

	public MenuMagasins(Menu menuPrecedent) {
		super(menuPrecedent);
		
		for (Entry<String, Voiture> c : StockVoitures.getVoitures().entrySet()) {
			ajouterElement(c.getKey(), new MenuVoitureMagasin(this, c.getValue()));
		}
	}

	@Override
	public void actionMenu() {
		Liseuse.lire("Vous pouvez acheter de nombreuses voitures dans les magasins.");

		lancer();
	}

}
