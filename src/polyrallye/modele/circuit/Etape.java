package polyrallye.modele;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.LWJGLUtil;

import sun.rmi.server.Util;

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
    public Etape(int numero, String uneEpreuve, List<Personne> participants ) {

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
    public void setClassement(Duree dureeJoueurEtape) {
        
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
        
        for (int i =0; i<10; ++i)
        classement.add(new Rang(nom, participants.get(i)));
        
        voitures.add(StockVoitures.getVoitureParNom("Bugatti Veyron 16.4 Super Sport"));
        voitures.add(StockVoitures.getVoitureParNom("Audi Quattro Sport S1 Pikes Peak"));
        voitures.add(StockVoitures.getVoitureParNom("Audi Quattro Sport S1"));
        voitures.add(StockVoitures.getVoitureParNom("Citroën DS3 WRC"));
        voitures.add(StockVoitures.getVoitureParNom("Peugeot 307 WRC"));
        voitures.add(StockVoitures.getVoitureParNom("Peugeot 306 S16"));
        voitures.add(StockVoitures.getVoitureParNom("Peugeot 206 WRC"));
        voitures.add(StockVoitures.getVoitureParNom("Renault 5 Turbo"));
        voitures.add(StockVoitures.getVoitureParNom("Subaru Impreza WRC Génération 3 Sti"));

        
        
        
        for (int i =0; i<10; ++i)
        classement.get(i).setDuree(Duree(t2s.util.Random.delta(i, n)));
        






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
     * Jouer le etape
     * 
     */
    public void jouer(int leTour, Duree D) {
        // a faire

    }

    /**
     * Effectue un classement par rang
     * 
     */
    public List<Rang> classer() {
        // a faire

        return null;
    }

}
