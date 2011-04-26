package polyrallye.modele;

import org.jdom.Element;

public class Permis {

	boolean obtenu;

	public Permis() {
		obtenu = false;
	}

	public Permis(Element noeud) {
		this();

		if (noeud == null) {
			return;
		}

		obtenu = "oui".equals(noeud.getAttribute("obtenu"));
	}

	public boolean isObtenu() {
		return obtenu;
	}
}
