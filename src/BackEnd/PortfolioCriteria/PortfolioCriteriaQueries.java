package BackEnd.PortfolioCriteria;

import BackEnd.Queries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PortfolioCriteriaQueries
{
    public static void addToDatabase(int idCriterion, int idPortfolio, int weight, String date, String usage)
    {
        Queries.insertInto("utiliser",idCriterion+","+idPortfolio+","+weight+",'"+date+"','"+usage+"'");
    }

    public static List<PortfolioCriteria> getCriteriaByPortfolio(int idPortfolio)
    {
        List<PortfolioCriteria> criteriaList=new ArrayList<>();
        ResultSet rs=Queries.getResultSetWhere("utiliser, critere","*","utiliser.idCritere=critere.id AND utiliser.idPortfeuille="+idPortfolio);
        try
        {
            while(rs.next())
            {
                int id=rs.getInt(1);
                int weight=rs.getInt(3);
                String genre=rs.getString(9);
                criteriaList.add(new PortfolioCriteria(id,idPortfolio,weight,genre));
            }
        }
        catch (SQLException e)
        {e.printStackTrace();}

        return criteriaList;
    }

    public static void resetPortfolio(int idPortfolio)
    {
        Queries.deleteRow("utiliser","idPortfeuille="+idPortfolio);
    }
}
