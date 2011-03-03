package polyrallye.ouie;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class FenetreNoire extends JFrame implements KeyListener {

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
		
		addKeyListener(this);
		setVisible(true);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println("ouch, c'est chaud "+e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
