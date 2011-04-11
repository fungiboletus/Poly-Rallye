package polyrallye.modele.championnat;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

public class PolyTour {

    protected String nom;
    protected List<Championnat> Championnats;

    public PolyTour(String nom, Duree duree, List<Championnat> Championnats) {
        this.nom = nom;

        if (nom == null)
            throw new NullPointerException(
                    "Vous devez au moins specifier le nom !");

        // si la liste de speciales est null : en créer une
        if (Championnats == null)
            this.Championnats = new ArrayList<Championnat>();
        else
            this.Championnats = Championnats;

    }

    public PolyTour(Element noeud) {
        nom = noeud.getChildText("nom");

        for (Object e : noeud.getChildren("championnat")) {
            Championnat balise = new Championnat((Element) e);
            Championnats.add(balise);
        }

    }
    
    public Element toXML() {
        
        Element noeud = new Element("PolyTour");

        noeud.addContent(new Element("nom").setText(nom));
        
        for (int i = 0; i < Championnats.size() - 1; ++i)
            noeud.addContent(Championnats.get(i).toXML());
        
        return noeud;
    }

    public List<Championnat> getChampionnats() {
        return Championnats;
    }

    public void setChampionnats(List<Championnat> Championnats) {
        this.Championnats = Championnats;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

}