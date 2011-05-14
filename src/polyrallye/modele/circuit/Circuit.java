package polyrallye.modele.circuit;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import org.jdom.Element;

import polyrallye.controlleur.Main;
import polyrallye.ouie.environnement.Environnement;
import polyrallye.ouie.environnement.Evenement;
import polyrallye.ouie.environnement.Terrain;
import polyrallye.ouie.environnement.TypeEvenement;
import polyrallye.utilitaires.Cartographie;
import polyrallye.utilitaires.GestionXML;

/**
 * Circuit.
 * 
 * Un circuit est créé à partir d'un chemin sur une carte OpenStreetMap.
 * 
 * La carte doit respecter certaines conventions :
 * 
 * Elle ne contient qu'un seul chemin (ou route).
 * 
 * Le premier noeud contient _obligatoirement_ les quatre attributs suivants :
 * 
 * - surface
 * - temps
 * - meteo
 * - environnement
 * 
 * Les noeuds suivants peuvent redéfinir un ou plusieurs des attributs suivants :
 * 
 * - surface
 * - environnement
 * - son
 */
public class Circuit {
	/**
	 * Nom du circuit.
	 */
	protected String nom;
	
	/**
	 * Environnement courant du circuit.
	 */
	protected Environnement environnement;
	
	/**
	 * Terrain courant du circuit.
	 */
	protected Terrain terrain;

	/**
	 * Portions du circuit.
	 */
	protected Queue<Portion> portions;


	protected double distance;

	public Circuit(Element noeud) {
		
		portions = new LinkedList<Portion>();
		
		if (noeud.getChildren("way").size() != 1) {
			System.err
					.println("Le fichier OSM ne contient pas un seul chemin.");
		}

		Element chemin = noeud.getChild("way");

		nom = getTagValue(chemin, "nom");

		// Récupération des noeuds
		HashMap<Integer, Element> noeuds = new HashMap<Integer, Element>();

		for (Object e : noeud.getChildren("node")) {
			Element ch = (Element) e;
			Integer id = GestionXML.getInt(ch.getAttributeValue("id"));
			noeuds.put(id, ch);
		}

		double latCourant = 0;
		double lonCourant = 0;

		double latPrecedent = 0;
		double lonPrecedent = 0;

		double distance = 0;

		//double angle = 0;

		int i = 0;
		
		Portion portionCourante;

		// Parcours du chemin
		for (Object e : chemin.getChildren("nd")) {
			Element ch = (Element) e;

			Integer ref = GestionXML.getInt(ch.getAttributeValue("ref"));
			Element n = noeuds.get(ref);

			if (n == null) {
				System.err.println("Le fichier OSM est corrompu !");
				continue; // Les vrais continuent toujours
			}

			double latSuivant = GestionXML
					.getDouble(n.getAttributeValue("lat"));
			double lonSuivant = GestionXML
					.getDouble(n.getAttributeValue("lon"));
			// System.out.println("Lat : "+lat);

			if (i == 0) {
				System.out.println("zero");

				String type = getTagValue(n, "environnement");
				String temps = getTagValue(n, "temps");
				String meteo = getTagValue(n, "meteo");
				System.out.println(type + temps + meteo);
				environnement = new Environnement(type, temps, meteo);
				String typeTerrain = getTagValue(n, "surface");
				// On veut de LA BOUEEEEEEE
				if (temps.equals("pluie") && typeTerrain.equals("terre"))
					typeTerrain = "boue";

				terrain = new Terrain(typeTerrain);
			}

			if (i > 0) {
				double distancePortion = Cartographie.distance(latCourant, lonCourant,
						latSuivant, lonSuivant);
				
				distance += distancePortion;

				if (i > 1) {
					
					double anglePortion = Cartographie.angleVirage(latPrecedent,
							lonPrecedent, latCourant, lonCourant, latSuivant,
							lonSuivant);
					
					TypeRoute typePortion = Cartographie.sensVirage(latPrecedent,
							lonPrecedent, latCourant, lonCourant, latSuivant,
							lonSuivant);

					//if (distancePortion < 50)
					// Maintenant que l'on a toutes les bonnes informations, création de la portion
					Portion portion = new Portion(distancePortion, typePortion, anglePortion);
					
					// Si des evenements lies
					if (n.getChildren() != null) {
						
						String parametre;
						
						if ((parametre = getTagValue(n, "environnement")) != null)
							portion.addEvenement(new Evenement(TypeEvenement.ENVIRONNEMENT, parametre));
						
						if ((parametre = getTagValue(n, "surface")) != null)
							portion.addEvenement(new Evenement(TypeEvenement.TERRAIN, parametre));
						
						if ((parametre = getTagValue(n, "son")) != null)
							portion.addEvenement(new Evenement(TypeEvenement.SON, parametre));
					}

					// On ajoute le virage
					portions.add(portion);
				}
			}

			latPrecedent = latCourant;
			lonPrecedent = lonCourant;

			latCourant = latSuivant;
			lonCourant = lonSuivant;

			++i;
		}
		
		Main.logInfo("Distance du circuit : "+distance);
	}

	public static String getTagValue(Element noeud, String tag) {
		for (Object t : noeud.getChildren("tag")) {
			Element tt = (Element) t;

			if (tt.getAttributeValue("k").equals(tag)) {
				return tt.getAttributeValue("v");
			}
		}

		return null;
	}

	public void changeTerrain(String terr) {
		terrain.change(terr);
	}

	public void changeEnvironnement(String envi) {
		environnement.change(envi);
	}

	public void start() {
		environnement.play();
		terrain.play();
	}

	public double getDistance() {
		return distance;
	}

	public String getNom() {
		return nom;
	}
	
	public void playCrash() {
		environnement.playCrash();
	}

	public void playFrottement() {
		terrain.playFrottement();
	}
	
	public void stopFrottement() {
		terrain.stopFrottement();
	}
	
	public void setDistance(double d) {
		environnement.setDistance(d);
	}

	public Portion nextPortion() {
		return portions.poll();
	}

	public void setVitesse(double vitesse) {
		terrain.setVitesse(vitesse);
		environnement.setVitesse(vitesse);
	}

	public void stop() {
		environnement.delete();
		terrain.delete();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Circuit [nom=");
		builder.append(nom);
		builder.append(", distance=");
		builder.append(distance);
		builder.append(", portions=");
		builder.append(portions);
		builder.append("]");
		return builder.toString();
	}
	

}
