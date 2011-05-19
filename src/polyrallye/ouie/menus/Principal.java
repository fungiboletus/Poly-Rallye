package polyrallye.ouie.menus;

import polyrallye.ouie.Menu;
import polyrallye.ouie.liseuse.Liseuse;
import polyrallye.ouie.menus.arcade.Arcade;
import polyrallye.ouie.menus.carriere.Carriere;
import polyrallye.ouie.utilitaires.Sound;

public class Principal extends Menu {

	protected static Sound musique;

	static {
		// musique = new Sound("Sons/foret/jour_1.wav");
		// musique.setLoop(true);
	}

	public Principal() {
		super(new Quitter());
	}

	public Sound getMusique() {
		return null;
	}

	public void lancer() {
		Liseuse.lire("Menu principal - Sélectionnez le mode de jeu");
		Liseuse.marquerPause();
		super.lancer();
	}

	@Override
	public void remplir() {
		ajouterElement(
				"Jeu libre - Faites des courses sans objectifs, juste pour jouer",
				new Arcade(this));
		ajouterElement(
				"Carrière - Concourez dans de nombreux championnats, achetez et vendez vos voitures, pour être le meilleur pilote possible",
				new Carriere(this));
		ajouterElement(
				"Configuration Joueur - Choisissez votre joueur et configurez le jeu",
				new ConfigJoueur(this));
	}

}
