package polyrallye.ouie.menus;

import polyrallye.modele.personnes.Joueur;
import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Menu;
import polyrallye.ouie.liseuse.Liseuse;
import polyrallye.utilitaires.EcritureFichier;

public class ConfigJoueur extends Menu implements ActionMenu {

	
	public ConfigJoueur(Menu menuPrecedent) {
		super(menuPrecedent);
	}
	
	@Override
	public void actionMenu() {
		Liseuse.lire("Configuration du joueur");
		Liseuse.lire("Vous êtes "+Joueur.session.getNom());
		lancer();
	}
	
	@Override
	public void remplir() {
		ajouterElement("Choisir un autre joueur", new SelectionJoueur(this));
		ajouterElement("Choisir ce joueur au lancement du jeu", new ActionMenu() {
			
			@Override
			public void actionMenu() {
				new EcritureFichier("Ressources/Comptes/").ecriturePremiereLigne("Autoload.cfg",Joueur.session.getNom());
				Liseuse.lire("Le joueur "+Joueur.session.getNom()+" sera chargé au début du jeu");
				Liseuse.lire("Vous êtes "+Joueur.session.getNom());

			}
		});
		ajouterElement("Creer un nouveau joueur", new CreationJoueur(this));
		ajouterElement("Effacer la progression du joueur", new SuppressionDonneesJoueur(menuPrecedent));
	}
	
	
	
	

}
