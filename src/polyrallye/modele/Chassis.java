package polyrallye.modele;

import org.jdom.Element;

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
}
