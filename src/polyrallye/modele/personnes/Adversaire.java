package polyrallye.modele;

public class Adversaire extends Personne
{
        protected Voiture voiture;
        
	public Adversaire(String nom) {
		super(nom);
		// TODO Auto-generated constructor stub
	}

    public Voiture getVoiture() {
        return voiture;
    }

    public void setVoiture(Voiture voiture) {
        this.voiture = voiture;
    }

	
}
