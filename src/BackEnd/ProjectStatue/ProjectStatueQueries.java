package BackEnd.ProjectStatue;

import BackEnd.Queries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectStatueQueries
{
    public static void addToDatabase(int idProject,String statue,int evaluationsCount,String date)
    {
        Queries.insertInto("projetetat",idProject+",'"+statue+"',"+evaluationsCount+",'"+date+"'");
    }

    public static List<ProjectStatue> getProjectStatue()
    {
        List<ProjectStatue> projectsStatue=new ArrayList<>();
        ResultSet rs= Queries.getResultSetAdvanced("projetetat","idprojet,etat,NombreEvaluation,max(date)","group by idprojet");
        try
        {
            while(rs.next())
            {
                int idProject=rs.getInt(1);
                String statue=rs.getString(2);
                int evaluationsCount=rs.getInt(3);
                projectsStatue.add(new ProjectStatue(idProject,statue,evaluationsCount));
            }

        }
        catch (SQLException e)
        {e.printStackTrace();}

        return projectsStatue;
    }
}
