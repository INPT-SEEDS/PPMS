package BackEnd.Portfolio;

public class Portfolio
{
	private int id;
	private String ref;
	private String label;

	public Portfolio(int id, String ref, String label)
	{
		this.id = id;
		this.ref = ref;
		this.label = label;
	}

	public String getRef() {
		return ref;
	}

	public String getLabel() {
		return label;
	}
}