package polyrallye.utilitaires;

/**
 * Classe utilitaire pour la gestion du multithreading.
 * 
 * @author antoine
 */
public abstract class Multithreading {

	/**
	 * Fait une pause, sans se soucier du fait qu'elle puisse être interrompue.
	 * 
	 * @param duree
	 *            Durée de la pause
	 */
	public static void dormir(long duree) {
		try {
			Thread.sleep(duree);
		} catch (InterruptedException e) {
		}
	}
}
