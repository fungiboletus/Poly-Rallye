package polyrallye.ouie.menus.carriere;

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

		StringBuilder sb = new StringBuilder("Vendre cette voiture pour ");
		sb.append(prix);
		sb.append(" euros, vous aurez alors ");
		sb.append(argent + prix);
		sb.append(" euros");

		Liseuse.lire(sb.toString());
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
