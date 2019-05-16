package BackEnd.User;

import BackEnd.Evaluate.Evaluate;
import BackEnd.Evaluate.EvaluateQueries;
import BackEnd.Profile.ProfileQueries;

import java.util.List;

public class User
{
    private int id;
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String phone;
    private int idProfile;
    private List<Evaluate> UserEvaluations;
    private List<Integer> userPrivileges;

    public User(int id, String username,String password, String firstname, String lastname, String phone, int idProfile)
    {
        this.id = id;
        this.username = username;
        this.password=password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.idProfile = idProfile;
    }

    public int getId()
    {
        return id;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public String getFirstname()
    {
        return firstname;
    }

    public String getLastname()
    {
        return lastname;
    }

    public String getPhone()
    {
        return phone;
    }

    public int getIdProfile()
    {
        return idProfile;
    }

    public List<Evaluate> getUserEvaluations(int idProject)
    {
        UserEvaluations = EvaluateQueries.getUserProjectEvaluation(idProject, id);
        return UserEvaluations;
    }

    public boolean isPrivilegedTo(int index)
    {
        userPrivileges= ProfileQueries.getPrivilegesById(idProfile);
        for(int i: userPrivileges)
        {
            if(i==index)
            return true;
        }
        return false;
    }

    public boolean isPrivilegedTo(int... integers)
    {
        userPrivileges= ProfileQueries.getPrivilegesById(idProfile);
        for(int i: userPrivileges)
        {
            for(int j: integers)
            {
                if(j==i)
                    return true;
            }
        }
        return false;
    }
}
