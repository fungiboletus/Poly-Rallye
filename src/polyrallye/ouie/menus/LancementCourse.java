package polyrallye.ouie.menus;

import polyrallye.controlleur.Course;
import polyrallye.modele.voiture.Voiture;
import polyrallye.ouie.ActionMenu;

public class LancementCourse implements ActionMenu {

	protected Voiture voiture;

	protected String circuit;

	public LancementCourse(Voiture voiture, String circuit) {
		this.voiture = voiture;
		this.circuit = circuit;
	}

	@Override
	public void actionMenu() {
		new Course(voiture, circuit).start();
	}
}