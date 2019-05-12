package BackEnd.Privilege;

import BackEnd.Queries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PrivilegeQueries
{
    public static List<Privilege> getPriviliges()
    {
        List<Privilege> privileges=new ArrayList<>();
        ResultSet rs= Queries.getResultSet("privilege");
        try
        {
            while(rs.next())
            {
                int id=rs.getInt(1);
                String name=rs.getString(2);
                privileges.add(new Privilege(id,name));
            }
        }
        catch (SQLException e)
        {e.printStackTrace();}

        return privileges;
    }
}
