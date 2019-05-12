package BackEnd.Evaluate;

public class Evaluate
{
    private int idProject;
    private int idCritere;
    private int weight;
    private int value;

    Evaluate(int idProject,int idCritere,int weight,int value)
    {
        this.idProject=idProject;
        this.idCritere=idCritere;
        this.weight=weight;
        this.value=value;
    }

    public int getIdCritere()
    {
        return idCritere;
    }

    public int getValue()
    {
        return value;
    }
}
