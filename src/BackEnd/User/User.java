package BackEnd.User;

public class User
{
    private int id;
    private String username;
    private String firstname;
    private String lastname;
    private String phone;
    private int idProfile;

    public User(int id, String username, String firstname, String lastname, String phone, int idProfile)
    {
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.idProfile = idProfile;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getPhone() {
        return phone;
    }

    public int getIdProfile() {
        return idProfile;
    }
}
