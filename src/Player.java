import java.util.Objects;

/**
 * Player class for PlayerActions
 */

public class Player implements PlayerActions, Comparable<Player>
{
    // Player name
    private String name;

    // Current room location
    private String currentRoom;

    public Player(String name, String startingRoom)
    {
        this.name = name;
        this.currentRoom = startingRoom;
    }

    @Override
    public void move(String direction)
    {
        System.out.println(name + " moves " + direction + " from " + currentRoom);
    }

    @Override
    public void look()
    {
        System.out.println(name + " looks around the " + currentRoom);
    }

    @Override
    public void takeItem(String itemName)
    {
        System.out.println(name + " takes the " + itemName);
    }

    @Override
    public void useItem(String itemName)
    {
        System.out.println(name + " uses the " + itemName);
    }

    @Override
    public String getStatus()
    {
        return name + " is in the " + currentRoom;
    }

    public String getName()
    {
        return name;
    }

    /**
     * Natural ordering for Player.
     * Sorts players by name A–Z.
     *
     * compareTo() defines the "default" sort order.
     * Sets use this when there is no Comparator given.
     */
    @Override
    public int compareTo(Player other)
    {
        return this.name.compareToIgnoreCase(other.name);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Player player)) return false;
        return Objects.equals(name, player.name);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name);
    }
}