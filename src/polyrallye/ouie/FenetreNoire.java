package polyrallye.ouie;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import polyrallye.controlleur.Main;

/**
 * @author Antoine Pultier
 * Fenêtre noire.
 */
public class FenetreNoire extends JFrame {

	private static final long serialVersionUID = -6277192979591219507L;

	protected JTextArea informations;
	protected JPanel panneau;
	protected JScrollPane defilement;
	
	protected boolean consoleAffichee = false;

	public FenetreNoire() {
		super("PolyRallye");

		requestFocus();
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setSize(800, 450);
		setExtendedState(MAXIMIZED_BOTH);
		requestFocusInWindow();

		// Pour avoir un fond noir, malgré le thème, il faut un jpanel
		panneau = new JPanel();
		panneau.setBackground(Color.BLACK);
		panneau.setLayout(new BorderLayout());

		// Chargement de la magnifique image de fond
		JLabel image = new JLabel(new ImageIcon("Ressources/logo.png"));
		image.setPreferredSize(new Dimension(400, 114));
		image.setPreferredSize(new Dimension(400, 114));

		informations = new JTextArea();
		informations.setColumns(50);
		informations.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		informations.setDisabledTextColor(Color.WHITE);
		informations.setBackground(Color.BLACK);
		informations.setRequestFocusEnabled(false);
		informations.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		informations.setEnabled(false);

		defilement = new JScrollPane(informations);
		defilement.setAutoscrolls(true);
		defilement.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		defilement.setBorder(BorderFactory.createEmptyBorder());
		
		//panneau.add(defilement, BorderLayout.EAST);
		panneau.add(image, BorderLayout.CENTER);
		
		add(panneau);

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
	public void afficherTexte(String texte) {
		informations.append(texte+"\n");
		informations.setCaretPosition(informations.getCaretPosition()+texte.length()+1);
	}
	
	/**
	 * Affiche ou masque la console.
	 */
	public void basculerAffichageConsole() {
		if (consoleAffichee) {
			panneau.remove(defilement);
			consoleAffichee = false;
		} else {
			panneau.add(defilement, BorderLayout.EAST);
			consoleAffichee = true;
		}
		
		panneau.revalidate();
		panneau.repaint();
	}

}
