package polyrallye.modele;

import java.awt.Desktop;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

import polyrallye.ouie.Liseuse;

public class Sources
{
	protected List<String> sources;
	
	public Sources(Element noeud)
	{
		sources = new ArrayList<String>();
		
		for (Object s : noeud.getChildren())
		{
			sources.add(((Element) s).getText());
		}
	}
	
	public void naviguer()
	{
		try {
			Desktop.getDesktop().browse(new URI(sources.get(0)));
		} catch (Exception e) {
			e.printStackTrace();
			Liseuse.lire("Impossible d'aller sur le web.");
		}
	}
}
