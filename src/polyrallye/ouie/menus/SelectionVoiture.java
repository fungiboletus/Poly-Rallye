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
import polyrallye.ouie.liseuse.Liseuse;
import polyrallye.ouie.utilitaires.Sound;

public class SelectionVoiture extends Menu implements ActionMenu {

	protected static Sound musique;
	
	protected Map<String, Map<String, Map<String, Voiture>>> hierarchie;
	
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
		this(menuPrecedent, modeArcade, StockVoitures.getHierarchie());
	}
	
	protected SelectionVoiture(Menu menuPrecedent, boolean modeArcade, Map<String, Map<String, Map<String, Voiture>>> hierarchie) {
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

		if (hierarchie == null) return;
		
		for (Entry<String, Map<String, Map<String, Voiture>>> c : hierarchie.entrySet()) {
			SelectionVoiture menuConstructeur = new SelectionVoiture(this, modeArcade, null);
			
			for (Entry<String, Map<String, Voiture>> cc : c.getValue().entrySet()) {
				SelectionVoiture menuModele = new SelectionVoiture(menuConstructeur, modeArcade, null);
				
				// Les voitures sont triées par prix, donc on utilise un tableau
				// temporaire
				List<Voiture> listeVoitures = new ArrayList<Voiture>();
				
				for (Entry<String, Voiture> ccc : cc.getValue().entrySet()) {
					listeVoitures.add(ccc.getValue());
				}
				
				// Comparateur en fonction des prix des voitures
				Comparator<Voiture> comparateur = new Comparator<Voiture>() {
					@Override
					public int compare(Voiture o1, Voiture o2) {
						return ((Double) o1.getScore()).compareTo(o2.getScore());
					}
				};

				// Trie des voitures en fonction de leur prix
				Collections.sort(listeVoitures, comparateur);

				for (Voiture v : listeVoitures) {
					String libelle = v.getVersion();
					if (libelle == null) {
						libelle = "de série";
					}
					
					if (modeArcade) {
						menuModele.ajouterElement(libelle, new VoitureArcade(menuModele, v));
					} else {
						menuModele.ajouterElement(libelle, new VoitureMagasin(menuModele, v));
					}
				}
				
				menuConstructeur.ajouterElement(cc.getKey(), menuModele);
			}
			
			ajouterElement(c.getKey(), menuConstructeur);
		}
	}

	@Override
	public void actionMenu() {
		if (hierarchie != null) {
			Liseuse.lire("Sélectionnez la voiture");
		}
		
		lancer();
	}

}