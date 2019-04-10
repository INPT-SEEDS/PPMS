package BackEnd.ProjectStatue;

public class ProjectStatue
{
    private int idProject;
    private String statue;
    private int evaluationsCount;

    ProjectStatue(int idProject,String statue,int evaluationsCount)
    {
        this.idProject=idProject;
        this.statue=statue;
        this.evaluationsCount=evaluationsCount;
    }

    public int getIdProject()
    {
        return idProject;
    }

    public String getStatue()
    {
        return statue;
    }

    public int getEvaluationsCount()
    {
        return evaluationsCount;
    }
}
