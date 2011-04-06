package polyrallye.ouie.menus;

import polyrallye.controlleur.Main;
import polyrallye.modele.personnes.Joueur;
import polyrallye.modele.voiture.Voiture;
import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Menu;
import polyrallye.ouie.liseuse.Liseuse;

public class VenteVoiture extends Menu implements ActionMenu {

	protected Voiture voiture;

	public VenteVoiture(final VoitureGarage menuPrecedent, Voiture v) {
		super(menuPrecedent);

		voiture = v;
	}

	@Override
	public void actionMenu() {

		int argent = Joueur.session.getArgent();
		int prix = (int) (voiture.getPrix()*0.7);

		Liseuse.lire("Vendre la voiture ");
		Liseuse.lire(voiture.getNomComplet());
		Liseuse.lire(" pour ");
		Liseuse.lire(prix);
		Liseuse.lire(" euros. Vous aurez alors ");
		Liseuse.lire(argent + prix);
		Liseuse.lire("euros. ");

		lancer();
	}

	@Override
	public void remplir() {
		ajouterElement("Confirmer", new ActionMenu() {

			@Override
			public void actionMenu() {
				try {
					Joueur.session.vendreVoiture(voiture);

					Liseuse.lire("Félicications pour cette vente");
					Main.revenirAuMenuPrincipal();

				} catch (Exception e) {
					Liseuse.lire("Impossible de vendre la voiture");
					Liseuse.lire(e.getMessage());
				}
			}
		});
	}
}
