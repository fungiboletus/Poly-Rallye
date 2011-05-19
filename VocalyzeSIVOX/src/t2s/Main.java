/*  
 * SI VOX Copyright (C) 2004 - 2005
 *
 * Author :
 *   ESSI2 school project (2004) : Affouard, Lemonnier, Fournols ,Lizzul
 *   Tutor                (2004) : H�l�ne Collavizza   [ helen@essi.fr    ] 
 *                                 Jean-Paul Stromboni [ strombon@essi.fr ]
 *
 * Contributor :
 *   (2004) : Louis Parisot [ parisot@essi.fr ]
 *   (2005) : S�bastien Mosser  [ mosser@essi.fr ]
 *
 * Institute : 
 *    Polytechnich school, University of Nice - Sophia Antipolis (FRANCE)
 *
 * This program is free software. It uses mbrola speech synthesizers system.
 * You can redistribute it and/or modify it under the terms of the MBROLA 
 * Licenses  { http://tcts.fpms.ac.be/synthesis/mbrola.html }.
 *
 */
package t2s;

//import t2s.son.*;
import t2s.util.*;
//import t2s.*;
import t2s.ihm.*;
import java.io.*;

/** La classe �x�cutable appel�e par le manifeste de l'archive auto �x�cutable
 */
public class Main{

	// La m�thode principale
	public static void main (String[] args){
		try {
			if(args.length == 0)
			{
				InterfaceGenerale i = new InterfaceGenerale();
				i.open();
			}
			else
			{
				if (args[0].equals("-ihm"))
				{
					InterfaceGenerale i = new InterfaceGenerale();
					i.open();
				}
				else if(args[0].equals("-config"))
				{
					ConfigFile.lister();
				}
				else if (args[0].equals("-f") && args.length == 2)
				{
					genererFichier(args[1], "");
				}
				else if (args[0].equals("-f") && args.length == 3)
				{
					genererFichier(args[1],args[2]);
				}
				else
				{
					usage();
					System.exit(0);
				}
			}
		}
		catch(Exception e){
			System.out.println("Une erreur est survenue !");
			e.printStackTrace();
			System.exit(0);
	}
    }


    /** Pour afficher l'aide
     */
    private static void usage(){
	System.out.println("SI_VOX : utilisation en ligne de commande");
	System.out.println("  java -jar SI_VOX.jar -option [FICHIERS]");
	System.out.println("  Options possibles : ");
	System.out.println("     -ihm             : lance l'interface graphique");
	System.out.println("     -f FICHIER       : Lit FICHIER a haute voix");
	System.out.println("     -f ENTREE SORTIE : g�n�re un fichier SORTIE.wav");
	System.out.println("     -config          : liste la configuration actuelle");
    }
    
	
    /** Pour generer le fichier wav sans le lire ...
     * @param in le nom du fichier a lire
     * @param out le nom souhait� pour le fichier de sortie (sans extension, le .wav est rajout�)
     */

//     private static void genererFichier(String in, String out) 
// 	throws Exception {
// 		SIVOXDevint vox= new SIVOXDevint();
// 		File f= new File(in);
// 		if(out == "")
// 		{
// 			vox.play(new LecteurFichier(f.toString()).toutLire(), false);
// 		}
// 		else
// 		{	
// 			vox.muet(new LecteurFichier(f.toString()).toutLire(), out);
// 		}
// 		System.exit(0);
//     }

    private static void genererFichier(String in, String out) 
	throws Exception {
	SIVOXDevint vox= new SIVOXDevint();
	File f= new File(in);
	vox.muet(new LecteurFichier(f.toString()).toutLire(), out);
	System.exit(0);
    }
}