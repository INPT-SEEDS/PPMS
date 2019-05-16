package BackEnd.Algorithms;

import BackEnd.PortfolioCriteria.PortfolioCriteria;
import BackEnd.PortfolioCriteria.PortfolioCriteriaQueries;
import BackEnd.Project.Project;

import java.util.List;

public class Priorisation
{
    static double maxcol(double[][] b, int i)
    {
        double max=b[0][i];
        for (double[] doubles : b)
        {
            if (doubles[i] > max)
            {
                max = doubles[i];
            }
        }
        return max;

    }

    static double mincol(double[][] b, int i)
    {
        double min=b[0][i];
        for (double[] doubles : b)
        {
            if (doubles[i] < min)
            {
                min = doubles[i];
            }
        }
        return min;

    }
    static double sommetab(double[] a)
    {
        double s=0;
        for (double v : a)
        {
            s = s + v;
        }
        return s;
    }
    static double sommecol(double[][] a, int i)
    {
        double s=0;
        for (double[] doubles : a)
        {
            s = s + doubles[i];
        }
        return s;
    }

    public static void algorithme(List<Project> projectsList)
    {
        int portfolioId=projectsList.get(0).getIdPortfolio();
        List<PortfolioCriteria> criteriaList= PortfolioCriteriaQueries.getCriteriaByPortfolio(portfolioId);

        int projectsCount=projectsList.size();
        int criteriaCount=criteriaList.size();

        double[][] matderiv = new double[projectsCount][criteriaCount];
        double[][] matproduit = new double[projectsCount][criteriaCount];
        double[][] matfinale = new double[projectsCount][criteriaCount];
        double[] tabsomme = new double[criteriaCount];
        double[] tabmax = new double[criteriaCount];
        double[] m = new double[criteriaCount];
        double s;

        double[][] a= new double[projectsCount][criteriaCount];
        int index=0;
        for(Project project:projectsList)
        {
            int j=0;
            double[] values=project.getCtriteriaValues();
            for(PortfolioCriteria criterion:criteriaList)
            {
                a[index][j]=values[j]*criterion.getWeight();
                System.out.println(criterion.getWeight());
                j++;
            }
            index++;
        }

        for(int i=0;i<criteriaCount;i++)
        {
            if(criteriaList.get(i).getGenre().equals("positif"))
            {
                tabmax[i]=maxcol(a,i);
            }
            else
            {
                tabmax[i]=mincol(a,i);
            }
        }

        for(int i=0;i<projectsCount;i++)
        {
            for(int j=0;j<criteriaCount;j++)
            {
                if(criteriaList.get(j).getGenre().equals("positif"))
                {
                    matderiv[i][j]=a[i][j]/tabmax[j];
                }
                else
                {
                    matderiv[i][j]=tabmax[j]/a[i][j];
                }
            }
        }

        for(int i=0;i<projectsCount;i++)
        {
            for(int j=0;j<criteriaCount;j++)
            {
                matproduit[i][j]=matderiv[i][j]*Math.log(matderiv[i][j]);
            }
        }
        for(int i=0;i<criteriaCount;i++)
        {
            tabsomme[i]=1-(-(1/Math.log(projectsCount))*sommecol(matproduit,i));
        }
        s=sommetab(tabsomme);
        for(int i=0;i<criteriaCount;i++)
        {
            m[i]=tabsomme[i]/s;
        }
        for(int i=0;i<projectsCount;i++)
        {
            for(int j=0;j<criteriaCount;j++)
            {
                matfinale[i][j]=matderiv[i][j]*m[j];
            }
        }

        int i=0;
        for(Project project:projectsList)
        {
            float value= (float) ((int)(10000*sommetab(matfinale[i])))/10000;
            project.setTotalValue(value);
            i++;
        }

        //Sorting projects
        for(int p=0;p<projectsList.size()-1;p++)
        {
            float max=projectsList.get(p).getTotalValue();
            for(int q=p+1;q<projectsList.size();q++)
            {
                if(projectsList.get(q).getTotalValue()>max)
                {
                    max=projectsList.get(q).getTotalValue();
                    Project temp=projectsList.get(p);
                    projectsList.set(p,projectsList.get(q));
                    projectsList.set(q,temp);
                }
            }
        }
    }
}