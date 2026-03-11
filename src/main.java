import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.util.Scanner;

public class main {
    public static final Logger logger = LogManager.getLogger(main.class);
    private static final String SAVE_FILE = "koherence_save.dat";

    public static class GameState implements Serializable {
        String koName = "Ko"; // I know this isn't used, I meant to fix it but ran out of time
        String currentRoom = "Bedroom"; // I know this isn't used, I meant to fix it but ran out of time
        boolean hasGameBoy = false;
        boolean hasKey = false;
        boolean foundStudy = false;
        boolean badEndingReached = false;
        boolean hasGem = false;
        boolean[] dayCompleted = new boolean[8]; // I know this isn't used, I meant to fix it but ran out of time;
        int currentDay = 0;
        int withCaretakerScore = 0;
        int againstCaretakerScore = 0;
        int shelfCleanedResets = 0;
        boolean wasResetThisTurn = false;
    }

    public static void main(String[] args) {
        logger.info("====Koherence====");
        try {
            runMenu(new Scanner(System.in));
        } catch (Exception e) {
            logger.error("Error", e);
        } finally {
            logger.info("Shutdown.");
        }
    }

    private static void runMenu(Scanner scanner) {
        while (true) {
            System.out.println(VerbParser.getMessage("ascii_title")); // I know this looks bad, I just generated something for it just to make it look like a menu.
            boolean saveExists = new File(SAVE_FILE).exists();

            System.out.println(VerbParser.getMessage("menu.start"));
            if (saveExists) System.out.println(VerbParser.getMessage("menu.continue"));
            System.out.println(VerbParser.getMessage("menu.reset"));
            System.out.println(VerbParser.getMessage("menu.quit"));
            System.out.print(VerbParser.getMessage("menu.prompt"));

            String choice = scanner.nextLine().trim().toLowerCase();
            if (choice.equals("1") || choice.equals("start")) {
                if (saveExists) {
                    System.out.println(VerbParser.getMessage("menu.save_exists"));
                    continue;
                }
                GameState state = new GameState();
                startGameLoop(scanner, state);
            } else if ((choice.equals("2") || choice.equals("continue")) && saveExists) {
                GameState state = loadGameState();
                startGameLoop(scanner, state);
            } else if (choice.equals("3") || choice.equals("reset")) {
                handleFullReset(scanner);
            } else if (choice.equals("4") || choice.equals("quit")) {
                break;
            }
        }
    }

    private static void startGameLoop(Scanner scanner, GameState state) {
        logger.info("Day " + state.currentDay + " starting");

        while (!state.badEndingReached && state.currentDay < 8) {
            Lore.runDay(scanner, state);
        }

        if (state.currentDay >= 8) {
            triggerBadEnding(scanner, state);
        }
    }


    public static void saveGameState(GameState state) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(state);
        } catch (IOException e) {
            logger.error("Save failed", e);
        }
    }

    public static GameState loadGameState() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
            return (GameState) ois.readObject();
        } catch (Exception e) {
            logger.error("Load failed", e);
            return null;
        }
    }

    private static void handleFullReset(Scanner scanner) {
        System.out.println(VerbParser.getMessage("reset.confirm"));
        if (scanner.nextLine().trim().equalsIgnoreCase("Okay")) {
            new File(SAVE_FILE).delete();
            ResetCounter.getInstance().clearResets();
            System.out.println(VerbParser.getMessage("reset.success"));
        }
    }

    private static void triggerBadEnding(Scanner scanner, GameState state) {
        System.out.println(VerbParser.getMessage("bad_ending.title"));
        int resets = ResetCounter.getInstance().getResetCount();
        System.out.println(VerbParser.getMessage("bad_ending.resets") + resets);
    }
}