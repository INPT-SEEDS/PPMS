package BackEnd.Algorithms;

import BackEnd.Project.Project;

import java.util.ArrayList;
import java.util.List;


public class Composition
{
	private boolean ReqSatisfied;
	private float value=0;
	
	private List<Project> composition;
	
	public Composition()
	{
		ReqSatisfied=true;
		composition=new ArrayList<Project>();
	}

	public void AddProject(Project project)
	{
		composition.add(project);
		value+=project.getTotalValue();
	}

	public List<Project> getProjects()
	{
		return composition;
	}

	public Project GetProject(int index)
	{
		return composition.get(index);
	}
	
	public int ProjectCount()
	{
		return composition.size();
	}
	
	public boolean AreReqSatisfied()
	{
		return ReqSatisfied;
	}

	public void SetReqSatisfied(boolean reqSatisfied)
	{
		ReqSatisfied = reqSatisfied;
	}

	public float GetValue()
	{
		return value;
	}
	
	public void display()
	{
		for(Project project:composition)
		{
			System.out.print(project.getLabel()+" ");
		}
		System.out.println("with a value of : "+value);
	}

	public void display2()
	{
		for(Project project:composition)
		{
			System.out.print(project.getLabel()+" ");
		}
	}

	public String GetCompositionString(int i)
	{
		String CompositionString = "";
		for (Project project : composition)
		{
			CompositionString = CompositionString + project.getLabel() + " ";
		}

		String Test = CompositionString;

		CompositionString = CompositionString + " (";

		for (int j = 1; j < i+1; j++) {
			String a = String.valueOf(j);

			if (j == i)
			{
				if (Test.contains(a))
				{
					CompositionString = CompositionString + " 1";

				}
				else
				{
					CompositionString = CompositionString + " 0";
				}

			}
			else if (Test.contains(a))
			{
				CompositionString = CompositionString + " 1,";
			}
			else
			{
				CompositionString = CompositionString + " 0,";
			}
		}

		CompositionString = CompositionString + ")";



		return CompositionString;
	}
}
