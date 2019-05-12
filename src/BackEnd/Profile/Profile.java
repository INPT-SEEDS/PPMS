package BackEnd.Profile;

import java.util.List;

public class Profile
{
    private int id;
    private String ref;
    private String label;
    private List<Integer> privileges;

    public Profile(int id, String ref, String label)
    {
        this.id = id;
        this.ref = ref;
        this.label = label;
        privileges=ProfileQueries.getPrivilegesById(id);
    }

    public List<Integer> getPrivileges()
    {
        return privileges;
    }

    public String getRef() {
        return ref;
    }

    public String getLabel() {
        return label;
    }
}
