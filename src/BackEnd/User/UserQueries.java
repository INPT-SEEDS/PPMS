package BackEnd.User;

import BackEnd.Queries;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserQueries
{
    public static void addToDatabase(int id,String username,String name,String firstname,String phone,int idProfile)
    {
        Queries.insertInto("utilisateur",id+",'"+username+"','****','"+name+"','"+firstname+"','"+phone+"',"+idProfile);
    }

    public static User getUserById(int id)
    {
        ResultSet rs=Queries.getResultSetWhere("utilisateur","*","id="+id);
        try
        {
            if(rs.next())
            {
                String usr=rs.getString(2);
                String fn=rs.getString(4);
                String ln=rs.getString(5);
                String ph=rs.getString(6);
                int idProfile=rs.getInt(7);

                return new User(id,usr,fn,ln,ph,idProfile);
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static ResultSet getResultSet()
    {
        return Queries.getResultSet("utilisateur","Id,username as \"nom d'utilisateur\",Nom,prenom,Telephone,idProfile");
    }
}
