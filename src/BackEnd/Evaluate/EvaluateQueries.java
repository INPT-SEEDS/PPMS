package BackEnd.Evaluate;

import BackEnd.Queries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EvaluateQueries
{
    public static void addToDatabase(int idProject,int idCriterion,String date,int weight,float value)
    {
        int id=maxId();
        Queries.insertInto("evaluer",id+","+idProject+","+idCriterion+",'"+date+"',"+weight+","+value);
    }

    public static List<Evaluate> getProjectEvaluation(int idProject)
    {
        List<Evaluate> evaluationList=new ArrayList<>();
        ResultSet rs=Queries.getResultSetWhere("evaluer","*","idProjet="+idProject);
        try
        {
            while(rs.next())
            {
                int idCriterion=rs.getInt(3);
                int weight=rs.getInt(5);
                float value=rs.getFloat(6);
                evaluationList.add(new Evaluate(idProject,idCriterion,weight,value));
            }
        }
        catch (SQLException e)
        {e.printStackTrace();}

        return evaluationList;
    }

    private static int maxId()
    {
        int maxId=0;
        ResultSet rs=Queries.getResultSet("evaluer","max(id)");
        try{
            if(rs.next())
            {
                maxId=rs.getInt(1)+1;
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return maxId;
    }

    public static void resetEvaluation(int idProject)
    {
        Queries.deleteRow("evaluer","idProjet="+idProject);
    }
}
