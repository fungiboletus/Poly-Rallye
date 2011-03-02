package polyrallye.modele;

public class Voiture extends Tuple
{
	protected String nom;
	protected String constructeur;
	
	protected int rarete;
	protected int prix;
	
	protected int debutDiffusion;
	protected int finDiffusion;
	
	protected Moteur moteur;
	
	protected Transmission transmission;
	
	protected Chassis chassis;
	
	protected Sources sources;
}
