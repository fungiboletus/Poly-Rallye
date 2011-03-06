package polyrallye.ouie;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GestionEntrees implements KeyListener {

	protected static EcouteurEntrees ecouteur = null;

	private static GestionEntrees singletonGestionEntree = null;

	private GestionEntrees() {

	}

	public static GestionEntrees getInstance() {
		if (singletonGestionEntree == null) {
			singletonGestionEntree = new GestionEntrees();
		}
		return singletonGestionEntree;
	}

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

	@Override
	public void keyPressed(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}

}
