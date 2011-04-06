package polyrallye.modele.championnat;

import polyrallye.modele.personnes.Personne;

/**
 * Classe Rang : représente un rang pour une epreuve donnée
 * 
 * @author zizou
 * 
 */
public class Rang {
    private String speciale;
    private int classement;
    private Personne personne;
    private Duree duree;
    private Duree ecart;

    /**
     * Constructeur
     * 
     * @param uneEpreuve
     */
    public Rang(String uneSpeciale, Personne personne ) {

        if (uneSpeciale == null || personne == null)
            throw new NullPointerException(
                    "Un ou des parametres du constructeur de Rang est incorrect");
        
        speciale = uneSpeciale;
        this.personne = personne;
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
        resultat.append(speciale + "(" + classement + pos + ": " + personne
                + ", duree -> " + duree + " )");

        return resultat.toString();
    }

}
