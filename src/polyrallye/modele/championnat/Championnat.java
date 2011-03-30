package polyrallye.modele.championnat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jdom.Element;

import polyrallye.modele.circuit.Etape;
import polyrallye.modele.voiture.Voiture;


public class Championnat {
    
    
    protected String nom;
    private Duree duree;
    protected List<Etape> etapes;
    protected Voiture voitureGagné;
    
    public Championnat(String nom,Duree duree, List<Etape> etapes, Voiture voitureGagné)
    {
            this.nom = nom;
            this.duree = duree;
            
            if (nom == null)
                throw new NullPointerException(
                        "Vous devez au moins specifier le nom de l epreuve !");
            
         // si la liste de speciales est null : en créer une
            if (etapes == null)
                this.etapes = new ArrayList<Etape>();
            else
                this.etapes = etapes;
            
    }
    
    public Championnat(Element noeud)
    {
            noeud.getChildText("nom");
            
            
    }
    
    public Duree getDuree() {
        return duree;
    }

    public void setDuree(Duree duree) {
        this.duree = duree;
    }

    public List<Etape> getEtapes() {
        return etapes;
    }

    public void setEtapes(List<Etape> etapes) {
        this.etapes = etapes;
    }

    public Voiture getVoitureGagné() {
        return voitureGagné;
    }

    public void setVoitureGagné(Voiture voitureGagné) {
        this.voitureGagné = voitureGagné;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }
}