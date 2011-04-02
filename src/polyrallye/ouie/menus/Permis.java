package polyrallye.ouie.menus;

import polyrallye.modele.personnes.Joueur;
import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Menu;
import polyrallye.ouie.liseuse.Liseuse;

public class Permis extends Menu implements ActionMenu {

	public Permis(Menu menuPrecedent) {
		super(menuPrecedent);
	}

	@Override
	public void actionMenu() {
		
		if (Joueur.session.getPermis().isObtenu())
		{
			Liseuse.lire("Tu as déjà ton permis de conduire. Tu peux cependant repasser les épreuves pour ne pas perdre la main.");
		}
		else
		{
			Liseuse.lire("Il existe des mondes où il vaut mieux avoir un permis de conduire.");
		}
	}

	@Override
	public void remplir() {
		// TODO Auto-generated method stub
		
	}

}
