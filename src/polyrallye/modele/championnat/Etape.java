package polyrallye.modele.championnat;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jdom.Element;

import polyrallye.modele.personnes.Adversaire;
import polyrallye.modele.personnes.Joueur;
import polyrallye.modele.personnes.Personne;
import polyrallye.modele.voiture.StockVoitures;
import polyrallye.modele.voiture.Voiture;
import polyrallye.ouie.liseuse.Liseuse;
import polyrallye.utilitaires.GestionXML;

/**
 * Classe Etape : représente une etape
 * 
 * @author zizou
 * 
 */
public class Etape {

    protected int numeroEtape;
    protected Joueur joueur = Joueur.session;
    protected String nom;
    protected String circuit;
    protected List<Personne> participants;
    protected List<Rang> classement;
    protected List<Voiture> voitures;

    /**
     * Constructeur
     * 
     * @param numero
     * @param uneEpreuve
     * @param participants
     * @param cir
     */
    public Etape(int numero, String uneEpreuve, List<Personne> participants,
            String cir) {

        if (uneEpreuve == null || numero <= 0)
            throw new NullPointerException(
                    "Un ou des parametres du constructeur de Etape est null");

        nom = uneEpreuve;
        numeroEtape = numero;

        // si la liste de participant est null : en créer une
        if (participants == null)
            participants = new ArrayList<Personne>();
        else
            this.setParticipants(participants);

        classement = new ArrayList<Rang>();

        voitures = new ArrayList<Voiture>();

        circuit = cir;
    }

    /**
     * Constructeur à partir d'un élément du fichier XML.
     * 
     * @param noeud
     */
    public Etape(Element noeud) {

        // joueur = new Joueur(noeud.getChildText("joueur"));

        circuit = noeud.getChildText("circuit");
        nom = noeud.getChildText("nom");

        voitures = new ArrayList<Voiture>();
        classement = new ArrayList<Rang>();
        for (Object e : noeud.getChildren("classement")) {
            Rang balise = new Rang((Element) e);
            if (balise.getEcart() == null)
                balise.setEcart(new Duree(0));
            balise.setSpeciale(nom);
            voitures.add(StockVoitures.getVoitureParNom(balise.getCar()));
            classement.add(balise);
        }

        participants = new ArrayList<Personne>();
        for (int i = 0; i < classement.size(); ++i) {
            participants.add(classement.get(i).getPersonne());
        }

        numeroEtape = GestionXML.getInt(noeud.getChildText("numero"));

    }

    public int getNumeroEtape() {
        return numeroEtape;
    }

    public void setNumeroEtape(int numeroEtape) {
        this.numeroEtape = numeroEtape;
    }

    public Joueur getJoueur() {
        return joueur;
    }

