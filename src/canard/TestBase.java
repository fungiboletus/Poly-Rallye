package canard;

/**
 * Abstract base class for tests.
 *
 * Contains physics properties like the number of bodies to generate, number of iterations per step,
 * etc. Both engines use the same values to get comparable values. That is, as far as it is possible
 * to call values comparable without checking the source of both in detail. 10 iterations in one
 * engine may mean something different than 10 in the other. Both are based on the same
 * concept, though. Thus it is assumed they mean the same.
 * 
 * @author Ciardhubh
 */
public abstract class TestBase {

    /** # of dynamic bodies that will be spawned. */
    protected int numberOfDynamicBodies = 1;
    /** Determines which form of dynamic bodies to use. */
    protected int bodyShape = 0;
    /** Number of iterations calculated per step. */
    protected static final int ITERATIONS_PER_STEP = 10;
    /** Time simulated in one step. */
    protected static final float TIME_PER_STEP = 1.0f / 60.0f;
    /** Gravity in y direction. */
    protected static final float Y_GRAVITY = 10.0f;

    /**
     * Creates a new test.
     *
     * @param noOfBodies # of dynamic bodies to spawn.
     * @param bodyShape Shape of bodies to spawn. Has to be one of GenericBody's shape constants.
     */
    public TestBase(int noOfBodies, int bodyShape) {
        this.numberOfDynamicBodies = noOfBodies;
        this.bodyShape = bodyShape;
    }

    /**
     * Calls step() in the used engine to simulate one physics step. The time this method takes is
     * measured in performance tests.
     */
    protected abstract void step();

}