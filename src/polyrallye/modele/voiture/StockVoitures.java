package polyrallye.modele.voiture;

import java.io.File;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.jdom.Element;

import polyrallye.ouie.liseuse.Liseuse;
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
	protected static Map<String, Voiture> voituresParNom;

	/**
	 * Arbre de listes de voitures correspondant à la hierarchie des voitures.
	 * 
	 * Le premier niveau correspond aux constructeurs, le deuxièmes aux modèles, et le troisième aux versions.
	 */
	protected static Map<String, Map<String, Map<String, Voiture>>> hierarchieVoitures;
	
	/**
	 * Arbre de voitures classées en fonction de leurs performances.
	 */
	protected static NavigableMap<Double, Voiture> voituresParPerformances;

	static {
		voituresParNom = new TreeMap<String, Voiture>();
		hierarchieVoitures = new TreeMap<String, Map<String, Map<String, Voiture>>>();
		voituresParPerformances = new TreeMap<Double, Voiture>();

		File dossier = new File("Voitures");

		if (!dossier.isDirectory()) {
			Liseuse.lire("Le jeu ne contient pas de dossier de voitures.");
		}

		chargerDossier(dossier);
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
	protected static void chargerDossier(File dossier) {
		for (File fichier : dossier.listFiles()) {
			if (fichier.isDirectory()) {
				chargerDossier(fichier);
			} else if (fichier.getName().endsWith(".xml")) {
				try {
					chargerFichierVoiture(fichier);
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
	protected static void chargerFichierVoiture(File fichier) throws Exception {
		
		Element noeud = GestionXML.chargerNoeudRacine(fichier);
		
		Voiture v = new Voiture(noeud);
		
		voituresParNom.put(v.getNomComplet(), v);
		
		double score = v.getScore();
		
		Voiture ancienne = voituresParPerformances.get(score);
		
		if (ancienne != null) {
			System.err.println("La voiture "+v.toString()+" a les mêmes performances que la voiture "+ancienne.toString()+". Elle est donc ignorée.");
		}
		else
		{
			voituresParPerformances.put(score, v);			
		}
		
		String constructeur = v.getConstructeur();
		String nom = v.getNom();
		String version = v.getVersion();
		
		Map<String, Map<String, Voiture>> mapConstructeur = hierarchieVoitures.get(constructeur);
		
		if (mapConstructeur == null) {
			mapConstructeur = new TreeMap<String, Map<String,Voiture>>();
			hierarchieVoitures.put(constructeur, mapConstructeur);
		}
		
		Map<String, Voiture> mapModeles = mapConstructeur.get(nom);
		
		if (mapModeles == null) {
			mapModeles = new TreeMap<String, Voiture>();
			mapConstructeur.put(nom, mapModeles);
		}
		
		mapModeles.put(version, v);
	}

	/**
	 * Récupère une voiture à partir de son nom.
	 * 
	 * @param nom
	 *            Nom de la voiture
	 * @return Voiture
	 */
	public static Voiture getVoitureParNom(String nom) {
		return voituresParNom.get(nom);
	}

	/**
	 * Récupère la liste des voitures (sous forme de tableau associatif)
	 * 
	 * @return Liste des voitures
	 */
	public static Map<String, Voiture> getVoituresParNom() {
		return voituresParNom;
	}

	/**
	 * Récupère l'arbre contenant la hiérarchie des voitures.
	 * 
	 * Les éléments de l'arbre peuvent être des sous arbres du même type, ou des
	 * voitures.
	 * 
	 * @return Hierachie des voitures
	 */
	public static Map<String, Map<String, Map<String, Voiture>>> getHierarchie() {
		return hierarchieVoitures;
	}

	public static NavigableMap<Double, Voiture> getVoituresParPerformances() {
		return voituresParPerformances;
	}

}
