package polyrallye.utilitaires;

public abstract class Multithreading {

	public static void dormir(long duree) {
		try {
			Thread.sleep(duree);
		} catch (InterruptedException e) {
		}
	}
	
	/**
	 * Get the accurate system time
	 * 
	 * @return The system time in milliseconds
	 */
	/*private long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}*/
}
