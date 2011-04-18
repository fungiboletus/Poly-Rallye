package polyrallye.ouie.menus.carriere;

import polyrallye.modele.voiture.Voiture;
import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Menu;
import polyrallye.ouie.menus.MenuSources;

public class VoitureGarage extends Menu implements ActionMenu {

	protected Voiture voiture;

	public VoitureGarage(Menu menuPrecedent, Voiture v) {
		super(menuPrecedent);

		voiture = v;
	}

	@Override
	public void actionMenu() {
		// Liseuse.lire(voiture.getNom());
		// voiture.getTransmission().calculerRapports();
		lancer();

	}

	@Override
	public void remplir() {
		ajouterElement("Tester la voiture", null);

		ajouterElement("Vendre la voiture", new VenteVoiture(this, voiture));

		ajouterElement("Écouter les spécifications", new ActionMenu() {

			@Override
			public void actionMenu() {
				voiture.lireSpecifications();
			}
		});

		ajouterElement("Voir plus d'informations sur le web", new MenuSources(this, voiture.getSources()));
	}

}
