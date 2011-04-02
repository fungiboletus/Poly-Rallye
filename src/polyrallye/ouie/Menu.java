package polyrallye.ouie;

import java.util.ArrayList;
import java.util.List;

import polyrallye.ouie.liseuse.Liseuse;
import polyrallye.ouie.utilitaires.Sound;

public abstract class Menu implements EcouteurEntrees {

	protected List<String> libelles;
	protected List<ActionMenu> actions;

	protected int courant;

	protected Menu menuPrecedent;
	
	protected boolean remplis = false;

	protected String messageMenuVide = "Le menu est vide";
	
	protected static Sound musiqueEnCours = null;
	
	public static Sound sonSuivant;
	public static Sound sonPrecedent;
	public static Sound sonTic;
	public static Sound sonBoucle;
	
	static {
		sonSuivant = new Sound("Sons/menus/suivant.ogg");
		sonPrecedent = new Sound("Sons/menus/precedent.ogg");
		sonTic = new Sound("Sons/menus/tic.wav");
		sonBoucle = new Sound("Sons/menus/suivant.ogg");
	}

	public Menu(Menu menuPrecedent) {

		initialiser();

		this.menuPrecedent = menuPrecedent;
	}

	public Sound getMusique() {
		return null;
	}
	
	public void changerMusique(){
		Sound musique = getMusique();
		
		System.out.println(musique);
		if (musiqueEnCours != musique){
			if (musiqueEnCours != null) {
				musiqueEnCours.fadeOut(500);
			}
			
			if (musique != null) {
				musique.fadeIn(500, 0.4f);
			}
			
			musiqueEnCours = musique;
		}
	}
	
	public void initialiser() {
		libelles = new ArrayList<String>();
		actions = new ArrayList<ActionMenu>();

		courant = 0;
		
		remplis = false;
	}

	public void lancer() {
		if (!remplis) {
			remplir();
			remplis = true;
		}
		
		if (libelles.size() > 0) {
			GestionEntrees.getInstance().setEcouteur(this);
		}
		changerMusique();
		ennoncer();
	}

	public void ajouterElement(String libelle, ActionMenu action) {
		libelles.add(libelle);
		actions.add(action);
	}

	public void suivant() {
		if (++courant == libelles.size()) {
			courant = 0;
			sonBoucle.play();
		}
		else
		{
			sonTic.play();			
		}

		Liseuse.interrompre();
		ennoncer();
	}

	public void precedent() {
		if (courant == 0) {
			courant = libelles.size();
		}

		--courant;

		sonTic.play();

		Liseuse.interrompre();
		ennoncer();
	}

	public void selectionner() {

		sonSuivant.play();

		Liseuse.interrompre();
		if (libelles.size() > 0) {
			ActionMenu am = actions.get(courant);

			if (am != null) {
				am.actionMenu();
				return;
			}
		}

		Liseuse.lire("Action invalide.");
	}

	public void annuler() {
		sonPrecedent.play();

		Liseuse.interrompre();
		menuPrecedent.lancer();
	}

	public void ennoncer() {
		if (libelles.size() > 0) {
			Liseuse.lire(libelles.get(courant));
		} else {
			Liseuse.lire(messageMenuVide);
		}
	}

	@Override
	public void haut() {
		precedent();
	}

	@Override
	public void bas() {
		suivant();
	}

	@Override
	public void gauche() {
		annuler();
	}

	@Override
	public void droite() {
		selectionner();
	}

	public void aide() {
		Liseuse.lire("Vous êtes dans un menu. Utilisez les touches haut et bas pour vous déplacer dans le menu. Entrée pour valider, et échap pour annuler.");
	}

	public void setMessageMenuVide(String messageMenuVide) {
		this.messageMenuVide = messageMenuVide;
	}
	
	public abstract void remplir();
}
