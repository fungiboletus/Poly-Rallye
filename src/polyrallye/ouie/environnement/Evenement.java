package polyrallye.ouie.environnement;

import polyrallye.modele.circuit.Circuit;
import polyrallye.ouie.utilitaires.Sound;

/**
 * Évènements sonores associés à une portion de circuit.
 */
public class Evenement {

	/**
	 * Type de l'évènement en cours.
	 * 
	 * Cette manière de programmer n'est pas très polymorphique, mais avoir 4
	 * classes pour 10 lignes de code m'emmerde.
	 */
	protected TypeEvenement type;

	/**
	 * Paramètre de l'évènement (comme le terrain si c'est un changement de
	 * terrain).
	 */
	protected String parametre;

	public Evenement(TypeEvenement type, String parametre) {
		this.type = type;
		this.parametre = parametre;
	}

	public void exec(Circuit circuit) {
		switch (type) {
		case ENVIRONNEMENT:
			circuit.changeEnvironnement(parametre);			
			break;
		case TERRAIN:
			circuit.changeTerrain(parametre);
			break;
		case SON:
			Sound s = new Sound("ressources/Sons/divers/" + parametre);
			s.playAndDelete();
			break;
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		builder.append(type);
		builder.append(" : ");
		builder.append(parametre);
		builder.append("]");
		return builder.toString();
	}

	public TypeEvenement getType() {
		return type;
	}

}
