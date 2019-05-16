package FrontEnd.DatabaseManagement.Graph;

import BackEnd.Algorithms.Composition;
import org.w3c.dom.Element;

import java.util.ArrayList;

public class Node 
{
	private ArrayList<Node> branches;
	private Composition composition;
	private int index;
	private Element branch;
	
	public Node(Composition composition,int index)
	{
		this.branches=new ArrayList<Node>();
		this.composition=composition;
		this.index=index;
	}
	
	public Node insertNode(Composition composition,int index)
	{
		Node node=new Node(composition,index);
		this.branches.add(node);
		return node;
	}
	
	public void setBranch(Element e)
	{
		this.branch = e ;
	}
	
	public Element getBranch()
	{ 
		return this.branch;
	}
	
	public ArrayList<Node> getBranches() {
		return branches;
	}

	public Composition getComposition() {
		return composition;
	}

	public int getIndex() {
		return index;
	}

}
