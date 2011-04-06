package polyrallye.modele.personnes;

import org.jdom.Element;

public class Personne {

	protected String nom;

	public String getNom() {
		return nom;
	}
	
	public Personne(String nom)
	{
		this.nom = nom;
	}
	
	public Personne(Element noeud)
        {
	    nom = noeud.getChildText("nom");
	}
}
