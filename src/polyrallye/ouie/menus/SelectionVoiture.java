package polyrallye.ouie.menus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import polyrallye.modele.voiture.StockVoitures;
import polyrallye.modele.voiture.Voiture;
import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Menu;
import polyrallye.ouie.utilitaires.Sound;

public class SelectionVoiture extends Menu implements ActionMenu {

	protected static Sound musique;
	
	protected Map<String, Object> hierarchie;
	
	protected boolean modeArcade;

	static {
		/*musique = new Sound("Ressources/Reno Project - 1.0/02 - Atlanta.ogg");
		musique.play();
		musique.pause(true);
		musique.setOffset(100);
		musique.setLoop(true);*/
	}
	
	public Sound getMusique() {
		return musique;
	}
	
	public SelectionVoiture(Menu menuPrecedent, boolean modeArcade) {
		this(menuPrecedent, StockVoitures.getHierarchie(), modeArcade);
	}

	public SelectionVoiture(Menu menuPrecedent, Map<String, Object> hierarchie, boolean modeArcade) {
		super(menuPrecedent);
		this.modeArcade = modeArcade;
		this.hierarchie = hierarchie;
	}

	/*@Override
	public void actionMenu() {
		if (hierarchie != null) {
			Liseuse.lire("Vous pouvez acheter de nombreuses voitures dans les magasins.");
		}
		
		lancer();
	}*/

	@Override
	public void remplir() {
		
		// Les voitures sont triées par prix, donc on utilise un tableau
		// temporaire
		List<Voiture> listeVoitures = new ArrayList<Voiture>();

		for (Entry<String, Object> c : hierarchie.entrySet()) {
			Object v = c.getValue();

			if (v instanceof Voiture) {
				// Ajout au tableau temporaire des voitures
				listeVoitures.add((Voiture) v);
			} else if (v instanceof Map<?, ?>) {
				// C'est parfois franchement très très moche le java
				@SuppressWarnings("unchecked")
				Map<String, Object> v2 = (Map<String, Object>) v;
				ajouterElement(c.getKey(), new SelectionVoiture(this, v2, modeArcade));
			}
		}

		if (listeVoitures.size() > 0) {
			// Comparateur en fonction des prix des voitures
			Comparator<Voiture> c = new Comparator<Voiture>() {
				@Override
				public int compare(Voiture o1, Voiture o2) {
					return ((Double) o1.getScore()).compareTo(o2.getScore());
				}
			};

			// Trie des voitures en fonction de leur prix
			Collections.sort(listeVoitures, c);

			for (Voiture v : listeVoitures) {
				String libelle = v.getVersion();
				if (libelle == null) {
					libelle = "de série";
				}
				
				if (modeArcade) {
					ajouterElement(libelle, new VoitureArcade(this, v));
				} else {
					ajouterElement(libelle, new VoitureMagasin(this, v));
				}
			}
		}
	}

	@Override
	public void actionMenu() {
		lancer();
	}

}
