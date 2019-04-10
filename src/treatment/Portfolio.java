package treatment;

import java.util.ArrayList;

public class Portfolio
{
	private ArrayList<Project> portfolio;
	private ArrayList<Resource> portfolioResources;
	private ArrayList<Integer> resourcesQuantity;

	public Portfolio()
	{
		this.portfolio=new ArrayList<Project>();
		this.portfolioResources=new ArrayList<Resource>();
		this.resourcesQuantity=new ArrayList<Integer>();
	}
	
	public void AddProject(Project project)
	{
		portfolio.add(project);
	}
	
	public Project getProject(int index)
	{
		return portfolio.get(index);
	}
	
	public int GetProjectsCount()
	{
		return portfolio.size();
	}
	public void AddResource(Resource resource,int quantity)
	{
		portfolioResources.add(resource);
		resourcesQuantity.add(0);
		this.setResourceQuantity(resource, quantity);
	}
	
	public void setResourceQuantity(Resource resource,int quantity)
	{
		int resourceIndex=portfolioResources.indexOf(resource);
		resourcesQuantity.set(resourceIndex, quantity);
	}
	
	public int getResourceQuantity(Resource resource)
	{
		int resourceIndex=portfolioResources.indexOf(resource);
		return resourcesQuantity.get(resourceIndex);
	}
	
	public int getResourcesCount()
	{
		return resourcesQuantity.size();
	}
	
	public Resource getResource(int index)
	{
		return portfolioResources.get(index);
	}
	
}