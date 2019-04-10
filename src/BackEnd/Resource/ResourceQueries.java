package BackEnd.Resource;

import BackEnd.Queries;

import java.sql.ResultSet;

public class ResourceQueries
{
    public static void addToDatabase(int id,String label,int idCategory)
    {
        Queries.insertInto("ressourcetotale",id+",'"+label+"',"+idCategory);
    }

    public static ResultSet getResultSet()
    {
        return Queries.getResultSet("ressourcetotale");
    }

}
