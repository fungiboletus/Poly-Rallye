package canard;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.input.Keyboard;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.util.glu.GLU;

/**
 * Base class for physics engine display tests. Initialises display and common settings. Provides a
 * common runtime framework.
 * 
 * First all required settings are set up. Afterwards the game loop is run. Various abstract methods
 * have to be implemented with engine-specific code; most notably step() and render().
 *
 * Uses the LWJGL to render via OpenGL. Therefor the LWJGL library jars have to be included in the
 * classpath and java.library.path has to be set to the native libraries of LWJGL.
 *
 * @author Ciardhubh
 */
public abstract class DisplayTest extends TestBase {

    /*
     * Properties of the display window. They are set by the constructors.
     */
    private final String windowTitle;
    private final int windowWidth;
    private final int windowHeight;
    private final int maxRefreshRate;
    private final int minBitsPerPixel;
    private final boolean vsync;
    private final boolean fullscreen;
    /*
     * Static default values for constructors with fewer parameters.
     */
    private static final String DEFAULT_TITLE = "LWJGL OpenGL";
    private static final int DEFAULT_WINDOW_WIDTH = 800;
    private static final int DEFAULT_WINDOW_HEIGHT = 600;
    private static final int DEFAULT_MAX_REFRESH_RATE = Display.getDisplayMode().getFrequency();
    private static final int DEFAULT_MIN_BITS_PER_PIXEL = Display.getDisplayMode().getBitsPerPixel();
    private static final boolean DEFAULT_VSYNC = false;
    private static final boolean DEFAULT_FULLSCREEN = false;

    /**
     * Constructor with default values.
     */
    public DisplayTest(int noOfBodies, int bodyShape) {
        this(DEFAULT_TITLE, DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT, noOfBodies, bodyShape);
    }

    /**
     * Contructor with custom window title and dimensions.
     */
    public DisplayTest(String windowTitle, int windowWidth, int windowHeight,
            int noOfBodies, int bodyShape) {
        this(windowTitle, windowWidth, windowHeight, DEFAULT_MAX_REFRESH_RATE,
                DEFAULT_MIN_BITS_PER_PIXEL, DEFAULT_VSYNC, DEFAULT_FULLSCREEN,
                noOfBodies, bodyShape);
    }

    /**
     * Fully customised constructor.
     */
    public DisplayTest(String windowTitle, int windowWidth, int windowHeight, int maxRefreshRate,
            int minBitsPerPixel, boolean vsync, boolean fullscreen,
            int noOfBodies, int bodyShape) {
        super(noOfBodies, bodyShape);
        this.windowTitle = windowTitle;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.maxRefreshRate = maxRefreshRate;
        this.minBitsPerPixel = minBitsPerPixel;
        this.vsync = vsync;
        this.fullscreen = fullscreen;
    }

    /**
     * Initialises everything and calls the run() method. Before exiting, cleanup() is called.
     */
    public void runTest() {
        try {
            initGl();
            run();
        } catch (LWJGLException ex) {
            Sys.alert(windowTitle, "An exception was thrown: " + ex.toString());
        } finally {
            Display.destroy();
        }
    }

    /**
     * Initialises the OpenGL display.
     */
    private void initGl() throws LWJGLException {
        // Set display mode and create display.
        DisplayMode[] modes = Display.getAvailableDisplayModes();
        for (DisplayMode m : modes) {
            if (m.getWidth() == windowWidth &&
                    m.getHeight() == windowHeight &&
                    m.getBitsPerPixel() >= minBitsPerPixel &&
                    m.getFrequency() <= maxRefreshRate) {
                Display.setDisplayMode(m);
            }
        }
        Display.setTitle(windowTitle);
        //Display.setFullscreen(fullscreen);
        Display.setVSyncEnabled(vsync);
        Display.create();

        // Initialises OpenGL drawing settings.
        GL11.glViewport(0, 0, windowWidth, windowHeight);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        float ratio = (float) windowWidth / (float) windowHeight;
        GLU.gluOrtho2D(0.0f, 100.0f * ratio, 100.0f, 0.0f);
    }

    /**
     * Does the actual work. Executed in an endless loop. Alternatingly calls step() and render() to
     * calculate a new state and render this state. It tries to synchronise to 60 FPS.
     *
     * If the display is not visible, physics simulation is stopped.
     */
    private void run() {
        while (!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && !Display.isCloseRequested()) {
            if (Display.isVisible()) {
                step();
                render();
                Display.sync(60);
            } else {
                if (Display.isDirty()) {
                    render();
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                }
            }
            Display.update();
        }
    }

    /**
     * Renders the scene.
     */
    protected abstract void render();
}