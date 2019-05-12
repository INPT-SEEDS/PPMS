package BackEnd.CriterionType;

import BackEnd.Queries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CriterionTypeQueries
{
    public static void addToDatabase(int id,String ref,String label)
    {
        Queries.insertInto("typecritere",id+",'"+ref+"','"+label+"'");
    }

    public static CriterionType getCriteriaTypeById(int id)
    {
        ResultSet rs=Queries.getResultSetWhere("typecritere","*","id="+id);
        try
        {
            if(rs.next())
            {
                String ref=rs.getString(2);
                String label=rs.getString(3);
                return new CriterionType(id,ref,label);
            }
        }
        catch (SQLException e)
        {e.printStackTrace();}

        return null;
    }

    public static List<CriterionType> getCriteriaTypes()
    {
        List<CriterionType> criteriaTypeList=new ArrayList<>();
        ResultSet rs=Queries.getResultSet("typecritere");
        try
        {
            while(rs.next())
            {
                int id=rs.getInt(1);
                String ref=rs.getString(2);
                String label=rs.getString(3);
                criteriaTypeList.add(new CriterionType(id,ref,label));
            }
        }
        catch (SQLException e)
        {e.printStackTrace();}

        return criteriaTypeList;
    }

    public static List<String> getCriteriaTypeRef()
    {
        List<String> criteriaTypeRef=new ArrayList<>();
        ResultSet rs=Queries.getResultSet("typecritere");
        try
        {
            while(rs.next())
            {
                criteriaTypeRef.add(rs.getString(2));
            }
        }
        catch (SQLException e)
        {e.printStackTrace();}

        return criteriaTypeRef;
    }

    public static ResultSet getResultSet()
    {
        return Queries.getResultSet("typecritere");
    }

    public static void update(int id,String ref,String label)
    {
        Queries.modifyCell("typecritere","libelle",label,"id="+id);
        Queries.modifyCell("typecritere","reference",ref,"id="+id);
    }
}
