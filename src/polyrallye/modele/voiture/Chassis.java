package polyrallye.modele.voiture;

import org.jdom.Element;

import polyrallye.ouie.liseuse.Liseuse;
import polyrallye.utilitaires.GestionXML;

public class Chassis
{
	protected int masse;
	
	protected int largeur;
	
	protected int longueur;
	
	protected int empattement;
	
	public Chassis(){
		
	}
	
	public Chassis(Element noeud)
	{
		masse = GestionXML.getInt(noeud.getChildText("poids"));
		largeur = GestionXML.getInt(noeud.getChildText("largeur"));
		longueur = GestionXML.getInt(noeud.getChildText("longueur"));
		empattement = GestionXML.getInt(noeud.getChildText("empattement"));
	}

	public int getMasse() {
		return masse;
	}

	public int getLargeur() {
		return largeur;
	}

	public int getLongueur() {
		return longueur;
	}

	public int getEmpattement() {
		return empattement;
	}
	
	public void lireSpecifications() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(masse);
		sb.append(" kilos pour ");
		sb.append((int)Math.round(largeur/1000.0));
		sb.append(" mètres de large sur ");
		sb.append((int)Math.round(longueur/1000.0));
		sb.append(" mètres de lon"); // pour rappel, les fautes sont volontaires
		
		Liseuse.lire(sb.toString());
	}
}
