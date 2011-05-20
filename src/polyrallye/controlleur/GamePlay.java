package polyrallye.controlleur;

import java.util.TimerTask;

import polyrallye.modele.circuit.Portion;
import polyrallye.modele.circuit.TypeRoute;
import polyrallye.modele.voiture.Moteur;
import polyrallye.modele.voiture.Transmission;

public class GamePlay extends TimerTask {

	protected Course c;

	/**
	 * Ce que doit être en train de faire le conducteur
	 */
	protected TypeAction actionCourante;

	/**
	 * Gain du son de frottement
	 */
	protected double sonFrottement;

	/**
	 * Distance avant le prochain virage
	 */
	protected double distanceAvantVirage;

	/**
	 * Temps du joueur avant qu'il réagisse à une action
	 */
	protected float tempsAvantReaction;

	protected double vitesseMaxVirage;

	protected double distanceFreinage;

	protected double vitesse;

	protected double position;

	protected float chrono;
	
	protected float tempsTick;

	/**
	 * Temps pendant lequel le joueur doit tourner dans le virage
	 */
	protected double tempsVirage;

	protected double chronoVirageNonDesire = 0.0;

	public final static double TEMPS_REACTION = 1.5;

	/**
	 * Portion courante du circuit.
	 */
	protected Portion portionCourante;

	/**
	 * Distance parcourue sur la section en cours
	 */
	protected double distancePortion = 0.0;

	public GamePlay(Course course) {
		super();
		this.c = course;

		portionCourante = c.circuit.nextPortion();
		actionCourante = TypeAction.ACCELERATION;
	}

	@Override
	public void run() {
		if (c.entrees.isEchap()) {
			c.fermer();
		}

		// Gestion du temps
		org.lwjgl.util.Timer.tick();
		chrono = c.timerCompteur.getTime();
		tempsTick = chrono - c.temps;
		c.temps = chrono;

		double distanceParcourue = c.conduite.tick(tempsTick
				* c.entrees.coeffVitesse);

		vitesse = c.conduite.getVitesseLineaire();
		position = c.conduite.getDistanceParcourue();

		c.circuit.setDistance(position);
		c.circuit.setVitesse(vitesse);

		sonFrottement = 0.0;

		gestionKlaxon();
		gestionCopilote();
		gestionRadio();
		gestionAccelerateur();

		gestionFreinage();
		gestionVirage();

		gestionPatinage();

		gestionBoiteAutomatique();

		gestionRegimeMoteur();

		gestionSonFrottement();

		// Gestion des portions du circuit

		distancePortion += distanceParcourue;

		Main.logDebug("Distance parcourue: " + position, 0);
		Main.logDebug("Distance portion: " + distancePortion, 1);

		// Gestion des virages
		distanceAvantVirage = portionCourante.getLongueur() - distancePortion;

		vitesseMaxVirage = c.conduite.getVitesseMaxPourVirage(portionCourante
				.getAngle());

		distanceFreinage = c.conduite.getDistanceFreinage(vitesseMaxVirage) * 2.0;

		Main.logDebug("Angle virage: " + portionCourante.getAngle(), 2);
		Main.logDebug("Vitesse max virage: " + vitesseMaxVirage * 3.6, 3);
		Main.logDebug("Distance Freinage: " + distanceFreinage, 4);
		Main.logDebug("Distance avant virage: " + distanceAvantVirage, 12);
		Main.logDebug("Pénalité: " + c.penalite, 17);

		switch (actionCourante) {
		case ACCELERATION:
			Main.logDebug("ACCELERATION", 16);
			modeAcceleration();
			break;

		case AVANT_FREINAGE:
			Main.logDebug("AVANT FREINAGE", 16);
			modeAvantFreinage();
			break;

		case FREINAGE:
			Main.logDebug("FREINAGE", 16);
			modeFreinage();
			break;

		case AVANT_VIRAGE:
			Main.logDebug("AVANT_VIRAGE", 16);
			modeAvantVirage();
			break;
		case VIRAGE:
			Main.logDebug("VIRAGE", 16);
			modeVirage();
			break;
		case APRES_VIRAGE:
			Main.logDebug("APRES VIRAGE", 16);
			break;
		case VIRAGE_NON_DESIRE:
			Main.logDebug("VIRAGE NON DESIRE", 16);
			modeVirageNonDesire();
			modeAcceleration();
			break;
		}

		// passageVirage();

	}

