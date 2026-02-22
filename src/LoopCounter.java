/**
 * Singleton for tracking loops
 */

public final class LoopCounter
{
    private static final LoopCounter INSTANCE = new LoopCounter();

    // Current loop number
    private int loopCount;
    private final DerbyManager derby;

    private LoopCounter()
    {
        this.derby = new DerbyManager();
        derby.initializeDatabase();
        this.loopCount = derby.readLoopCount();
    }

    public static LoopCounter getInstance()
    {
        return INSTANCE;
    }

    public int getLoopCount()
    {
        return loopCount;
    }

    public void incrementLoop()
    {
        loopCount++;
        derby.updateLoopCount(loopCount);
        System.out.println("\"It feels like I've been here before...\" (" + loopCount + ")");
    }

    /*
     * Thoughts on the performance using a Singleton:
     * - No synchronization overhead at runtime.
     * - Created at class load time (with only a tiny memory cost).
     * - Perfect for a single time loop tracker.
     */
}
