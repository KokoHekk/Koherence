/**
 * Singleton for tracking total resets across runs.
 */
public final class ResetCounter {

    private static final ResetCounter INSTANCE = new ResetCounter();

    private int resetCount;
    private final DerbyManager derby;

    private ResetCounter() {
        this.derby = new DerbyManager();
        derby.initializeDatabase();
        this.resetCount = derby.readLoopCount(); // reuse column as reset count
    }

    public static ResetCounter getInstance() {
        return INSTANCE;
    }

    public int getResetCount() {
        return resetCount;
    }

    public void incrementReset() {
        resetCount++;
        derby.updateLoopCount(resetCount);
    }

    public void clearResets() {
        resetCount = 0;
        derby.updateLoopCount(0);
    }
}