	public void gestionKlaxon() {
		if (c.entrees.isKlaxon()) {
			c.klaxon.pouet();
		} else {
			c.klaxon.pasPouet();
		}
	}

	public void gestionCopilote() {
		// Gestion copilote
		if (c.entrees.isCopilotte()) {
			c.copilote.togglePipelette();
			c.entrees.copiloteChecked();
		}
	}

	public void gestionRadio() {
		if (c.entrees.isRadio()) {
			c.radio.toggleRadio();
			c.entrees.radioChecked();
		} else if (c.entrees.isStation()) {
			c.radio.changeStation();
			c.entrees.stationChecked();
		} else if (c.entrees.isVLD()) {
			c.radio.diminuerSon();
			c.entrees.vldChecked();
		} else if (c.entrees.isVLU()) {
			c.radio.augmenterSon();
			c.entrees.vluChecked();
		}
	}

	public void gestionAccelerateur() {

		c.voiture.getMoteur().setAccelere(
				c.entrees.isAccelere() && c.cpTicksPassageRapport == 0);

		if (c.cpTicksPassageRapport > 0)
			--c.cpTicksPassageRapport;

	}

	public void gestionFreinage() {
		// Et du frein
		if (c.entrees.isFreine()) {
			c.conduite.setFreinage(true);
			if (vitesse > 0.0) {
				if (vitesse < 1.0) {
					c.sonVoiture.sonFreinage();
				} else {
					sonFrottement = 0.22;
				}
			}
		} else {
			c.conduite.setFreinage(false);
		}
	}

	public void gestionVirage() {

		boolean virage = c.entrees.gauche || c.entrees.droite;

		c.conduite.setVirage(virage);

		if (virage && vitesse > 3.0) {
			sonFrottement = 0.34;

			/*
			 * if (actionCourante == TypeAction.ACCELERATION || actionCourante
			 * == TypeAction.AVANT_FREINAGE || actionCourante ==
			 * TypeAction.FREINAGE) { actionCourante =
			 * TypeAction.VIRAGE_NON_DESIRE; chronoVirageNonDesire = chrono; }
			 */
		}
	}

	public void gestionPatinage() {
		if (c.conduite.isPatinage()) {
			sonFrottement = 0.45;
		}
	}

	public void gestionBoiteAutomatique() {
		// Maintenant, du passage des vitesses
		final Transmission t = c.voiture.getTransmission();
		final Moteur m = c.voiture.getMoteur();

		final double regimeMoteur = m.getRegimeCourant();

		final double regimePuissanceMax = m.getRegimePuissanceMax();

		// Ceci est une belle condition avec des appels de méthodes <3
		if (((c.entrees.isRapportSup() || (c.entrees.automatique && (regimeMoteur > (regimePuissanceMax + 250.0) || m
				.isRupteurEnclanche()))) && t.passerVitesse())
				|| ((c.entrees.isRapportInf() || (c.entrees.automatique && regimeMoteur < (regimePuissanceMax + 250)
						* t.getCoeffBoiteAutomatique())) && t.retrograder())) {
			c.sonVoiture.passageRapport();
			c.cpTicksPassageRapport = 5;
		}
	}

	public void gestionRegimeMoteur() {
		c.sonVoiture.setRegime(
				(float) c.voiture.getMoteur().getRegimeCourant(),
				c.entrees.isAccelere());
	}

	public void gestionSonFrottement() {
		if (sonFrottement > 0.0) {
			c.circuit.playFrottement((float) sonFrottement);
		} else {
			c.circuit.stopFrottement();
		}
	}

	public void modeAcceleration() {
		// Si le freinage approche trop
		if (distanceFreinage >= distanceAvantVirage
				&& distanceFreinage - distanceAvantVirage > 3.0) {
			c.copilote.playFreine();
			actionCourante = TypeAction.AVANT_FREINAGE;
			tempsAvantReaction = chrono;
		} else if (distanceAvantVirage < 0.0) {
			virageSuivant();
		} else if (c.entrees.isGauche() || c.entrees.isDroite()) {
			if (chronoVirageNonDesire > TEMPS_REACTION) {
				Main.logInfo("Il ne faut pas tourner n'importe quand");
				c.crash();
				distancePortion = 0.0;
				chronoVirageNonDesire = 0.0;
			} else {
				chronoVirageNonDesire += tempsTick;
			}
		}
	}

