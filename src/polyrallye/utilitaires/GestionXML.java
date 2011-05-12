package polyrallye.utilitaires;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * Classe de gestion XML.
 * 
 * @author Antoine Pultier
 */
public abstract class GestionXML {
	/**
	 * Demande polimentà jdom d'écrire le xml sous forme de texte lisible.
	 * 
	 * @param flux
	 *            Fichier dans lequel écrire le document.
	 * @param document
	 *            Document à écrire
	 * @throws Exception
	 *             Exception Problème lors de l'écriture
	 */
	public static void ecrireXML(OutputStream flux, Document document)
			throws Exception {
		// On affiche de façon à ce que ça soit lisible
		XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
		sortie.output(document, flux);
	}

	/**
	 * Création d'un document jdom à partir d'un flux.
	 * 
	 * @param flux
	 *            . Flux dans lequel il faut lire le document xml.
	 * @return Document xml construit à partir du flux.
	 * @throws Exception
	 *             Problème de lecture ou de construction du document.
	 */
	public static Document creerDocument(InputStream flux) throws Exception {
		return new SAXBuilder().build(flux);
	}

	/**
	 * Création d'un document jdom pour écrire dedans.
	 * 
	 * @param nom
	 *            Nom du document à créer.
	 * @return Nouveau document créé.
	 */
	public static Document creerDocument(String nom) {
		Document doc = new Document();
		doc.setRootElement(new Element(nom));
		return doc;
	}

	public static Document creerDocument(Element root) {
		Document doc = new Document();
		doc.setRootElement(root);
		return doc;
	}

	/**
	 * Récupère le noeud parent d'un document.
	 * 
	 * @param document
	 *            Document dans lequel il faut charger les noeuds xml.
	 * @return Noeud parent.
	 * @throws Exception
	 *             Le document est vide.
	 */
	public static Element chargerElementFlux(Document document)
			throws Exception {
		Element e = document.getRootElement();

		if (e == null) {
			throw new Exception("Il n'y a pas d'éléments");
		}

		return e;
	}

	/**
	 * Transforme un string en int
	 * 
	 * @param nombre
	 *            Chaîne de caractères représentant le nombre
	 * @return Le nombre
	 */
	public static int getInt(String nombre) {
		if (nombre == null)
			return 0;
		return Integer.parseInt(nombre.replace(" ", ""));
	}

	/**
	 * Transforme un string en double
	 * 
	 * @param nombre
	 *            Chaîne de caractères représentant le nombre
	 * @return Le nombre
	 */
	public static double getDouble(String nombre) {
		if (nombre == null)
			return 0.0;
		return Double.parseDouble(nombre.replace(" ", ""));
	}

	/**
	 * Charge le premier élément contenu dans un fichier
	 * 
	 * @param fichier
	 *            Le fichier à ouvrir
	 * @return La racine
	 * @throws Exception
	 */
	public static Element chargerNoeudRacine(File fichier) throws Exception {
		FileInputStream lecteurFichier = new FileInputStream(fichier);
		Element noeud = chargerElementFlux(creerDocument(lecteurFichier));
		lecteurFichier.close();

		return noeud;
	}

	/**
	 * Enregistre un arbre XML dans un fichier.
	 * 
	 * @param nomFichier
	 *            Le fichier dans lequel il faut écrire les données
	 * @param noeud
	 *            La racine des nœuds xml à enregistrer
	 * @throws Exception
	 */
	public static void enregistrerRacine(String nomFichier, Element noeud)
			throws Exception {
		Document doc = creerDocument(noeud);

		FileOutputStream f = new FileOutputStream(nomFichier);
		ecrireXML(f, doc);

		f.close();
	}
}
