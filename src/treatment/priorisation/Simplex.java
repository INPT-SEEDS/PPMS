package treatment.priorisation;

import treatment.Portfolio;

public class Simplex
{

	public static int[] simplex(Portfolio portfolio)
	{
		int projectsCount=portfolio.GetProjectsCount();
		int compositionsCount=(int)Math.pow(2, projectsCount);
		int[][] compositions=new int[compositionsCount][projectsCount];
		
		for(int j=0; j<projectsCount;j++)
		{
			int power=(int) Math.pow(2,projectsCount-j-1 );
			for(int i=0; i<compositionsCount; i++)
			{
				int value=(i/power)%2;
				compositions[i][j]=value;
			}
		}
		
		
		
		return null;
	}
}
