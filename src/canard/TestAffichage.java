package canard;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.shapes.Circle;
import net.phys2d.raw.BasicJoint;
import net.phys2d.raw.StaticBody;
import net.phys2d.raw.World;
import net.phys2d.raw.Joint;
import net.phys2d.raw.shapes.Box;
import net.phys2d.raw.shapes.Shape;

import org.lwjgl.opengl.GL11;

/**
 * Display test of Phys2D. Creates a world, runs the simulation with step() and draws the current
 * state with render() to a window.
 *
 * Can be run via main(). Runs until stopped by the user.
 *
 * @author Ciardhubh
 */
public class TestAffichage extends DisplayTest {

    /** The physics world. */
    protected World world;

    /**
     * Runs the test in a 800*600 window, with 100 circles.
     *
     * @param args Ignored
     */
    
    public static Body roueA;
    public static Body caisse;
    public static int i = 0;
    public static void main(String[] args) {
    	World monde = new World(new Vector2f(0.0f, 9.81f), 10);
		
		
		StaticBody sol = new StaticBody("sol", new Box(1000, 1));
		sol.setPosition(0, 10);
		//sol.setRestitution(1.0f);
		sol.setFriction(0.6f);
		monde.add(sol);
		
		caisse = new Body("caisse", new Box(4f, 0.2f), 1165);
		caisse.setPosition(4, 7.2f);
		//caisse.setTorque(5.0f);
		//caisse.setRestitution(0.5f);
		monde.add(caisse);
		//caisse.setForce(10, 0.0f);
		Vector2f positionRoueA = new Vector2f(2.5f, 8f);
		Vector2f positionRoueB = new Vector2f(5.5f, 8f);
		
		roueA = new Body("roue avant", new Circle(0.42f), 10);
		roueA.setPosition(positionRoueA.x, positionRoueA.y);
		monde.add(roueA);
		roueA.setFriction(0.8f);
		
		roueA.setTorque(0.01f);
		
		Body roueB = new Body("roue arrière", new Circle(0.42f), 10);
		roueB.setPosition(positionRoueB.x, positionRoueB.y);
		monde.add(roueB);
		
		//roueB.adjustAngularVelocity(100.0f);
		
		BasicJoint fixationA = new BasicJoint(caisse, roueA, positionRoueA);
		fixationA.setRelaxation(0.0f);
		monde.add(fixationA);
		
		BasicJoint fixationB = new BasicJoint(caisse, roueB, positionRoueB);
		fixationB.setRelaxation(0.0f);
		monde.add(fixationB);
    	
        new TestAffichage("Phys2D Test", 640, 480, 2, 0, monde).runTest();
    }

    public TestAffichage(String title, int windowWidth, int windowHeight, int noOfBodies,
            int shape, World monde) {
        super(title, windowWidth, windowHeight, noOfBodies, shape);
        world = monde;//Phys2DWorldLoader.getWorld(noOfBodies, shape, Y_GRAVITY, ITERATIONS_PER_STEP, true);
    }

    public static float canard = 0;
    protected void step() {
        //world.step(1/600.0f);
    	System.out.println(roueA.getAngularVelocity());
    	//System.out.println(canard);
    	System.out.println(((int)(caisse.getVelocity().getX()*3.6))+" km/h");
    	//System.out.println(roueA.getBiasedAngularVelocity());
    	//System.out.println();
    	world.step();
    	
    	if (i++>= 200  && i < 300) {
    		roueA.adjustAngularVelocity(canard);
    		canard += 0.5f;
    		//roueA.adjustAngularVelocity((float)(25.0f*2*Math.PI));
    	} else if (i++ >= 400) {
    		roueA.adjustAngularVelocity(-roueA.getAngularVelocity());
    	}
    }

    /**
     * Loops through all bodies and draws them according to their shape using OpenGL calls.
     */
    @Override
    protected void render() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        final float lala = 3.5f;
        // Bodies
        GL11.glColor3f(1, 1, 1);
        for (int i = 0; i < world.getBodies().size(); i++) {
            Body body = world.getBodies().get(i);
            Shape shape = body.getShape();

            if (shape instanceof Box) {
                Vector2f[] vertices = ((Box) shape).getPoints(body.getPosition(), body.getRotation());
                GL11.glBegin(GL11.GL_LINE_LOOP);
                for (int m = 0; m < vertices.length; m++) {
                    GL11.glVertex2f(vertices[m].x*lala, vertices[m].y*lala);
                }
                GL11.glEnd();

            } else if (shape instanceof Circle) {
                float radius = ((Circle) shape).getRadius()*lala;
                float x = body.getPosition().getX()*lala;
                float y = body.getPosition().getY()*lala;
                float vectorX = x;
                float vectorY = y + radius;
                GL11.glBegin(GL11.GL_LINE_LOOP);
                for (double angle = 0.0d; angle <= (2.0d * Math.PI); angle += 0.4d) {
                    GL11.glVertex2f(vectorX, vectorY);
                    vectorX = x + (radius * (float) Math.sin(angle));
                    vectorY = y + (radius * (float) Math.cos(angle));
                }
                GL11.glEnd();

            } else {
                throw new IllegalArgumentException("Unrecognised body shape.");
            }
        }

        // Joints
        GL11.glColor3f(0, 1, 0);
        for (int i = 0; i < world.getJoints().size(); i++) {
            Joint joint = world.getJoints().get(i);
            GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex2f(joint.getBody1().getPosition().getX()*lala,
                    joint.getBody1().getPosition().getY()*lala);
            GL11.glVertex2f(joint.getBody2().getPosition().getX()*lala,
                    joint.getBody2().getPosition().getY()*lala);
            GL11.glEnd();
        }
    }
}

