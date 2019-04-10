package BackEnd.Project;

import BackEnd.Evaluate.Evaluate;
import BackEnd.Evaluate.EvaluateQueries;
import BackEnd.Queries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectQueries
{
    public static void addToDatabase(int id,String label,int idPor,int idType)
    {
        Queries.insertInto("projet",id+",'"+label+"',"+idPor+","+idType);
    }

    public static ResultSet getResultSet()
    {
        return Queries.getResultSet("projet");
    }

    public static Project getProjectById(int id)
    {
        ResultSet rs=Queries.getResultSetWhere("projet","*","id="+id);
        try
        {
            if(rs.next())
            {
                String label=rs.getString(2);
                int idPortfolio=rs.getInt(3);
                int idType=rs.getInt(4);
                return new Project(id,label,idPortfolio,idType);
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Project> getProjectsByPortfolio(int idPort)
    {
        List<Project> projects=new ArrayList<>();
        ResultSet rs=Queries.getResultSetAdvanced("projet","*","where idPortfeuille="+idPort+" order by id");
        try
        {
            while(rs.next())
            {
                int id=rs.getInt(1);
                String label=rs.getString(2);
                int idPortfolio=rs.getInt(3);
                int idType=rs.getInt(4);
                projects.add(new Project(id,label,idPortfolio,idType));
            }
            for(Project project:projects)
            {
                List<Evaluate> projectEvaluation= EvaluateQueries.getProjectEvaluation(project.getId());
                project.setProjectEvaluation(projectEvaluation);
            }

        }
        catch (SQLException e)
        {e.printStackTrace();}

        return projects;
    }

    public static void updateProject(int id,String label,int idPortfolio,int idType)
    {
        Queries.modifyCell("projet","libelle",label,"id="+id);
        Queries.modifyCell("projet","idTypeProjet",String.valueOf(idType),"id="+id);
        Queries.modifyCell("projet","idPortfeuille",String.valueOf(idPortfolio),"id="+id);

    }

    public static List<String> getProjectsRef()
    {
        List<String> projectsRef=new ArrayList<>();
        ResultSet rs=Queries.getResultSet("projet");
        try
        {
            while(rs.next())
            {
                projectsRef.add(rs.getString(2));
            }
        }
        catch (SQLException e)
        {e.printStackTrace();}

        return projectsRef;
    }
}
