package polyrallye.controlleur;

import java.awt.event.KeyListener;
import java.util.Map.Entry;

import polyrallye.modele.personnes.Joueur;
import polyrallye.modele.voiture.StockVoitures;
import polyrallye.modele.voiture.Voiture;
import polyrallye.ouie.FenetreNoire;
import polyrallye.ouie.liseuse.Liseuse;
import polyrallye.ouie.menus.Principal;
import polyrallye.ouie.utilitaires.Sound;
import polyrallye.utilitaires.LectureFichier;
import polyrallye.utilitaires.Multithreading;

/**
 * @author antoine
 *
 *	Classe qui lance le jeu.
 */
public class Main {

	/**
	 * Fenêtre du jeu.
	 */
	protected static FenetreNoire fenetre;

	/**
	 * Premier menu à lancer.
	 */
	protected static Principal menuPrincipal;

	/**
	 * Le mode de debug affiche un grand lot d'informations sur la sortie
	 * standard.
	 */
	protected static boolean debug = false;
	
	/**
	 * @param args
	 *            Arguments (pas vraiment gérés)
	 */
	public static void main(String[] args) {

		if (args.length > 0 && args[0].equals("voitures")) {
			voitures();
		} else {
			classique(args);
		}

	}

	/**
	 * Affiche toutes les voitures sur la sortie standard.
	 */
	public static void voitures() {
		for (Entry<Double, Voiture> s : StockVoitures
				.getVoituresParPerformances().entrySet()) {
			Main.logInfo("v : " + Math.round(s.getKey()) + "\t"
					+ s.getValue().getNomComplet() + " --- "
					+ s.getValue().getPrix() + " €");
		}
	}

	/**
	 * Lancement classique du jeu.
	 * 
	 * @param args
	 *            Arguments (nom en paramètre)
	 */
	public static void classique(String[] args) {

		fenetre = new FenetreNoire();

		logInfo("Les fautes sont la plupart du temps volontaires,\npour la synthèse vocale.");

		// Lancement du thread de la liseuse
		Liseuse.lancer();

		// Récupération des informations du joueur
		String nomJoueur = args.length > 1 ? args[0] : (new LectureFichier("Ressources/Comptes/")).lirePremiereLigne("Autoload.cfg");
		Joueur j = Joueur.chargerJoueur(nomJoueur);
		j.setSessionCourante();

		Liseuse.lire("PolyRallye");

		changerGestionEntrees(GestionEntreesMenu.getInstance());

		menuPrincipal = new Principal();
		menuPrincipal.lancer();

		// TODO à enlever quand on ne sera plus en développement
		//fenetre.basculerAffichageConsole();
	}

	/**
	 * Fonction appelée lorsque le jeu est quitté.
	 */
	public static void quitter() {
		Joueur.EnregistrerJoueur(Joueur.session);

		Liseuse.interrompre();

		Liseuse.lire("Salut");

		Multithreading.dormir(800);

		Liseuse.arreter();
		
		Sound.afficherCache();
	}

	/**
	 * Quitte le jeu, en fermant la fenêtre du jeu par la même occasion.
	 */
	public static void demanderAQuitter() {
		quitter();
		fenetre.dispose();
	}

	/**
	 * Revenir au premier menu du jeu.
	 */
	public static void revenirAuMenuPrincipal() {
		Liseuse.lire("Retour au menu principal");
		menuPrincipal.lancer();
	}

	/**
	 * Affiche un texte avec le niveau d'information.
	 * 
	 * @param texte
	 *            Texte à afficher.
	 */
	public static void logInfo(String texte) {
		if (fenetre != null) {
			fenetre.logInfo(texte);
		}
		System.out.println(texte);
	}

	/**
	 * Affiche un texte avec le niveau de la liseuse..
	 * 
	 * @param texte
	 *            Texte à afficher.
	 */
	public static void logLiseuse(String texte) {
		if (fenetre != null) {
			fenetre.logLiseuse(texte);
		}
		System.out.println(texte);
	}

	/**
	 * Affiche un texte avec le niveau important.
	 * 
	 * @param texte
	 *            Texte à afficher.
	 */
	public static void logImportant(String texte) {
		if (fenetre != null) {
			fenetre.logImportant(texte);
		}
		System.out.println(texte);
	}

	/**
	 * Affiche un texte si le mode debug est à vrai.
	 * 
	 * Le texte est uniquement affiché sur la sortie standard.
	 * 
	 * @param texte
	 *            Texte à afficher.
	 */
	public static void logDebug(String texte, int index) {
		if (debug) {
			System.out.println(texte);
			fenetre.logDebug(texte, index);
		}
	}
	
	/**
	 * Masque ou affiche la console.
	 */
	public static void basculerAffichageConsole() {
		if (fenetre != null) {
			fenetre.basculerAffichageConsole();
		}
	}

	/**
	 * Récupère le menu principal.
	 * 
	 * @return Le menu principal.
	 */
	public static Principal getMenuPrincipal() {
		return menuPrincipal;
	}

	/**
	 * Change l'objet qui récupère les évènenents clavier.
	 * 
	 * @param listener
	 *            Nouveau listener
	 */
	public static void changerGestionEntrees(KeyListener listener) {
		fenetre.changerGestionEntrees(listener);
	}
}
