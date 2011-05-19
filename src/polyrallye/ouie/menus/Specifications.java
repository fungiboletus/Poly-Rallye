package polyrallye.ouie.menus;

import polyrallye.modele.voiture.Voiture;
import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Menu;

public class Specifications extends Menu implements ActionMenu {

	protected Voiture voiture;
	
	public Specifications(Menu menuPrecedent, Voiture voiture) {
		super(menuPrecedent);
		this.voiture = voiture;
	}

	@Override
	public void actionMenu() {
		voiture.lireSpecifications();
	}

	@Override
	public void remplir() {
	}

}
