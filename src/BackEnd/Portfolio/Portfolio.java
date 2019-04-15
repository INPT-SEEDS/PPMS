package BackEnd.Portfolio;

import BackEnd.Project.Project;

import java.util.List;

public class Portfolio
{
	private int id;
	private String ref;
	private String label;
	private List<Project> projectsList;

	public Portfolio(int id, String ref, String label)
	{
		this.id = id;
		this.ref = ref;
		this.label = label;
		projectsList=PortfolioQueries.getProjectsByPortfolio(id);
	}

	public int getId()
	{
		return id;
	}

	public String getRef()
	{
		return ref;
	}

	public String getLabel()
	{
		return label;
	}


	public int GetProjectsCount()
	{
		return projectsList.size();
	}

	public Project getProject(int i)
	{
		return projectsList.get(i);
	}
}