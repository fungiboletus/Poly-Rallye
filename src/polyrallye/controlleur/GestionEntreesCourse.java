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
	
	@Override
	public void keyPressed(KeyEvent touche) {
		switch (touche.getKeyCode()) {
		case KeyEvent.VK_UP:
			accelere = true;
			break;
		case KeyEvent.VK_DOWN:
			freine = true;
			break;
		case KeyEvent.VK_LEFT:
			gauche = true;
			break;
		case KeyEvent.VK_RIGHT:
			droite = true;
			break;
		case KeyEvent.VK_SHIFT:
			rapportSup = true;
			break;
		case KeyEvent.VK_CONTROL:
			rapportInf = true;
			break;
		case KeyEvent.VK_K:
		case KeyEvent.VK_SPACE:
			klaxon = true;
			break;
		case KeyEvent.VK_ESCAPE:
			echap = true;
			break;
		case KeyEvent.VK_A:
			automatique = !automatique;
			break;
		case KeyEvent.VK_D:
			Main.basculerAffichageConsole();
			break;
		case KeyEvent.VK_C:
			copilote = true;
			break;
		case KeyEvent.VK_E:
			radio = true;
			break;
		case KeyEvent.VK_R:
			station = true;
			break;
		case KeyEvent.VK_T:
			volumup = true;
			break;
		case KeyEvent.VK_Y:
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
			accelere = false;
			break;
		case KeyEvent.VK_DOWN:
			freine = false;
			break;
		case KeyEvent.VK_LEFT:
			gauche = false;
			break;
		case KeyEvent.VK_RIGHT:
			droite = false;
			break;
		case KeyEvent.VK_K:
		case KeyEvent.VK_SPACE:
			klaxon = false;
			break;
		case KeyEvent.VK_F1:
			coeffVitesse =  coeffVitesse > 0.12 ? coeffVitesse*0.5 : coeffVitesse;
			Main.logImportant("Coefficient vitesse: "+coeffVitesse);
			break;
		case KeyEvent.VK_F2:
			coeffVitesse =  coeffVitesse < 3.0 ? coeffVitesse*2.0 : coeffVitesse;
			Main.logImportant("Coefficient vitesse: "+coeffVitesse);
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
}
