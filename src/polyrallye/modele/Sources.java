package polyrallye.modele;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

public class Sources
{
	protected List<String> sources;
	
	public Sources()
	{
		
	}
	
	public Sources(Element noeud)
	{
		sources = new ArrayList<String>();
		
		for (Object s : noeud.getChildren())
		{
			sources.add(((Element) s).getText());
		}
	}
}
