package BackEnd.ResourceCategory;

import BackEnd.Queries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResourceCategoryQueries
{
    public static void addToDatabase(int id,String ref,String label)
    {
        Queries.insertInto("categorie",id+",'"+ref+"','"+label+"'");
    }

    public static ResultSet getResultSet()
    {
        return Queries.getResultSet("categorie");
    }

    public static List<String> getCategoriesRef()
    {
        List<String> categoriesRefList=new ArrayList<>();
        ResultSet rs=getResultSet();
        try
        {
            while(rs.next())
            {
                String ref=rs.getString(2);
                categoriesRefList.add(ref);
            }
        }
        catch (SQLException e) { e.printStackTrace(); }
        return categoriesRefList;
    }
}
