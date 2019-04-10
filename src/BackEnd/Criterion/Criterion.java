package BackEnd.Criterion;

public class Criterion
{
    private int id;
    private String ref;
    private String label;
    private String genre;
    private int idType;

    public Criterion(int id,String ref, String label,String genre, int idType)
    {
        this.id=id;
        this.ref=ref;
        this.label=label;
        this.genre=genre;
        this.idType=idType;
    }

    public int getId()
    {
        return id;
    }

    public String getRef()
    {
        return ref;
    }

    public String getGenre()
    {
        return genre;
    }
}

