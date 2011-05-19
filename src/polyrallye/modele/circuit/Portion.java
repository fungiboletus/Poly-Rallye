package polyrallye.modele.circuit;

import java.util.ArrayList;
import java.util.List;

import polyrallye.ouie.environnement.Evenement;

/**
 * Portion d'un circuit.
 * 
 * Cela peut être un virage, une ligne droite, ou simplement un changement de
 * surface.
 */
public class Portion {

	/**
	 * Longueur de la portion.
	 */
	protected double longueur;

	/**
	 * Angle de la portion par rapport à la portion précédente. L'angle est
	 * entre 0 (tout droit) et 180° (demi tour).
	 * 
	 * Si on souhaite savoir de quel cotés tourner, il faut regarder le
	 * TypeRoute.
	 */
	protected double angle;

	/**
	 * Type de la route (ligne droite, virage à gauche, ou à droite)
	 */
	protected TypeRoute type;

	/**
	 * Évènements (sonores) associé à cette portion.
	 */
	protected List<Evenement> evenements;
	
	public Portion(double longueur, TypeRoute type, double angle) {
		this.longueur = longueur;

		this.type = type;

		this.angle = angle;
		
		evenements = new ArrayList<Evenement>();
	}

	public double getLongueur() {
		return longueur;
	}

	public double getAngle() {
		return angle;
	}
	
	public TypeRoute getType() {
		return type;
	}
	
	public void setLongueur(double longueur) {
		this.longueur = longueur;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public void addEvenement(Evenement e) {
		evenements.add(e);
	}

	public List<Evenement> getEvenements() {
		return evenements;
	}
	
	public boolean aDesEvenements() {
		return !evenements.isEmpty();
	}
	
	public void execution(Circuit cc) {
		for (int i = 0; i < evenements.size(); i++) {
			evenements.get(i).exec(cc);
		}
		evenements.clear();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("\nPortion [longueur=");
		builder.append(longueur);
		builder.append(", angle=");
		builder.append(angle);
		builder.append(", type=");
		builder.append(type);
		builder.append(", evenements=");
		builder.append(evenements);
		builder.append("]");
		return builder.toString();
	}

}
