package polyrallye.ouie.menus.carriere;

import polyrallye.modele.voiture.Voiture;
import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Menu;
import polyrallye.ouie.liseuse.Liseuse;
import polyrallye.ouie.menus.MenuSources;
import polyrallye.ouie.utilitaires.Sound;

public class VoitureMagasin extends Menu implements ActionMenu {

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

	public VoitureMagasin(Menu menuPrecedent, Voiture v) {
		super(menuPrecedent);
		
		voiture = v;
	}

	@Override
	public void actionMenu() {
		Liseuse.lire(voiture.getNomComplet());
		Liseuse.lire("Prix "+voiture.getPrix()+" euros");
		lancer();

	}

	@Override
	public void remplir() {
		ajouterElement("Acheter la voiture", new AchatVoiture(this, voiture));

		ajouterElement("Écouter les spécifications complètes", new ActionMenu() {
			
			@Override
			public void actionMenu() {
				voiture.lireSpecifications();
			}
		});
		
		ajouterElement("Voir plus d'informations sur le web", new MenuSources(this, voiture.getSources()));

	}

}