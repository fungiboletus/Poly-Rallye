package polyrallye.ouie.menus;

import polyrallye.controlleur.Main;
import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.EcouteurEntrees;
import polyrallye.ouie.FenetreNoire;
import polyrallye.ouie.GestionEntrees;
import polyrallye.ouie.Menu;
import polyrallye.ouie.SonMoteur;
import polyrallye.ouie.environnement.Environnement;
import polyrallye.ouie.liseuse.Liseuse;
import polyrallye.ouie.utilitaires.Sound;

public class Principal extends Menu {

	protected static Sound musique;
	
	static {
		//musique = new Sound("Sons/foret/jour_1.wav");
		//musique.setLoop(true);
	}
	
	public Principal() {
		super(new Quitter());

		/*ajouterElement("Course rapide", new ActionMenu() {
			
			@Override
			public void actionMenu() {
				Environnement e = new Environnement("foret", "nuit", "vent");
				
				e.play();
				
				//final SonMoteur sm = new SonMoteur();
				
				EcouteurEntrees ee = new EcouteurEntrees() {
					
					@Override
					public void selectionner() {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void haut() {
						SonMoteur.accelere = true;
					}
					
					@Override
					public void gauche() {
						SonMoteur.regime *= 1.3;
					}
					
					@Override
					public void droite() {
						SonMoteur.regime *= 0.73;
					}
					
					@Override
					public void bas() {
						SonMoteur.accelere = false;
					}
					
					@Override
					public void annuler() {
						Main.demanderAQuitter();
					}
					
					@Override
					public void aide() {
						// TODO Auto-generated method stub
						
					}
				};
				
				GestionEntrees.getInstance().setEcouteur(ee);
				
				/*new Thread()
				{
					public void run()
					{
						try {*/
							//SonMoteur.lancer();
						/*} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}						
					}
				//}.start();/
			}
		});
		ajouterElement("Garage", new MenuGarage(this));
		ajouterElement("Magasins", new MenuMagasins(this));
		ajouterElement("Permis", new MenuPermis(this));*/
	}
	
	public Sound getMusique() {
		return null;
	}
	
	public void lancer()
	{
		Liseuse.lire("Menu principal");
		super.lancer();
	}

	@Override
	public void remplir() {
		ajouterElement("Mode libre", new Libre(this));
		//ajouterElement("Carrière", new Carriere(this));
	}

}
