package polyrallye.modele.championnat;

import polyrallye.modele.personnes.Joueur;

/**
 * Classe Rang : représente un rang pour une epreuve donnée
 * 
 * @author zizou
 * 
 */
public class Rang {
    private String speciale;
    private int classement;
    private Joueur joueur;
    private Duree duree;
    private Duree ecart;

    /**
     * Constructeur
     * 
     * @param uneEpreuve
     */
    public Rang(String uneSpeciale, int unClassement, Joueur joueur, Duree duree, Duree ecart) {
        // tester les parametres
        if (uneSpeciale == null || unClassement < 0 
                || joueur == null || duree == null)
            throw new NullPointerException(
                    "Un ou des parametres du constructeur de Rang est incorrect");
        // renseigner les attributs
        speciale = uneSpeciale;
        classement = unClassement;
        this.joueur = joueur;
        this.duree = duree;
        this.ecart = ecart;
    }

    /**
     * retourne la speciale concernée par ce rang
     * 
     * @return
     */
    public String getSpeciale() {
        return speciale;
    }  
    
    /**
     * retourne l'ecart
     * 
     * @return
     */
    public Duree getEcart() {
        return ecart;
    }  
    
    /**
     * retourne la duree concernée par ce rang
     * 
     * @return
     */
    public Duree getDuree() {
        return duree;
    }
    

    /**
     * retourne le classement
     * 
     * @return
     */
    public int getClassement() {
        return classement;
    }

    /**
     * Met à jour le classement
     * 
     * @param nouveau
     */
    public void setClassement(int nouveau) {
        classement = nouveau;
    }
    
    /**
     * Met à jour la duree
     * 
     * @param nouveau
     */
    public void setDuree(Duree D) {
        duree = D;
    }
    
    /**
     * Met à jour l'ecart
     * 
     * @param nouveau
     */
    public void setEcart(Duree ec) {
        ecart = ec;
    }

    /**
     * toString
     */
    @Override
    public String toString() {
        StringBuilder resultat = new StringBuilder();

        // obtenir le suffixe de la position
        //
        String pos = "eme";
        if (classement == 1)
            pos = "er";

        // completer le resultat
        //
        resultat.append(speciale + "(" + classement + pos + ": " + joueur
                + ", duree -> " + duree + " )");

        return resultat.toString();
    }
}
