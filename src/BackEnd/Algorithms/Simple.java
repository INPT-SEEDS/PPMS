package BackEnd.Algorithms;

import BackEnd.Project.Project;
import BackEnd.ResToPortfolio.ResToPortfolioQueries;
import BackEnd.Resource.AssignedResource;

import java.util.List;

public class Simple
{
	public static Composition[] simple(int idPortfolio,List<Project> projectList)
	{
		//-Create-The-Composition-------------------------------------------------------------------------------------------
		for(Project project:projectList)
		{
			project.resourceInit();
		}
		int projectsCount=projectList.size();
		int compositionsCount=(int)Math.pow(2, projectsCount)-1;
		Composition[] compositions=new Composition[compositionsCount];	
		
		for(int i=1; i<compositionsCount+1; i++)
		{
			compositions[i-1]=new Composition();
			for(int j=0; j<projectsCount;j++)
			{
				int power=(int) Math.pow(2,projectsCount-j-1 );
				if((i/power)%2==1)
				{
					compositions[i-1].AddProject(projectList.get(j));
				}
			}
		}

		//-Sort-The-Composition-------------------------------------------------------------------------------------------
		for(int i=0; i<compositionsCount-1; i++) 
		{
			int maxValueIndex=i;
			for(int j=i+1;j<compositionsCount;j++)
			{
				Composition composition=compositions[j];
				float value=composition.GetValue();
				if(value>compositions[maxValueIndex].GetValue())
				{
						maxValueIndex=j;
				}
			}
			Composition tempComposition=compositions[maxValueIndex];
			compositions[maxValueIndex]=compositions[i];
			compositions[i]=tempComposition;
		}

		//-Filter-The-Composition-------------------------------------------------------------------------------------------
		List<AssignedResource> portfolioResources= ResToPortfolioQueries.getResourceByPortfolio(idPortfolio);
		int satisfiedCompositionsCount=compositionsCount;

		for(Composition composition:compositions)
		{
			for(AssignedResource resource:portfolioResources)
			{
				int idResource=resource.getIdResource();
				int resourceRequired=0;
				for(Project project:composition.getProjects())
				{
					AssignedResource pr=project.getResource(idResource);
					if(pr!=null)
					resourceRequired+=pr.getQuantity();
				}

				System.out.println("	"+resource.getLabel()+" :: "+resourceRequired);

				if(resourceRequired>resource.getQuantity())
				{
					if(composition.AreReqSatisfied())
					{
						satisfiedCompositionsCount--;
					}
					composition.SetReqSatisfied(false);
				}
			}
		}

		
		Composition[] sortedCompositions=new Composition[satisfiedCompositionsCount];
		
		int index=0;
		for(int i=0; i<satisfiedCompositionsCount;i++)
		{
			while(!compositions[index].AreReqSatisfied()) 
			{
				index++;
			}
			sortedCompositions[i]=compositions[index];
			index++;
		}
		
		return sortedCompositions;
	}
}
