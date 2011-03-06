package polyrallye.ouie;

import java.awt.Color;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import polyrallye.controlleur.Main;

public class FenetreNoire extends JFrame implements WindowListener {

	private static final long serialVersionUID = -6277192979591219507L;

	public FenetreNoire()
	{
		super("PolyRallye");
		
		requestFocus();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		setSize(400, 225);
		//setExtendedState(MAXIMIZED_BOTH);
		
		// Pour avoir un fond noir, malgré le thème, il faut un jpanel
		JPanel p = new JPanel();
		p.setBackground(Color.BLACK);
		add(p);
		
		addKeyListener(GestionEntrees.getInstance());
		addWindowListener(this);
		
		setVisible(true);
	}

	@Override
	public void windowClosing(WindowEvent e) {
		Main.quitter();
	}

	@Override
	public void windowActivated(WindowEvent e) {	
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}


	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}
	


}
