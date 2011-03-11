import java.util.Random;

import org.lwjgl.openal.AL10;

import polyrallye.ouie.Sound;

public class TestSoundPerso {

	public static void main(String[] args) throws InterruptedException {
		Sound m = new Sound("Ressources/Reno Project - 1.0/test.ogg");
		
		m.setOffset(120);
		m.play();
		
		float x = -10.0f;
		
		float vitesse = 0.10f;
		
		Random r = new Random();
		
		while (true)
		{
			vitesse += (0.51-r.nextFloat())/100;
		
			m.setPosition(x, 0.0f, 0.0f);
			m.setVelocity(vitesse, 0.0f, 0.0f);
			
			// Note Ça marche putain de bien
			AL10.alDopplerFactor(50.0f);
			
			x += vitesse;
			
			System.out.println(x);
			Thread.sleep(100);
		}
	}
}