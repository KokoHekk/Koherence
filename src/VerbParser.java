import java.util.ResourceBundle;

public class VerbParser {
    private static final ResourceBundle messages = ResourceBundle.getBundle("messages");

    public static String getMessage(String key) {
        return messages.getString(key);
    }

    public static String parseVerb(String input) {
        return input.toLowerCase();
    }
}

