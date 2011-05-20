package polyrallye.ouie.menus;



import polyrallye.modele.personnes.Joueur;
import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Menu;
import polyrallye.ouie.liseuse.Liseuse;


public class SuppressionDonneesJoueur extends Menu implements ActionMenu{
	
	protected String nom;
	
	public SuppressionDonneesJoueur(Menu menuPrecedent,String nom) {
		super(menuPrecedent);
		this.nom=nom;
	}
	
	@Override
	public void actionMenu() {
		Liseuse.lire("Voulez vous vraiment supprimer la progression du joueur "+nom+" ?");
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
				ajouterElement("Effacer la progression du joueur "+nom, new ActionMenu() {
					
					@Override
					public void actionMenu() {
						Joueur.session.RemiseAZero();
						Joueur.EnregistrerJoueur(Joueur.session);
//						String[] annonces = new String[2];
//						annonces[0]= "La progression du joueur a ete effacee";
//						annonces[1]= "Vous êtes "+Joueur.session.getNom();
						Liseuse.lire("La progression du joueur "+nom+" a été effacé");
						Liseuse.lire("Vous êtes "+Joueur.session.getNom());
						ennoncer();
					}
				});
	
	
		
	}

	

}
