import java.util.Comparator;

/**
 * Value object that represents a player's story progress.
 * Also has a Comparator for sorting by progress (highest first).
 */
public record PlayerProgress(String playerName, int progress)
{
    /**
     * Comparator that orders PlayerProgress by progress (descending).
     * This gives an alternate sort order without changing the record itself.
     */
    public static final Comparator<PlayerProgress> BY_PROGRESS_DESC = (a, b) -> Integer.compare(b.progress(), a.progress());
}
