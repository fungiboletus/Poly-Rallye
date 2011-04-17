import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

public class UtilitaireZut {

	public static void main(String[] args)  {

	try {
		BufferedReader brMarqueurs = new BufferedReader(new FileReader("Paroles/marqueurs.txt"));
		BufferedReader brParoles = new BufferedReader(new FileReader("Paroles/paroles.txt"));
		
		
		String ligneMarqueurs;
		String ligneParoles;
		
		int i = 1;
		float ic = 0.0f;
		
		while ((ligneMarqueurs = brMarqueurs.readLine()) != null && (ligneParoles = brParoles.readLine()) != null){

			Scanner sc = new Scanner(ligneMarqueurs);
			
			if (i++ == 25)
			{
				ic = 1.286068f;
			}
			
			System.out.print((""+(sc.nextFloat()+ic)).replace(".", ","));
			System.out.print('\t');
			System.out.print((""+(sc.nextFloat()+ic)).replace(".", ","));
			System.out.print('\t');
			System.out.println(ligneParoles);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	}
}
