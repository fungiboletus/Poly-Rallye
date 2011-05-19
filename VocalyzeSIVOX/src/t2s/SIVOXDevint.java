
package t2s;

import t2s.newProsodies.Analyser;
import t2s.prosodie.Phoneme;
import t2s.son.*;
import t2s.util.*;

import java.io.*;
import java.util.Vector;

/** Une A.P.I. pour utiliser la synth�se vocale dans les projets DeViNT
 * @author Ecole Polytechnique Universitaire Nice Sophia Antipolis
 */
public class SIVOXDevint {
    private JukeBox jk; // pour jouer les wav
    static private LecteurTexte lt = new LecteurTexte(); // pour choisir une voix
    static private SynthetiseurMbrola s;
    static private Analyser an;
    static public boolean sjoue;
    static private  boolean interruptible=true;
    
    
    private boolean on; // true/false pour valider/invalider la synth�se SIVOX
    private int prosodie; // code la prosodie utilis�e, de 1 � 3 (3 par d�faut)
    
    /**
     * Constructeur par d�faut : voix de Thierry
     * prosodie = 3, la plus performante
     */
    public SIVOXDevint() {
    	jk = null;
    	sjoue = false;
    	on = true;
    	lt.setVoix(1);
    	prosodie = 3;
    }

    /**
     * Constructeur pour fixer la voix
     * @param voix, de 1 � 7 pour fr1, fr2, ... fr7
     */
    public SIVOXDevint(int voix) {
    	int vox;
    	int nbvoix= Integer.parseInt(ConfigFile.rechercher("NBVOIX")); // nombre de voix disponibles
    	System.out.println(nbvoix);
    	jk = null;
    	sjoue = false;
    	on = true;
    	prosodie = 3; // la meilleure prosodie
    	vox =(voix > nbvoix) ? nbvoix : voix;
    	vox =(voix < 1) ? 1 : voix;
        lt.setVoix(vox);
    }

    /**
     * Pour lire un texte long � voix haute en boucle
     * @param text, cha�ne � lire � voix haute
     */
    public void loopText(String text) {
    	play(text, true);
    }
    
    /**
     * Pour lire le son d'un fichier .wav en boucle
     * @param fichier nom du fichier (wave) � lire
     */
    public void loopWav(String fichier) {
		jk = new JukeBox(fichier);
		jk.loopSound();
    }
    
    /**
     * Pour lire un texte court (sans ponctuation) � voix haute
     * @param text, cha�ne de caract�res � lire � voix haute
     */

    public void playShortText(String text) {
    	if ( !on ) return;
	an = new Analyser(text, prosodie);
	Vector<Phoneme> listePhonemes = an.analyserGroupes();
		s = new SynthetiseurMbrola(lt.getVoix(), 
			ConfigFile.rechercher("REPERTOIRE_PHO_WAV"), 
			ConfigFile.rechercher("FICHIER_PHO_WAV"));
	s.play();
	sjoue=true;
    }
    
    /**
     * Pour lire un texte long � voix haute
     * @param text, cha�ne � lire � voix haute
     */
    public void playText(String text) {
	play(text, false);
    }
    
    
    /**
     * Pour lire le son d'un fichier .wav
     * @param fichier wave � lire
     */
    public void playWav(String fichier) {
    	jk = new JukeBox(fichier);
		jk.playSound();
    }
  
    /**
     * Pour fixer la prosodie utilis�e
     * @param p, entier de 1 � 3
     */
    public void setProsodie(int p) {
    	int pro;
    	pro = (p<1)?1:p;
    	pro = (p>3)?3:p;
    	prosodie = pro;
    }
    
    public int getProsodie() {
    	return prosodie;
    }
    
    /**
     * Pour fixer la voix utilis�e si la synth�se parle
     * @param voix, de 1 � 7
     */
    public void setVoix(int voix) {
    	int vox;
    	int nbvoix= Integer.parseInt(ConfigFile.rechercher("NBVOIX")); // nombre de voix disponibles dans ressources
    	System.out.println(nbvoix); 
    	vox =(voix > nbvoix) ? nbvoix : voix;
    	vox =(voix < 1) ? 1 : voix;
		lt.setVoix(vox);
    }
    
    /**
     * Pour stopper la synth�se vocale et donc arr�ter le son en cours de lecture
     * on stoppe le jukebox jk, qui lit les sons wave, le lecteur texte lt, et la synth�se s
     */
    public void stop() {
    	if (!interruptible) return;
    	if (jk!=null) jk.stop();
    	if (lt!= null) lt.stop();
    	if (sjoue==true) {
    		sjoue=false;
    		s.stop();
    	}
    }
    
    // Pour basculer entre voix on / voix off
    public void toggle() {
		on = !on;
    }
    /**
     * renvoie l'�tat du toggle voix on/voix off
     * @return
     */
    public boolean getToggle(){
    	return on;
    }
    
    //pour cr�er des fichiers wave en silence
    public void muet(String text, String out) {
	//if ( !on ) return;
	an = new Analyser(text, prosodie);
	Vector<Phoneme> listePhonemes = an.analyserGroupes();
	String chainePho=an.afficher(listePhonemes);
	try {
	    FileWriter fw= new FileWriter(out+".pho");
	    fw.write(chainePho);
	    fw.close();
	} catch (Exception e) {
	    System.out.println("erreur cr�ation fichier phon�me.");}
		s = new SynthetiseurMbrola(lt.getVoix(),out,"");
	s.muet();
    }

    //appel� par loopText et playText avec valeur flagloop diff�rente
    public void play(String text, boolean flagloop) {
	if ( !on ) return;
	an = new Analyser(text, prosodie);
	Vector<Phoneme> listePhonemes = an.analyserGroupes();
	s = new SynthetiseurMbrola(lt.getVoix(),
				   ConfigFile.rechercher("REPERTOIRE_PHO_WAV"),
				   ConfigFile.rechercher("FICHIER_PHO_WAV"));
	//System.out.println("RAPIDITE: "+ConfigFile.rechercher("RAPIDITE"));
	if (flagloop) s.loop(); else s.play();
	sjoue=true;
    }
}
