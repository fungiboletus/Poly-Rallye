package polyrallye.ouie.menus;

import polyrallye.modele.Voiture;
import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Menu;

public class MenuVoitureGarage extends Menu implements ActionMenu {

	protected Voiture voiture;
	
	public MenuVoitureGarage(Menu menuPrecedent, Voiture v) {
		super(menuPrecedent);
		
		voiture = v;		

		ajouterElement("Spécifications", null);
		ajouterElement("Tester", null);
		ajouterElement("Vendre", new MenuVenteVoiture(this, voiture));
		ajouterElement("Web", new ActionMenu() {
			
			@Override
			public void actionMenu() {
				voiture.getSources().naviguer();
			}
		});
	}

	@Override
	public void actionMenu() {
		//Liseuse.lire(voiture.getNom());
		//voiture.getTransmission().calculerRapports();
		lancer();
		
	}

}
