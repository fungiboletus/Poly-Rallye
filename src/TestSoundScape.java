import java.util.Random;

import org.lwjgl.openal.AL10;

import polyrallye.ouie.SoundScape;


public class TestSoundScape {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		SoundScape.create();
		
		int m = SoundScape.loadSoundData("Ressources/Reno Project - 1.0/test.ogg");
		
		int s = SoundScape.makeSoundSource(m);
		
		SoundScape.play(s);
		
		float x = -100.0f;
		
		float vitesse = 1.0f;
		
		while (true)
		{
			vitesse += (0.5-new Random().nextFloat())/3;
			
			SoundScape.setSoundPosition(s, x, 0.0f, 0.0f);
			SoundScape.setVelocity(s, vitesse, 0.0f, 0.0f);
			
			// Note Ça marche putain de bien
			AL10.alDopplerFactor(50.0f);
			
			x += vitesse;
			Thread.sleep(100);
		}
	}

}
