package polyrallye.ouie.menus;



import java.io.File;

import polyrallye.modele.personnes.Joueur;
import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Menu;
import polyrallye.ouie.liseuse.Liseuse;


public class SuppressionDonneesJoueur extends Menu implements ActionMenu{
	
	protected String nom;
	
	public SuppressionDonneesJoueur(Menu menuPrecedent) {
		super(menuPrecedent);
		nom = Joueur.session.getNom();
	}
	
	@Override
	public void actionMenu() {
		Liseuse.lire("Voulez vous vraiment supprimer la progression du joueur "+Joueur.session.getNom()+" ?");
		lancer();
	}
	
	@Override
	public void remplir() {
	
				ajouterElement("Retourner a la configuration du joueur", new ActionMenu() {
					
					@Override
					public void actionMenu() {
						Liseuse.lire("Vous êtes "+Joueur.session.getNom());
						ennoncer();
						
					}
				});
				ajouterElement("Effacer la progression du joueur "+Joueur.session.getNom(), new ActionMenu() {
					
					@Override
					public void actionMenu() {
						Joueur.session.RemiseAZero();
						Joueur.EnregistrerJoueur(Joueur.session);
						
						String[] list = new File("Ressources/Championnats/").list();
						for (int i = 0; i < list.length; i++) {
							if(list[i].contains(Joueur.session.getNom()))
								new File("Ressources/Championnats/"+list[i]).delete();
						}
						

						Liseuse.lire("La progression du joueur "+Joueur.session.getNom()+" a été effacé");
						Liseuse.lire("Vous êtes "+Joueur.session.getNom());
						ennoncer();
					}
				});
	
	
		
	}

	

}
