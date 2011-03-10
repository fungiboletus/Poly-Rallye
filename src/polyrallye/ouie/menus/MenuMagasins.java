package polyrallye.ouie.menus;

import java.util.Map;
import java.util.Map.Entry;

import polyrallye.modele.StockVoitures;
import polyrallye.modele.Voiture;
import polyrallye.ouie.ActionMenu;
import polyrallye.ouie.Liseuse;
import polyrallye.ouie.Menu;

public class MenuMagasins extends Menu implements ActionMenu {
	
	public MenuMagasins(Menu menuPrecedent)
	{
		this(menuPrecedent, StockVoitures.getHierarchie());
	}
	
	public MenuMagasins(Menu menuPrecedent, Map<String, Object> hierarchie) {
		super(menuPrecedent);
		
		for (Entry<String, Object> c : hierarchie.entrySet()) {
			Object v = c.getValue();
			
			System.out.println(v);
			
			if (v instanceof Voiture)
			{				
				ajouterElement(c.getKey(), new MenuVoitureMagasin(this, (Voiture) v));
			}
			else if (v instanceof Map<?, ?>)
			{
				// C'est parfois franchement très très moche le java
				@SuppressWarnings("unchecked")
				Map<String,Object> v2 = (Map<String,Object>) v;
				ajouterElement(c.getKey(), new MenuMagasins(this, v2));
			}
		}
	}

	@Override
	public void actionMenu() {
		Liseuse.lire("Vous pouvez acheter de nombreuses voitures dans les magasins.");

		lancer();
	}

}
