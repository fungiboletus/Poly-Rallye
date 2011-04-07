package polyrallye.ouie.menus;

import polyrallye.modele.voiture.Voiture;
import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Menu;
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

		ajouterElement("Acheter la voiture", new AchatVoiture(this, voiture));

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

    @Override
    public void remplir() {
        // TODO Auto-generated method stub
        
    }

}