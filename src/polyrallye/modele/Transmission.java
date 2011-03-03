package polyrallye.modele;

import org.jdom.Element;

public class Transmission {
	protected int nbVitesses;

	protected TypeTransmission type;

	public Transmission() {

	}

	public Transmission(Element noeud) {
		nbVitesses = GestionXML.getInt(noeud.getChildText("nb_rapports"));

		String sTypeTransmission = noeud.getChildText("type").toUpperCase();

		if (sTypeTransmission.equals("TRACTION")) {
			type = TypeTransmission.TRACTION;
		} else if (sTypeTransmission.equals("PROPULSION")) {
			type = TypeTransmission.PROPULSION;
		} else {
			type = TypeTransmission.QUATTRO;
		}
	}
}
