package treatment;

import java.util.ArrayList;

public class Project
{
	private String ref;
	private int value;
	private ArrayList<Resource> resourceRequired;
	private ArrayList<Integer> resourcesQuantity;
	
	Project(String ref,int value)
	{
		this.ref=ref;
		this.value=value;
		this.resourceRequired=new ArrayList<Resource>();
		this.resourcesQuantity=new ArrayList<Integer>();
	}

	public float getValue()
	{
		return value;
	}

	public void setValue(int value)
	{
		this.value = value;
	}
	
	public void addResource(Resource resource,int quantity)
	{
		resourceRequired.add(resource);
		resourcesQuantity.add(0);
		this.setRequiredResourceQuantity(resource, quantity);
	}
	
	public void setRequiredResourceQuantity(Resource resource,int quantity)
	{
		int resourceIndex=resourceRequired.indexOf(resource);
		resourcesQuantity.set(resourceIndex, quantity);
	}
	
	public int getRequiredResource(Resource resource)
	{
		int resourceIndex=resourceRequired.indexOf(resource);
		return resourcesQuantity.get(resourceIndex);
	}

	public String getRef()
	{
		return ref;
	}
}
