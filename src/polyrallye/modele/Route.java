package polyrallye.modele;

public class Route implements ContenuCircuit {
	
	protected long distance;
	protected long longueur;
	protected long force;
	
	protected TypeRoute type;
	
	public Route(long distance,long longueur, TypeRoute type) {
		this.distance=distance;
		this.longueur=longueur;
		this.type=type;
		this.force=0;
	}
	
	public Route(long distance,long longueur, TypeRoute type,long force) {
		this(distance,longueur,type);
		this.force=force;
	}
	
	
	@Override
	public long getDistance() {
		return distance;
	}

	@Override
	public long getLongueur() {
		return longueur;
	}

	public long getForce() {
		return force;
	}

	public void setForce(long force) {
		this.force = force;
	}

	public TypeRoute getType() {
		return type;
	}

	public void setType(TypeRoute type) {
		this.type = type;
	}

}
