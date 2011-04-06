package polyrallye.modele.championnat;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

import polyrallye.modele.personnes.Joueur;
import polyrallye.modele.voiture.Voiture;
import polyrallye.utilitaires.GestionXML;

public class Championnat {

    protected Joueur player;
    protected String nom;
    protected List<Etape> etapes;
    protected Voiture voitureGagné;
    protected int argentGagné;

    public Championnat(Joueur J, String nom, Duree duree, List<Etape> etapes,
            Voiture voitureGagné, int argentGagné) {
        this.player = J;
        this.argentGagné = argentGagné;
        this.nom = nom;

        if (nom == null)
            throw new NullPointerException(
                    "Vous devez au moins specifier le nom du championnat !");

        // si la liste de speciales est null : en créer une
        if (etapes == null)
            this.etapes = new ArrayList<Etape>();
        else
            this.etapes = etapes;

    }

    public Championnat(Element noeud) {

        player = new Joueur(noeud.getChildText("joueur"));

        nom = noeud.getChildText("nom");

        for (Object e : noeud.getChildren("etapes")) {
            Etape balise = new Etape((Element) e);
            etapes.add(balise);
        }

        voitureGagné = new Voiture(noeud.getChild("voitureEnJeu"));

        argentGagné = GestionXML.getInt(noeud.getChildText("argentEnJeu"));

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

    public void RemisePrix() throws Exception {
        player.getGarage().ajouter(voitureGagné);
        player.ajouterArgent(argentGagné);
    }
}