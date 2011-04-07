package polyrallye.modele.championnat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jdom.Element;

import polyrallye.modele.circuit.Circuit;
import polyrallye.modele.personnes.Adversaire;
import polyrallye.modele.personnes.Joueur;
import polyrallye.modele.personnes.Personne;
import polyrallye.modele.voiture.StockVoitures;
import polyrallye.modele.voiture.Voiture;
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
    protected String nom;
    protected Joueur joueur;
    protected Circuit circuit;
    protected List<Personne> participants;
    protected List<Rang> classement;
    protected List<Voiture> voitures;

    /**
     * Constructeur
     * 
     */
    public Etape(int numero, String uneEpreuve, List<Personne> participants) {

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

    }

    public Etape(Element noeud) {

        joueur = new Joueur(noeud.getChildText("joueur"));

        circuit = new Circuit(noeud.getChild("circuit"));
        nom = noeud.getChildText("nom");

        for (Object e : noeud.getChildren("participants")) {
            Personne balise = new Personne((Element) e);
            participants.add(balise);
        }

        for (Object e : noeud.getChildren("voitures")) {
            Voiture balise = new Voiture((Element) e);
            voitures.add(balise);
        }

        numeroEtape = GestionXML.getInt(noeud.getChildText("numero"));

    }
    
    public Element toXML() {
        
        Element noeud = new Element("Etape");

        noeud.addContent(new Element("numero").setText(""+ numeroEtape));
        noeud.addContent(new Element("nom").setText(nom));
        
        noeud.addContent(joueur.toXML());
        
        noeud.addContent(new Element("circuit").setText(circuit.getNom()));
        
        for(int i=0;i<participants.size()-1;++i)
            noeud.addContent(new Element("participant").setText(participants.get(i).getNom()));
        
        for(int i=0;i<classement.size()-1;++i)
            noeud.addContent(classement.get(i).toXML());
            
        for(int i=0;i<voitures.size()-1;++i)
            noeud.addContent(voitures.get(i).getNomComplet());

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
    public Duree ramdomduree(Personne pers, Duree dureeIdeale, Joueur J,
            Voiture voitJoueur) {
        Adversaire adv = (Adversaire) pers;
        double scorevoitadv = adv.getVoiture().getScore();
        double scorevoitjoueur = voitJoueur.getScore();

        double rapport = scorevoitjoueur / scorevoitadv;

        int sec = dureeIdeale.ConvertToSeconds();

        int approx = (int) (sec * rapport);
        Duree approxDuree = new Duree(approx);
        int fin = t2s.util.Random.delta(-(5 * approxDuree.getMinutes() + 1),
                5 * approxDuree.getMinutes() + 1);

        return new Duree(fin);

    }

    /**
     * affecte le classement de la spéciale
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public void setClassement(Duree dureeJoueurEtape, Voiture voitJoueur) {

        participants.add(joueur);
        participants.add(new Adversaire("Jean"));
        participants.add(new Adversaire("Dupont"));
        participants.add(new Adversaire("Esposa"));
        participants.add(new Adversaire("Munoz"));
        participants.add(new Adversaire("Trapatoni"));
        participants.add(new Adversaire("Paolista"));
        participants.add(new Adversaire("Barbosi"));
        participants.add(new Adversaire("Zicko"));
        participants.add(new Adversaire("Papisto"));

        classement.add(new Rang(nom, participants.get(0), dureeJoueurEtape));
        for (int i = 1; i < 10; ++i) {
            classement
                    .add(new Rang(nom, participants.get(i), ramdomduree(
                            participants.get(i), dureeJoueurEtape, joueur,
                            voitJoueur)));
        }

        voitures.add(StockVoitures
                .getVoitureParNom("Bugatti Veyron 16.4 Super Sport"));
        voitures.add(StockVoitures
                .getVoitureParNom("Audi Quattro Sport S1 Pikes Peak"));
        voitures.add(StockVoitures.getVoitureParNom("Audi Quattro Sport S1"));
        voitures.add(StockVoitures.getVoitureParNom("Citroën DS3 WRC"));
        voitures.add(StockVoitures.getVoitureParNom("Peugeot 307 WRC"));
        voitures.add(StockVoitures.getVoitureParNom("Peugeot 306 S16"));
        voitures.add(StockVoitures.getVoitureParNom("Peugeot 206 WRC"));
        voitures.add(StockVoitures.getVoitureParNom("Renault 5 Turbo"));
        voitures.add(StockVoitures
                .getVoitureParNom("Subaru Impreza WRC Génération 3 Sti"));

        // réorganisation, trie de la liste classement
        Collections.sort(classement);

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
}