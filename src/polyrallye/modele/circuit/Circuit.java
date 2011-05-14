package polyrallye.modele.circuit;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import org.jdom.Element;

import polyrallye.ouie.environnement.Environnement;
import polyrallye.ouie.environnement.Evenement;
import polyrallye.ouie.environnement.Terrain;
import polyrallye.utilitaires.Cartographie;
import polyrallye.utilitaires.GestionXML;

public class Circuit {
	protected String nom;
	protected Environnement environnement;
	protected Queue<ContenuCircuit> contenu;
	protected Terrain terrain;



	protected double distance;

	// public Circuit(String file) {
	// try {
	// Element racine = GestionXML.chargerNoeudRacine(new File("Circuits/"
	// + file + ".xml"));
	// nom = racine.getChildText("nom");
	// terrain = new Terrain(racine.getChildText("terrain"));
	// String type = racine.getChildText("environnement");
	// String temps = racine.getChildText("temps");
	// String meteo = racine.getChildText("meteo");
	// environnement = new Environnement(type, temps, meteo);
	// distance=0;
	//
	// contenu = new LinkedList<ContenuCircuit>();
	// Element parcours = racine.getChild("contenu");
	//
	// for (Iterator iterator = parcours.getChildren().iterator(); iterator
	// .hasNext();) {
	// Element element = (Element) iterator.next();
	// if (element.getName().equals("gauche")
	// || element.getName().equals("droite")) {
	// contenu.add(new Route(Double.valueOf(element
	// .getAttributeValue("distance")), Long
	// .valueOf(element.getAttributeValue("longueur")),
	// TypeRoute.valueOf(element.getName()),
	// Double.valueOf(element.getAttributeValue("force"))));
	//
	// } else if (element.getName().equals("fin")) {
	// distance = Double.valueOf(element.getAttributeValue("distance"));
	// } else {
	//
	// contenu.add(new Evenement(element.getName(), Double
	// .valueOf(element.getAttributeValue("distance")),
	// Double.valueOf(element.getAttributeValue("longueur")),element.getAttributeValue("type"),
	// this));
	// }
	//
	// }
	//
	// } catch (Exception e) {
	// System.out.println("Erreur chargement xml");
	// }
	//
	// }

	public Circuit(Element noeud) {
		
		contenu = new LinkedList<ContenuCircuit>();
		
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
				double d = Cartographie.distance(latCourant, lonCourant,
						latSuivant, lonSuivant);

				System.out.println("Distance : " + d);

				distance += d;

				if (i > 1) {

					double a = Cartographie.angleVirage(latPrecedent,
							lonPrecedent, latCourant, lonCourant, latSuivant,
							lonSuivant);
					System.out.println("Angle : " + a);

					TypeRoute s = Cartographie.sensVirage(latPrecedent,
							lonPrecedent, latCourant, lonCourant, latSuivant,
							lonSuivant);

					// On ajoute le virage
					contenu.add(new Route(d, s, a));
					// Si des evenements lies
					if (n.getChildren() != null) {
						String param;
						if ((param = getTagValue(n, "environnement")) != null)
							contenu.add(new Evenement("environnement",
									distance, param, this));
						if ((param = getTagValue(n, "surface")) != null)
							contenu.add(new Evenement("surface", distance,
									param, this));
						if ((param = getTagValue(n, "son")) != null)
							contenu.add(new Evenement("son", distance, param,
									this));
					}

					System.out.println("Sens : "
							+ (s == TypeRoute.GAUCHE ? "gauche" : "droite"));

				}
			}

			latPrecedent = latCourant;
			lonPrecedent = lonCourant;

			latCourant = latSuivant;
			lonCourant = lonSuivant;

			++i;

			System.out.println();
		}

		System.out.println("Distance totale : " + distance);
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
		ContenuCircuit temp;
		if (contenu.element().getDistance() <= d) {
			temp = contenu.poll();
			while (temp != null && !temp.getType().equals("virage")) {
				Evenement temp2 = (Evenement) temp;
				temp2.exec();
				temp = contenu.poll();
			}
		}

	}

	public Route nextVirage() {
		return (Route) contenu.element();
	}

	public void setVitesse(double vitesse) {
		terrain.setVitesse(vitesse);
	}

	public void stop() {
		environnement.delete();
		terrain.delete();
	}

	public static void main(String[] args) throws Exception {
		/*
		 * Circuit test = new Circuit("Circuit_1"); test.start(); Scanner sc =
		 * new Scanner(System.in); while (!sc.next().equals("e")) ;
		 * test.changeEnvironnement("mer"); while (!sc.next().equals("e")) ;
		 */

		Element noeud = GestionXML.chargerNoeudRacine(new File(
				"Circuits/Calenzana.osm"));

		Circuit temp = new Circuit(noeud);
		temp.start();
	}

}
