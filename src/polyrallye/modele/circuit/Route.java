package polyrallye.modele.circuit;


public class Route implements ContenuCircuit {
	
	protected double distance;
	//protected double longueur;
	protected double force;
	
	protected TypeRoute type;
	
//	public Route(double distance,double longueur, TypeRoute type) {
//		this.distance=distance;
//		this.longueur=longueur;
//		this.type=type;
//		this.force=0;
//	}
	
	public Route(double distance, TypeRoute type,double force) {
		this.distance=distance;

		this.type=type;

		this.force=force;
	}
	
	
	@Override
	public double getDistance() {
		return distance;
	}

//	@Override
//	public double getLongueur() {
//		return longueur;
//	}

	public double getForce() {
		return force;
	}

	public void setForce(double force) {
		this.force = force;
	}

	public TypeRoute getType() {
		return type;
	}

	public void setType(TypeRoute type) {
		this.type = type;
	}

}
