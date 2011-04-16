package polyrallye.modele.championnat;

import org.jdom.Element;

import polyrallye.modele.personnes.Personne;
import polyrallye.modele.voiture.Voiture;
import polyrallye.utilitaires.GestionXML;

/**
 * Classe Rang : représente un rang pour une epreuve donnée
 * 
 * @author zizou
 * 
 */
public class Rang implements Comparable<Rang> {
    private String speciale;
    private int classement;
    private Personne personne;
    private Duree duree;
    private Duree ecart;
    private Voiture car;

    /**
     * Constructeur
     * 
     * @param uneEpreuve
     */
    public Rang(String uneSpeciale, Personne personne, Duree d, Voiture car) {

        // if (uneSpeciale == null || personne == null)
        // throw new NullPointerException(
        // "Un ou des parametres du constructeur de Rang est incorrect");

        speciale = uneSpeciale;
        this.personne = personne;
        duree = d;
        this.car = car;
    }

    public Rang(Element noeud) {

        speciale = noeud.getChildText("speciale");

        classement = GestionXML.getInt(noeud.getChildText("classement"));

        personne = new Personne(noeud.getChildText("nom"));

        duree = new Duree(GestionXML.getInt(noeud.getChildText("duree")));

        ecart = new Duree(GestionXML.getInt(noeud.getChildText("ecart")));

    }

    public Element toXML() {

        Element noeud = new Element("classement");

        noeud.addContent(new Element("classement").setText("" + classement));

        noeud.addContent(new Element("nom").setText(personne.getNom()));

        noeud.addContent(new Element("voiture").setText(car.getNomComplet()));

        noeud.addContent(new Element("duree").setText(duree.toString()));

        if (classement != 1)
            noeud.addContent(new Element("ecart").setText(ecart.toString()));

        return noeud;

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
        String affichEcart = "";
        if (classement == 1) {
            pos = "er";
        } else
            affichEcart = " ; écart --> " + ecart;

        // completer le resultat

        resultat.append(speciale + " (" + classement + pos + ": "
                + personne.getNom() + ", duree -> " + duree + affichEcart
                + " )");

        return resultat.toString();
    }

    @Override
    public int compareTo(Rang rang) {

        int valeur = rang.duree.ConvertToDixiemes();

        return (this.duree.ConvertToDixiemes() - valeur);

    }

    public Personne getPersonne() {
        return personne;
    }

    public void setPersonne(Personne personne) {
        this.personne = personne;
    }

    public void setSpeciale(String speciale) {
        this.speciale = speciale;
    }
}
