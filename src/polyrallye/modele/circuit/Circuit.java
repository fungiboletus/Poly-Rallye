package polyrallye.modele.circuit;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

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

	public Circuit(String file) {
		try {
			Element racine = GestionXML.chargerNoeudRacine(new File("Circuits/"
					+ file + ".xml"));
			nom = racine.getChildText("nom");
			terrain = new Terrain(racine.getChildText("terrain"));
			String type = racine.getChildText("environnement");
			String temps = racine.getChildText("temps");
			String meteo = racine.getChildText("meteo");
			environnement = new Environnement(type, temps, meteo);

			contenu = new LinkedList<ContenuCircuit>();
			Element parcours = racine.getChild("contenu");

			for (Iterator iterator = parcours.getChildren().iterator(); iterator
					.hasNext();) {
				Element element = (Element) iterator.next();
				if (element.getName().equals("gauche")
						|| element.getName().equals("droite")) {
					contenu.add(new Route(Long.valueOf(element
							.getAttributeValue("distance")), Long
							.valueOf(element.getAttributeValue("longueur")),
							TypeRoute.valueOf(element.getName()),
							Long.valueOf(element.getAttributeValue("force"))));

				} else {

					contenu.add(new Evenement(element.getName(), Long
							.valueOf(element.getAttributeValue("distance")),
							Long.valueOf(element.getAttributeValue("longueur")),element.getAttributeValue("type"), this));
				}

			}

		} catch (Exception e) {
			System.out.println("Erreur chargement xml");
		}

	}
	
	public Circuit(Element noeud) {
		if (noeud.getChildren("way").size() != 1) {
			System.err.println("Le fichier OSM ne contient pas un seul chemin.");
		}
		
		Element chemin = noeud.getChild("way");
		
		nom = getTagValue(chemin, "nom");
		//terrain = getTagValue(chemin, "terrain");
		String type = getTagValue(chemin, "environnement");
		String temps = getTagValue(chemin, "temps");
		String meteo = getTagValue(chemin, "meteo");
		
		//environnement = new Environnement(type, temps, meteo);
		
		
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
			
			double latSuivant = GestionXML.getDouble(n.getAttributeValue("lat"));
			double lonSuivant = GestionXML.getDouble(n.getAttributeValue("lon"));
			//System.out.println("Lat : "+lat);
			
			if (i > 0)
			{
				double d = Cartographie.distance(latCourant, lonCourant, latSuivant, lonSuivant);
			
				System.out.println("Distance : "+d);
			
				distance += d;
			
				if (i > 2) {
					
					double a = Cartographie.angleVirage(latPrecedent,
							lonPrecedent, latCourant, lonCourant, latSuivant,
							lonSuivant);
					System.out.println("Angle : " + a);

					TypeRoute s = Cartographie.sensVirage(latPrecedent,
							lonPrecedent, latCourant, lonCourant, latSuivant,
							lonSuivant);
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
		
		System.out.println("Distance totale : "+distance);
	}

	public static String getTagValue(Element noeud, String tag) {
		for (Object t : noeud.getChildren(tag)) {
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
	}

	public static void main(String[] args) throws Exception {
		/*Circuit test = new Circuit("Circuit_1");
		test.start();
		Scanner sc = new Scanner(System.in);
		while (!sc.next().equals("e"))
			;
		test.changeEnvironnement("mer");
		while (!sc.next().equals("e"))
			;*/
		Element noeud = GestionXML.chargerNoeudRacine(new File("Circuits/CoteReserveCalvi.osm"));
		
		new Circuit(noeud);
	}
}
