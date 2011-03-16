package polyrallye.ouie.menus;

import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Liseuse;
import polyrallye.ouie.Menu;
import polyrallye.ouie.Sound;
import polyrallye.ouie.environnement.Environnement;

public class MenuPrincipal extends Menu {

	protected static Sound musique;
	
	static {
		//musique = new Sound("Sons/foret/jour_1.wav");
		//musique.setLoop(true);
	}
	
	public MenuPrincipal() {
		super(new MenuQuitter());


		ajouterElement("Course rapide", new ActionMenu() {
			
			@Override
			public void actionMenu() {
				Environnement e = new Environnement("foret", "nuit", "vent");
				
				e.play();
			}
		});
		ajouterElement("Garage", new MenuGarage(this));
		ajouterElement("Magasins", new MenuMagasins(this));
		ajouterElement("Permis", new MenuPermis(this));
	}
	
	public Sound getMusique() {
		return null;
	}
	
	public void lancer()
	{
		Liseuse.lire("Menu principal");
		super.lancer();
	}

}
