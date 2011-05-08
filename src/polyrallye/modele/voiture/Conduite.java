package polyrallye.modele.voiture;

import polyrallye.controlleur.Main;




/**
 * Moteur physique du jeu.
 * 
 * @author Abdoul Karimou Macina
 * @author Antoine Pultier
 */
public class Conduite {
    
	/**
     * Accélération de la voiture (m/s^2)
     */
    protected double acceleration;
    
    /**
     * Vitesse de la voiture (m/s)
     */
    protected double vitesse;
    
    
    /**
     * Distance parcourue par rapport au début du circuit (m)
     */
    protected double position;
    
    /**
     * Coefficient d'adhérence ou de frottement
     */
    protected double coeffAdherenceFrottement;
    
    /**
     * Est-ce que la voiture patine ?
     */
    protected boolean patinage;
    
    /**
     * La voiture conduite.
     */
    protected Voiture voiture;
    
    
    /**
     * Copie de la valeur de la masse de la voiture.
     * Car cette valeur est souvent appelée.
     */
    protected double masse;
    
    
    /**
     * Pareil
     */
    protected Moteur moteur;
    
    protected Transmission transmission;
    
    protected boolean freinage;
    
    public Conduite(Voiture v) {
        this.voiture = v;
        acceleration = 0.0;
        vitesse = 0.0;
        position = 0.0;
        patinage = false;
        masse = v.getChassis().getMasse();
        coeffAdherenceFrottement = 0.8;
        this.moteur = voiture.getMoteur();
        this.transmission = voiture.getTransmission();
    }

    /**
     * @param temps Temps passé par rapport au tick précédent
     */
    public void tick(double temps) {

		double forceRestitance = resistanceAerodynamique()+resistanceRoulement();
		//Main.logInfo("Resistance aérodynamique: "+resistanceAerodynamique());
		
		//double puissanceRequise = forceRestitance*vitesse;
		
		double forceMotrice;
		if (freinage) {
			
			// Pour simplifier de façon ÉNORME les calculs,
			// on suppose que toutes les voitures ont des freins et un ABS parfait
			// qui ne bloque même pas les roues une seule fois tellement il
			// est parfait <3
			
			forceMotrice = coeffAdherenceFrottement*masse*3.0*-9.81;
		} else {
			forceMotrice = forceMotrice();
		}
		
		double somme = forceMotrice - forceRestitance;
		
		// Inertie des pièces mécaniques de la voiture
		double ratioInertie = 1.12;
		
		// F = masse * acceleration
		acceleration = somme / masse / ratioInertie;
		
		Main.logDebug("Vitesse: "+vitesse*3.6);
		//Main.logInfo("Acceleration: "+acceleration);
		Main.logDebug("Force motrice: "+forceMotrice);
		//Main.logInfo("Force restitante: "+forceRestitance);
		Main.logDebug("=================");
		
		// C'est magique <3
		position += 0.5 * acceleration * temps * temps + vitesse * temps;
		vitesse += acceleration*temps;
		
		double regime = (vitesse/(2*Math.PI*Transmission.RAYON_ROUE))*transmission.getCoefCourant()*60;
		
		Main.logDebug("Régime: "+regime);
		moteur.setRegimeCourant(regime);
		
		// Histoire de ne pas partir en arrière à cause de la restitance de roulement…
		if (vitesse < 0.0) vitesse = 0.0;
		//System.out.println("t : "+temps+"\t\t"+vitesse);
    }
    
    /**
     * 
     * @param m
     * @param t
     * @return
     */
    public void vitesseAvancement(){
        vitesse = vitesseRoues()*2*Math.PI*Transmission.RAYON_ROUE;
        System.out.println("vitesseAvancement "+vitesse);
    }
    /**
     * Calcule la vitesse des roues
     * @return
     */
    public double vitesseRoues(){
        double rMax = (voiture.moteur.getPuissanceMax()*716)/(double)voiture.moteur.getCoupleMax();
        double res =  rMax*(1/voiture.transmission.getCoefCourant());
        return res;
        
    }
    /**
     * Calcule l'acceleration en fonction de la puissance, la masse la rayon de
     * la roue et le regime
     *  de la voiture.
     * 
     * @param m
     * @param masse
     */
    public void acceleration(TypeTerrain t) {
        acceleration = ((voiture.moteur.getCouple()/ Transmission.RAYON_ROUE) - t.frottement)*(1/(double)(voiture.chassis.getMasse()));
    }

