package BackEnd.Algorithms;

public class Priorisation
{
    static double maxcol(double[][] b, int i)
    {
        double max=b[0][i];
        for(int j=0;j<b.length;j++)
        {
            if(b[j][i]>max)
            {
                max=b[j][i];
            }

        }
        return max;

    }

    static double mincol(double[][] b, int i)
    {
        double min=b[0][i];
        for(int j=0;j<b.length;j++)
        {
            if(b[j][i]<min)
            {
                min=b[j][i];
            }

        }
        return min;

    }
    static double sommetab(double[] a)
    {
        double s=0;
        for(int j=0;j<a.length;j++)
        {
            s=s+a[j];
        }
        return s;
    }
    static double sommecol(double[][] a, int i)
    {
        double s=0;
        for(int j=0;j<a.length;j++)
        {
            s=s+a[j][i];
        }
        return s;
    }
    public static double[] algorithme(double[][] a, int x, int y)
    {
        int ligne=a.length;
        int col=a[0].length;
        double[][] matderiv = new double[ligne][col];
        double[][] matproduit = new double[ligne][col];
        double[][] matfinale = new double[ligne][col];
        double[] tabsomme = new double[col];
        double[] tabmax = new double[col];
        double[] m = new double[col];
        double[] resultat = new double[ligne];
        double s;
        for(int i=0;i<x;i++)
        {
            tabmax[i]=maxcol(a,i);
            //System.out.println(tabmax[i]);
        }
        for(int i=x;i<col;i++)
        {
            tabmax[i]=mincol(a,i);
            //System.out.println(tabmax[i]);
        }

        for(int i=0;i<ligne;i++)
        {
            for(int j=0;j<x;j++)
            {
                matderiv[i][j]=a[i][j]/tabmax[j];

            }
            for(int j=x;j<col;j++)
            {
                matderiv[i][j]=tabmax[j]/a[i][j];

            }
        }

        for(int i=0;i<ligne;i++)
        {
            for(int j=0;j<col;j++)
            {
                matproduit[i][j]=matderiv[i][j]*Math.log(matderiv[i][j]);
            }
        }
        for(int i=0;i<col;i++)
        {
            tabsomme[i]=1-(-(1/Math.log(y))*sommecol(matproduit,i));
        }
        s=sommetab(tabsomme);
        for(int i=0;i<col;i++)
        {
            m[i]=tabsomme[i]/s;
        }
        for(int i=0;i<ligne;i++)
        {
            for(int j=0;j<col;j++)
            {
                matfinale[i][j]=matderiv[i][j]*m[j];
            }
        }
        for(int i=0;i<ligne;i++)
        {
            resultat[i]=sommetab(matfinale[i]);
        }
        return resultat;
    }
}