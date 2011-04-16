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
 * Classe Match : représente un etape, avec ses deux equipes, son arbitre et son
 * gagnant
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

    public Etape(Element noeud) {

        joueur = new Joueur(Joueur.session.getNom());

        circuit = noeud.getChildText("circuit");
        nom = noeud.getChildText("nom");

        for (Object e : noeud.getChildren("participant")) {
            Personne balise = new Personne((Element) e);
            participants.add(balise);
        }

        for (Object e : noeud.getChildren("voiture")) {
            Voiture balise = new Voiture((Element) e);
            voitures.add(balise);
        }

        for (Object e : noeud.getChildren("classement")) {
            Rang balise = new Rang((Element) e);
            classement.add(balise);
        }

        numeroEtape = GestionXML.getInt(noeud.getChildText("numero"));

    }

    public Element toXML() {

        Element noeud = new Element("etape");

        noeud.addContent(new Element("numero").setText("" + numeroEtape));
        noeud.addContent(new Element("nom").setText(nom));

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
     * @return
     */
    public void setClassement(List<Rang> classement) {
        this.classement = classement;
    }

    /**
     * affecte le classement de la spéciale
     * 
     * @return
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
     * affecte le classement de la spéciale
     * 
     * @return
     */
    public void setClassement(Duree dureeJoueurEtape, Voiture voitJoueur) {

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

        voitures.add(voitJoueur);
        voitures.addAll(StockVoitures.getVoituresEquivalentes(voitJoueur, 9));

        classement.add(new Rang(nom, participants.get(0), dureeJoueurEtape,
                voitures.get(0)));
        for (int i = 1; i < 10; ++i) {
            Adversaire adv = (Adversaire) participants.get(i);
            adv.setVoiture(voitures.get(i - 1));
            classement.add(new Rang(nom, participants.get(i), ramdomduree(adv,
                    voitures.get(i - 1), dureeJoueurEtape, Joueur.session,
                    voitJoueur), (voitures.get(i - 1))));
        }

        // réorganisation, trie de la liste classement
        Collections.sort(classement);

        // mise a jour des écarts
        setecart();

        for (int i = 0; i < 10; ++i) {
            classement.get(i).setClassement(i + 1);
            System.out.println(classement.get(i));
            if (i > 1
                    && classement.get(i).getDuree().equals(
                            classement.get(i - 1).getDuree()))
                classement.get(i).getDuree().setDixiemes(
                        classement.get(i).getDuree().getDixiemes() - 5);
        }

    }

    /**
     * 
     * 
     * @return
     */
    public void setecart() {
        int premier = classement.get(0).getDuree().ConvertToDixiemes();

        for (int i = 1; i < 10; ++i) {
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

    public static void EnregistrerEtape(Etape et) {
        try {
            File d = new File("Championnat");

            if (!d.exists()) {
                d.mkdir();
            }

            GestionXML.enregistrerRacine("Championnat/" + et.getEtape()
                    + ".xml", et.toXML());
        } catch (Exception e) {
            e.printStackTrace();
            Liseuse.lire("Impossible de sauvegarder la progression.");
        }
    }
}