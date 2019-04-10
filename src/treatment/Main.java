package treatment;

public class Main
{	
	public static void main(String[] args)
	{
		Portfolio portfolio=new Portfolio();
		
		Resource R1=new Resource("A");
		Resource R2=new Resource("B");
		
		portfolio.AddResource(R1, 8);
		portfolio.AddResource(R2, 8);

		Project P1=new Project("P1",10);	portfolio.AddProject(P1);
		Project P2=new Project("P2",10);	portfolio.AddProject(P2);
		Project P3=new Project("P3",10);	portfolio.AddProject(P3);
		Project P4=new Project("P4",100);	portfolio.AddProject(P4);

		
		P1.addResource(R1, 2);
		P1.addResource(R2, 2);
		
		P2.addResource(R1, 2);
		P2.addResource(R2, 2);
		
		P3.addResource(R1, 2);
		P3.addResource(R2, 2);
		
		P4.addResource(R1, 8);
		P4.addResource(R2, 8);

		/*Composition[] compositions=Simple.simple(portfolio);
		for(Composition composition:compositions)
		composition.display();*/
	}
}
