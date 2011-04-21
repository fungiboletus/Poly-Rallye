package polyrallye.ouie.menus;

import polyrallye.ouie.Menu;
import polyrallye.ouie.liseuse.Liseuse;
import polyrallye.ouie.utilitaires.Sound;

public class Principal extends Menu {

	protected static Sound musique;
	
	static {
		//musique = new Sound("Sons/foret/jour_1.wav");
		//musique.setLoop(true);
	}
	
	public Principal() {
		super(new Quitter());
	}
	
	public Sound getMusique() {
		return null;
	}
	
	public void lancer()
	{
		Liseuse.lire("Menu principal");
		super.lancer();
	}

	@Override
	public void remplir() {
		ajouterElement("Arcade", new SelectionVoiture(this, true));
		ajouterElement("Carrière", new Carriere(this));
	}

}
