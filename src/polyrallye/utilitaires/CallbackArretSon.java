package polyrallye.utilitaires;

/**
 * @author Antoine Pultier
 * 
 *         Interface qui défini la méthode à utiliser pour savoir si il faut
 *         continuer à lire un son.
 */
public interface CallbackArretSon {

	/**
	 * Méthode appelée pour savoir si il faut continuer à lire un son.
	 * 
	 * @return Vrai si le son doit toujours être lu.
	 */
	public boolean continuerLecture();
}
