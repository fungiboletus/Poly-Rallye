package polyrallye.ouie;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * @author Antoine Pultier
 * 
 *         Singleton de gestion des entrées clavier.
 */
public class GestionEntrees extends KeyAdapter {

	/**
	 * Objet qui capture les entrées actuellement.
	 */
	protected static EcouteurEntrees ecouteur = null;

	private static GestionEntrees singletonGestionEntree = null;

	private GestionEntrees() {

	}

	/**
	 * Récupère l'instance du singleton.
	 * 
	 * @return Instance
	 */
	public static GestionEntrees getInstance() {
		if (singletonGestionEntree == null) {
			singletonGestionEntree = new GestionEntrees();
		}
		return singletonGestionEntree;
	}

	/**
	 * Spécifie l'écouteur qui doit gérer les entrées clavier pendant un temps
	 * donné.
	 * 
	 * @param e
	 *            Écouteur
	 */
	public void setEcouteur(EcouteurEntrees e) {
		ecouteur = e;
	}

	@Override
	public void keyReleased(KeyEvent codeTouche) {
		if (ecouteur != null) {
			switch (codeTouche.getKeyCode()) {
			case KeyEvent.VK_UP:
				ecouteur.haut();
				break;
			case KeyEvent.VK_DOWN:
				ecouteur.bas();
				break;
			case KeyEvent.VK_LEFT:
				ecouteur.gauche();
				break;
			case KeyEvent.VK_RIGHT:
				ecouteur.droite();
				break;
			case KeyEvent.VK_ENTER:
				ecouteur.selectionner();
				break;
			case KeyEvent.VK_ESCAPE:
				ecouteur.annuler();
				break;
			case KeyEvent.VK_F1:
				ecouteur.aide();
				break;

			}
		}
	}

}
