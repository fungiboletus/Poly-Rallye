package polyrallye.utilitaires;

public abstract class Multithreading {

	public static void dormir(long duree) {
		try {
			Thread.sleep(duree);
		} catch (InterruptedException e) {
		}
	}
}
