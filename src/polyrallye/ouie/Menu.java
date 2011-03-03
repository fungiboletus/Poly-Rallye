package polyrallye.ouie;

import java.util.ArrayList;
import java.util.List;

public class Menu {

	protected List<String> libelles;
	protected List<Object> actions;
	
	protected int courant;
	
	public Menu()
	{
		libelles = new ArrayList<String>();
		actions = new ArrayList<Object>();
		
		courant = 0;
	}
	
	public void ajouterElement(String libelle, Object action)
	{
		libelles.add(libelle);
		actions.add(action);
	}
	
	public void suivant()
	{
		if (++courant == libelles.size())
		{
			courant = 0;
		}
		
		ennoncer();
	}
	
	public void precedent()
	{
		if (courant == 0)
		{
			courant = libelles.size();
		}
		
		--courant;
		
		ennoncer();
	}
	
	public void selectionner()
	{
		
	}
	
	public void ennoncer()
	{
		Liseuse.lire(libelles.get(courant));
	}
}
