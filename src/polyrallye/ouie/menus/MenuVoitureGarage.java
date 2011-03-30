package polyrallye.ouie.menus;

import polyrallye.modele.voiture.Voiture;
import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Menu;

public class MenuVoitureGarage extends Menu implements ActionMenu {

	protected Voiture voiture;

	public MenuVoitureGarage(Menu menuPrecedent, Voiture v) {
		super(menuPrecedent);

		voiture = v;

		ajouterElement("Tester la voiture", null);

		ajouterElement("Vendre la voiture", new MenuVenteVoiture(this, voiture));

		ajouterElement("Écouter les spécifications", new ActionMenu() {

			@Override
			public void actionMenu() {
				voiture.lireSpecifications();
			}
		});

		ajouterElement("Voir plus d'informations sur le web", new MenuSources(this, v.getSources()));
	}

	@Override
	public void actionMenu() {
		// Liseuse.lire(voiture.getNom());
		// voiture.getTransmission().calculerRapports();
		lancer();

	}

}
