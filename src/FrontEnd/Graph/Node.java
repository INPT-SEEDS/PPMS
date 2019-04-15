package  FrontEnd.Graph;

import BackEnd.Algorithms.Composition;

import java.util.ArrayList;

public class Node
{
  private ArrayList<Node> branches;
  private Composition composition;
  private int index;
  private org.w3c.dom.Element branch;
  
  public Node(Composition composition, int index)
  {
    branches = new ArrayList();
    this.composition = composition;
    this.index = index;
  }
  
  public Node insertNode(Composition composition, int index)
  {
    Node node = new Node(composition, index);
    branches.add(node);
    return node;
  }
  
  public void setBranch(org.w3c.dom.Element e)
  {
    branch = e;
  }
  
  public org.w3c.dom.Element getBranch()
  {
    return branch;
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
