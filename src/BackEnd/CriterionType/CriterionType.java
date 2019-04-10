package BackEnd.CriterionType;

public class CriterionType
{
    private int id;
    private String ref;
    private String label;

    CriterionType(int id,String ref,String label)
    {
        this.id=id;
        this.ref=ref;
        this.label=label;
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
