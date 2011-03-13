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

		fenetre = new FenetreNoire();
		
		Liseuse.lancer();

		Joueur j = Joueur.chargerJoueur("Bob");
		j.setSessionCourante();

		Liseuse.lire("PolyRallye");

		menuPrincipal = new MenuPrincipal();
		menuPrincipal.lancer();
	}

	public static void quitter() {
		Joueur.EnregistrerJoueur(Joueur.session);

		Liseuse.lire("Salut");
		
		Liseuse.arreter();
	}

	public static void demanderAQuitter() {
		quitter();
		fenetre.dispose();
	}

	public static void revenirAuMenuPrincipal() {
		Liseuse.lire("Retour au menu principal");
		menuPrincipal.lancer();
	}

	public static void changerTexteFenetre(String texte) {
		if (fenetre != null) {
			fenetre.changerTexte(texte);
		}
	}

	public static MenuPrincipal getMenuPrincipal() {
		return menuPrincipal;
	}
}
