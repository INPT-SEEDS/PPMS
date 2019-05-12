package BackEnd.ProjectType;

import BackEnd.Queries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectTypeQueries
{
    public static void addToDatabase(int id,String ref,String label)
    {
        Queries.insertInto("typeprojet",id+",'"+ref+"','"+label+"'");
    }

    public static ResultSet getResultSet()
    {
        return Queries.getResultSet("typeprojet");
    }

    public static ProjectType getProjectTypeById(int id)
    {
        ResultSet rs=Queries.getResultSetWhere("typeprojet","*","id="+id);
        try
        {
            if(rs.next())
            {
                String ref=rs.getString(2);
                String label=rs.getString(3);
                return new ProjectType(id,ref,label);
            }
        }
        catch (SQLException e)
        {e.printStackTrace();}

        return null;
    }

    public static List<String> getProjectTypesRef()
    {
        List<String> projectsRef=new ArrayList<>();
        ResultSet rs=Queries.getResultSet("typeprojet");
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

    public static void update(int id,String ref,String label)
    {
        Queries.modifyCell("typeprojet","libelle",label,"id="+id);
        Queries.modifyCell("typeprojet","reference",ref,"id="+id);
    }
}
