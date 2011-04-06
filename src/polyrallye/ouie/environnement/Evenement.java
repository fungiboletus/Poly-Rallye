package polyrallye.ouie.environnement;

import polyrallye.modele.Circuit;
import polyrallye.modele.ContenuCircuit;
import polyrallye.ouie.Sound;
import polyrallye.ouie.Sound.SoundException;

public class Evenement implements ContenuCircuit {
	
	protected Circuit circuit;
	
	protected String type;
	protected double distance;
	protected double longueur;	
	protected String param;
	
	protected Sound son;
	
	public Evenement(String t,double d,double l,String p,Circuit c) {
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
	public double getDistance() {
		return distance;
	}

//	@Override
//	public double getLongueur() {
//		return longueur;
//	}

}
