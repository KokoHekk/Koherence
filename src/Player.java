/**
 * Player class for PlayerActions
 */

public class Player implements PlayerActions
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
        System.out.println(name + " looks around " + currentRoom);
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
}