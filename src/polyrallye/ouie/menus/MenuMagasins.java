package polyrallye.ouie.menus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import polyrallye.modele.StockVoitures;
import polyrallye.modele.Voiture;
import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Liseuse;
import polyrallye.ouie.Menu;

public class MenuMagasins extends Menu implements ActionMenu {

	protected boolean racineMagasin;

	public MenuMagasins(Menu menuPrecedent) {
		this(menuPrecedent, StockVoitures.getHierarchie());

		racineMagasin = true;
	}

	public MenuMagasins(Menu menuPrecedent, Map<String, Object> hierarchie) {
		super(menuPrecedent);

		racineMagasin = false;

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
				ajouterElement(c.getKey(), new MenuMagasins(this, v2));
			}
		}

		if (listeVoitures.size() > 0) {
			// Comparateur en fonction des prix des voitures
			Comparator<Voiture> c = new Comparator<Voiture>() {
				@Override
				public int compare(Voiture o1, Voiture o2) {
					return ((Integer) o1.getPrix()).compareTo(o2.getPrix());
				}
			};

			// Trie des voitures en fonction de leur prix
			Collections.sort(listeVoitures, c);

			for (Voiture v : listeVoitures) {
				ajouterElement(v.getNom(), new MenuVoitureMagasin(this, v));
			}
		}
	}

	@Override
	public void actionMenu() {
		if (racineMagasin) {
			Liseuse.lire("Vous pouvez acheter de nombreuses voitures dans les magasins.");
		}
		
		lancer();
	}

}
