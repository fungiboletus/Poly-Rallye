package polyrallye.ouie;

public class Parole {

	protected float debut;
	
	protected float fin;
	
	protected String texte;

	public Parole(float debut, float fin, String texte) {
		super();
		this.debut = debut;
		this.fin = fin;
		this.texte = texte;
	}

	public float getDebut() {
		return debut;
	}

	public float getFin() {
		return fin;
	}

	public String getTexte() {
		return texte;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Parole [debut=");
		builder.append(debut);
		builder.append(", fin=");
		builder.append(fin);
		builder.append(", ");
		if (texte != null) {
			builder.append("texte=");
			builder.append(texte);
		}
		builder.append("]");
		return builder.toString();
	}
}
