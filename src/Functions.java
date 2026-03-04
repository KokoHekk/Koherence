import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * Functional helpers used to drive small game behaviors.
 */
public class Functions
{
    private static final Random RANDOM = new Random();

    /**
     * Supplier example:
     * Provides a random, slightly-varying loop hint for the player.
     */
    public static Supplier<String> loopHintSupplier()
    {
        List<String> hints = List.of(
                "You notice the same coffee stain on the table as yesterday.",
                "The clock ticks... but the hands haven't moved.",
                "Your phone buzzes with the exact same message as before.",
                "A strange feeling of deja vu washes over you."
        );
        return () -> hints.get(RANDOM.nextInt(hints.size()));
    }

    /**
     * Consumer example:
     * Applies a mood effect to the player (e.g., for future stats or UI).
     */
    public static Consumer<Player> moodConsumer(String mood)
    {
        return player -> System.out.println("\n" + player.getName() + " feels " + mood + ".");
    }

    /**
     * Predicate example:
     * Checks whether a room should reveal its secret based on loop count.
     */
    public static Predicate<Integer> secretRoomPredicate()
    {
        return loopCount -> loopCount >= 3;
    }

    /**
     * Function example:
     * Maps the current loop count to a short narrative description.
     */
    public static Function<Integer, String> loopMoodFunction()
    {
        return loopCount ->
        {
            if (loopCount == 0)
            {
                return "Everything feels normal.";
            } else if (loopCount < 3)
            {
                return "Something feels slightly off.";
            } else
            {
                return "Something is definitely going on here.";
            }
        };
    }

    /**
     * Unary Operator example:
     * Advances the loop count, with a soft cap to avoid overflow.
     */
    public static UnaryOperator<Integer> loopAdvanceOperator()
    {
        return current -> Math.min(current + 1, 999);
    }

    /**
     * Small demo for the functional interfaces.
     */
    public static void runFunctionalDemo()
    {
        Player player = new Player("Koko", "Lounging Room");
        int currentLoop = LoopCounter.getInstance().getLoopCount();

        // Supplier: get a random hint about the loop.
        String hint = loopHintSupplier().get();
        System.out.println("\n*" + hint + "*");

        // Function: describe how the world feels at this loop.
        String moodDescription = loopMoodFunction().apply(currentLoop);
        System.out.println("\n<" + moodDescription + ">");

        // Predicate: decide if a secret should be revealed.
        if (secretRoomPredicate().test(currentLoop))
        {
            System.out.println("\n<Wait, was that like that before?>");
        } else
        {
            System.out.println("\n\"The room seems ordinary...\"");
        }

        // Consumer: apply a mood effect to the player.
        moodConsumer("uneasy").accept(player);

        // Unary Operator: advance the loop count and persist it.
        int nextLoop = loopAdvanceOperator().apply(currentLoop);
        while (LoopCounter.getInstance().getLoopCount() < nextLoop)
        {
            LoopCounter.getInstance().incrementLoop();
        }
    }
}