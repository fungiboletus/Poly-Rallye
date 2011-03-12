import java.io.File;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;


public class TestSonSlick {

	/**
	 * @param args
	 * @throws SlickException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws SlickException, InterruptedException {
		File f = new File("Ressources/Reno Project - 1.0/02 - Atlanta.ogg");
		System.out.println(f.exists());
		Sound m = new Sound("Ressources/Reno Project - 1.0/test.ogg");
		m.playAt(5.0f,-1.0f,0);
		//m.setPosition(50.0f);
		//m.play(0.5f, 1.0f);
		
		
		
		Thread.sleep(10000);
	}

}
