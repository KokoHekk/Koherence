import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class main
{
    private static final Logger logger = LogManager.getLogger(main.class);

    public static void main(String[] args) {
        logger.info("====Koherence====");

        try
        {
            // Something that could fail
            startGame();
        }
        catch (Exception e)
        {
            // Error handling
            logger.error("An unexpected error occurred", e);
        }
        finally
        {
            logger.info("Shutting down. Thanks for playing!");
        }
    }

    private static void startGame() throws Exception {
        // Later this will hold the main game loop:
        // Will read player input, move between rooms, update inventory, and whatever else
        logger.info("Game loop starting...");
    }
}