/**
 * Interface for player actions
 */

public interface PlayerActions
{
    // Move player
    void move(String direction);

    // Look around the room
    void look();

    // Pick up item
    void takeItem(String itemName);

    // Use item
    void useItem(String itemName);

    // Player status
    String getStatus();
}