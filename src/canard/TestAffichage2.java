package canard;

import org.jbox2d.collision.AABB;
import org.jbox2d.collision.CircleShape;
import org.jbox2d.collision.PolygonDef;
import org.jbox2d.collision.PolygonShape;
import org.jbox2d.collision.Shape;
import org.jbox2d.collision.ShapeDef;
import org.jbox2d.collision.ShapeType;
import org.jbox2d.common.Vec2;
import org.jbox2d.common.XForm;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

import org.lwjgl.opengl.GL11;

/**
 * Display test of JBox2D. Creates a world, runs the simulation with step() and draws the current
 * state with render() to a window.
 *
 * Can be run via main(). Runs until stopped by the user.
 *
 * @author Ciardhubh
 */
public class TestAffichage2 extends DisplayTest {

    /** World containing physics objects. */
    private World world;
    /** All bodies in the world. */
    private Body bodies;

    /**
     * Runs the test in a 800*600 window, with 100 circles.
     *
     * @param args Ignored
     */
    public static void main(String[] args) {
    	World monde = new World(new AABB(new Vec2(-50, -50), new Vec2(50,50)), new Vec2(0, -9.81f), true);
		
		PolygonDef pd = new PolygonDef();
		pd.setAsBox(3.948f, 2.0f);
		
		BodyDef solBodyDef = new BodyDef();
		solBodyDef.position.set(0.0f, -10.0f);
		Body solBody = monde.createBody(solBodyDef);
		PolygonDef solBoite = new PolygonDef();
		solBoite.setAsBox(3.948f, 2.0f);
		//solBody.fi
		/*StaticBody sol = new StaticBody("sol", new Box(100, 1));
		sol.setPosition(0, 10);
		//sol.setRestitution(1.0f);
		sol.setFriction(0.8f);
		monde.add(sol);
		
		Body caisse = new Body("caisse", new Box(3.948f, 2.0f), 1165);
		caisse.setPosition(3, 8.5f);
		//caisse.setTorque(5.0f);
		//caisse.setRestitution(0.5f);
		monde.add(caisse);
		//caisse.setForce(10, 0.0f);
		
		Body roue = new Body("roue avant", new Circle(0.42f), 10);
		roue.setPosition(3, 5);
		
		monde.add(roue);*/
    	
        new TestAffichage2("JBox2D Test", 800, 600, 100, 0, monde).runTest();
    }

    public TestAffichage2(String title, int windowWidth, int windowHeight, int noOfBodies,
            int shape, World monde) {
        super(title, windowWidth, windowHeight, noOfBodies, shape);
        world = monde;//JBox2DWorldLoader.getWorld(noOfBodies, bodyShape, Y_GRAVITY, true);
    }

    @Override
    protected void step() {
        world.step(TIME_PER_STEP, ITERATIONS_PER_STEP);
    }

    /**
     * Loops through all bodies and draws them according to their shape using OpenGL calls.
     */
    @Override
    protected void render() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        final float lala = 10;
        // Draw all bodies.
        GL11.glColor3f(1, 1, 1);
        bodies = world.getBodyList();
        while (bodies != null) {

        	
            // Draw all shapes of current body.
            Shape shapes = bodies.getShapeList();
            while (shapes != null) {

                if (shapes.getType() == ShapeType.POLYGON_SHAPE) {
                    GL11.glBegin(GL11.GL_LINE_LOOP);
                    Vec2 worldVertex;
                    for (Vec2 bodyVertex : ((PolygonShape) shapes).getVertices()) {
                        worldVertex = XForm.mul(bodies.getXForm(), bodyVertex);
                        GL11.glVertex2f(worldVertex.x*lala, worldVertex.y*lala);
                    }
                    GL11.glEnd();

                } else if (shapes.getType() == ShapeType.CIRCLE_SHAPE) {
                    float radius = ((CircleShape) shapes).getRadius()*lala;
                    float x = bodies.getPosition().x*lala;
                    float y = bodies.getPosition().y*lala;
                    float vectorX = x;
                    float vectorY = y + radius;
                    GL11.glBegin(GL11.GL_LINE_LOOP);
                    for (double angle = 0.0d; angle <= (2.0d * Math.PI); angle += 0.4d) {
                        GL11.glVertex2d(vectorX, vectorY);
                        vectorX = x + (radius * (float) Math.sin(angle));
                        vectorY = y + (radius * (float) Math.cos(angle));
                    }
                    GL11.glEnd();

                } else {
                    System.out.println("Unsupported shape.");
                }

                shapes = shapes.getNext();
            }

            bodies = bodies.getNext();
        }

        // Joints
        GL11.glColor3f(0, 1, 0);
        Joint currentJoint = world.getJointList();
        while (currentJoint != null) {
            GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex2f(currentJoint.getAnchor1().x*lala, currentJoint.getAnchor1().y*lala);
            GL11.glVertex2f(currentJoint.getAnchor2().x*lala, currentJoint.getAnchor2().y*lala);
            GL11.glEnd();
            currentJoint = currentJoint.getNext();
        }
    }
}