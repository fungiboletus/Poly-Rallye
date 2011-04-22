package polyrallye.ouie;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import polyrallye.controlleur.Main;

/**
 * @author Antoine Pultier
 * Fenêtre noire.
 */
public class FenetreNoire extends JFrame {

	private static final long serialVersionUID = -6277192979591219507L;

	protected JTextPane console;
	protected StyledDocument documentConsole;
	protected SimpleAttributeSet consoleBleu;
	protected SimpleAttributeSet consoleBlanc;
	protected SimpleAttributeSet consoleRouge;
	
	protected JPanel panneau;
	protected JScrollPane defilement;
	
	protected KeyListener gestionEntrees;
	
	protected boolean consoleAffichee = false;

	public FenetreNoire() {
		super("PolyRallye");
		
		requestFocus();
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setSize(960, 540);
		setExtendedState(MAXIMIZED_BOTH);
		requestFocusInWindow();

		// Pour avoir un fond noir, malgré le thème, il faut un jpanel
		panneau = new JPanel();
		panneau.setBackground(Color.BLACK);
		panneau.setLayout(new BorderLayout());

		// Chargement de la magnifique image de fond
		JLabel image = new JLabel(new ImageIcon("Ressources/logo.png"));
		image.setPreferredSize(new Dimension(480, 114));

		console = new JTextPane();
		documentConsole = console.getStyledDocument();
		
		consoleBleu = new SimpleAttributeSet();
		consoleBlanc = new SimpleAttributeSet();
		consoleRouge = new SimpleAttributeSet();
		
		consoleBleu.addAttribute(StyleConstants.Foreground, Color.BLUE);
		consoleBlanc.addAttribute(StyleConstants.Foreground, Color.WHITE);
		consoleRouge.addAttribute(StyleConstants.Foreground, Color.RED);
		
		console.setPreferredSize(new Dimension(480, 0));
		console.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		console.setBackground(Color.BLACK);
		console.setRequestFocusEnabled(false);
		console.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		defilement = new JScrollPane(console);
		defilement.setAutoscrolls(true);
		defilement.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		defilement.setBorder(BorderFactory.createEmptyBorder());
		
		//panneau.add(defilement, BorderLayout.EAST);
		panneau.add(image, BorderLayout.CENTER);
		
		add(panneau);
		
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
	protected void log(String texte, SimpleAttributeSet couleur) {
		try {
			documentConsole.insertString(documentConsole.getLength(), texte+"\n", couleur);
		} catch (BadLocationException e) {
		}
		
		//console.setCaretPosition(console.getCaretPosition()+texte.length()+1);
		console.setCaretPosition(documentConsole.getLength());
		
	}
	
	public void logInfo(String texte) {
		log(texte, consoleBleu);
	}
	
	public void logLiseuse(String texte) {
		log(texte, consoleBlanc);
	}
	
	public void logImportant(String texte) {
		log(texte, consoleRouge);
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
	
	public void changerGestionEntrees(KeyListener nouveauListener)
	{
		if (gestionEntrees != null)
		{
			removeKeyListener(gestionEntrees);
		}
		addKeyListener(nouveauListener);
		gestionEntrees = nouveauListener;
	}

}
