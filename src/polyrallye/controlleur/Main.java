package polyrallye.controlleur;

import java.util.Map.Entry;

import polyrallye.modele.personnes.Joueur;
import polyrallye.modele.voiture.StockVoitures;
import polyrallye.modele.voiture.Voiture;
import polyrallye.ouie.FenetreNoire;
import polyrallye.ouie.liseuse.Liseuse;
import polyrallye.ouie.menus.Principal;
import polyrallye.utilitaires.Multithreading;

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
		
		for (Entry<Double, Voiture> s : StockVoitures.getVoituresParPerformances().entrySet())
		{
			System.out.println(s.getValue().getNomComplet() + "  " + s.getKey());
		}
	}

	public static void quitter() {
		Joueur.EnregistrerJoueur(Joueur.session);

		Liseuse.interrompre();

		Liseuse.lire("Salut");
		
		Multithreading.dormir(800);
		
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
