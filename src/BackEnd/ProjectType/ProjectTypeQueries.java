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
}
