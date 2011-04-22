package polyrallye.ouie.menus.carriere;

import polyrallye.controlleur.Main;
import polyrallye.modele.personnes.Joueur;
import polyrallye.modele.voiture.Voiture;
import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Menu;
import polyrallye.ouie.liseuse.Liseuse;

public class AchatVoiture extends Menu implements ActionMenu {

	protected Voiture voiture;

	public AchatVoiture(final VoitureMagasin menuPrecedent, Voiture v) {
		super(menuPrecedent);

		voiture = v;
	}

	@Override
	public void actionMenu() {
		
		int argent = Joueur.session.getArgent();
		int prix = voiture.getPrix();
		
		if (prix > argent)
		{
			StringBuilder sb = new StringBuilder("Il vous manque ");
			sb.append(prix-argent);
			sb.append(" euros pour acheter cette voiture");
			Liseuse.lire(sb.toString());
		} else
		{
			StringBuilder sb = new StringBuilder("Êtes vous certain de vouloir acheter cette voiture pour ");
			sb.append(prix);
			sb.append(" euros, il vous restera ");
			sb.append(argent-prix);
			sb.append(" euros");
			
			Liseuse.lire(sb.toString());
			
			lancer();
		}
	}

	@Override
	public void remplir() {
		ajouterElement("Confirmer", new ActionMenu() {

			@Override
			public void actionMenu() {
				try {
					Joueur.session.acheterVoiture(voiture);
					
					Liseuse.lire("félicitations pour votre achat. la voiture est dans votre garage");
					Main.revenirAuMenuPrincipal();
					
				} catch (Exception e) {
					Liseuse.lire("Impossible d'acheter la voiture");
					Liseuse.lire(e.getMessage());
				}
			}
		});
	}
}
