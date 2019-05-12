package BackEnd.Resource;

import BackEnd.Queries;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResourceQueries
{
    public static void addToDatabase(int id,String label,int idCategory)
    {
        Queries.insertInto("ressourcetotale",id+",'"+label+"',"+idCategory);
    }

    public static Resource getResourceTypeById(int id)
    {
        ResultSet rs=Queries.getResultSetWhere("ressourcetotale","*","idRessource="+id);
        try
        {
            if(rs.next())
            {
                String label=rs.getString(2);
                int idType=rs.getInt(3);
                return new Resource(id,label,idType);
            }
        }
        catch (SQLException e)
        {e.printStackTrace();}

        return null;
    }

    public static ResultSet getResultSet()
    {
        return Queries.getResultSet("ressourcetotale");
    }

}
