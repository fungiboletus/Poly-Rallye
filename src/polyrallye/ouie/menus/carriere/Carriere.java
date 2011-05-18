package polyrallye.ouie.menus.carriere;

import polyrallye.modele.personnes.Joueur;
import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Menu;
import polyrallye.ouie.liseuse.Liseuse;
import polyrallye.ouie.menus.SelectionVoitureMagasin;

public class Carriere extends Menu implements ActionMenu {

	public Carriere(Menu menuPrecedent) {
		super(menuPrecedent);
	}

	@Override
	public void actionMenu() {
		lancer();
	}

	@Override
	public void remplir() {
		
	ajouterElement("Garage", new Garage(this));
	ajouterElement("Magasins", new SelectionVoitureMagasin(this, false));
	ajouterElement("Permis", new Permis(this));
	ajouterElement("Championnat", new Championnat(this));
	ajouterElement("Combien ais-je d'argent ?", new ActionMenu() {
		
		@Override
		public void actionMenu() {
			Liseuse.lire("Vous avez "+Joueur.session.getArgent()+" euros");
		}
	});
	}

}
