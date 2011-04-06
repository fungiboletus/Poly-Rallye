package polyrallye.modele.voiture;

import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

import polyrallye.ouie.liseuse.Liseuse;

public class Sources
{
	protected List<URI> sources;
	
	public Sources(Element noeud)
	{
		sources = new ArrayList<URI>();
		
		for (Object s : noeud.getChildren())
		{
			try {
				sources.add(new URI(((Element) s).getText()));
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void naviguer(URI u)
	{
		if (u == null){
			if (sources.size() == 0)
			{
				return;
			}
			u = sources.get(0);
		}
		try {
			Desktop.getDesktop().browse(u);
		} catch (Exception e) {
			e.printStackTrace();
			Liseuse.lire("Impossible d'aller sur le web.");
		}
	}

	public List<URI> getSources() {
		return sources;
	}
}
