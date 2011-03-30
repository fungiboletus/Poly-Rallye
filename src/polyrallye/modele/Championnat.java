package polyrallye.modele;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jdom.Element;


public class Championnat {
    
    protected Garage garage;
    protected Joueur player;
    protected String nom;
    protected Duree duree;
    protected List<Etape> etapes;
    protected Voiture voitureGagné;
    protected int argentGagné;
    
    public Championnat(Garage G, Joueur J, String nom, Duree duree, List<Etape> etapes, Voiture voitureGagné, int argentGagné)
    {
            this.garage = G;
            this.player = J;
            this.argentGagné = argentGagné;
            this.nom = nom;
            this.duree = duree;
            
            if (nom == null)
                throw new NullPointerException(
                        "Vous devez au moins specifier le nom de la spéciale !");
            
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
    
    public void setJoueur(Joueur j) {
        this.player = j;
    }

    public Joueur getJoueur() {
        return player;
    }
    
    public void setGarage(Garage g) {
        this.garage = g;
    }

    public Garage getGarage() {
        return garage;
    }
    
    public void RemisePrix() throws Exception {
        garage.ajouter(voitureGagné);
        player.ajouterArgent(argentGagné);    
    }
}