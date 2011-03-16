package polyrallye.ouie.menus;

import polyrallye.modele.Voiture;
import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Menu;
import polyrallye.ouie.Sound;

public class MenuVoitureMagasin extends Menu implements ActionMenu {

	protected static Sound musique;

	static {
		/*musique = new Sound("Ressources/Reno Project - 1.0/02 - Atlanta.ogg");
		musique.setOffset(100);
		musique.setLoop(true);*/
	}
	
	public Sound getMusique() {
		return musique;
	}
	
	protected Voiture voiture;

	public MenuVoitureMagasin(Menu menuPrecedent, Voiture v) {
		super(menuPrecedent);
		
		voiture = v;		

		ajouterElement("Acheter la voiture", new MenuAchatVoiture(this, voiture));

		ajouterElement("Écouter les spécifications", new ActionMenu() {
			
			@Override
			public void actionMenu() {
				voiture.lireSpecifications();
			}
		});
		
		ajouterElement("Voir plus d'informations sur le web", new MenuSources(this, v.getSources()));
	}

	@Override
	public void actionMenu() {
		/*Liseuse.lire(voiture.getPrix());
		Liseuse.lire(" euros.");*/
		lancer();

	}

}