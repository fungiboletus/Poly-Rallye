package polyrallye.modele.voiture;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import org.jdom.Element;

import polyrallye.ouie.Liseuse;
import polyrallye.utilitaires.GestionXML;

/**
 * @author Antoine Pultier Cette classe s'occupe de charger les voitures
 *         stockées dans des fichiers xml dans le dossier Voitures.
 * 
 *         Ensuite, il est possible d'obtenir une voiture avec son nom, la liste
 *         des voitures, ou la hiérarchie des voitures, à partir de celle du
 *         système de fichiers.
 */
public abstract class StockVoitures {

	/**
	 * Map permettant d'obtenir une voiture à partir de son nom, de manière
	 * efficace.
	 */
	protected static Map<String, Voiture> voitures;

	/**
	 * Arbre de listes de voitures correspondant à la hierarchie des voitures.
	 */
	protected static Map<String, Object> hierarchieVoitures;

	static {
		voitures = new TreeMap<String, Voiture>();
		hierarchieVoitures = new TreeMap<String, Object>();

		File dossier = new File("Voitures");

		if (!dossier.isDirectory()) {
			Liseuse.lire("Le jeu ne contient pas de dossier de voitures.");
		}

		chargerDossier(dossier, hierarchieVoitures);
	}

	/**
	 * Charge récursivement les voitures à partir des dossiers.
	 * 
	 * Les voitures sont ajoutés dans la liste globale des voitures, et dans
	 * l'arbre hiérarchique.
	 * 
	 * @param dossier
	 *            Dossier dans lequel charger récursivement les voitures
	 * @param hierarchie
	 *            Noeud de l'arbre permettant de stocker la hiérachie des
	 *            voitures
	 */
	protected static void chargerDossier(File dossier,
			Map<String, Object> hierarchie) {
		for (File fichier : dossier.listFiles()) {
			if (fichier.isDirectory()) {

				Map<String, Object> nouvelleHierarchie = new TreeMap<String, Object>();

				chargerDossier(fichier, nouvelleHierarchie);

				hierarchie.put(fichier.getName(), nouvelleHierarchie);

			} else if (fichier.isFile() && !fichier.isHidden()
					&& fichier.getName().endsWith(".xml")) {
				try {
					chargerFichierVoiture(fichier, hierarchie);
				} catch (Exception e) {
					System.out.println("Problème avec le fichier " + fichier);
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Charge une voiture à partir d'un fichier.
	 * 
	 * @param fichier
	 *            Fichier XML dans lequel sont contenues les informations de la
	 *            voiture
	 * @param hierarchie
	 *            Noeud de l'arbre permettant de stocker la hiérarchie des
	 *            voitures.
	 * @throws Exception
	 *             Problème lors du chargement du fichier (fichier non valide,
	 *             voiture non valide)
	 */
	protected static void chargerFichierVoiture(File fichier,
			Map<String, Object> hierarchie) throws Exception {
		Element noeud = GestionXML.chargerNoeudRacine(fichier);
		Voiture v = new Voiture(noeud);
		voitures.put(v.getNom(), v);
		hierarchie.put(v.getNom(), v);
	}

	/**
	 * Récupère une voiture à partir de son nom.
	 * 
	 * @param nom
	 *            Nom de la voiture
	 * @return Voiture
	 */
	public static Voiture getVoitureParNom(String nom) {
		return voitures.get(nom);
	}

	/**
	 * Récupère la liste des voitures (sous forme de tableau associatif)
	 * 
	 * @return Liste des voitures
	 */
	public static Map<String, Voiture> getVoitures() {
		return voitures;
	}

	/**
	 * Récupère l'arbre contenant la hiérarchie des voitures.
	 * 
	 * Les éléments de l'arbre peuvent être des sous arbres du même type, ou des
	 * voitures.
	 * 
	 * @return Hierachie des voitures
	 */
	public static Map<String, Object> getHierarchie() {
		return hierarchieVoitures;
	}

}
