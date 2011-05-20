package polyrallye.ouie.menus;

import polyrallye.controlleur.Course;
import polyrallye.modele.championnat.Championnat;
import polyrallye.modele.championnat.Etape;
import polyrallye.modele.voiture.Voiture;
import polyrallye.ouie.ActionMenu;

public class LancementCourse implements ActionMenu {

	protected Voiture voiture;

	protected String circuit;
	protected Championnat champ;
	protected Etape etape;

	public LancementCourse(Voiture voiture, String circuit, Etape etape, Championnat champ) {
		this.voiture = voiture;
		this.circuit = circuit;
		this.etape = etape;
		this.champ = champ;
	}

	@Override
	public void actionMenu() {
		new Course(voiture, circuit, etape, champ).start();
	}
}