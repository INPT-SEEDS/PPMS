package BackEnd.Evaluate;

import BackEnd.PortfolioCriteria.PortfolioCriteriaQueries;
import BackEnd.Project.ProjectQueries;
import BackEnd.Queries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EvaluateQueries
{
    public static void addToDatabase(int idProject,int idCriterion,int idUser,String date,int weight,int value)
    {
        int id=maxId();
        Queries.insertInto("evaluer",id+","+idProject+","+idCriterion+","+idUser+",'"+date+"',"+weight+","+value);
    }

    public static List<Evaluate> getUserProjectEvaluation(int idProject, int idUser)
    {
        List<Evaluate> evaluationList=new ArrayList<>();
        ResultSet rs=Queries.getResultSetWhere("evaluer,utiliser,projet","evaluer.idCritere,evaluer.poids,evaluer.valeur","evaluer.idProjet="+idProject+
                " AND evaluer.idUtilisateur="+idUser+" and utiliser.idPortfeuille=projet.idPortfeuille and  evaluer.idCritere=utiliser.idCritere group by utiliser.idCritere");
        try
        {
            while(rs.next())
            {
                int idCriterion=rs.getInt(1);
                int weight=rs.getInt(2);
                int value=rs.getInt(3);
                evaluationList.add(new Evaluate(idProject,idCriterion,weight,value));
            }
        }
        catch (SQLException e)
        {e.printStackTrace();}

        return evaluationList;
    }

    public static List<Evaluate> getProjectEvaluation(int idProject)
    {
        List<Evaluate> evaluationList=new ArrayList<>();
        ResultSet rs=Queries.getResultSetWhere("evaluer,utiliser,projet","evaluer.idCritere,evaluer.poids,AVG(evaluer.valeur)","evaluer.idProjet="+idProject+
                " and utiliser.idPortfeuille=projet.idPortfeuille and  evaluer.idCritere=utiliser.idCritere group by utiliser.idCritere");
        try
        {
            while(rs.next())
            {
                int idCriterion=rs.getInt(1);
                int weight=rs.getInt(2);
                int value=rs.getInt(3);
                evaluationList.add(new Evaluate(idProject,idCriterion,weight,value));
            }
        }
        catch (SQLException e)
        {e.printStackTrace();}

        return evaluationList;
    }

    public static boolean isEvalutedByUser(int idProject,int idUser)
    {
        ResultSet rs=Queries.getResultSetWhere("evaluer","count(*)","idProjet="+idProject+" AND idUtilisateur="+idUser);
        try
        {
            if(rs.next())
            {
                int portfolioCriteria= PortfolioCriteriaQueries.getCriteriaByPortfolio(ProjectQueries.getProjectById(idProject).getIdPortfolio()).size();
                if(rs.getInt(1)==portfolioCriteria)
                return true;
            }
        }
        catch (SQLException e)
        {e.printStackTrace();}

        return false;
    }

    public static boolean isEvalutedByUser(int idProject,int idUser,int idCriterion)
    {
        ResultSet rs=Queries.getResultSetWhere("evaluer","*","idProjet="+idProject+" AND idUtilisateur="+idUser+" AND idCritere="+idCriterion);
        try
        {
            if(rs.next())
            {
                return true;
            }
        }
        catch (SQLException e)
        {e.printStackTrace();}

        return false;
    }

    public static int getUserEvaluationIndex(int idUser,int idProject, int idCriterion)
    {
        ResultSet rs=Queries.getResultSetWhere("evaluer","valeur","idProjet="+idProject+" AND idUtilisateur="+idUser+" AND idCritere="+idCriterion);
        try
        {
            if(rs.next())
            {
                return rs.getInt(1)/10-1;
            }
        }
        catch (SQLException e)
        {e.printStackTrace();}

        return -1;
    }

    public static int evaluationCount(int idCriterion,int idProject)
    {
        ResultSet rs=Queries.getResultSetAdvanced("evaluer","count(*)","where idProjet="+idProject+" AND idCritere="+idCriterion);
        try
        {
            if(rs.next())
            {
                return rs.getInt(1);
            }
        }
        catch (SQLException e)
        {e.printStackTrace();}

        return -1;
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

    public static void deleteCriterionEvaluation(int idProject,int idCriterion)
    {
        Queries.deleteRow("evaluer","idProjet="+idProject+" AND idCritere="+idCriterion);
    }

    public static void resetEvaluation(int idProject,int idUser)
    {
        Queries.deleteRow("evaluer","idProjet="+idProject+" AND idUtilisateur="+idUser);
    }
    public static void resetEvaluation(int idProject)
    {
        Queries.deleteRow("evaluer","idProjet="+idProject);
    }
}
