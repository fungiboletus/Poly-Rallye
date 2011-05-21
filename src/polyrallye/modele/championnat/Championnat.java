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

/**
 * Classe Championnat : représente un championnat
 * 
 * @author ochi
 * 
 */

public class Championnat {

    protected Joueur player;
    protected String nom;
    protected List<Etape> etapes;
    protected List<Rang> classement;
    protected Voiture voitureGagne;
    protected int argentGagne;

    /**
     * Création d'un championnat
     * 
     * @param J
     *            joueur
     * @param nom
     *            nom de l'étape
     * @param etapes
     *            Listes d'étapes
     * @param voitureGagne
     *            voitureGagné
     * @param argentGagne
     *            argentGagné
     */
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
        else
            this.etapes = etapes;
    }

    /**
     * Création d'un championnat à partir d'un noeud
     * 
     * @param noeud
     */
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

    /**
     * Effectue l'insertion XML du championnat
     * 
     * @param noeud
     */
    public Element toXML() {

        Element noeud = new Element("Championnat");

        noeud.addContent(new Element("nom").setText(nom));
        noeud
                .addContent(new Element("joueur").setText(Joueur.session
                        .getNom()));

        for (int i = 0; i < etapes.size(); ++i)
            noeud.addContent(etapes.get(i).toXML());

        noeud.addContent(new Element("voitureEnJeu").setText(voitureGagne
                .getNomComplet()));

        noeud.addContent(new Element("argentEnJeu").setText("" + argentGagne));

        return noeud;
    }

    /**
     * Getter
     * 
     * @return List<Etape>
     */
    public List<Etape> getEtapes() {
        return etapes;
    }

    /**
     * Setter
     * 
     * @param etapes
     */
    public void setEtapes(List<Etape> etapes) {
        this.etapes = etapes;
    }

    /**
     * Retourne la voiture à gagner
     * 
     * @return Voiture
     */
    public Voiture getVoitureGagne() {
        return voitureGagne;
    }

    /**
     * Affecte la voiture gagné
     * 
     * @param voitureGagne
     */
    public void setVoitureGagne(Voiture voitureGagne) {
        this.voitureGagne = voitureGagne;
    }

    /**
     * Setter
     * 
     * @param nom
     */
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

    /**
     * Remise du Prix. On affecte la voiture et l'argent gagné
     * 
     * @throws Exception
     */
    public void RemisePrix() throws Exception {
        player.getGarage().ajouter(voitureGagne);
        player.ajouterArgent(argentGagne);
    }

    /**
     * Effectue le classement. Effectue la somme de toutes les étapes.
     */
    public void setClassement() {
        classement = etapes.get(0).getClassement();

        for (int i = 0; i < classement.size(); ++i) {
            classement.get(i).setSpeciale(nom);
            classement.get(i).setCar(null);
        }

        for (int k = 1; k < etapes.size(); ++k) {
            for (int i = 0; i < classement.size() && k >= 1; ++i) {
                for (int j = 0; j < classement.size(); ++j)
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

        for (int i = 0; i < classement.size(); ++i) {
            System.out.println(classement.get(i));
        }

    }

    /**
     * Acualise l'écart
     * 
     * 
     */
    public void setecart() {
        int premier = classement.get(0).getDuree().ConvertToDixiemes();
        classement.get(0).setClassement(1);
        for (int i = 1; i < classement.size(); ++i) {
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

    /**
     * Chargement du championnat
     * 
     * @param nom
     * @return Championnat
     */
    public static Championnat chargerChampionnat(String nom) {
        File f = new File("ressources/Championnats/" + nom + ".xml");
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

    /**
     * Enregistrement du championnat
     * 
     * @param c
     */
    public static void EnregistrerChampionnat(Championnat c) {
        try {
            File d = new File("ressources/Championnats");

            if (!d.exists()) {
                d.mkdir();
            }

            GestionXML.enregistrerRacine("ressources/Championnats/" + c.getNom() + ".xml", c.toXML());
        } catch (Exception e) {
            e.printStackTrace();
            Liseuse.lire("Impossible de sauvegarder la progression.");
        }
    }
}