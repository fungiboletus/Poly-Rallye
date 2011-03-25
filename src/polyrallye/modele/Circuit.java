package polyrallye.modele;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import org.jdom.Element;

import polyrallye.ouie.environnement.Environnement;
import polyrallye.ouie.environnement.Evenement;
import polyrallye.ouie.environnement.Terrain;

public class Circuit {
	protected String nom;
	protected Environnement environnement;
	protected Queue<ContenuCircuit> contenu;
	protected Terrain terrain;
	
	protected float distance;
	protected float longueur;

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
			distance=0;

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

				} else if (element.getName().equals("fin")) {
					
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
	
	public float getLongueur() {
		return longueur;
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

	public static void main(String[] args) {
		Circuit test = new Circuit("Circuit_1");
		test.start();
		Scanner sc = new Scanner(System.in);
		while (!sc.next().equals("e"))
			;
		test.changeEnvironnement("mer");
		while (!sc.next().equals("e"))
			;
	}
}
