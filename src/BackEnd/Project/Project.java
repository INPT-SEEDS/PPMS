package BackEnd.Project;

import BackEnd.Evaluate.Evaluate;
import BackEnd.Evaluate.EvaluateQueries;
import BackEnd.ResToProject.ResToProjectQueries;
import BackEnd.Resource.AssignedResource;

import java.util.List;

public class Project
{
	private int id;
	private String label;
	private int idPortfolio;
	private int idType;
	private float totalValue;

	private List<Evaluate> projectEvaluation;

	private List<AssignedResource> resourceRequired;
	
	public Project(int id,String label,int idPortfolio,int idType)
	{
		this.id=id;
		this.label=label;
		this.idPortfolio=idPortfolio;
		this.idType=idType;
	}

	public int getId()
	{
		return id;
	}

	public int getIdPortfolio()
	{
		return idPortfolio;
	}

	public int getIdType()
	{
		return idType;
	}

	public void resourceInit()
	{
		this.resourceRequired= ResToProjectQueries.getResourceByProject(id);
	}

	public void setResourceRequired(List<AssignedResource> resourceRequired)
	{
		this.resourceRequired = resourceRequired;
	}

	public AssignedResource getResource(int idResource)
	{
		for(AssignedResource resource:resourceRequired)
		{
			if(resource.getIdResource()==idResource)
			return resource;
		}
		return null;
	}

	public List<AssignedResource> getResourceRequired()
	{
		return resourceRequired;
	}

	public List<Evaluate> getProjectEvaluation()
	{
		return projectEvaluation;
	}


	public double[] getCtriteriaValues()
	{
		this.projectEvaluation = EvaluateQueries.getProjectEvaluation(id);
		double[] ret=new double[projectEvaluation.size()];
		int index=0;
		for(Evaluate e:projectEvaluation)
		{
			ret[index]=e.getValue();
			System.out.println(e.getValue());
			index++;
		}
		return ret;
	}

	public float getTotalValue()
	{
		return totalValue;
	}

	public void setTotalValue(float totalValue)
	{
		this.totalValue = totalValue;
	}


	public String getLabel()
	{
		return label;
	}
}
