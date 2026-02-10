/**
 * Rooms and Interactions
 */

// Interface for interactable objects
interface Interactive
{
    void interact(Player player);
}

// Abstract base for all rooms
abstract class BaseRoom implements GameObject
{
    protected String name;
    protected String description;

    public BaseRoom(String name, String description)
    {
        this.name = name;
        this.description = description;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getDescription()
    {
        return description;
    }
}

// Time loop room
class LoopRoom extends BaseRoom implements Interactive
{
    public LoopRoom(String name, String description)
    {
        super(name, description);
    }

    @Override
    public void interact(Player player)
    {
        System.out.println("========\n*The world around you fades to black...*\n========\n" + player.getStatus());
    }
}