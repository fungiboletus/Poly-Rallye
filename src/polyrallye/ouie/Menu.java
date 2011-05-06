package polyrallye.ouie;

import java.util.ArrayList;
import java.util.List;

import polyrallye.controlleur.EcouteurEntrees;
import polyrallye.controlleur.GestionEntreesMenu;
import polyrallye.ouie.liseuse.Liseuse;
import polyrallye.ouie.utilitaires.Sound;

/**
 * @author Antoine Pultier
 * 
 *         Classe abstraite de gestion des menus.
 */
public abstract class Menu implements EcouteurEntrees {

	/**
	 * Liste des libellées dans le menu.
	 */
	protected List<String> libelles;

	/**
	 * Liste des références vers les actions dans le menu.
	 */
	protected List<ActionMenu> actions;

	/**
	 * Index de l'élement courant.
	 */
	protected int courant;

	/**
	 * Référence vers le menu précédent (si l'on veut revenir en arrière).
	 */
	protected Menu menuPrecedent;

	/**
	 * Si le menu est remplis ?
	 * 
	 * Le remplissage se fait uniquement si le menu est sélectionné, pour
	 * optimiser les performances.
	 */
	protected boolean remplis = false;

	/**
	 * Message énnoncé si le menu est vide.
	 */
	protected String messageMenuVide = "Le menu est vide";

	protected static Sound musiqueEnCours = null;

	// Sons de l'interface
	public static Sound sonSuivant;
	public static Sound sonPrecedent;
	public static Sound sonTic;
	public static Sound sonBoucle;

	static {
		// Chargement des sons de l'interface
		sonSuivant = new Sound("Sons/menus/suivant.ogg");
		sonPrecedent = new Sound("Sons/menus/precedent.ogg");
		sonTic = new Sound("Sons/menus/tic.wav");
		sonBoucle = new Sound("Sons/menus/suivant.ogg");
	}

	/**
	 * Constructeur de base.
	 * 
	 * @param menuPrecedent
	 *            Menu appelé en cas de retour
	 */
	public Menu(Menu menuPrecedent) {

		initialiser();

		this.menuPrecedent = menuPrecedent;
	}

	public Sound getMusique() {
		return null;
	}

	public void changerMusique() {
		Sound musique = getMusique();

		System.out.println(musique);
		if (musiqueEnCours != musique) {
			if (musiqueEnCours != null) {
				musiqueEnCours.fadeOut(500);
			}

			if (musique != null) {
				musique.fadeIn(500, 0.4f);
			}

			musiqueEnCours = musique;
		}
	}

	/**
	 * Initialisation du menu.
	 * 
	 * Cette méthode ne se fait pas dans le constructeur, car un menu peut être
	 * initialisé plusieurs fois.
	 */
	public void initialiser() {
		libelles = new ArrayList<String>();
		actions = new ArrayList<ActionMenu>();

		courant = 0;

		remplis = false;
	}

	/**
	 * Méthode appelée pour lancer le menu. C'est à dire quand l'utilisateur est
	 * vraiment dans le menu.
	 */
	public void lancer() {
		if (!remplis) {
			remplir();
			remplis = true;
		}

		//if (libelles.size() > 0) {
			GestionEntreesMenu.getInstance().setEcouteur(this);
		//}
		changerMusique();
		ennoncer();
	}

	/**
	 * Ajoute un élément dans le menu.
	 * 
	 * @param libelle
	 *            Libellé de l'élement à ajouter
	 * @param action
	 *            Action à ajouter
	 */
	public void ajouterElement(String libelle, ActionMenu action) {
		libelles.add(libelle);
		actions.add(action);
	}

	/**
	 * Passe à l'élement suivant.
	 */
	public void suivant() {
		if (++courant == libelles.size()) {
			courant = 0;
			sonBoucle.play();
		} else {
			sonTic.play();
		}

		Liseuse.interrompre();
		ennoncer();
	}

	/**
	 * Passe à l'élément précedent.
	 */
	public void precedent() {
		if (courant == 0) {
			courant = libelles.size();
		}

		--courant;

		sonTic.play();

		Liseuse.interrompre();
		ennoncer();
	}

	@Override
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

		Liseuse.lire("Action invalide");
	}

	@Override
	public void annuler() {
		sonPrecedent.play();

		Liseuse.interrompre();
		menuPrecedent.lancer();
	}

	/**
	 * Énnonce le libellé de l'élement courant.
	 */
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
		//annuler();
	}

	@Override
	public void droite() {
		//selectionner();
	}

	public void aide() {
		Liseuse.lire("Vous êtes dans un menu. Utilisez les touches haut et bas pour vous déplacer dans le menu. Entrée pour valider, et échap pour annuler.");
	}

	/**
	 * @param messageMenuVide
	 *            Nouveau message
	 */
	public void setMessageMenuVide(String messageMenuVide) {
		this.messageMenuVide = messageMenuVide;
	}

	/**
	 * Fonction appelée pour remplir le menu.
	 */
	public abstract void remplir();
}
