package polyrallye.ouie;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class FenetreNoire extends JFrame{

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
		setVisible(true);
	}
	


}
