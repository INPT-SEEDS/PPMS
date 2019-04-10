package BackEnd.Resource;

public class AssignedResource
{
    private int idResource;
    private String label;
    private int quantity;

    public AssignedResource(int idResource, String label, int quantity)
    {
        this.idResource=idResource;
        this.label=label;
        this.quantity=quantity;
    }

    public int getIdResource()
    {
        return idResource;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public String getLabel()
    {
        return label;
    }
}
