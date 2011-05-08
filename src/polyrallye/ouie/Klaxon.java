package polyrallye.ouie;

import java.io.File;

import polyrallye.ouie.utilitaires.Sound;

public class Klaxon {
	protected String name;

	protected Sound klaxon;
	
	protected boolean enclanche;

	public Klaxon(String t) {

		String rep = "Sons/klaxon/";

		if (t.contains("Gendarmerie"))
			name = "gendarmerie";
		else if (t.contains("2CV"))
			name = "honka";
		else
			name = t.substring(0, t.indexOf(" "));

		if (!new File(rep + name + ".wav").exists())
			name = "defaut";

		klaxon = new Sound(rep + name + ".wav");

		klaxon.setGain(2f);
		klaxon.setPosition(0, 0, 0);
		klaxon.setLoop(true);

		klaxon.play();
		klaxon.pause(true);

	}

	public void pouet() {
		if (!enclanche) {
			klaxon.pause(false);
			enclanche = true;
		}
	}

	public void pasPouet() {
		if (enclanche) {
			klaxon.pause(true);
			klaxon.setOffset(0.0f);
			enclanche = false;
		}
	}
	
	public void delete() {
		klaxon.delete();
	}
}
