package polyrallye.ouie.menus;

import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Menu;
import polyrallye.ouie.liseuse.Liseuse;

public class SelectionVoiturePerformances extends Menu implements ActionMenu {
	
	public SelectionVoiturePerformances(Menu menuPrecedent) {
		super(menuPrecedent);
	}

	@Override
	public void actionMenu() {
		Liseuse.lire("Choisissez le niveau de performances");
		lancer();
	}

	@Override
	public void remplir() {
	
		ajouterElement("Faibles", new SelectionneurPerformances(this, 0, 200));
		ajouterElement("Moyennes", new SelectionneurPerformances(this, 200, 300));
		ajouterElement("Bonnes", new SelectionneurPerformances(this, 300, 400));
		ajouterElement("Très bonnes", new SelectionneurPerformances(this, 4, 650));
		ajouterElement("Exceptionnelles", new SelectionneurPerformances(this, 650, 1000));
	}

}
