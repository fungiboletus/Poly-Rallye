package polyrallye.ouie.menus;

import java.io.File;
import java.util.List;

import org.jdom.Element;

import polyrallye.modele.personnes.Joueur;
import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Menu;
import polyrallye.ouie.liseuse.Liseuse;
import polyrallye.utilitaires.GestionXML;

public class SuppressionDonneesJoueur extends Menu implements ActionMenu{
	
	protected String nom;
	
	public SuppressionDonneesJoueur(Menu menuPrecedent) {
		super(menuPrecedent);
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
						annuler();
						
					}
				});
				ajouterElement("Effacer la progression du joueur "+nom+" ?", new ActionMenu() {
					
					@Override
					public void actionMenu() {
						Joueur.session.RemiseAZero();
						Joueur.EnregistrerJoueur(Joueur.session);
						Liseuse.lire("La progression du joueur a ete effacee");
						String[] annonces = new String[1];
						annonces[0]= "Vous êtes "+Joueur.session.getNom();
						annulerEnnoncer(annonces);
					}
				});
	
	
		
	}

	

}
