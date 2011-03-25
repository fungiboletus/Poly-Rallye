package polyrallye.modele;

import java.util.ArrayList;
import java.util.List;


/**
 * Classe Match : représente un match, avec ses deux equipes, son arbitre et son
 * gagnant
 * 
 * @author zizou
 * 
 */
public class Etape {

    private static int nofEtapes = 0;
    private int specialeID;
    private int numero;
    protected String nom;
    protected Circuit circuit;
    private List<Joueur> participants;
    private List<Rang> classement;

    /**
     * Constructeur
     * 
     */
    public Etape(int numeroMatch, String uneEpreuve,
            List<Joueur> participants) {
        specialeID = ++nofEtapes;
        // tester les parametres
        //
        if (uneEpreuve == null || numeroMatch <= 0)
            throw new NullPointerException(
                    "Un ou des parametres du constructeur de Match est null");

        // renseigner les attributs
        //
        nom = uneEpreuve;
        numero = numeroMatch;

        // si la liste de participant est null : en créer une
        if (participants == null)
            participants = new ArrayList<Joueur>();
        else
            this.setParticipants(participants);
    }

    public int getMatchID() {
        return specialeID;
    }

    /**
     * retourne l'speciale concernée par le speciale
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
     * retourne le numéro de la spéciale
     * 
     * @return
     */
    public int getNumero() {
        return numero;
    }

    /**
     * affecte les joueurs de la spéciale
     * 
     * @return
     */
    public void setParticipants(List<Joueur> participants) {
        this.participants = participants;
    }

    /**
     * 
     * @return joueurs de la spéciale
     */
    public List<Joueur> getParticipants() {
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
