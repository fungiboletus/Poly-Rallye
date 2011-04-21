package polyrallye.modele.championnat;

/**
 * * Représentation d'une durée de type hh:mm:ss:d * * @author zizou
 * */
public class Duree implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private int heures;
    private int minutes;
    private int secondes;
    private int dixiemes;

    /**
     * Construit la durée en précisant les minutes, les secondes et les heures.
     * * Les deux nombres doivent être positifs et les minutes strictement
     * inférieures à 60.
     * 
     * @param m
     *            Nombre de minutes de la durée.
     * @param h
     *            Nombre d'heures
     * @param s
     *            Nombres Secondes
     * @param s
     *            Nombres dixiemes
     * @throws IllegalArgumentException
     *             La condition énoncé n'est pas respecté.
     * */
    public Duree(int h, int m, int s, int d) {
        if (m >= 0 && m < 60 && h >= 0 && s >= 0 && s < 60 && d >= 0 && d < 10) {
            this.minutes = m;
            this.heures = h;
            this.secondes = s;
            this.dixiemes = d;
        } else {
            throw new IllegalArgumentException();
        }
    }
    
    public Duree(int sec) {
        if (sec >= 0) {
            this.heures = sec / 3600;
            this.heures = (sec - this.heures) / 60;
            this.secondes = (sec - this.heures - this.minutes);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Getter de minutes. Seules les minutes sont retournées
     * 
     * @return Nombre de minutes de la durée.
     */
    public int getMinutes() {
        return this.minutes;
    }

    /**
     * Setter de minutes. Le nombre de minutes de la durée ne peut ni être
     * négatif ni supérieur ou égal à 60.
     * 
     * @param m
     *            Nombre de minutes de la durée.
     * @throws IllegalArgumentException
     *             Le nombre de minutes est négatif ou supérieur ou égal à 60.
     */
    public void setMinutes(int m) {
        if (m >= 0 && m < 60) {
            this.minutes = m;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Getter de dixiemes. Seules les minutes sont retournées
     * 
     * @return Nombre de dixiemes de la durée.
     */
    public int getDixiemes() {
        return this.dixiemes;
    }

    /**
     * Setter de dixieme. Le nombre de dixieme de la durée ne peut ni être
     * négatif ni supérieur ou égal à 10.
     * 
     * @param m
     *            Nombre de minutes de la durée.
     * @throws IllegalArgumentException
     *             Le nombre de dixieme est négatif ou supérieur ou égal à 10.
     */
    public void setDixiemes(int c) {
        if (c >= 0 && c < 10) {
            this.dixiemes = c;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Getter de secondes. Seules les secondes sont retournées
     * 
     * @return Nombre de minutes de la durée.
     */
    public int getSecondes() {
        return this.secondes;
    }

    /**
     * Setter de minutes. Le nombre de secondes de la durée ne peut ni être
     * négatif ni supérieur ou égal à 60.
     * 
     * @param m
     *            Nombre de minutes de la durée.
     * @throws IllegalArgumentException
     *             Le nombre de minutes est négatif ou supérieur ou égal à 60.
     */
    public void setSecondes(int s) {
        if (s >= 0 && s < 60) {
            this.secondes = s;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Getter de heures.
     * 
     * @return Nombre d'heures de la durée.
     */
    public int getHeures() {
        return this.heures;
    }

    /**
     * Setter de heures. Le nombre d'heures de la durée doit être positif.
     * 
     * @param h
     *            Nombre d'heures de la durée.
     * @throws IllegalArgumentException
     *             Le nombre d'heures est négatif.
     */
    public void setHeures(int h) {
        if (h >= 0) {
            this.heures = h;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Vérifie l'équalité entre l'objet courant et un autre objet.
     * 
     * @param o
     *            Objet quelconque.
     * @return L'équalité entre les deux les objets.
     */
    public boolean equals(Object o) {
        if (!(o instanceof Duree))
            return false;
        else {
            Duree autre = (Duree) o;
            if (this.minutes == autre.minutes && this.heures == autre.heures
                    && this.secondes == autre.secondes
                    && this.dixiemes == autre.dixiemes)
                return true;
            else
                return false;
        }
    }

    /**
     * Retourne une copie de l'objet courant.
     * 
     * @return Copie de l'objet courant.
     */
    public Object clone() {
        return new Duree(this.heures, this.minutes, this.secondes,
                this.dixiemes);
    }

    /**
     * Retourne une représentation chainée de l'objet. Représentation sous la
     * forme hh:mm:ss.d
     * 
     * @return Représentation chainée de l'objet.
     */
    public String toString() {
        String fin = null;
        if (this.minutes < 10)
            fin = ":0" + this.minutes + ":" + this.secondes + "."
                    + this.dixiemes;
        else if (this.secondes < 10)
            fin += ":" + this.minutes + ":0" + this.secondes + "."
                    + this.dixiemes;
        else
            fin = ":" + this.minutes + ":" + this.secondes + "."
                    + this.dixiemes;
        return "" + this.heures + fin;
    }

    /**
     * Converti en secondes.
     * 
     * @return entier
     */
    public int ConvertToSeconds() {
        int result = 0;
        result += this.minutes * 60;
        result += this.heures * 3600;
        result += this.secondes;
        return result;
    }

}
