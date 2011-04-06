package polyrallye.modele;

import org.jdom.Element;

import polyrallye.utilitaires.GestionXML;

public class Chassis
{
	protected int poids;
	
	protected int largeur;
	
	protected int longueur;
	
	protected int empattement;
	
	public Chassis(){
		
	}
	
	public Chassis(Element noeud)
	{
		poids = GestionXML.getInt(noeud.getChildText("poids"));
		largeur = GestionXML.getInt(noeud.getChildText("largeur"));
		longueur = GestionXML.getInt(noeud.getChildText("longueur"));
		empattement = GestionXML.getInt(noeud.getChildText("empattement"));
	}

	public int getPoids() {
		return poids;
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
}
