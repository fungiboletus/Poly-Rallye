package polyrallye.ouie.menus;

import polyrallye.controlleur.Course;
import polyrallye.modele.voiture.StockVoitures;
import polyrallye.modele.voiture.Voiture;
import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Menu;
import polyrallye.ouie.liseuse.Liseuse;

public class Permis extends Menu implements ActionMenu {
	
	public Permis(Menu menuPrecedent) {
		super(menuPrecedent);
	}

	@Override
	public void actionMenu() {
		Liseuse.lire("Choisissez le permis");
		lancer();
	}

	@Override
	public void remplir() {
	
		ajouterElement("Permis ligne droite - Apprenez les bases en roulant en ligne droite", new ActionMenu() {
			
			@Override
			public void actionMenu() {
				Voiture v = StockVoitures.getVoitureParNom("Fiat Panda 4x4");
				new Course(v,"Permis/ligne").start();
				
				
			}
		});
		ajouterElement("Permis Virage - Apprenez les bases en passant un virage", new ActionMenu() {
			
			@Override
			public void actionMenu() {
				Voiture v = StockVoitures.getVoitureParNom("Fiat Panda 4x4");
				new Course(v,"Permis/Virage").start();
				
				
			}
		});
		ajouterElement("Permis Boite de Vitesse - Apprenez a rouler avec une boite de vitesse manuelle", new ActionMenu() {
			
			@Override
			public void actionMenu() {
				Voiture v = StockVoitures.getVoitureParNom("Fiat Panda 4x4");
				new Course(v,"Permis/boite").start();
				
				
			}
		});
		ajouterElement("Permis divers - Découvrez les petits plus de PolyRallye", new ActionMenu() {
			
			@Override
			public void actionMenu() {
				Voiture v = StockVoitures.getVoitureParNom("Fiat Panda 4x4");
				new Course(v,"Permis/divers").start();
				
				
			}
		});
	}

}
