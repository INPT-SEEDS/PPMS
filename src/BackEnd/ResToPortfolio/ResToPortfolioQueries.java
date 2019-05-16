package BackEnd.ResToPortfolio;

import BackEnd.Queries;
import BackEnd.Resource.AssignedResource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResToPortfolioQueries
{
    public static void addToDatabase(int id, int idRes, String date, int quantity)
    {
        resetTable(id,idRes);
        Queries.insertInto("affecterPor",id+","+idRes+",'"+date+"',"+quantity);
    }

    public static List<AssignedResource> getResourceByPortfolio(int idPortfolio)
    {
        List<AssignedResource> resourceList=new ArrayList<>();
        ResultSet rs=Queries.getResultSetWhere("affecterPor,ressourcetotale",
                "ressourcetotale.idRessource,libelle,quantite",
                "ressourcetotale.idRessource=affecterPor.idRessource AND affecterPor.idPortfeuille="+idPortfolio
                +" order by affecterPor.idRessource");
        try
        {
            while(rs.next())
            {
                int id=rs.getInt(1);
                String label=rs.getString(2);
                int quantity=rs.getInt(3);
                resourceList.add(new AssignedResource(id,label,quantity));
            }
        }
        catch (SQLException e)
        {e.printStackTrace();}

        return resourceList;
    }

    public static int getResourceCount(int idPortfolio,int idRes)
    {
        ResultSet rs=Queries.getResultSetWhere("affecterPor","*","idPortfeuille="+idPortfolio+" AND idRessource="+idRes);
        try
        {
            if(rs.next())
            {
                return rs.getInt(4);
            }
        }
        catch (SQLException e)
        {e.printStackTrace();}

        return 0;
    }

    public static void resetTable(int idPortfolio, int idResource)
    {
        Queries.deleteRow("affecterPor","idPortfeuille="+idPortfolio+" AND idRessource="+idResource);
    }

    public static void resetTable(int idPortfolio)
    {
        Queries.deleteRow("affecterPor","idPortfeuille="+idPortfolio);
    }



}
