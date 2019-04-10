package BackEnd.Criterion;

import BackEnd.Queries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CriterionQueries
{
    public static void addToDatabase(int id,String ref,String label,String genre,int idType)
    {
        Queries.insertInto("critere",id+",'"+ref+"','"+label+"','"+genre+"',"+idType);
    }

    public static List<Criterion> getCriteria()
    {
        List<Criterion> criteriaList=new ArrayList<>();
        ResultSet rs=Queries.getResultSet("critere");
        try
        {
            while(rs.next())
            {
                int id=rs.getInt(1);
                String ref=rs.getString(2);
                String label=rs.getString(3);
                String genre=rs.getString(4);
                int idType=rs.getInt(5);
                criteriaList.add(new Criterion(id,ref,label,genre,idType));
            }
        }
        catch (SQLException e)
        {e.printStackTrace();}

        return criteriaList;
    }

    public static List<String> getCriteriaRef()
    {
        List<String> criteriaRef=new ArrayList<>();
        ResultSet rs=Queries.getResultSet("critere");
        try
        {
            while(rs.next())
            {
                criteriaRef.add(rs.getString(2));
            }
        }
        catch (SQLException e)
        {e.printStackTrace();}

        return criteriaRef;
    }

    public static ResultSet getResultSet()
    {
        return Queries.getResultSet("critere");
    }
}
