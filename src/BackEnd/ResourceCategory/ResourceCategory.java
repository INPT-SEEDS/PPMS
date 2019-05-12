package BackEnd.ResourceCategory;

public class ResourceCategory
{
    int id;
    String ref;
    String label;

    public ResourceCategory(int id, String ref, String label)
    {
        this.id = id;
        this.ref = ref;
        this.label = label;
    }

    public int getId()
    {
        return id;
    }

    public String getRef()
    {
        return ref;
    }

    public String getLabel()
    {
        return label;
    }
}
