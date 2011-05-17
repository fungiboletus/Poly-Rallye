package polyrallye.modele.championnat;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jdom.Element;

import polyrallye.modele.personnes.Joueur;
import polyrallye.modele.voiture.StockVoitures;
import polyrallye.modele.voiture.Voiture;
import polyrallye.ouie.liseuse.Liseuse;
import polyrallye.utilitaires.GestionXML;

public class Championnat {

    protected Joueur player;
    protected String nom;
    protected List<Etape> etapes;
    protected List<Rang> classement;
    protected Voiture voitureGagne;
    protected int argentGagne;

    public Championnat(Joueur J, String nom, List<Etape> etapes,
            Voiture voitureGagne, int argentGagne) {
        this.player = J;
        this.argentGagne = argentGagne;
        this.voitureGagne = voitureGagne;
        this.nom = nom;

        if (nom == null)
            throw new NullPointerException(
                    "Vous devez au moins specifier le nom du championnat !");

        // si la liste de speciales est null : en creer une
        if (etapes == null)
            this.etapes = new ArrayList<Etape>();
        else
            this.etapes = etapes;
    }

    public Championnat(Element noeud) {

        player = new Joueur(noeud.getChildText("joueur"));

        nom = noeud.getChildText("nom");

        etapes = new ArrayList<Etape>();
        for (Object e : noeud.getChildren("etape")) {
            Etape balise = new Etape((Element) e);
            etapes.add(balise);
        }

        voitureGagne = StockVoitures.getVoitureParNom((noeud
                .getChildText("voitureEnJeu")));

        argentGagne = GestionXML.getInt(noeud.getChildText("argentEnJeu"));

    }

    public Element toXML() {

        Element noeud = new Element("Championnat");

        noeud.addContent(new Element("nom").setText(nom));
        noeud.addContent(new Element("joueur").setText(player.getNom()));

        for (int i = 0; i < etapes.size(); ++i)
            noeud.addContent(etapes.get(i).toXML());

        noeud.addContent(new Element("voitureEnJeu").setText(voitureGagne
                .getNomComplet()));

        noeud.addContent(new Element("argentEnJeu").setText("" + argentGagne));

        return noeud;
    }

    public List<Etape> getEtapes() {
        return etapes;
    }

    public void setEtapes(List<Etape> etapes) {
        this.etapes = etapes;
    }

    public Voiture getVoitureGagne() {
        return voitureGagne;
    }

    public void setVoitureGagne(Voiture voitureGagne) {
        this.voitureGagne = voitureGagne;
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
        player.getGarage().ajouter(voitureGagne);
        player.ajouterArgent(argentGagne);
    }

    public void setClassement() {
        classement = new ArrayList<Rang>();

        classement = etapes.get(0).getClassement();

        for (int i = 0; i < 10; ++i) {
            classement.get(i).setSpeciale(nom);
            classement.get(i).setCar(null);
        }

        for (int k = 1; k < etapes.size(); ++k) {
            for (int i = 0; i < 10 && k >= 1; ++i) {
                for (int j = 0; j < 10; ++j)
                    if (classement.get(i).getPersonne().getNom()
                            .equalsIgnoreCase(
                                    etapes.get(k).getClassement().get(j)
                                            .getPersonne().getNom()))
                        classement.get(i)
                                .setDuree(
                                        new Duree(classement.get(i).getDuree()
                                                .ConvertToDixiemes()
                                                + etapes.get(k).getClassement()
                                                        .get(j).getDuree()
                                                        .ConvertToDixiemes()));
            }
        }

        // reorganisation, trie de la liste classement
        Collections.sort(classement);

        // mise a jour des ecarts
        setecart();

        for (int i = 0; i < 10; ++i) {
            System.out.println(classement.get(i));
        }

    }

    /**
     * 
     * 
     * @return
     */
    public void setecart() {
        int premier = classement.get(0).getDuree().ConvertToDixiemes();
        classement.get(0).setClassement(1);
        for (int i = 1; i < 10; ++i) {
            classement.get(i).setClassement(i + 1);
            classement.get(i).setEcart(
                    new Duree(classement.get(i).getDuree().ConvertToDixiemes()
                            - premier));
        }
    }

    public Joueur getPlayer() {
        return player;
    }

    public void setPlayer(Joueur player) {
        this.player = player;
    }

    public List<Rang> getClassement() {
        return classement;
    }

    public void setClassement(List<Rang> classement) {
        this.classement = classement;
    }

    public int getArgentGagne() {
        return argentGagne;
    }

    public void setArgentGagne(int argentGagne) {
        this.argentGagne = argentGagne;
    }

    public static Championnat chargerChampionnat(String nom) {
        File f = new File("Championnats/" + nom + ".xml");

        if (f.exists()) {
            Element n;
            try {
                n = GestionXML.chargerNoeudRacine(f);
                return new Championnat(n);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalAccessError();
        }
        return null;
    }

    public static void EnregistrerChampionnat(Championnat c) {
        try {
            File d = new File("Championnat");

            if (!d.exists()) {
                d.mkdir();
            }

            GestionXML.enregistrerRacine("Championnat/" + c.getNom() + ".xml",
                    c.toXML());
        } catch (Exception e) {
            e.printStackTrace();
            Liseuse.lire("Impossible de sauvegarder la progression.");
        }
    }
}