package polyrallye.modele.voiture;


public class Conduite {
    protected double acceleration;
    protected double vitesse;
    protected double distanceParcourue;
    
    protected static double RAYON = 0.42;
    
    protected double frottement;
    
    public Conduite(){
        acceleration=0.0;
        vitesse=0.0;
        distanceParcourue=0.0;
    }
    
    /**
     * Calcule l'acceleration en fonction de la puissance, la masse
     * la rayon de la roue et le regime de la voiture.
     * @param m
     * @param masse
     */
    public void acceleration(Moteur m, double masse){
        acceleration = (masse*RAYON*m.getPuissanceMax())/((double) m.getRegimeCoupleMax());
    }

    /**
     * Calcule la distance parcourue apres un temps d'acceleration.
     * @param tempsAcceleration
     */
    public void distanceAcceleration(int tempsAcceleration){
        distanceParcourue += vitesse * tempsAcceleration
                + acceleration * Math.sqrt(tempsAcceleration) / (double) 2;
    }
    /**
     * Calcule la distance en fonction de la vitesse
     * @param temps
     */
    public void distanceVitesseConstante(int temps){
        distanceParcourue += vitesse*temps;
    }
}
