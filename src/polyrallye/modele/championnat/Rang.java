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
    private String car;

    /**
     * Constructeur
     * 
     * @param uneEpreuve
     */
    public Rang(String uneSpeciale, Personne personne, Duree d, String car) {

        // if (uneSpeciale == null || personne == null)
        // throw new NullPointerException(
        // "Un ou des parametres du constructeur de Rang est incorrect");

        speciale = uneSpeciale;
        this.personne = personne;
        duree = d;
        this.car = car;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public Rang(Element noeud) {

        classement = GestionXML.getInt(noeud.getChildText("position"));

        personne = new Personne(noeud.getChildText("nom"));

        car = noeud.getChildText("voiture");
        
        duree = new Duree(GestionXML.getInt(noeud.getChildText("duree")));

        if (classement != 1)
            ecart = new Duree(GestionXML.getInt(noeud.getChildText("ecart")));
            
        }

    public Element toXML() {

        Element noeud = new Element("classement");

        noeud.addContent(new Element("position").setText("" + classement));

        noeud.addContent(new Element("nom").setText(personne.getNom()));

        noeud.addContent(new Element("voiture").setText(car));

        noeud.addContent(new Element("duree").setText(""
                + duree.ConvertToDixiemes()));

        if (classement != 1)
            noeud.addContent(new Element("ecart").setText(""
                    + ecart.ConvertToDixiemes()));

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
        String affichCar = "";
        String affichEcart = "";
        if (classement == 1) {
            pos = "er";
        } else
            affichEcart = " ; écart --> " + ecart;

        if (car != null) affichCar = ", " + car;
        
        System.out.println(duree);
        if (speciale != null) {
            resultat.append("\n" + speciale + " (" + classement + pos + ": "
                    + personne.getNom() + affichCar + ", duree -> " + duree + affichEcart
                    + " )");
        } else {
            resultat.append("\n"+ " (" + classement + pos + ": "
                    + personne.getNom() + ", " + car + ", duree -> " + duree + affichEcart
                    + " )");
        }

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
