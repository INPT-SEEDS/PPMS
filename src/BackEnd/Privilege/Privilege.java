package BackEnd.Privilege;

public class Privilege
{
    private int id;
    private String name;

    public Privilege(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }
}
