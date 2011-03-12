package polyrallye.ouie.menus;

import polyrallye.controlleur.Main;
import polyrallye.modele.Joueur;
import polyrallye.modele.Voiture;
import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Liseuse;
import polyrallye.ouie.Menu;

public class MenuVenteVoiture extends Menu implements ActionMenu {

	protected Voiture voiture;

	public MenuVenteVoiture(final MenuVoitureGarage menuPrecedent, Voiture v) {
		super(menuPrecedent);

		voiture = v;

		ajouterElement("Confirmer", new ActionMenu() {

			@Override
			public void actionMenu() {
				try {
					Joueur.session.vendreVoiture(voiture);

					Liseuse.lire("Félicications cette vente.");
					Main.revenirAuMenuPrincipal();

				} catch (Exception e) {
					Liseuse.lire("Impossible de vendre la voiture");
					Liseuse.lire(e.getMessage());
				}
			}
		});
	}

	@Override
	public void actionMenu() {

		int argent = Joueur.session.getArgent();
		int prix = (int) (voiture.getPrix()*0.7);

		Liseuse.lire("Vendre la voiture ");
		Liseuse.lire(voiture.getNom());
		Liseuse.lire(" pour ");
		Liseuse.lire(prix);
		Liseuse.lire(" euros. Vous aurez alors ");
		Liseuse.lire(argent + prix);
		Liseuse.lire("euros. ");

		lancer();
	}
}
