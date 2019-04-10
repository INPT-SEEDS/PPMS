package BackEnd.ResToProject;

import BackEnd.Queries;
import BackEnd.Resource.AssignedResource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResToProjectQueries
{
    public static void addToDatabase(int id, int idRes, String date, int quantity)
    {
        Queries.insertInto("affecterPro",id+","+idRes+",'"+date+"',"+quantity);
    }

    public static List<AssignedResource> getResourceByProject(int idProject)
    {
        List<AssignedResource> resourceList=new ArrayList<>();
        ResultSet rs=Queries.getResultSetWhere("affecterPro,ressourcetotale",
                "ressourcetotale.idRessource,libelle,quantite",
                "ressourcetotale.idRessource=affecterPro.idRessource AND affecterPro.idProjet="+idProject
                        +" order by affecterPro.idRessource");
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

    public static void resetTable(int idProject, int idResource)
    {
        Queries.deleteRow("affecterPro","idProjet="+idProject+" AND idRessource="+idResource);
    }
    public static void resetTable(int idProject)
    {
        Queries.deleteRow("affecterPro","idProjet="+idProject);
    }
}
