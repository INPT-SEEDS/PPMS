package BackEnd.Evaluate;

public class Evaluate
{
    private int idProject;
    private int idCritere;
    private int weight;
    private float value;

    Evaluate(int idProject,int idCritere,int weight,float value)
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

    public float getValue()
    {
        return value;
    }
}