	public void modeAvantFreinage() {
		if (chrono - tempsAvantReaction > TEMPS_REACTION) {
			Main.logInfo("Le freinage na pas été effectué à temps");
			c.crash();
			actionCourante = TypeAction.ACCELERATION;
			distancePortion = 0.0;
			chronoVirageNonDesire = 0.0;
			// On se remet là où on devait être pour freiner
			// c.conduite.setPosition(positionAvantFreinage);
			c.penalite += TEMPS_REACTION + 10.0;
		} else {
			if (c.entrees.isFreine()) {
				actionCourante = TypeAction.FREINAGE;

				// On se remet à l'endroit idéal pour freiner, en
				// fonction de la nouvelle vitesse
				// c.conduite.setPosition(position - distanceFreinage);
				// distancePortion -= distanceFreinage;

				c.penalite += chrono - tempsAvantReaction;
			}
		}
	}

	public void modeAvantVirage() {
		if (chrono - tempsAvantReaction > TEMPS_REACTION) {
			Main.logInfo("Vous n'avez pas tourné à temps");
			c.crash();
			actionCourante = TypeAction.ACCELERATION;
			distancePortion = 0.0;
			chronoVirageNonDesire = 0.0;
			c.penalite += TEMPS_REACTION + 5.0;
		} else {
			if ((portionCourante.getType() == TypeRoute.GAUCHE
					&& c.entrees.isGauche() && !c.entrees.isDroite())
					|| (portionCourante.getType() == TypeRoute.DROITE
							&& !c.entrees.isGauche() && c.entrees.isDroite())) {
				actionCourante = TypeAction.VIRAGE;
				tempsAvantReaction = chrono;
				tempsVirage = c.conduite.getTempsPourVirage(portionCourante
						.getAngle());
				//Main.logImportant("Temps de : " + tempsVirage);
			}
		}
	}

	public void modeVirage() {
		distancePortion = 0.0;
		if ((portionCourante.getType() == TypeRoute.GAUCHE
				&& c.entrees.isGauche() && !c.entrees.isDroite())
				|| (portionCourante.getType() == TypeRoute.DROITE
						&& !c.entrees.isGauche() && c.entrees.isDroite())) {
			if (chrono - tempsAvantReaction > tempsVirage) {
				c.copilote.playOk();
				actionCourante = TypeAction.ACCELERATION;
				chronoVirageNonDesire = 0.0;
			}
		} else {
			Main.logInfo("Vous n'avez pas tourné assez longtemps");
			c.crash();
			actionCourante = TypeAction.ACCELERATION;

			chronoVirageNonDesire = 0.0;
			c.penalite += TEMPS_REACTION + 5.0;
		}
	}

	public void modeFreinage() {
		// Si on freine trop
		if (vitesse > 0.02 && vitesse <= vitesseMaxVirage) {
			virageSuivant();
		}
	}

	public void virageSuivant() {
		
		do {			
			portionCourante = c.circuit.nextPortion();
		} while (portionCourante != null && portionCourante.getAngle() < 3.0);
		
		distancePortion = 0.0;

		if (portionCourante == null) {
			c.finDeCourse();
		} else {

			actionCourante = TypeAction.AVANT_VIRAGE;
			tempsAvantReaction = chrono;

			switch (portionCourante.getType()) {
			case GAUCHE:
				c.copilote.playGauche();
				break;
			case DROITE:
				c.copilote.playDroite();
				break;
			}
		}
	}

	public void modeVirageNonDesire() {

		if (chrono - tempsAvantReaction > TEMPS_REACTION) {
			c.crash();
			actionCourante = TypeAction.ACCELERATION;
			// On se remet là où on devait être pour freiner
			// c.conduite.setPosition(positionAvantFreinage);
			c.penalite += TEMPS_REACTION + 10.0;
		}
	}
}
