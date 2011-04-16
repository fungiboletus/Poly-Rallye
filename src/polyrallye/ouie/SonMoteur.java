package polyrallye.ouie;

import java.io.File;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

import polyrallye.modele.voiture.Voiture;
import polyrallye.ouie.liseuse.Liseuse;
import polyrallye.ouie.utilitaires.Sound;

/**
 * @author Antoine Pultier
 *
 * Classe de gestion du son moteur. En cours de développement.
 */
public class SonMoteur {

	protected NavigableMap<Integer, Sound> sons;
	
	protected Voiture voiture;
	
	protected Sound passageRapport;
	
	public SonMoteur(Voiture voiture) {
		this.voiture = voiture;
		sons = new TreeMap<Integer, Sound>();
		
		String variante = "r5";
		String constructeur = voiture.getConstructeur();
		
		if (constructeur.equals("Audi") || constructeur.equals("Subaru")) {
			variante = "cobra";
		}
		else if (constructeur.equals("Bugatti")) {
			variante = "bugatti";
		}
		else if (constructeur.equals("Peugeot")) {
			variante = "207";
		}
		else if (constructeur.equals("Fiat")) {
			variante = "saab";
		}
		else if (constructeur.equals("Ferrari")) {
			variante = "enzo";
		}
		
		System.out.println(variante);
		//Liseuse.lire(variante);
		
		File dossier = new File("Sons/moteurs/"+variante);
		
		for (File son : dossier.listFiles()) {
			String nom = son.getName();
			Integer nb = Integer.parseInt(nom.substring(0, nom.indexOf(".")));
			
			Sound s = new Sound(son.getAbsolutePath());
			s.setLoop(true);
			s.setOffset(2.0f);
			sons.put(nb, s);
		}
		
		passageRapport = new Sound("Sons/voiture/rapport.wav");
		passageRapport.setGain(0.8f);
	}

	public void play() {
		for (Entry<Integer, Sound> tuple : sons.entrySet()) {
			Sound s = tuple.getValue();
			s.play();
			s.pause(true);
		}
	}
	
	public void stop() {
		for (Entry<Integer, Sound> tuple : sons.entrySet()) {
			Sound s = tuple.getValue();
			s.stop();
			s.delete();
		}
	}

	public void setRegime(float regime, boolean acceleration) {
		// System.out.println(regime);

		Integer intRegime = (int) regime;

		// TODO Recoder ça proprement
		float gain = (0.85f + 0.3f * (regime / 3000.0f));
		
		if (!acceleration) {
			gain *= 0.7f;
		}
		
		gain *= 0.45;

		// Cas particulier plutôt rare, le régime est déjà dans les sons
		Sound sonParfait = sons.get(intRegime);
		if (sonParfait != null) {
			sonParfait.setPitch(1.0f);
			sonParfait.setGain(gain);
			sonParfait.pause(false);

			for (Entry<Integer, Sound> e : sons.entrySet()) {
				if (!e.getKey().equals(intRegime)) {
					e.getValue().pause(true);
					// e.getValue().setGain(0.0f);
				}
			}
		} else {
			// NavigableMap <3
			Entry<Integer, Sound> min = sons.lowerEntry(intRegime);
			Entry<Integer, Sound> max = sons.higherEntry(intRegime);

			Integer i_min = (min != null) ? min.getKey() : 0;
			Integer i_max = (max != null) ? max.getKey() : 0;

			if (min != null) {
				Sound s_min = min.getValue();

				float r_a = (max != null) ? (i_max - regime) / (i_max - i_min)
						: 1.0f;
				s_min.setGain(r_a * gain);
				s_min.setPitch(regime / i_min);
				s_min.pause(false);

			}

			if (max != null) {
				Sound s_max = max.getValue();

				float r_b = (min != null) ? (regime - i_min) / (i_max - i_min)
						: 1.0f;
				s_max.setGain(r_b * gain);
				s_max.setPitch(regime / i_max);
				s_max.pause(false);

			}

			// System.out.println(i_min);
			for (Entry<Integer, Sound> e : sons.entrySet()) {
				if (!(e.getKey().equals(i_max) || e.getKey().equals(i_min))) {
					e.getValue().pause(true);
				}
			}
		}
	}
	
	public void passageRapport() {
		passageRapport.play();
	}

}
