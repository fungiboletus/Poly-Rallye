package polyrallye.ouie.menus.carriere;

import java.util.List;
import java.util.ListIterator;

import polyrallye.modele.personnes.Joueur;
import polyrallye.modele.voiture.Voiture;
import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Menu;
import polyrallye.ouie.liseuse.Liseuse;
import polyrallye.ouie.utilitaires.Sound;

public class Garage extends Menu implements ActionMenu {

	protected static Sound musique;

	static {
//		musique = new Sound("Sons/foret/jour_6.wav");
//		musique.setLoop(true);
	}

	public Garage(Menu menuPrecedent) {
		super(menuPrecedent);

		messageMenuVide = "Vous n'avez aucune voiture. Pour obtenir des voitures, vous pouvez en acheter en magasin, ou en gagner dans certains championnats.";
	}

	public Sound getMusique() {
		return musique;
	}
	
	@Override
	public void actionMenu() {

		if (Joueur.session.getGarage().verifierChangement()) {
			initialiser();
		}

		Liseuse.lire("Vous êtes dans votre garage. Vous pouvez sélectionner vos voitures.");

		lancer();
	}

	@Override
	public void remplir() {
		List<Voiture> l = Joueur.session.getGarage().getVoitures();
		ListIterator<Voiture> i = l.listIterator(l.size());

		while (i.hasPrevious()) {
			Voiture v = i.previous();
			//System.out.println(v);
			ajouterElement(v.getNomComplet(), new VoitureGarage(this, v));
		}
	}
}
