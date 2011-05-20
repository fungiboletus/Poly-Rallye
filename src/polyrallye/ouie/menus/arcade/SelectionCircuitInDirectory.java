package polyrallye.ouie.menus.arcade;

import java.io.File;

import polyrallye.modele.voiture.Voiture;
import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Menu;
import polyrallye.ouie.liseuse.Liseuse;
import polyrallye.ouie.menus.LancementCourse;

public class SelectionCircuitInDirectory extends Menu implements ActionMenu {

	private Voiture voiture;
	String rep;

	public SelectionCircuitInDirectory(Menu menuPrecedent, String t, Voiture v) {
		super(menuPrecedent);
		voiture = v;
		rep = t;

	}

	@Override
	public void actionMenu() {
		Liseuse.lire("Veuillez choisir un circuit");
		lancer();
	}

	@Override
	public void remplir() {
		File[] op = new File("Circuits/" + rep).listFiles();
		for (int i = 0; i < op.length; i++) {
			String t = op[i].getName().substring(0,
					op[i].getName().indexOf("."));
			if (!op[i].isDirectory())
				ajouterElement(t, new LancementCourse(voiture, rep + "/" + t,
						null));
		}
	}

}
