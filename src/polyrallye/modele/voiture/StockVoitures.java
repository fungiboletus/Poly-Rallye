package polyrallye.modele.voiture;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.jdom.Element;

import polyrallye.ouie.liseuse.Liseuse;
import polyrallye.utilitaires.GestionXML;
import t2s.util.Random;

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
	
	/**
	 * Arbre de voitures classées en fonction de leurs prix.
	 */
	protected static NavigableMap<Integer, Voiture> voituresParPrix;

	static {
		voituresParNom = new TreeMap<String, Voiture>();
		hierarchieVoitures = new TreeMap<String, Map<String, Map<String, Voiture>>>();
		voituresParPerformances = new TreeMap<Double, Voiture>();
		voituresParPrix = new TreeMap<Integer, Voiture>();

		File dossier = new File("Ressources/Voitures");

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
		
		voituresParPrix.put(v.getPrix(), v);
		
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

	

	@SuppressWarnings("unchecked")
	public static Voiture getVoitureParPerformances(double min, double max) {
		Object voitures [] = voituresParPerformances.subMap(min, true, max, true).entrySet().toArray();
		
		return ((Entry<Double, Voiture>) voitures[Random.unsignedDelta(0, voitures.length)]).getValue();
	}
	
	public static NavigableMap<Integer,Voiture> getVoituresParPrix(int max) {
		return voituresParPrix;
	}
	
	public static NavigableMap<Double, Voiture> getVoituresParPerformances() {
		return voituresParPerformances;
	}

	/**
	 * Calcule une liste de voitures aux performances et caractéristiques équivalentes à une voiture donnée.
	 * 
	 * C'est très utile pour avoir des adversaires aux voitures équivalentes.
	 * 
	 * @param v Voiture clef
	 * @param nb Nombre de voitures
	 * @return Liste des voitures équivalentes
	 */
	@SuppressWarnings("unchecked")
	public static List<Voiture> getVoituresEquivalentes(Voiture v, int nb) {
		List<Voiture> liste = new ArrayList<Voiture>();
		
		double score = v.getScore();
		
		// 50 => 15
		// 1000 => 100
		double marge = 15.0 + (score - 50.0)*((85.0)/(950.0));
		
		Object voitures [] = voituresParPerformances.subMap(score-marge, true, score+marge, true).entrySet().toArray();
		
		int nbVoitures = voitures.length;
		
		int nnb = nb;
		for (int i = 0; i < nnb;++i) {
			
			Voiture nouvelleVoiture = ((Entry<Double, Voiture>) voitures[Random.unsignedDelta(0, nbVoitures)]).getValue();
			
			if (liste.contains(nouvelleVoiture) && i < nb*3) {
				++nnb;
			} else {				
				liste.add(nouvelleVoiture);
			}
		}
		
		return liste;
	}
	
}
