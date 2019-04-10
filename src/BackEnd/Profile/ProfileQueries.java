package BackEnd.Profile;

import BackEnd.Queries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProfileQueries
{
    public static void addToDatabase(int id,String ref,String label)
    {
        Queries.insertInto("profile",id+",'"+ref+"','"+label+"'");
    }

    public static Profile getProfileById(int id)
    {
        ResultSet rs=Queries.getResultSetWhere("profile","*","id="+id);
        try
        {
            if(rs.next())
            {
                String ref=rs.getString(2);
                String label=rs.getString(3);

                return new Profile(id,ref,label);
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
        return Queries.getResultSet("profile");
    }

    public static List<Profile> getProfiles()
    {
        List<Profile> profileList=new ArrayList<>();
        ResultSet rs=getResultSet();
        try
        {
             while(rs.next())
             {
                 int id=rs.getInt(1);
                 String ref=rs.getString(2);
                 String label=rs.getString(3);
                 profileList.add(new Profile(id,ref,label));
             }
        }
        catch (SQLException e) { e.printStackTrace(); }
        return profileList;
    }
    public static List<String> getProfilesRef()
    {
        List<String> profileRefList=new ArrayList<>();
        ResultSet rs=getResultSet();
        try
        {
            while(rs.next())
            {
                String ref=rs.getString(2);
                profileRefList.add(ref);
            }
        }
        catch (SQLException e) { e.printStackTrace(); }
        return profileRefList;
    }

}
