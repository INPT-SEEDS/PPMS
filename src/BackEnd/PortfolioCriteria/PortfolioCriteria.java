package BackEnd.PortfolioCriteria;

public class PortfolioCriteria
{
    private int id;
    private int idPortfolio;
    private int weight;
    private String usage;

    public PortfolioCriteria(int id,int idPortfolio,int weight,String usage)
    {
        this.id=id;
        this.idPortfolio=idPortfolio;
        this.weight=weight;
        this.usage=usage;
    }

    public int getId()
    {
        return id;
    }
    public int getWeight()
    {
        return weight;
    }
}
