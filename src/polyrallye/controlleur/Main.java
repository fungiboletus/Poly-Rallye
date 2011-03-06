package polyrallye.controlleur;

import polyrallye.modele.Joueur;
import polyrallye.ouie.FenetreNoire;
import polyrallye.ouie.Liseuse;
import polyrallye.ouie.menus.MenuPrincipal;

public class Main {

	protected static FenetreNoire fenetre;
	
	protected static MenuPrincipal menuPrincipal;
	
	/**
	 * @param args
	 * @throws LWJGLException 
	 */
	public static void main(String[] args) {
		Liseuse.lire("PolyRallye");
		
		Joueur j = Joueur.chargerJoueur("Bob");
		j.setSessionCourante();
		
		fenetre = new FenetreNoire();
		
		menuPrincipal = new MenuPrincipal();
		menuPrincipal.lancer();
	}
	
	public static void quitter()
	{
		Joueur.EnregistrerJoueur(Joueur.session);
		
		Liseuse.lire("Salut");
	}

	public static void demanderAQuitter()
	{
		quitter();
		fenetre.dispose(); 
	}
	
	public static void revenirAuMenuPrincipal()
	{
		Liseuse.lire("Retour au menu principal");
		menuPrincipal.lancer();
	}
}
