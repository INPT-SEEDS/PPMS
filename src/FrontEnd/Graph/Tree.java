package  FrontEnd.Graph;

import BackEnd.Algorithms.Composition;
import BackEnd.Portfolio.Portfolio;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.util.ArrayList;

public class Tree
{
  public static ArrayList<Element> ElementList = new ArrayList<>();
  public static Document document;
  private Portfolio portfolio;
  private ArrayList<Composition> compositions;
  private int num;
  private Node root;
  
  public Tree(Portfolio portfolio)
  {
    this.portfolio = portfolio;
    compositions = new ArrayList<>();
    num = portfolio.GetProjectsCount();
    root = new Node(new Composition(), -1);
    branche(root);
  }

  public void branche(Node root)
  {
    if (root == null) return;
    for (int i = root.getIndex() + 1; i < num; i++)
    {
      Composition composition = new Composition();
      Composition rootComposition = root.getComposition();
      for (int p = 0; p < rootComposition.ProjectCount(); p++)
      {
        composition.AddProject(rootComposition.GetProject(p));
      }
      compositions.add(composition);
      branche(root.insertNode(composition, i));
    }
  }
  
  public void traverse(Node root)
  {
    if (root == null) { return;
    }
    


    System.out.println();
    for (Node node : root.getBranches()) {
      root.getComposition().display();
      traverse(node);
    }
  }
  
  public ArrayList<Composition> getCompositions() { return compositions; }
  


  public void BrancheNoRes(Node root)
  {
    if (root == null)
    {
      return;
    }
    for (int i = root.getIndex() + 1; i < num; i++)
    {
      Composition composition = new Composition();
      Composition rootComposition = root.getComposition();
      for (int p = 0; p < rootComposition.ProjectCount(); p++)
      {
        composition.AddProject(rootComposition.GetProject(p));
      }
      composition.AddProject(portfolio.getProject(i));
      compositions.add(composition);
      
      branche(root.insertNode(composition, i));
    }
  }
  
  public static void CreateXML(Tree t, int i)
  {
    String xmlFilePath = "Branch.xml";
    try
    {
      DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
      
      DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
      
      document = documentBuilder.newDocument();

      Element tree = document.createElement("tree");
      document.appendChild(tree);

      Element declarations = document.createElement("declarations");
      
      tree.appendChild(declarations);

      Element attributeDecl = document.createElement("attributeDecl");

      Attr name = document.createAttribute("name");
      Attr type = document.createAttribute("type");
      Attr value = document.createAttribute("value");
      
      name.setValue("name");
      type.setValue("String");
      
      attributeDecl.setAttributeNode(name);
      attributeDecl.setAttributeNode(type);
      
      declarations.appendChild(attributeDecl);
      
      Element branch = document.createElement("branch");

      Element attribute = document.createElement("attribute");
      
      Attr name1 = document.createAttribute("name");
      name1.setValue("name");
      
      value.setValue("value");
      
      attribute.setAttributeNode(name1);
      attribute.setAttributeNode(value);

      tree.appendChild(branch);

      NodeXmlTest(t.getRoot(), branch, i);

      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      javax.xml.transform.Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      DOMSource domSource = new DOMSource(document);
      StreamResult streamResult = new StreamResult(new java.io.File("Branch.xml"));

      transformer.transform(domSource, streamResult);
    }
    catch (ParserConfigurationException | TransformerException pce)
    {
      pce.printStackTrace();
    }
  }

  public static void NodeXmlTest(Node root, Element BranchPere, int i)
  {
    if (root == null) return;
    for (Node node : root.getBranches())
    {
      Element BranchFils = document.createElement("branch");
      Element attribute = document.createElement("attribute");
      
      Attr name = document.createAttribute("name");
      Attr value = document.createAttribute("value");
      
      name.setValue("name");
      value.setValue(node.getComposition().GetCompositionString(i));

      attribute.setAttributeNode(name);
      attribute.setAttributeNode(value);
      
      BranchPere.appendChild(BranchFils);
      
      BranchFils.appendChild(attribute);
      
      NodeXmlTest(node, BranchFils, i);
    }
  }
  
  public static void NodeXml(Node root, int i) {
    if (root == null) return;
    for (Node node : root.getBranches())
    {
      BrancheXML(root, node, document, i);
      NodeXml(node, i);
    }
  }
  
  public static void BrancheXML(Node pere, Node fils, Document document, int i) {
    Element attribute = document.createElement("attribute");
    
    Attr name = document.createAttribute("name");
    Attr value = document.createAttribute("value");
    
    name.setValue("name");
    value.setValue(fils.getComposition().GetCompositionString(i));
    
    attribute.setAttributeNode(name);
    attribute.setAttributeNode(value);
    


    ElementList.get(ElementList.indexOf(pere.getBranch())).appendChild(fils.getBranch());

    fils.getBranch().appendChild(attribute);
  }

  public void FeuilleXML(Node pere, Node fils) {}

  public Node getRoot()
  {
    return root;
  }
  
  public Portfolio getPortfolio() {
    return portfolio;
  }
}
