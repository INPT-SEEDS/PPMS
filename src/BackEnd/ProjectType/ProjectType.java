package BackEnd.ProjectType;

public class ProjectType
{
    int id;
    String ref;
    String label;

    public ProjectType(int id, String ref, String label)
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