    public void setJoueur(Joueur joueur) {
        this.joueur = joueur;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCircuit() {
        return circuit;
    }

    public void setCircuit(String circuit) {
        this.circuit = circuit;
    }

    public List<Voiture> getVoitures() {
        return voitures;
    }

    public void setVoitures(List<Voiture> voitures) {
        this.voitures = voitures;
    }

    /**
     * Insertion XML
     * 
     * @return Element
     */
    public Element toXML() {

        Element noeud = new Element("etape");

        noeud.addContent(new Element("numero").setText("" + numeroEtape));
        noeud.addContent(new Element("nom").setText(nom+"_"+Joueur.session.getNom()));

        noeud.addContent(joueur.toXML());

        noeud.addContent(new Element("circuit").setText(circuit));

        // for (int i = 0; i < participants.size() - 1; ++i)
        // noeud.addContent(new Element("participant").setText(participants
        // .get(i).getNom()));

        setecart();

        noeud.addContent(classement.get(0).toXML());

        for (int i = 1; i < classement.size(); ++i) {
            noeud.addContent(classement.get(i).toXML());
        }

        return noeud;
    }

    /**
     * retourne la peciale concernée par le speciale
     * 
     * @return
     */
    public String getEtape() {
        return nom;
    }

    /**
     * retourne le résultat de la spéciale
     * 
     * @return
     */
    public List<Rang> getClassement() {
        return classement;
    }

    /**
     * affecte le classement de la spéciale
     * 
     * @param classement
     */
    public void setClassement(List<Rang> classement) {
        this.classement = classement;
    }

    /**
     * Duree aléatoire
     * 
     * @param adv
     * @param voiture
     * @param dureeIdeale
     * @param J
     * @param voitJoueur
     * @return Duree
     */
    public Duree ramdomduree(Adversaire adv, Voiture voiture,
            Duree dureeIdeale, Joueur J, Voiture voitJoueur) {

        double scorevoitadv = voiture.getScore();

        double scorevoitjoueur = voitJoueur.getScore();

        double rapport = scorevoitjoueur / scorevoitadv;

        int dix = dureeIdeale.ConvertToDixiemes();

        int approx = (int) (dix * rapport);

        // approx += t2s.util.Random.unsignedDelta((20 * ((int)(1.5*rapport))),
        // 20 * ((int)(1.5*rapport)));

        approx += (int) (Math.random() * (100 - (-100))) - 100;

        System.out.println(adv.getNom() + " ---- " + voiture);

        return new Duree(approx);

    }

    /**
     * affecte le classement de la spéciale (de façon aléatoire)
     * 
     * @param dureeJoueurEtape
     * @param voitJoueur
     */
    public void setClassement(Duree dureeJoueurEtape, Voiture voitJoueur) {
        classement = new ArrayList<Rang>();
        participants = new ArrayList<Personne>();
        participants.add(Joueur.session);
        participants.add(new Adversaire("Chang"));
        participants.add(new Adversaire("Dupont"));
        participants.add(new Adversaire("Esposa"));
        participants.add(new Adversaire("Munoz"));
        participants.add(new Adversaire("Trapatoni"));
        participants.add(new Adversaire("Paolista"));
        participants.add(new Adversaire("Salem"));
        participants.add(new Adversaire("Zicko"));
        participants.add(new Adversaire("Dialo"));

        voitures = new ArrayList<Voiture>();
        voitures.add(voitJoueur);
        voitures.addAll(StockVoitures.getVoituresEquivalentes(voitJoueur, 9));

        classement.add(new Rang(nom, participants.get(0), dureeJoueurEtape,
                voitures.get(0).getNomComplet()));
        for (int i = 1; i < 10; ++i) {
            Adversaire adv = (Adversaire) participants.get(i);
            adv.setVoiture(voitures.get(i - 1));
            classement.add(new Rang(nom, participants.get(i), ramdomduree(adv,
                    voitures.get(i - 1), dureeJoueurEtape, Joueur.session,
                    voitJoueur), (voitures.get(i - 1)).getNomComplet()));
        }

        // réorganisation, trie de la liste classement
        Collections.sort(classement);

        // mise a jour des écarts
        setecart();

        for (int i = 0; i < classement.size(); ++i) {
            classement.get(i).setClassement(i + 1);
            // System.out.println(classement.get(i));
            if (i > 1
                    && classement.get(i).getDuree().equals(
                            classement.get(i - 1).getDuree()))
                classement.get(i).getDuree().setDixiemes(
                        classement.get(i).getDuree().getDixiemes() - 5);
        }
    }

    /**
     * 
     * Actualise l'écart
     */
    public void setecart() {
        int premier = classement.get(0).getDuree().ConvertToDixiemes();

        for (int i = 1; i < classement.size() ; ++i) {
            classement.get(i).setEcart(
                    new Duree(classement.get(i).getDuree().ConvertToDixiemes()
                            - premier));
        }

    }

    /**
     * retourne le numéro de la spéciale
     * 
     * @return
     */
    public int getNumero() {
        return numeroEtape;
    }

    /**
     * affecte les joueurs de la spéciale
     * 
     * @return
     */
    public void setParticipants(List<Personne> participants) {
        this.participants = participants;
    }

    /**
     * 
     * @return joueurs de la spéciale
     */
    public List<Personne> getParticipants() {
        return participants;
    }

    /**
     * Chargement de l'Etape
     * 
     * @param nom
     * @return Etape
     */
    public static Etape chargerEtape(String nom) {
        
        File f = new File("Championnats/" + nom +".xml");

        if (f.exists()) {
            Element n;
            try {
                n = GestionXML.chargerNoeudRacine(f);
                return new Etape(n);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalAccessError();
        }
        return null;
    }

    /**
     * Enregistement de l'étape
     * 
     * @param et
     */
    public static void EnregistrerEtape(Etape et) {
        try {
            File d = new File("Championnats");

            if (!d.exists()) {
                d.mkdir();
            }

            GestionXML.enregistrerRacine("Championnats/" + et.getEtape() + ".xml", et.toXML());
        } catch (Exception e) {
            e.printStackTrace();
            Liseuse.lire("Impossible de sauvegarder la progression.");
        }
    }

    
    /**
     * Vide le classement. Ce sont les durées qui sont mise à zéro.
     * 
     */
    public void viderClassement() {
        for (int i = 0; i < classement.size(); ++i) {
            classement.get(i).setDuree(new Duree(0));
        }
    }
}