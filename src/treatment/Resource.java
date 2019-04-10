package treatment;

public class Resource
{
	private int id;
	private String reference;
	
	private static int count=0;

	public Resource(String refrence)
	{
		this.reference=refrence;
		id=count++;
	}

	public int getId()
	{
		return id;
	}
	
	public String getReference()
	{
		return reference;
	}
}
