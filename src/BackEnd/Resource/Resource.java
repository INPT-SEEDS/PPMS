package BackEnd.Resource;

public class Resource
{
	private int id;
	private int idCategory;
	private String label;
	
	private static int count=0;

	public Resource(int id,String label,int idCategory)
	{
		this.id=id;
		this.label=label;
		this.idCategory=idCategory;
	}

	public int getId()
	{
		return id;
	}
	
	public String getLabel()
	{
		return label;
	}
}
