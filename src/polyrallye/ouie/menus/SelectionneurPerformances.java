package polyrallye.ouie.menus;

import polyrallye.modele.voiture.StockVoitures;
import polyrallye.modele.voiture.Voiture;
import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Menu;
import polyrallye.ouie.liseuse.Liseuse;
import polyrallye.ouie.menus.arcade.VoitureArcade;

public class SelectionneurPerformances implements ActionMenu {

	protected double min;
	protected double max;
	protected Menu menuPrecedent;
	
	public SelectionneurPerformances(Menu menuPrecedent, double min, double max) {
		this.menuPrecedent = menuPrecedent;
		this.min = min;
		this.max = max;
	}

	@Override
	public void actionMenu() {
		Voiture v = StockVoitures.getVoitureParPerformances(min, max);
		Liseuse.lire(v.getNomComplet());
		new VoitureArcade(menuPrecedent, v).lancer();
	}

}
