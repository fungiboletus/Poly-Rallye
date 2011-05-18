package polyrallye.ouie.menus.arcade;

import java.io.File;

import polyrallye.controlleur.Course;
import polyrallye.controlleur.Main;
import polyrallye.modele.voiture.Voiture;
import polyrallye.ouie.Menu;
import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.liseuse.Liseuse;
import polyrallye.ouie.menus.SelectionVoitureMagasin;
import polyrallye.ouie.menus.SelectionVoiturePerformances;

public class SelectionCircuitInDirectory extends Menu implements ActionMenu {
	
	private Voiture voiture;
	String rep;


		public SelectionCircuitInDirectory(Menu menuPrecedent,String t,Voiture v) {
			super(menuPrecedent);
			voiture=v;
			rep=t;
			
		}
		
		@Override
		public void actionMenu() {
			Liseuse.lire("Veuillez choisir un circuit");
			lancer();
		}
		
		@Override
		public void remplir() {
			File[] op = new File("Circuits/"+rep).listFiles();
			for (int i = 0; i < op.length; i++) {
				Main.logImportant(op[i].getName());
				String t = op[i].getName().substring(0, op[i].getName().indexOf("."));
				if(!op[i].isDirectory())
				ajouterElement(t,new Course( voiture,rep+"/"+t));
			}
		}

	


}


