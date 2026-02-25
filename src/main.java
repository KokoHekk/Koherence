import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class main
{
    private static final Logger logger = LogManager.getLogger(main.class);

    public static void main(String[] args)
    {
        logger.info("====Koherence====");

        try
        {
            // Something that could fail
            startGame();
            sorting();
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

    private static void startGame()
    {
        // This will hold the main game loop
        // Will read player input, move between rooms, update inventory, and whatever else
        logger.info("Game loop starting...");

        // Testing for the new classes
        Player player = new Player("Koko", "Lounging Room");
        player.look();

        LoopRoom room = new LoopRoom("Lounging Room", "Everything feels familiar...");
        room.interact(player);

        LoopCounter.getInstance().incrementLoop();
    }

    /**
     * Examples that show the use of a Set and a Map when sorting objects.
     *
     * - TreeSet uses Player.compareTo() (Comparable) for natural ordering.
     * - Another TreeSet uses PlayerScore.BY_SCORE_DESC (Comparator) for custom ordering.
     * - TreeMap sorts entries by key.
     */
    private static void sorting()
    {
        // TreeSet with Comparable (Player)
        TreeSet<Player> playersByName = new TreeSet<>();
        playersByName.add(new Player("Mira", "Hallway"));
        playersByName.add(new Player("Rumi", "Kitchen"));
        playersByName.add(new Player("Zoe", "Library"));

        System.out.println("\nPlayers sorted by name (TreeSet + Comparable):");
        for (Player p : playersByName)
        {
            System.out.println(" - " + p.getName());
        }

        // TreeSet with Comparator (PlayerScore)
        TreeSet<PlayerProgress> progressByValue = new TreeSet<>(PlayerProgress.BY_PROGRESS_DESC);
        progressByValue.add(new PlayerProgress("Mira", 5));
        progressByValue.add(new PlayerProgress("Rumi", 12));
        progressByValue.add(new PlayerProgress("Zoe", 8));

        System.out.println("\nScores sorted by value (TreeSet + Comparator):");
        for (PlayerProgress pp : progressByValue)
        {
            System.out.println(" - " + pp.playerName() + ": " + pp.progress());
        }

        // TreeMap (Map sorted by key)
        TreeMap<Integer, Player> turnOrder = new TreeMap<>();
        turnOrder.put(3, new Player("Mira", "Hallway"));
        turnOrder.put(1, new Player("Rumi", "Kitchen"));
        turnOrder.put(2, new Player("Zoe", "Library"));

        System.out.println("\nTurn order sorted by key (TreeMap):");
        for (Map.Entry<Integer, Player> entry : turnOrder.entrySet())
        {
            System.out.println("Turn " + entry.getKey() + " -> " + entry.getValue().getName());
        }

        /*
         * NOTES:
         * - TreeSet keeps elements unique and sorted.
         *   This uses Comparable by default, or the Comparator provide.
         * - TreeMap keeps key/value pairs sorted by key.
         * - Comparable vs Comparator:
         *   Comparable (compareTo) - one natural ordering, defined in the class.
         *   Comparator (compare)   = external/custom ordering, can have many strategies.
         */
    }
}