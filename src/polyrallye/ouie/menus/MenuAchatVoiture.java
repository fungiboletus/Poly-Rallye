package polyrallye.ouie.menus;

import polyrallye.controlleur.Main;
import polyrallye.modele.Joueur;
import polyrallye.modele.Voiture;
import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Liseuse;
import polyrallye.ouie.Menu;

public class MenuAchatVoiture extends Menu implements ActionMenu {

	protected Voiture voiture;

	public MenuAchatVoiture(final MenuVoitureMagasin menuPrecedent, Voiture v) {
		super(menuPrecedent);

		voiture = v;

		ajouterElement("Confirmer", new ActionMenu() {

			@Override
			public void actionMenu() {
				try {
					Joueur.session.acheterVoiture(voiture);
					
					Liseuse.lire("Félicications pour votre achat. La voiture est dans votre garage");
					Main.revenirAuMenuPrincipal();
					
				} catch (Exception e) {
					Liseuse.lire("Impossible d'acheter la voiture");
					Liseuse.lire(e.getMessage());
				}
			}
		});
	}

	@Override
	public void actionMenu() {
		
		int argent = Joueur.session.getArgent();
		int prix = voiture.getPrix();
		
		if (prix > argent)
		{
			Liseuse.lire("Il vous manque ");
			Liseuse.lire(prix-argent);
			Liseuse.lire(" euros pour acheter la voiture ");
			Liseuse.lire(voiture.getNom());
		} else
		{
			Liseuse.lire("Acheter la voiture ");
			Liseuse.lire(voiture.getNom());
			Liseuse.lire(" pour ");
			Liseuse.lire(prix);
			Liseuse.lire(" euros. Il vous restera ");
			Liseuse.lire(argent-prix);
			Liseuse.lire("euros. ");
			
			lancer();
		}
	}
}
