import polyrallye.utilitaires.Multithreading;
import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.StaticBody;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Box;




public class TestPhys2D{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		World monde = new World(new Vector2f(0.0f, -9.81f), 10);
		
		
		StaticBody sol = new StaticBody("sol", new Box(1800, 1));
		sol.setPosition(0, 0);		
		monde.add(sol);
		
		Body caisse = new Body("caisse", new Box(100, 1), 1000);
		caisse.setPosition(50, 1);
		monde.add(caisse);
		caisse.setForce(10, 0.0f);
		
		while (true) {
			monde.step(5/60f);
			afficherObjet("caisse", caisse);
			//afficherObjet("sol", sol);
			//System.out.print("X : "+ caisse.getPosition().getX());
			//System.out.print("  Y : "+ caisse.getPosition().getY());
			//System.out.println("  R : "+ sol.getRotation());
			//System.out.println(sol.getVelocity().getY());
			Multithreading.dormir(150);
		}
	}
	
	public static void afficherObjet(String nom, Body obj) {
		System.out.print(nom);
		System.out.print("\tX : "+ obj.getPosition().getX());
		System.out.println(" Y : "+ obj.getPosition().getY());
	}

}
