package polyrallye.modele;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

public class Garage {
	protected List<Voiture> voitures;
	protected boolean changement = false;

	public Garage(Element noeud) {
		voitures = new ArrayList<Voiture>();

		if (noeud != null) {
			for (Object e : noeud.getChildren()) {
				Voiture v = StockVoitures.getVoitureParNom(((Element) e)
						.getText());

				if (v != null) {
					voitures.add(v);
				}
			}
		}
	}

	public Element toXML() {
		Element noeud = new Element("garage");

		for (Voiture v : voitures) {
			Element n = new Element("voiture");
			n.setText(v.getNom());

			noeud.addContent(n);
		}

		return noeud;
	}

	public List<Voiture> getVoitures() {
		return voitures;
	}

	public void acheter(Voiture v) throws Exception {
		if (voitures.contains(v)) {
			throw new Exception("Voiture déjà présente dans le garage");
		}
		voitures.add(v);
		changement = true;
	}

	public int vendre(Voiture v) throws Exception {
		if (!voitures.contains(v)) {
			throw new Exception("Voiture non présente dans le garage");
		}
		voitures.remove(v);
		changement = true;
		return (int) (v.getPrix() * 0.7);
	}

	public boolean verifierChangement() {
		boolean temp = changement;
		changement = false;
		return temp;
	}
}
