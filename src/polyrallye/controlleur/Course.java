package polyrallye.controlleur;

import java.io.File;

import org.jdom.Element;

import polyrallye.modele.championnat.Championnat;
import polyrallye.modele.championnat.Duree;
import polyrallye.modele.championnat.Etape;
import polyrallye.modele.circuit.Circuit;
import polyrallye.modele.personnes.Joueur;
import polyrallye.modele.voiture.Moteur;
import polyrallye.modele.voiture.StockVoitures;
import polyrallye.modele.voiture.Voiture;
import polyrallye.ouie.Copilote;
import polyrallye.ouie.Klaxon;
import polyrallye.ouie.Radio;
import polyrallye.ouie.SonVoiture;
import polyrallye.ouie.liseuse.Liseuse;
import polyrallye.ouie.utilitaires.Sound;
import polyrallye.utilitaires.GestionXML;
import polyrallye.utilitaires.Multithreading;

/**
 * Gestion d'une course (copilotes, environnement, circuit, sons)
 * 
 * @author antoine
 * 
 */
public class Course {

	/**
	 * Le timer qui excécute la course.
	 */
	protected java.util.Timer timerOrganisateur;

	/**
	 * Le timer qui compte le temps passé. Celui-ci n'a rien à voir avec le
	 * timer précédent.
	 */
	protected org.lwjgl.util.Timer timerCompteur;

	/**
	 * Temps entre chaque tick d'horloge.
	 */
	protected float temps;

	/**
	 * Entrées de la course.
	 */
	protected GestionEntreesCourse entrees;

	/**
	 * Son du moteur.
	 */
	protected SonVoiture sonVoiture;

	/**
	 * Voiture conduite.
	 */
	protected Voiture voiture;

	/**
	 * Gestion physique de la conduite.
	 */
	protected Conduite conduite;

	/**
	 * Circuit parcouru.
	 */
	protected Circuit circuit;

	/**
	 * Bruit du klaxon.
	 */
	protected Klaxon klaxon;

	/**
	 * Copilote.
	 */
	protected Copilote copilote;
	
	/**
	 * Radio
	 */
	protected Radio radio;

	/**
	 * Score du joueur pour la course.
	 */
	protected double score;

	/**
	 * À chaque passage de rapport, il faut arrêter d'accélerer plus ou moins
	 * longtemps
	 */
	protected int cpTicksPassageRapport;
	
	/**
	 * Temps que le joueur met en plus pour finir la course par rapport au temps
	 * idéal
	 */
	protected double penalite = 0.0;

	protected Championnat championnat;

	protected Etape etape;
	
	protected GamePlay ordonnanceur;
	
	protected float tempsTimer;

	public Course(Voiture voiture, Circuit circuit) {
		this.voiture = voiture;
		this.circuit = circuit;
	}

	public Course(Voiture voiture, Circuit circuit, Etape et) {
		this.voiture = voiture;
		this.circuit = circuit;
		this.etape = et;
	}

	public Course(Voiture voiture) {
		this(voiture, "Nouveau/Monaco");
		// this(voiture, "Autoroute");

	}

	public Course(Voiture voiture, String fichierCircuit, Etape etape) {
		this(voiture, fichierCircuit);
		this.etape = etape;
	}
	
	public Course(Voiture voiture, String fichierCircuit) {
		this.voiture = voiture;

		try {
			Element noeud = GestionXML.chargerNoeudRacine(new File("Circuits/"
					+ fichierCircuit + ".osm"));
			circuit = new Circuit(noeud);
		} catch (Exception e) {
			Liseuse.lire("Désolé il y a un problème avec ce circuit");
			Main.logImportant(e.getMessage());
			e.printStackTrace();
		}
		
		ordonnanceur = new GamePlay(this);

	}

	public void start() {
		if (circuit == null)
			return;

		entrees = new GestionEntreesCourse();

		Main.changerGestionEntrees(entrees);

		System.out.println(circuit);
		
		// Création du son du moteur
		sonVoiture = new SonVoiture(voiture);

		// Création du moteur physique
		conduite = new Conduite(voiture);
		
		// Le klaxon, c'est important
		klaxon = new Klaxon(voiture.getNomComplet());

		// Creation copilote
		copilote = new Copilote();

		
		// Creation radio
		radio = new Radio();

		// Si on part en première, c'est mieux
		voiture.getTransmission().setPremiere();

		// Timer qui s'occupe de faire le travail 50 fois par secondes
		timerOrganisateur = new java.util.Timer();

		// Timer qui sert à compter le temps passé,
		// ce qui n'a rien à voir avec le timer précedent,
		// bien qu'ils aient le même nom
		timerCompteur = new org.lwjgl.util.Timer();

		org.lwjgl.util.Timer.tick();

		score = voiture.getMoteur().getPuissanceMax();

		final Moteur moteur = voiture.getMoteur();
		moteur.reset();

		// Démarrage des sons
		circuit.start();
		sonVoiture.play();
		// POur activer la radio (pas directement dans le jeu)
		radio.start();

		// À 50Hz, comme le courant EDF
		timerOrganisateur.schedule(ordonnanceur, 0, 20);// 20

		temps = timerCompteur.getTime();

		Main.logInfo("La course est lancée");
	}

	/**
	 * Fonction appelée lors d'un crash.
	 */
	public void crash() {
		circuit.playCrash();
		circuit.stopFrottement();
		conduite.stop();
		sonVoiture.setRegime(800.0f, false);
		Multithreading.dormir(1500);
		copilote.playCrash();
		Multithreading.dormir(1000);
	}

	public void finDeCourse(int nbSecondesCourse) {
		fermer();
		
		Liseuse.lire("Fin de la course");
		Sound sonFin = new Sound("Sons/divers/fin.wav");
		sonFin.setGain(2.0f);
		sonFin.playAndWait();
		sonFin.delete();
		
		Main
        .changerGestionEntrees(GestionEntreesMenu
                .getInstance());
		
		Duree tempsEtape = new Duree(nbSecondesCourse);
		
		Liseuse.lire("Tu as mis "+tempsEtape.getMinutes()+" minutes et "+tempsEtape.getSecondes()+" secondes");
		
		if (etape == null) return;
		
		etape.setClassement(tempsEtape,
				StockVoitures.getVoitureParNom(voiture.getNomComplet()));
		Etape.EnregistrerEtape(etape);
		
		championnat.setClassement();

		int nbplayed = 0;
		boolean isfinished = false;

		for (int i = 0; i < championnat.getEtapes().size(); ++i) {
			for (int j = 0; j < championnat.getEtapes().get(i).getClassement()
					.size(); ++j) {
				if (Joueur.session.getNom().equals(
						championnat.getEtapes().get(i).getClassement().get(j)
								.getPersonne().getNom())) {
					nbplayed++;
				}
			}
		}

		if (nbplayed == 10)
			isfinished = true;

		if (isfinished
				&& championnat.getClassement().get(0).getPersonne().getNom()
						.equals(Joueur.session.getNom())) {
			try {
				championnat.RemisePrix();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Liseuse.lire("vous avez gagné le championnat et vous avez remporté la voiture "
					+ championnat.getVoitureGagne()
					+ " et "
					+ championnat.getArgentGagne() + " euros");
		}
		Championnat.EnregistrerChampionnat(championnat);
	}

	public void fermer() {
		circuit.stopFrottement();
		circuit.stop();
		sonVoiture.stop();
		timerOrganisateur.cancel();
		klaxon.delete();
		radio.delete();
		copilote.delete();
		Main.changerGestionEntrees(GestionEntreesMenu.getInstance());
	}
}
