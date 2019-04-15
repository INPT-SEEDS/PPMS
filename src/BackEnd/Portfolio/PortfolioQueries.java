package BackEnd.Portfolio;

import BackEnd.Project.Project;
import BackEnd.Queries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PortfolioQueries
{
    public static void addToDatabase(int id,String ref,String label)
    {
        Queries.insertInto("portfeuille",id+",'"+ref+"','"+label+"'");
    }

    public static ResultSet getResultSet()
    {
        return Queries.getResultSet("portfeuille");
    }

    public static Portfolio getPortfolioById(int id)
    {
        ResultSet rs=Queries.getResultSetWhere("portfeuille","*","idPortfeuille="+id);
        try
        {
            if(rs.next())
            {
                String ref=rs.getString(2);
                String label=rs.getString(3);
                return new Portfolio(id,ref,label);
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static void updatePortfolio(int id,String ref,String label)
    {
        Queries.modifyCell("portfeuille","reference",ref,"idPortfeuille="+id);
        Queries.modifyCell("portfeuille","libelle",label,"idPortfeuille="+id);
    }

    public static List<String> getPortfoliosRef()
    {
        List<String> portfoliosRef=new ArrayList<>();
        ResultSet rs=Queries.getResultSet("portfeuille");
        try
        {
            while(rs.next())
            {
                portfoliosRef.add(rs.getString(2));
            }
        }
        catch (SQLException e)
        {e.printStackTrace();}

        return portfoliosRef;
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
        }
        catch (SQLException e)
        {e.printStackTrace();}

        return projects;
    }
}
