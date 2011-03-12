package polyrallye.ouie.menus;

import polyrallye.modele.Voiture;
import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Menu;

public class MenuVoitureMagasin extends Menu implements ActionMenu {

	protected Voiture voiture;

	public MenuVoitureMagasin(Menu menuPrecedent, Voiture v) {
		super(menuPrecedent);
		
		voiture = v;		

		ajouterElement("Spécifications", new ActionMenu() {
			
			@Override
			public void actionMenu() {
				voiture.lireSpecifications();
			}
		});
		ajouterElement("Acheter", new MenuAchatVoiture(this, voiture));
		
		ajouterElement("Web", new ActionMenu() {
			
			@Override
			public void actionMenu() {
				voiture.getSources().naviguer();
			}
		});
	}

	@Override
	public void actionMenu() {
		/*Liseuse.lire(voiture.getPrix());
		Liseuse.lire(" euros.");*/
		lancer();

	}

}