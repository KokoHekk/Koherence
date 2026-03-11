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
public class Functions {
    private static final Random RANDOM = new Random();

    /**
     * Provides a random, slightly varying loop hint for the player.
     */
    public static Supplier<String> subtleDejaVuSupplier() {
        return () -> {
            int resets = ResetCounter.getInstance().getResetCount();
            if (resets == 0) {
                return "";
            } else if (resets < 3) {
                return "For a moment, the way the light hits the floor feels oddly familiar.";
            } else if (resets < 10) {
                return "Your stomach twists. You've seen this morning before. Haven't you?";
            } else {
                return "Every word, every step feels like a script you've repeated too many times.";
            }
        };
    }

    public static Supplier<String> caretakerMoodSupplier() {
        return () -> {
            int resets = ResetCounter.getInstance().getResetCount();
            if (resets == 0) {
                return "\"Good morning, Ko,\" she says, voice warm and honey‑soft.";
            } else if (resets < 3) {
                return "\"Good morning again, Ko,\" she says, a tiny pause on the word \"again\".";
            } else if (resets < 10) {
                return "She smiles, but the corners of her eyes look tired. \"Morning, Ko.\"";
            } else {
                return "Her smile is thin now, stretched over something sharp. She reaches out, combing through your hair. \"Morning.\"";
            }
        };
    }

    public static Supplier<String> innerUneaseSupplier() {
        return () -> {
            int resets = ResetCounter.getInstance().getResetCount();
            if (resets < 2) return "";
            if (resets < 6) {
                return "You tell yourself it's just a strange dream you can't quite remember.";
            } else if (resets < 12) {
                return "You try to shake off the feeling. The room feels staged, like it's waiting for your lines.";
            } else {
                return "You know something is wrong, even if you can't say what. The feeling sits in your chest like a stone.";
            }
        };
    }
}