package FrontEnd.DatabaseManagement.Graph;

import BackEnd.Algorithms.Composition;
import BackEnd.Portfolio.Portfolio;

public class Sort
{
	public static Composition[] sort(Portfolio portfolio)
	{
		Tree tree=new Tree(portfolio);
		int compositionsCount=tree.getCompositions().size();
		Composition[] compositions=tree.getCompositions().toArray(new Composition[compositionsCount]);			
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
		return compositions;
	}
}
