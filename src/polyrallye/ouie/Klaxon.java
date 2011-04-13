package polyrallye.ouie;

import java.io.File;

import polyrallye.ouie.utilitaires.Sound;

public class Klaxon {
	protected String name;
	
	protected Sound klaxon;
	
	public Klaxon(String t) {
		
		String rep = "Sons/klaxon/";
		
		if (t.contains("gendarmerie"))
			name = "gendarmerie";
		else
			name = t.substring(0, t.indexOf(" "));
		
		if (!new File(rep + name+".wav").exists())
			name = "defaut";
		
		klaxon = new Sound(rep+name+".wav");
		
		klaxon.setGain(1f);
		klaxon.setPosition(0, 0, 0);
		klaxon.setLoop(true);
		
		klaxon.play();
		klaxon.pause(true);
		
	}
	
	public void play() {
		klaxon.pause(false);
	}
	
	public void pause() {
		klaxon.pause(true);
	}
}
