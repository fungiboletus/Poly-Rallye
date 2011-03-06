package polyrallye.ouie;

import java.util.ArrayList;
import java.util.List;

public abstract class Menu implements EcouteurEntrees {

	protected List<String> libelles;
	protected List<ActionMenu> actions;

	protected int courant;

	protected Menu menuPrecedent;

	public Menu(Menu menuPrecedent) {

		initialiser();

		this.menuPrecedent = menuPrecedent;
	}

	public void initialiser() {
		libelles = new ArrayList<String>();
		actions = new ArrayList<ActionMenu>();

		courant = 0;
	}

	public void lancer() {
		GestionEntrees.getInstance().setEcouteur(this);
		ennoncer();
	}

	public void ajouterElement(String libelle, ActionMenu action) {
		libelles.add(libelle);
		actions.add(action);
	}

	public void suivant() {
		if (++courant == libelles.size()) {
			courant = 0;
		}

		ennoncer();
	}

	public void precedent() {
		if (courant == 0) {
			courant = libelles.size();
		}

		--courant;

		ennoncer();
	}

	public void selectionner() {

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
		menuPrecedent.lancer();
	}

	public void ennoncer() {
		if (libelles.size() > 0) {
			Liseuse.lire(libelles.get(courant));
		} else {
			Liseuse.lire("Le menu est vide.");
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
}
