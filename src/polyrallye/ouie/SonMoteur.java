package polyrallye.ouie;

import java.io.File;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

import polyrallye.modele.voiture.Voiture;
import polyrallye.ouie.utilitaires.Sound;

/**
 * @author Antoine Pultier
 * 
 *         Classe de gestion du son moteur. En cours de développement.
 */
public class SonMoteur {

	/**
	 * Les différents régimes enregistrés.
	 */
	protected NavigableMap<Integer, Sound> sons;

	/**
	 * La voiture correspondant au moteur.
	 */
	protected Voiture voiture;

	/**
	 * Régime du rupteur correspondant à la voiture.
	 * 
	 * Il est contenu dans cette classe dans le but d'améliorer les
	 * performances.
	 */
	protected float regimeRupteur;

	/**
	 * Gain final pour le volume du moteur.
	 * 
	 * Ce gain change en fonction de la voiture.
	 */
	protected float gainFinal;

	/**
	 * Son du passage d'un rapport.
	 * 
	 * De manière logique, ce son devrait plus se trouver dans une classe ayant
	 * rapport à la transmission, mais créer une classe juste pour un son est
	 * plutôt contre-productif.
	 */
	protected Sound passageRapport;

	public SonMoteur(Voiture voiture) {
		this.voiture = voiture;

		this.regimeRupteur = (float) voiture.getMoteur().getRegimeRupteur();

		// Gain final en fonction du score de la voiture
		{
			float xa = 100.0f;
			float xb = 900.0f;

			float ya = 1.0f;
			float yb = 1.20f;

			gainFinal = ya + ((float) voiture.getScore() - xa)
					* ((yb - ya) / (xb - xa));
		}

		sons = new TreeMap<Integer, Sound>();

		String variante = "r5";
		String constructeur = voiture.getConstructeur();

		if (constructeur.equals("Audi") || constructeur.equals("Subaru")) {
			variante = "cobra";
		} else if (constructeur.equals("Bugatti")) {
			variante = "bugatti";
		} else if (constructeur.equals("Peugeot")) {
			variante = "207";
		} else if (constructeur.equals("Fiat")) {
			variante = "saab";
		} else if (constructeur.equals("Ferrari")) {
			variante = "enzo";
		}

		System.out.println(variante);
		// Liseuse.lire(variante);

		File dossier = new File("Sons/moteurs/" + variante);

		// Récupération des sons correspondants aux différents régimes.
		for (File son : dossier.listFiles()) {
			String nom = son.getName();
			Integer nb = Integer.parseInt(nom.substring(0, nom.indexOf(".")));

			Sound s = new Sound(son.getAbsolutePath());
			s.setLoop(true);
			s.setOffset(2.0f);
			sons.put(nb, s);
		}

		// Récupération du son du passage des rapports.
		passageRapport = new Sound("Sons/voiture/rapport.wav");
		passageRapport.setGain(0.4f);
	}

	/**
	 * Lance les sons du moteur.
	 */
	public void play() {
		for (Entry<Integer, Sound> tuple : sons.entrySet()) {
			Sound s = tuple.getValue();
			s.play();
			s.pause(true);
		}
	}

	/**
	 * Arrête les sons du moteur.
	 */
	public void stop() {
		for (Entry<Integer, Sound> tuple : sons.entrySet()) {
			Sound s = tuple.getValue();
			s.stop();
			s.delete();
		}
	}

	/**
	 * Règle le régime du moteur pour que le son soit adapté en conséquence.
	 * 
	 * @param regime
	 *            Régime du moteur
	 * @param acceleration
	 *            Est-ce que le moteur est en phase d'accélération
	 */
	public void setRegime(float regime, boolean acceleration) {

		// Arrondi du régime, pour gérer le cas du régime égal à un régime
		// enregistré.
		Integer intRegime = (int) regime;

		// Réglage du volume du moteur, en fonction du régime
		// avec une interpolation linéaire, bien évidemment

		float xa = 800.0f;
		float xb = regimeRupteur;

		float ya = 0.75f;
		float yb = 1.0f;

		float gain = ya + (regime - xa) * ((yb - ya) / (xb - xa));

		if (!acceleration) {
			gain *= 0.7f;
		}

		// Gain final (au lieu de modifier la formule, c'est plus simple de
		// faire varier celle-là
		gain *= gainFinal;

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

	/**
	 * Son du passage d'un rapport.
	 */
	public void passageRapport() {
		passageRapport.play();
	}

}
