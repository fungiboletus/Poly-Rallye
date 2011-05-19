package polyrallye.ouie.menus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import polyrallye.modele.personnes.Joueur;
import polyrallye.modele.voiture.StockVoitures;
import polyrallye.modele.voiture.Voiture;
import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Menu;
import polyrallye.ouie.liseuse.Liseuse;
import polyrallye.ouie.menus.arcade.VoitureArcade;
import polyrallye.ouie.menus.carriere.VoitureMagasin;
import polyrallye.ouie.utilitaires.Sound;

public class SelectionVoitureMagasin extends Menu implements ActionMenu {

	protected static Sound musique;

	protected enum niveauHierarchie {
		CONSTRUCTEURS, MODELES, VARIANTE
	};

	protected niveauHierarchie niveau;

	protected boolean modeArcade;
	protected boolean trie;

	static {
		/*
		 * musique = new
		 * Sound("Ressources/Reno Project - 1.0/02 - Atlanta.ogg");
		 * musique.play(); musique.pause(true); musique.setOffset(100);
		 * musique.setLoop(true);
		 */
	}

	public Sound getMusique() {
		return musique;
	}

	public SelectionVoitureMagasin(Menu menuPrecedent, boolean modeArcade) {
		this(menuPrecedent, modeArcade, StockVoitures.getHierarchie(), false);
	}

	public SelectionVoitureMagasin(Menu menuPrecedent, boolean modeArcade,
			boolean trie) {
		this(menuPrecedent, modeArcade, StockVoitures.getHierarchie(), trie);

	}

	protected SelectionVoitureMagasin(Menu menuPrecedent,
			niveauHierarchie niveau) {
		super(menuPrecedent);
		this.niveau = niveau;
	}

	protected SelectionVoitureMagasin(Menu menuPrecedent, boolean modeArcade,
			Map<String, Map<String, Map<String, Voiture>>> hierarchie,
			boolean trie) {
		this(menuPrecedent, niveauHierarchie.CONSTRUCTEURS);
		this.trie=trie;
		this.modeArcade = modeArcade;
			
			for (Entry<String, Map<String, Map<String, Voiture>>> c : hierarchie
					.entrySet()) {
				SelectionVoitureMagasin menuConstructeur = new SelectionVoitureMagasin(
						this, niveauHierarchie.MODELES);

				for (Entry<String, Map<String, Voiture>> cc : c.getValue()
						.entrySet()) {
					SelectionVoitureMagasin menuModele = new SelectionVoitureMagasin(
							menuConstructeur, niveauHierarchie.VARIANTE);

					// Les voitures sont triées par prix, donc on utilise un
					// tableau
					// temporaire
					List<Voiture> listeVoitures = new ArrayList<Voiture>();

					for (Entry<String, Voiture> ccc : cc.getValue().entrySet()) {
						listeVoitures.add(ccc.getValue());
					}

					// Comparateur en fonction des prix des voitures
					Comparator<Voiture> comparateur = new Comparator<Voiture>() {
						@Override
						public int compare(Voiture o1, Voiture o2) {
							return ((Double) o1.getScore()).compareTo(o2
									.getScore());
						}
					};

					// Trie des voitures en fonction de leur prix
					Collections.sort(listeVoitures, comparateur);

					for (Voiture v : listeVoitures) {
						String libelle = v.getVersion();
						if (libelle == null || libelle.equals("serie")) {
							libelle = "de série";
						}

						if (modeArcade) {
							menuModele.ajouterElement(libelle,
									new VoitureArcade(menuModele, v));
						} else {
							if (!trie || v.getPrix()<=Joueur.session.getArgent())
							menuModele.ajouterElement(libelle,
									new VoitureMagasin(menuModele, v));

						}
					}
					if(!menuModele.isEmpty())
					menuConstructeur.ajouterElement(cc.getKey(), menuModele);
				}
				if(!menuConstructeur.isEmpty())
				ajouterElement(c.getKey(), menuConstructeur);
			}
		
	}

	@Override
	public void remplir() {

	}

	@Override
	public void actionMenu() {
		switch (niveau) {
		case CONSTRUCTEURS:

			if (!modeArcade) {
				Liseuse.lire("Vous avez " + Joueur.session.getArgent()
						+ " euros");
			}
			Liseuse.lire("Sélectionnez le constructeur");
			break;
		case MODELES:
			Liseuse.lire("Sélectionnez le modèle");
			break;
		case VARIANTE:
			Liseuse.lire("Sélectionnez la version");
			break;
		}

		lancer();
	}

}
