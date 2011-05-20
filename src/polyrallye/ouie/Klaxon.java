package polyrallye.ouie;

import java.io.File;

import polyrallye.ouie.utilitaires.Sound;

/**
 * Klaxon de la voiture.
 * 
 */
public class Klaxon {

	/**
	 * Nom du klaxon.
	 */
	protected String name;

	/**
	 * Son du klaxon.
	 */
	protected Sound klaxon;

	/**
	 * Est-ce que le klaxon est déjà enclanché ?
	 */
	protected boolean enclanche;

	public Klaxon(String t) {

		String rep = "Ressources/Sons/klaxon/";

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

	/**
	 * Enclanche le klaxon
	 */
	public void pouet() {
		if (!enclanche) {
			klaxon.pause(false);
			enclanche = true;
		}
	}

	/**
	 * Arrête le klaxon.
	 */
	public void pasPouet() {
		if (enclanche) {
			klaxon.pause(true);
			klaxon.setOffset(0.0f);
			enclanche = false;
		}
	}

	/**
	 * Supprime le klaxon, et donc le son associé.
	 */
	public void delete() {
		klaxon.delete();
	}
}
