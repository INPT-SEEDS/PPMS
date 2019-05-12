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

    public static List<Integer> getPrivilegesById(int id)
    {
        List<Integer> privileges=new ArrayList<>();
        ResultSet rs=Queries.getResultSetWhere("profileprivilege","*","idProfile="+id);
        try
        {
            while(rs.next())
            {
                privileges.add(rs.getInt(2));
            }
        }
        catch (SQLException e) { e.printStackTrace(); }
        return privileges;
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

    public static void updatePrivileges(int id, List<Integer> privileges)
    {
        Queries.deleteRow("profileprivilege","idProfile="+id);
        for(int privilege:privileges)
        {
            Queries.insertInto("profileprivilege",id+","+privilege);
        }
    }

}
