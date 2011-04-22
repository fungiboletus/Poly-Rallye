package polyrallye.controlleur;

import java.awt.event.KeyListener;
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

		if (args.length > 0 && args[0].equals("voitures")) {
			voitures();
		} else {
			classique(args);
		}
		
	}
	
	public static void voitures() {		
		for (Entry<Double, Voiture> s : StockVoitures.getVoituresParPerformances().entrySet())
		{
			Main.logInfo("v : "+ Math.round(s.getKey()) + "\t"  + s.getValue().getNomComplet() + " --- "+s.getValue().getPrix()+" €");
		}
	}
	
	public static void classique(String [] args) {
		
		fenetre = new FenetreNoire();
		
		logInfo("Les fautes sont la plupart du temps volontaires,\npour la synthèse vocale.");
		
		Liseuse.lancer();
		
		
		String nomJoueur = args.length > 1 ? args[0] : "Bob";
		Joueur j = Joueur.chargerJoueur(nomJoueur);
		j.setSessionCourante();
		
		Liseuse.lire("PolyRallye");
		
		changerGestionEntrees(GestionEntreesMenu.getInstance());
		
		menuPrincipal = new Principal();
		menuPrincipal.lancer();
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
	
	public static void logInfo(String texte) {
		if (fenetre != null) {
			fenetre.logInfo(texte);
		}
		System.out.println(texte);
	}
	
	public static void logLiseuse(String texte) {
		if (fenetre != null) {
			fenetre.logLiseuse(texte);
		}
		System.out.println(texte);
	}
	
	public static void logImportant(String texte) {
		if (fenetre != null) {
			fenetre.logImportant(texte);
		}
		System.out.println(texte);
	}
	
	
	public static void basculerAffichageConsole() {
		if (fenetre != null) {
			fenetre.basculerAffichageConsole();
		}
	}
	
	public static Principal getMenuPrincipal() {
		return menuPrincipal;
	}
	
	public static void changerGestionEntrees(KeyListener listener) {
		fenetre.changerGestionEntrees(listener);
	}
}
