package polyrallye.utilitaires;

import polyrallye.modele.circuit.TypeRoute;

/**
 * Quelques fonctions de cartographies bien utiles.
 * 
 * Les formules sont étonnament précises, et ne sortent pas d'un chapeau.
 */
public abstract class Cartographie {

	/**
	 * Distance entre deux points de coordonées géographiques.
	 * 
	 * Il s'agit de formules adaptées à partir du logiciel libre JOSM, un
	 * éditeur de cartes OpenStreetMap en Java.
	 * 
	 * La méthode utilisée est celle de Haversine.
	 * 
	 * @return Distance en mètres.
	 */
	public static double distance(double latA, double lonA, double latB,
			double lonB) {
		double R = 6378137;
		double sinHalfLat = Math.sin(Math.toRadians(latB - latA) / 2);
		double sinHalfLon = Math.sin(Math.toRadians(lonB - lonA) / 2);
		double d = 2
				* R
				* Math.asin(Math.sqrt(sinHalfLat * sinHalfLat
						+ Math.cos(Math.toRadians(latA))
						* Math.cos(Math.toRadians(latB)) * sinHalfLon
						* sinHalfLon));

		// For points opposite to each other on the sphere,
		// rounding errors could make the argument of asin greater than 1
		// (This should almost never happen.)
		if (java.lang.Double.isNaN(d)) {
			System.err.println("Error: NaN in greatCircleDistance");
			d = Math.PI * R;
		}
		return d;
	}

	/**
	 * Angle d'un virage entre trois points.
	 * 
	 * Il s'agit d'une application du théorème d'Al-Kashi qui utilise la
	 * fonction distance.
	 * 
	 * @return Angle en degrés
	 */
	public static double angleVirage(double latA, double lonA, double latB,
			double lonB, double latC, double lonC) {

		/*
		 * System.out.println("A : " + latA + " : " + lonA + "\nB : " + latB +
		 * " : " + lonB + "\nC : " + latC + " : " + lonC);
		 */

		double lA = distance(latA, lonA, latB, lonB);
		double lB = distance(latB, lonB, latC, lonC);
		double lC = distance(latA, lonA, latC, lonC);

		return 180 - Math.toDegrees(Math.acos((lA * lA + lB * lB - lC * lC)
				/ (2 * lA * lB)));
	}

	/**
	 * Sens d'un virage entre trois points.
	 * 
	 * La technique utilisée est le produit vectoriel (espace en trois
	 * dimensions) entre les vecteurs AB et BC. Si le signe du composant Z est
	 * positif, c'est un virage à droite, sinon, c'est un virage à gauche.
	 * 
	 * Cette technique a été trouvée après avoir demandé de l'aide sur le web :
	 * http://linuxfr.org/forums/g%C3%A9n%C3%A9ralhors-sujets/posts/d%C3%A9terminer-le-sens-dun-virage-dans-un-chemin-openstreetmap
	 * 
	 * @return Sens
	 */
	public static TypeRoute sensVirage(double latA, double lonA, double latB,
			double lonB, double latC, double lonC) {

		// Vecteur AB
		double latAB = latB - latA;
		double lonAB = lonB - lonA;

		// Vecteur BC
		double latBC = latC - latB;
		double lonBC = lonC - lonB;

		// Composant Z du produit vectoriel entre AB et BC
		return (latAB * lonBC - lonAB * latBC) > 0 ? TypeRoute.DROITE : TypeRoute.GAUCHE;
	}
}
