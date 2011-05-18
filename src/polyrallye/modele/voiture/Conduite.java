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
	 * Copie de la valeur de la masse de la voiture. Car cette valeur est
	 * souvent appelée.
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
	 * @param temps
	 *            Temps passé par rapport au tick précédent
	 * @return Distance parcourue lors du tick
	 */
	public double tick(double temps) {

		double forceRestitance = resistanceAerodynamique()
				+ resistanceRoulement();
		// Main.logInfo("Resistance aérodynamique: "+resistanceAerodynamique());

		// double puissanceRequise = forceRestitance*vitesse;

		double forceMotrice;
		double forceMotriceMax = coeffAdherenceFrottement * masse * 9.81;

		if (freinage) {

			// Pour simplifier de façon ÉNORME les calculs,
			// on suppose que toutes les voitures ont des freins et un ABS
			// parfait
			// qui ne bloque même pas les roues une seule fois tellement il
			// est parfait <3

			forceMotrice = forceMotriceMax * -2.5;
		} else {

			double coupleMoteur = moteur.getCouple();

			forceMotrice = forceMotrice(forceMotriceMax * 1.3, coupleMoteur);
		}

		double somme = forceMotrice - forceRestitance;

		// Inertie des pièces mécaniques de la voiture
		final double ratioInertie = 1.12;

		// F = masse * acceleration
		acceleration = somme / masse / ratioInertie;

		Main.logDebug("Vitesse: " + vitesse * 3.6, 5);
		Main.logDebug("Rapport: " + transmission.getRapportCourant(), 6);
		Main.logDebug("Acceleration: " + acceleration, 7);

		// C'est magique <3
		double distanceParcourue = 0.5 * acceleration * temps * temps + vitesse
				* temps;
		vitesse += acceleration * temps;

		// Histoire de ne pas partir en arrière à cause de la restitance de
		// roulement…
		if (vitesse < 0.0)
			vitesse = 0.0;

		if (distanceParcourue < 0.0)
			distanceParcourue = 0.0;

		position += distanceParcourue;

		double regime = (vitesse / (2 * Math.PI * Transmission.RAYON_ROUE))
				* transmission.getCoefCourant() * 60;

		// Si on patine, le moteur tourne à fond \o/
		if (patinage) {
			regime /= coeffAdherenceFrottement;
		}

		Main.logDebug("Régime: " + regime, 8);
		moteur.setRegimeCourant(regime);

		// System.out.println("t : "+temps+"\t\t"+vitesse);

		return distanceParcourue;
	}

	/**
	 * @return the patinage
	 */
	public boolean isPatinage() {
		return patinage;
	}

	public boolean isFreinage() {
		return freinage;
	}

	public void setFreinage(boolean freinage) {
		this.freinage = freinage;
	}

	/**
	 * Cette méthode permet verefier si une voiture v passe un virage ou non En
	 * fonction des caracteristiques du virage: rayon, angle de relevement,
	 * angle de frottement
	 */
	public boolean passageVirage(double angleRelevement,
			double angleFrottement, double rayon) {
		double res = Math.sqrt(rayon * 9.81 * Math.tan(angleFrottement)
				+ angleRelevement);
		return (vitesse < res) ? true : false;
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
	 * Calcule la distance de freinage.
	 * 
	 * C'est la formule que l'on trouve partout.
	 * 
	 * Elle considère que l'on effectue un freinage parfait…
	 * 
	 * @param vFinale
	 *            vitesse à atteindre en m^2
	 * @return Distance en mètres pour s'arrêter
	 */
	public double getDistanceFreinage(double vFinale) {
		if (vFinale > vitesse) {
			return 0.0;
		}

		double vDiff = vFinale-vitesse;

		return (vDiff * vDiff) / (2.0 * 9.81 * coeffAdherenceFrottement);
	}

	
	/**
	 * @param angleVirage Angle entre 0 (ligne droite) et 180°
	 * @return Vitesse maximale (en m/s)
	 */
	public double getVitesseMaxPourVirage(double angleVirage) {
		double xa = 0.0;
		double xb = 180.0;
		
		double ya = 40.0*coeffAdherenceFrottement;
		double yb = 8.0*coeffAdherenceFrottement;
		
		return ya + (angleVirage - xa)*((yb-ya)/(xb-xa));
	}
	
	/**
	 * Calcule la resistance aérodynamique en fonction de la voiture, et de sa
	 * vitesse.
	 * 
	 * @return Resistance
	 */
	public double resistanceAerodynamique() {
		// Le coefficient de resistance est un fonction des caractèristiques de
		// la voiture
		// Actuellement, les meilleures voitures de série sur ce domaine ont un
		// coefficient de 0,25
		// Certaines voitures peuvent descendre en dessous, d'autres sont bien
		// au dessus, tel un SUV
		// Les formules 1, ont un coefficient de 0.9, car les flux d'airs sont
		// très utilisés
		// Pour simplifier nos calculs, nous utilisons un coefficient constant,
		// car on ne peux s'amuser à passer en souflerie chacune des voitures du
		// jeu.
		final double Cx = 0.267;

		// Calcul de la surface frontale, à partir de la largeur
		// ce n'est pas très lié, mais ça permet d'établir des différences entre
		// les voitures
		double A = 0.0014 * voiture.getChassis().getLargeur();

		// Et voici la formule magique
		return Cx * A * 0.5 * vitesse * vitesse * 1.202;
	}

	/**
	 * Calcule la resistance au roulement en fonction de la voiture, et du
	 * terrain.
	 * 
	 * @return Resistance
	 */
	public double resistanceRoulement() {

		// Coefficient de restitance au roulement
		// Cela dépend de la voiture, du pneu, et d'une grande quantité d'autres
		// facteurs
		// mais dans notre cas, 1% est une valeur moyenne.
		// Normalement, la vitesse joue un tout petit peu, mais c'est
		// négligeable par
		// rapport à la restitance aéordynamique
		double fr = 0.01;

		return fr * masse * 9.81;
	}

	/**
	 * Calcule la force motrice pour l'ensemble des roues.
	 * 
	 * @param forceMotriceMax
	 *            Force maximale possible
	 * @param coupleMoteur
	 *            Couple du moteur
	 * @return Force motrice
	 */
	public double forceMotrice(double forceMotriceMax, double coupleMoteur) {

		double forceAvant = transmission.getCoupleParRoue(PositionRoue.AVANT,
				coupleMoteur) / Transmission.RAYON_ROUE;
		double forceArriere = transmission.getCoupleParRoue(
				PositionRoue.ARRIERE, coupleMoteur) / Transmission.RAYON_ROUE;

		boolean patinageAvant = false;
		if (forceAvant > forceMotriceMax) {
			forceAvant = forceMotriceMax * coeffAdherenceFrottement;
			patinage = true;
		} else {
			patinage = false;
			patinageAvant = true;
		}

		if (forceArriere > forceMotriceMax) {
			forceArriere = forceMotriceMax * coeffAdherenceFrottement;
			patinage = true;
		} else if (!patinageAvant) {
			patinage = false;
		}

		return forceAvant + forceArriere;

	}

	public void setPosition(double position) {
		this.position = position;
	}
	
	/**
	 * Fonction appelée lors d'un crash…
	 */
	public void crash() {
		vitesse = 0.0;
		moteur.setRegimeCourant(800.0);
	}
}
