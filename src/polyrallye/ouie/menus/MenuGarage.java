package polyrallye.ouie.menus;

import java.util.List;
import java.util.ListIterator;

import polyrallye.modele.Joueur;
import polyrallye.modele.Voiture;
import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Liseuse;
import polyrallye.ouie.Menu;

public class MenuGarage extends Menu implements ActionMenu {

	public MenuGarage(Menu menuPrecedent) {
		super(menuPrecedent);

		listerVoitures();
	}

	public void listerVoitures() {
		/*
		 * for (Voiture v : Joueur.session.getGarage().getVoitures()) {
		 * ajouterElement(v.getNom(), new MenuVoitureGarage(this, v)); }
		 */

		List<Voiture> l = Joueur.session.getGarage().getVoitures();
		ListIterator<Voiture> i = l.listIterator(l.size());

		while (i.hasPrevious()) {
			Voiture v = i.previous();
			ajouterElement(v.getNom(), new MenuVoitureGarage(this, v));
		}
	}

	@Override
	public void actionMenu() {
		Liseuse.lire("Vous êtes dans votre garage. Vous pouvez sélectionner vos voitures.");

		if (Joueur.session.getGarage().verifierChangement()) {
			initialiser();
			listerVoitures();
		}

		lancer();
	}
}