    /**
     * Calcule la distance parcourue apres un temps d'acceleration.
     * 
     * @param tempsAcceleration
     */
    public void distanceAcceleration(int tempsAcceleration) {
        position += (vitesse/(double)3600) * tempsAcceleration + acceleration
                * Math.sqrt(tempsAcceleration) / (double) 2;
        System.out.println("distance en Mode A "+position);
    }

    /**
     * Calcule la distance en fonction de la vitesse.
     * 
     * @param tempsTick
     */
    public void distanceVitesseConstante(int tempsVariation) {
        position += (vitesse/(double)3600) * tempsVariation;
        System.out.println("distance en Mode V "+position);
    }

    /**
     * calcul de la possiton de la voiture.
     * @param mode
     * @param temps
     */
    public void distance(String  mode, int temps){
        if(mode.equals("acceleration"))
            distanceAcceleration(temps);
        if(mode.equals("vitesse"))
            distanceVitesseConstante(temps);
        System.out.println("Distance "+position);
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

    public boolean isFreinage() {
		return freinage;
	}

	public void setFreinage(boolean freinage) {
		this.freinage = freinage;
	}

	/**
     * Cette méthode permet verefier si une voiture v passe un virage ou non
     * En fonction  des caracteristiques du virage: rayon, angle de relevement, angle de frottement
     */
    public boolean passageVirage(double angleRelevement, double angleFrottement, double rayon){
        double res = Math.sqrt(rayon*9.81*Math.tan(angleFrottement)+angleRelevement);
        return (vitesse<res)?true:false;
    }

	public double getAcceleration() {
		return acceleration;
	}

	public double getVitesseLineaire() {
		return vitesse;
	}

	public double getDistanceParcourue() {
		return position;
	}
 
	
	/**
	 * Calcule la resistance aérodynamique en fonction de la voiture, 
	 * et de sa vitesse.
	 * 
	 * @return Resistance
	 */
	public double resistanceAerodynamique() {
		// Le coefficient de resistance est un fonction des caractèristiques de la voiture
		// Actuellement, les meilleures voitures de série sur ce domaine ont un coefficient de 0,25
		// Certaines voitures peuvent descendre en dessous, d'autres sont bien au dessus, tel un SUV
		// Les formules 1, ont un coefficient de 0.9, car les flux d'airs sont très utilisés
		// Pour simplifier nos calculs, nous utilisons un coefficient constant,
		// car on ne peux s'amuser à passer en souflerie chacune des voitures du jeu.
		final double Cx = 0.267;
		
		// Calcul de la surface frontale, à partir de la largeur
		// ce n'est pas très lié, mais ça permet d'établir des différences entre
		// les voitures
		double A = 0.0014*voiture.getChassis().getLargeur();
		
		// Et voici la formule magique
		return Cx * A * 0.5 * vitesse*vitesse * 1.202;
	}
	
	/**
	 * Calcule la resistance au roulement en fonction de la voiture,
	 * et du terrain.
	 * @return Resistance
	 */
	public double resistanceRoulement() {
		
		// Coefficient de restitance au roulement
		// Cela dépend de la voiture, du pneu, et d'une grande quantité d'autres facteurs
		// mais dans notre cas, 1% est une valeur moyenne.
		// Normalement, la vitesse joue un tout petit peu, mais c'est négligeable par
		// rapport à la restitance aéordynamique
		double fr = 0.01;
		
		return fr * masse*9.81;
	}
	
	/**
	 * Calcule la force motrice pour l'ensemble des roues.
	 * @return Force motrice
	 */
	public double forceMotrice() {
		
		double coupleRoues = transmission.getCoupleParRoue(PositionRoue.AVANT) + transmission.getCoupleParRoue(PositionRoue.ARRIERE);
		
		//Main.logImportant("Couple: "+coupleRoues);
		
		return coupleRoues/Transmission.RAYON_ROUE;
		
	}
}
