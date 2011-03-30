package polyrallye.ouie.environnement;

import polyrallye.modele.circuit.Circuit;
import polyrallye.modele.circuit.ContenuCircuit;
import polyrallye.ouie.Sound;
import polyrallye.ouie.Sound.SoundException;

public class Evenement implements ContenuCircuit {
	
	protected Circuit circuit;
	
	protected String type;
	protected long distance;
	protected long longueur;	
	protected String param;
	
	protected Sound son;
	
	public Evenement(String t,long d,long l,String p,Circuit c) {
		type=t;
		distance=d;
		longueur=l;
		param=p;
		circuit=c;
		son=null;
		if (type.equals("son")) {
			son = new Sound();
		}
	}
	
	public void exec() {
		if(type.equals("environnement")) {
			circuit.changeEnvironnement(param);
		}
		else if (type.equals("terrain")) {
			circuit.changeTerrain(param);
		}
		else if (type.equals("son")) {
			try {
				son.charger("Sons/divers/"+param);
				son.play();
				while(son.isPlaying());
				son.delete();
			} catch (SoundException e) {
				System.out.println("Erreur chargement "+param);
			}
		}
	}
	
	
	@Override
	public long getDistance() {
		return distance;
	}

	@Override
	public long getLongueur() {
		return longueur;
	}

}
