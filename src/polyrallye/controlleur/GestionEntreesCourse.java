package polyrallye.controlleur;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * @author Antoine Pultier
 * 
 *         Gestion des entrées clavier des courses.
 */
public class GestionEntreesCourse extends KeyAdapter {

	protected boolean accelere;
	protected boolean freine;
	protected boolean gauche;
	protected boolean droite;
	protected boolean rapportSup;
	protected boolean rapportInf;
	protected boolean klaxon;
	protected boolean echap;
	protected boolean automatique = true;
	protected boolean volumup = false;
	protected boolean volumdown = false;
	protected boolean radio = false;
	protected boolean station = false;
	protected boolean copilote = false;
	protected double coeffVitesse = 1.0;
	protected static boolean isActive = true;
	
	@Override
	public void keyPressed(KeyEvent touche) {
		switch (touche.getKeyCode()) {
		case KeyEvent.VK_UP:
			if (isActive)
			accelere = true;
			break;
		case KeyEvent.VK_DOWN:
			if (isActive)
			freine = true;
			break;
		case KeyEvent.VK_LEFT:
			if (isActive)
			gauche = true;
			break;
		case KeyEvent.VK_RIGHT:
			if (isActive)
			droite = true;
			break;
		case KeyEvent.VK_SHIFT:
			if (isActive)
			rapportSup = true;
			break;
		case KeyEvent.VK_CONTROL:
			if (isActive)
			rapportInf = true;
			break;
		case KeyEvent.VK_K:
		case KeyEvent.VK_SPACE:
			if (isActive)
			klaxon = true;
			break;
		case KeyEvent.VK_ESCAPE:
			if (isActive)
			echap = true;
			break;
		case KeyEvent.VK_A:
			if (isActive)
			automatique = !automatique;
			break;
		case KeyEvent.VK_D:
			Main.basculerAffichageConsole();
			break;
		case KeyEvent.VK_C:
			if (isActive)
			copilote = true;
			break;
		case KeyEvent.VK_E:
			if (isActive)
			radio = true;
			break;
		case KeyEvent.VK_R:
			if (isActive)
			station = true;
			break;
		case KeyEvent.VK_T:
			if (isActive)
			volumup = true;
			break;
		case KeyEvent.VK_Y:
			if (isActive)
			volumdown = true;
			break;
		case KeyEvent.VK_V:
			Main.debug = !Main.debug;
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent touche) {
		switch (touche.getKeyCode()) {
		case KeyEvent.VK_UP:
			if (isActive)
			accelere = false;
			break;
		case KeyEvent.VK_DOWN:
			if (isActive)
			freine = false;
			break;
		case KeyEvent.VK_LEFT:
			if (isActive)
			gauche = false;
			break;
		case KeyEvent.VK_RIGHT:
			if (isActive)
			droite = false;
			break;
		case KeyEvent.VK_K:
		case KeyEvent.VK_SPACE:
			if (isActive)
			klaxon = false;
			break;
		case KeyEvent.VK_F1:
			if (isActive) {
			coeffVitesse =  coeffVitesse > 0.12 ? coeffVitesse*0.33 : coeffVitesse;
			Main.logImportant("Coefficient vitesse: "+coeffVitesse);
			}
			break;
		case KeyEvent.VK_F2:
			if (isActive) {
			coeffVitesse =  coeffVitesse < 3.0 ? coeffVitesse*1.33 : coeffVitesse;
			Main.logImportant("Coefficient vitesse: "+coeffVitesse);
			}
			break;
		// Pour les passages de rapports, c'est des évènements
		// à réinitialiser sois même
		/*
		 * case KeyEvent.VK_SHIFT: rapportSup = true; break; case
		 * KeyEvent.VK_CONTROL: rapportInf = true; break;
		 */

		}
	}

	public boolean isAccelere() {
		return accelere;
	}

	public boolean isFreine() {
		return freine;
	}

	public boolean isGauche() {
		return gauche;
	}

	public boolean isDroite() {
		return droite;
	}

	public boolean isRapportSup() {
		boolean tmp = rapportSup;
		rapportSup = false;
		return tmp;
	}

	public boolean isRapportInf() {
		boolean tmp = rapportInf;
		rapportInf = false;
		return tmp;
	}

	public boolean isKlaxon() {
		return klaxon;
	}

	public boolean isEchap() {
		return echap;
	}

	public boolean isCopilotte() {
		return copilote;
	}

	public boolean isRadio() {
		return radio;
	}

	public boolean isStation() {
		return station;
	}

	public boolean isVLD() {
		return volumdown;
	}

	public boolean isVLU() {
		return volumup;
	}

	// Mode bricolage
	public void copiloteChecked() {
		copilote = false;
	}

	public void radioChecked() {
		radio = false;
	}

	public void stationChecked() {
		station = false;
	}

	public void vldChecked() {
		volumdown = false;
	}

	public void vluChecked() {
		volumup = false;
	}
	
	public static void setActive(boolean t) {
		isActive= t;
	}
	

	
}
