package polyrallye.ouie.menus;

import java.io.File;
import java.util.List;

import org.jdom.Element;

import polyrallye.modele.personnes.Joueur;
import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Menu;
import polyrallye.ouie.liseuse.Liseuse;
import polyrallye.utilitaires.GestionXML;

public class SelectionJoueur extends Menu implements ActionMenu{
	

	
	public SelectionJoueur(Menu menuPrecedent) {
		super(menuPrecedent);
	}
	
	@Override
	public void actionMenu() {
		Liseuse.lire("Selectionnez un joueur");
		lancer();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void remplir() {
		
		try {
			Element noeud = GestionXML.chargerNoeudRacine(new File("ressources/Comptes/nomutilises.xml"));
			List<Element> noms = noeud.getChildren("nom");
			
			for (int i = 0; i < noms.size(); i++) {
				ajouterElement(noms.get(i).getText(), new Chargement(noms.get(i).getText()));
			}
			
		} catch (Exception e) {
			System.err.println("Erreur chargement liste de noms");
			e.printStackTrace();
		}
	
		
	}
	
	private class Chargement implements ActionMenu {

		String nom;
		
		public Chargement(String nom) {
			this.nom=nom;
		}
		
		@Override
		public void actionMenu() {
			Joueur.EnregistrerJoueur(Joueur.session);
			Joueur.chargerJoueur(nom).setSessionCourante();
			String[] annonces = new String[1];
			annonces[0]= "Vous êtes "+Joueur.session.getNom();
			annulerEnnoncer(annonces);

		
		}
		
	}

	

}
