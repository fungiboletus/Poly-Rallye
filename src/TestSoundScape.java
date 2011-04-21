import java.util.Random;

import org.lwjgl.openal.AL10;

import polyrallye.ouie.utilitaires.SoundScape;


public class TestSoundScape {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		//SoundScape.create();
		
		int m = SoundScape.loadSoundData("Ressources/Reno Project - 1.0/test.ogg");
		
		int s = SoundScape.makeSoundSource(m);
		
		SoundScape.setOffset(s, 120);
		
		SoundScape.play(s);
		
		float x = -10.0f;
		
		float vitesse = 0.10f;

		Random r = new Random();
		
		while (true)
		{
			vitesse += (0.51-r.nextFloat())/100;
			
			SoundScape.setSoundPosition(s, x, 0.0f, 0.0f);
			SoundScape.setVelocity(s, vitesse, 0.0f, 0.0f);
			
			// Note Ça marche putain de bien
			AL10.alDopplerFactor(50.0f);
			
			x += vitesse;
			
			System.out.println(x);
			Thread.sleep(100);
		}
	}

}
