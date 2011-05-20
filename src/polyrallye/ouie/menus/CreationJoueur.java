package polyrallye.ouie.menus;

import java.io.File;
import java.util.List;

import org.jdom.Element;

import polyrallye.modele.personnes.Joueur;
import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Menu;
import polyrallye.ouie.liseuse.Liseuse;
import polyrallye.utilitaires.GestionXML;

public class CreationJoueur extends Menu implements ActionMenu{
	

	
	public CreationJoueur(Menu menuPrecedent) {
		super(menuPrecedent);
	}

	@Override
	public void actionMenu() {
		Liseuse.lire("Selectionnez un nom de joueur");
		lancer();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void remplir() {
		try {
			Element noeud = GestionXML.chargerNoeudRacine(new File("Comptes/nomsdispo.xml"));
			List<Element> noms = noeud.getChildren("nom");
			
			for (int i = 0; i < noms.size(); i++) {
				ajouterElement(noms.get(i).getText(), new Suppression(noms.get(i).getText()));
			}
			
		} catch (Exception e) {
			System.err.println("Erreur chargement liste de noms");
			e.printStackTrace();
		}
		
	}
	
	private class Suppression implements ActionMenu {

		String nom;
		
		public Suppression(String nom) {
			this.nom=nom;
		}
		
		@Override
		public void actionMenu() {
			
			try {
				Element racine = GestionXML.chargerNoeudRacine(new File("Comptes/nomsdispo.xml"));
				//Sinon ça ne marche pas
				Element trueRacine = new Element("noms");
				
				
				@SuppressWarnings("unchecked")
				List<Element> noms = racine.getChildren("nom");
				for (int i = 0; i < noms.size(); i++) {
					if(!noms.get(i).getText().equals(nom)) {
						Element temp = new Element("nom");
						temp.setText(noms.get(i).getText());
						trueRacine.addContent(temp);
					}
						
				}
				
				
				GestionXML.enregistrerRacine("Comptes/nomsdispo.xml", trueRacine);

				
			} catch (Exception e) {
				System.err.println("Erreur supression nom");
				e.printStackTrace();
			}
			
			try {
				Element racine = GestionXML.chargerNoeudRacine(new File("Comptes/nomutilises.xml"));
				//Sinon ça ne marche pas
				Element trueRacine = new Element("noms");
				
				
				Element nouveau = new Element("nom");
				nouveau.setText(nom);
				trueRacine.addContent(nouveau);
				
				for (Object cp : racine.getChildren("nom")) {
					Element temp = new Element("nom");
					temp.setText(((Element)cp).getText());
					trueRacine.addContent(temp);
				}
				
				GestionXML.enregistrerRacine("Comptes/nomutilises.xml", trueRacine);
				
			} catch (Exception e) {
				System.err.println("Erreur ajout nom");
				e.printStackTrace();
			}
			
			Joueur.EnregistrerJoueur(Joueur.session);
			Joueur.chargerJoueur(nom).setSessionCourante();
			
			String[] annonces = new String[1];
			annonces[0]= "Vous êtes "+Joueur.session.getNom();
			annulerEnnoncer(annonces);

		
		}
	}

}
