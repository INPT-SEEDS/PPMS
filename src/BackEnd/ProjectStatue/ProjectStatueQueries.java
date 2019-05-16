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
        delete(idProject);
        Queries.insertInto("projetetat",idProject+",'"+statue+"',"+evaluationsCount+",'"+date+"'");
    }

    public static List<ProjectStatue> getProjectStatue()
    {
        List<ProjectStatue> projectsStatue=new ArrayList<>();
        ResultSet rs= Queries.getResultSet("projetetat","*");
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

    public static String getProjectStatue(int idProject)
    {
        ResultSet rs= Queries.getResultSetWhere("projetetat","idprojet,etat,NombreEvaluation,max(date)","idProjet="+idProject+" group by idprojet");
        try
        {
            if (rs.next())
            {
                return rs.getString(2);
            }

        }
        catch (SQLException e)
        {e.printStackTrace();}

        return null;
    }

    public static int getProjectEvaluationCount(int idProject)
    {
        ResultSet rs= Queries.getResultSetWhere("projetetat","NombreEvaluation","idProjet="+idProject);
        try
        {
            if(rs.next())
            {
                return rs.getInt(1);
            }

        }
        catch (SQLException e)
        {e.printStackTrace();}
        return 0;
    }

    public static void delete(int idProject)
    {
        Queries.deleteRow("projetetat","idProjet="+idProject);
    }
}