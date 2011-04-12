package polyrallye.modele.voiture;




/**
 * 
 * @author macina
 */
public class Conduite {
    protected double acceleration;
    protected double vitesseLineaire;
    protected double distanceParcourue;
    protected double frottement;
    protected boolean patinage;
    protected String mode;
    Voiture v;
    public Conduite(Voiture v) {
        this.v = v;
        acceleration = 0.0;
        vitesseLineaire = 0.0;
        distanceParcourue = 0.0;
        patinage = false;
        mode=null;
    }

    /**
     * 
     * @param m
     * @param t
     * @return
     */
    public void vitesseAvancement(){
        vitesseLineaire = vitesseRoues()*2*Math.PI*v.transmission.RAYON_ROUE;
        System.out.println("vitesseAvancement "+vitesseLineaire);
    }
    /**
     * Calcule la vitesse des roues
     * @return
     */
    public double vitesseRoues(){
        double vitesseMax = (v.moteur.getPuissanceMax()*716)/(double)v.moteur.getCoupleMax();
        System.out.println("vitesseMAX "+vitesseMax);
        double res =  vitesseMax*(1/v.transmission.getCoefCourant());
        return res;
        
    }
    /**
     * Calcule l'acceleration en fonction de la puissance, la masse la rayon de
     * la roue et le regime de la voiture.
     * 
     * @param m
     * @param masse
     */
    public void acceleration(TypeTerrain t) {
        acceleration = ((v.moteur.getCouple(v.moteur.getRegimeCourant())/ v.transmission.RAYON_ROUE) - t.frottement)*(1/(double)(v.chassis.getPoids()));
        System.out.println("acceleration "+acceleration);
    }

    /**
     * Calcule la distance parcourue apres un temps d'acceleration.
     * 
     * @param tempsAcceleration
     */
    public void distanceAcceleration(int tempsAcceleration) {
        distanceParcourue += vitesseLineaire * tempsAcceleration + acceleration
                * Math.sqrt(tempsAcceleration) / (double) 2;
        System.out.println("distance en Mode A "+distanceParcourue);
    }

    /**
     * Calcule la distance en fonction de la vitesse.
     * 
     * @param temps
     */
    public void distanceVitesseConstante(int tempsVariation) {
        distanceParcourue += vitesseLineaire * tempsVariation;
        System.out.println("distance en Mode V "+distanceParcourue);
    }

    /**
     * calcul de la possiton de la voiture.
     * @param mode
     * @param temps
     */
    public void distance(String  mode, int temps){
        if(mode.equals("acceleration"))
            distanceAcceleration(temps);
        if(mode.equals("vitesse constante"))
            distanceVitesseConstante(temps);
        System.out.println("Distance "+this.distanceParcourue);
    }
    /**
     * @return the patinage
     */
    public boolean isPatinage() {
        return patinage;
    }

    /**
     * @param patinage
     *            the patinage to set
     */
    public void setPatinage() {
        int i=0;
        if(i==0){
            patinage = true;
        }
        else
            patinage = false;
    }

    /**
     * Cette méthode permet verefier si une voiture v passe un virage ou non
     * En fonction  des caracteristiques du virage: rayon, angle de relevement, angle de frottement
     */
    public boolean passageVirage(double angleRelevement, double angleFrottement, double rayon){
        double res = Math.sqrt(rayon*9.81*Math.tan(angleFrottement)+angleRelevement);
        return (vitesseLineaire<res)?true:false;
    }
    
    public static void main(String []args){
       Voiture v = StockVoitures.getVoitureParNom("Peugeot 205 Turbo 16 Évolution 2");
       System.out.println("voiture "+v.toString());
       v.getTransmission().passerVitesse();
       v.getTransmission().passerVitesse();
       v.getTransmission().passerVitesse();
       Conduite c = new Conduite(v);
//       c.vitesseRoues();
       v.moteur.regimeCourant(v.transmission.getCoefCourant());
       c.vitesseAvancement();
       c.acceleration(TypeTerrain.ASPHALT);
       System.out.println("\ncomparaison\n"); 
      Voiture vv = StockVoitures.getVoitureParNom("Fiat Panda 4x4");
      System.out.println("voiture "+vv.toString());
      vv.getTransmission().passerVitesse();
      vv.getTransmission().passerVitesse();
      vv.getTransmission().passerVitesse();
      vv.moteur.regimeCourant(v.transmission.getCoefCourant());
      Conduite cc = new Conduite(vv);
//      cc.vitesseRoues();
      cc.vitesseAvancement();
      cc.acceleration(TypeTerrain.ASPHALT);
    }
}
