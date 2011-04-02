package polyrallye.ouie.menus;

import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Menu;
import polyrallye.ouie.liseuse.Liseuse;

public class Carriere extends Menu implements ActionMenu {

	public Carriere(Menu menuPrecedent) {
		super(menuPrecedent);
	}

	@Override
	public void actionMenu() {
		Liseuse.lire("Le mode carrière vous permet de dominer le monde !");
		lancer();
	}

	@Override
	public void remplir() {
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
	});*/
	ajouterElement("Garage", new Garage(this));
	ajouterElement("Magasins", new SelectionVoiture(this, false));
	ajouterElement("Permis", new Permis(this));
	}

}
