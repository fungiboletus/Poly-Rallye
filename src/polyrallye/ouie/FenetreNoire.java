package polyrallye.ouie;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
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

	/**
	 * Truc de swing qui sert à rien… 
	 */
	private static final long serialVersionUID = -6277192979591219507L;

	/**
	 * Console qui affiche les messages.
	 */
	protected JTextPane console;
	
	/**
	 * Gestion des styles de la console.
	 */
	protected StyledDocument documentConsole;
	
	/**
	 * Style d'affichage dans la console en bleu.
	 */
	protected SimpleAttributeSet consoleBleu;
	
	/**
	 * Style d'affichage dans la console en blanc. 
	 */
	protected SimpleAttributeSet consoleBlanc;
	
	/**
	 * Style d'affichage dans la console en rouge.
	 */
	protected SimpleAttributeSet consoleRouge;
	
	/**
	 * Un panneau est présent dans la JFrame.
	 * 
	 * L'unique raison de sa présence est de pouvoir changer la couleur de fond.
	 */
	protected JPanel panneau;
	
	/**
	 * Gestion du défilement de la console.
	 */
	protected JScrollPane defilement;
	
	/**
	 * Listener des entrées clavier de la fenêtre, et donc du jeu.
	 */
	protected KeyListener gestionEntrees;
	
	/**
	 * Est-ce que la console est affichée ?
	 */
	protected boolean consoleAffichee = false;
	
	/**
	 * Panneau qui contient les informations de debug
	 */
	protected JPanel panneauInfosDebug;
	
	/**
	 * Liste des labels qui contiennent les informations de débug
	 */
	protected JLabel[] informationsDebug;

	public FenetreNoire() {
		super("PolyRallye");
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setSize(960, 540);
		setExtendedState(MAXIMIZED_BOTH);
		
		// JE VEUX LE FOCUS
		requestFocus();
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
		
		panneauInfosDebug = new JPanel();
		panneauInfosDebug.setBackground(Color.BLACK);
		panneauInfosDebug.setPreferredSize(new Dimension(450, 0));
		panneauInfosDebug.setLayout(new GridLayout(20, 1));
		
		informationsDebug = new JLabel[20];
		
		for (int i = 0; i < 20; ++i) {
			JLabel l = new JLabel();
			l.setForeground(Color.WHITE);
			informationsDebug[i] = l;
			panneauInfosDebug.add(l);
		}
		
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
	
	public void logDebug(String texte, int index) {
		if (consoleAffichee) {
			if (index >= 20) index %= 20;
			informationsDebug[index].setText(texte);			
		}
	}
	
	/**
	 * Affiche ou masque la console.
	 */
	public void basculerAffichageConsole() {
		if (consoleAffichee) {
			panneau.remove(defilement);
			panneau.remove(panneauInfosDebug);
			consoleAffichee = false;
		} else {
			panneau.add(defilement, BorderLayout.EAST);
			panneau.add(panneauInfosDebug, BorderLayout.WEST);
			consoleAffichee = true;
		}
		
		panneau.revalidate();
		panneau.repaint();
	}
	
	/**
	 * Change le listener qui récupère les évènements du clavier.
	 * 
	 * @param nouveauListener Le nouveau listener
	 */
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
