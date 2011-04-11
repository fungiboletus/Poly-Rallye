package polyrallye.modele.championnat;

import org.jdom.Element;

import polyrallye.modele.personnes.Personne;
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

    /**
     * Constructeur
     * 
     * @param uneEpreuve
     */
    public Rang(String uneSpeciale, Personne personne, Duree d) {

        if (uneSpeciale == null || personne == null)
            throw new NullPointerException(
                    "Un ou des parametres du constructeur de Rang est incorrect");

        speciale = uneSpeciale;
        this.personne = personne;
        duree = d;
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

        noeud.addContent(new Element("speciale").setText(speciale));
        noeud.addContent(new Element("classement").setText("" + classement));

        noeud.addContent(new Element("nom").setText(personne.getNom()));

        noeud.addContent(new Element("duree").setText(""
                + duree.ConvertToSeconds()));

        noeud.addContent(new Element("ecart").setText(""
                + ecart.ConvertToSeconds()));

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
        if (classement == 1)
            pos = "er";

        // completer le resultat
        //
        resultat.append(speciale + "(" + classement + pos + ": " + personne
                + ", duree -> " + duree + " )");

        return resultat.toString();
    }

    @Override
    public int compareTo(Rang rang) {

        int valeur = rang.duree.ConvertToSeconds();

        return (this.duree.ConvertToSeconds() - valeur);

    }
}
