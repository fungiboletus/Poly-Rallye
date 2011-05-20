package polyrallye.ouie.menus.arcade;

import java.io.File;

import polyrallye.modele.voiture.Voiture;
import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Menu;
import polyrallye.ouie.liseuse.Liseuse;

public class SelectionCircuit extends Menu implements ActionMenu {
	
	private Voiture voiture;


		public SelectionCircuit(Menu menuPrecedent,Voiture v) {
			super(menuPrecedent);
			voiture=v;
			
		}
		
		@Override
		public void actionMenu() {
			Liseuse.lire("Veuillez selectionner une categorie de circuit");
			lancer();
		}
		
		@Override
		public void remplir() {
			File[] op = new File("Circuits/").listFiles();
			for (int i = 0; i < op.length; i++) {
				String t = op[i].getName();
				if(op[i].isDirectory())
				ajouterElement(t,new SelectionCircuitInDirectory(this,t, voiture));
			}
			
			

		}

	


}
