package polyrallye.controlleur;

import polyrallye.modele.personnes.Joueur;
import polyrallye.ouie.FenetreNoire;
import polyrallye.ouie.liseuse.Liseuse;
import polyrallye.ouie.menus.Principal;

public class Main {

	protected static FenetreNoire fenetre;

	protected static Principal menuPrincipal;

	/**
	 * @param args
	 * @throws LWJGLException
	 */
	public static void main(String[] args) {

		fenetre = new FenetreNoire();
		
		Liseuse.lancer();

		String nomJoueur = args.length > 1 ? args[0] : "Bob";
		Joueur j = Joueur.chargerJoueur(nomJoueur);
		j.setSessionCourante();

		Liseuse.lire("PolyRallye");

		menuPrincipal = new Principal();
		menuPrincipal.lancer();
	}

	public static void quitter() {
		Joueur.EnregistrerJoueur(Joueur.session);

		Liseuse.interrompre();

		Liseuse.lire("Salut");
		
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {}
		
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

	public static Principal getMenuPrincipal() {
		return menuPrincipal;
	}
}
