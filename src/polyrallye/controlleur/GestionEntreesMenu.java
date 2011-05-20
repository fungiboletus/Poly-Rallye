package polyrallye.controlleur;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * @author Antoine Pultier
 * 
 *         Singleton de gestion des entrées clavier des menus.
 */
public class GestionEntreesMenu extends KeyAdapter {

	/**
	 * Objet qui capture les entrées actuellement.
	 */
	protected static EcouteurEntrees ecouteur = null;

	private static GestionEntreesMenu singletonGestionEntree = null;

	private GestionEntreesMenu() {

	}

	/**
	 * Récupère l'instance du singleton.
	 * 
	 * @return Instance
	 */
	public static GestionEntreesMenu getInstance() {
		if (singletonGestionEntree == null) {
			singletonGestionEntree = new GestionEntreesMenu();
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
				ecouteur.but();
				break;
			case KeyEvent.VK_F2:
				ecouteur.aide();
				break;
			case KeyEvent.VK_D:
				Main.basculerAffichageConsole();
				break;
			case KeyEvent.VK_V:
				Main.voitures();
				break;

			}
		}
	}

}
