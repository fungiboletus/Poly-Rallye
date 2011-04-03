package polyrallye.ouie;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import polyrallye.controlleur.Main;

/**
 * @author Antoine Pultier
 * Fenêtre noire.
 */
public class FenetreNoire extends JFrame {

	private static final long serialVersionUID = -6277192979591219507L;

	protected JLabel texte;

	public FenetreNoire() {
		super("PolyRallye");

		requestFocus();
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setSize(400, 225);
		setExtendedState(MAXIMIZED_BOTH);
		requestFocusInWindow();

		// Pour avoir un fond noir, malgré le thème, il faut un jpanel
		JPanel p = new JPanel();
		p.setBackground(Color.BLACK);
		p.setLayout(new BorderLayout());

		// Chargement de la magnifique image de fond
		JLabel image = new JLabel(new ImageIcon("Ressources/logo.png"));
		image.setPreferredSize(new Dimension(400, 114));
		image.setPreferredSize(new Dimension(400, 114));

		// Texte en bas de la fenêtre, pour les voyants
		texte = new JLabel();
		texte.setForeground(Color.WHITE);
		texte.setHorizontalTextPosition(JLabel.CENTER);
		texte.setHorizontalAlignment(JLabel.CENTER);
		texte.setPreferredSize(new Dimension(400, 50));

		p.add(image, BorderLayout.CENTER);
		p.add(texte, BorderLayout.SOUTH);

		p.add(image, BorderLayout.CENTER);
		add(p);

		addKeyListener(GestionEntrees.getInstance());
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Main.quitter();
			}
		});

		setVisible(true);
	}

	/**
	 * Changer le texte qui s'affiche en bas de la fenêtre.
	 * 
	 * @param texte Le texte à afficher
	 */
	public void changerTexte(String texte) {
		this.texte.setText(texte);
	}

}
