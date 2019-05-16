package FrontEnd.DatabaseManagement.Graph;

import BackEnd.Algorithms.Composition;
import BackEnd.Portfolio.Portfolio;
import BackEnd.Project.Project;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Tree
{
	public static ArrayList<Element> ElementList = new ArrayList<Element>();
	public static Document document;
	private Portfolio portfolio;
	private List<Project> projects;
	private ArrayList<Composition>compositions;
	private int num;
	private Node root;
	
	public Tree(Portfolio portfolio)
	{
		this.portfolio=portfolio;
		this.compositions=new ArrayList<Composition>();
		this.num=portfolio.GetProjectsCount();
		this.root=new Node(new Composition(),-1);
		this.branche(root);
	}
	public Tree(List<Project> projects)
	{
		this.projects=projects;
		this.compositions=new ArrayList<Composition>();
		this.num=projects.size();
		this.root=new Node(new Composition(),-1);
		this.branche2(root);
	}
	
	public void branche(Node root)
	{
		if (root==null)return;
		for(int i=root.getIndex()+1;i<this.num;i++)
		{
			Composition composition=new Composition();
			Composition rootComposition=root.getComposition();
			for(int p=0;p<rootComposition.ProjectCount();p++)
			{
				composition.AddProject(rootComposition.GetProject(p));
			}
			composition.AddProject(portfolio.getProject(i));
			/*for(AssignedResource resource: ResToPortfolioQueries.getResourceByPortfolio(portfolio.getId()))
			{
				int resourceRequired=0;
				
				for(int j=0;j<composition.ProjectCount();j++)
				{
					resourceRequired+=composition.GetProject(j).getRequiredResource(resource);
				}
				
				if(resourceRequired>portfolio.getResourceQuantity(resource))
				{
					composition.SetReqSatisfied(false);
					return;
				}
			}*/
			compositions.add(composition);
			branche(root.insertNode(composition,i));
		}
	}
	public void branche2(Node root)
	{
		if (root==null)return;
		for(int i=root.getIndex()+1;i<this.num;i++)
		{
			Composition composition=new Composition();
			Composition rootComposition=root.getComposition();
			for(int p=0;p<rootComposition.ProjectCount();p++)
			{
				composition.AddProject(rootComposition.GetProject(p));
			}
			composition.AddProject(projects.get(i));
			compositions.add(composition);
			branche2(root.insertNode(composition,i));
		}
	}
	
	public void traverse(Node root)
	{
		if (root==null)return;
		/*for(int i=0;i<this.num;i++) 
		{
			System.out.print(root.values[i]+"  ");
		}*/
		System.out.println();
		for(Node node:root.getBranches()) { 
			root.getComposition().display();
			traverse(node);}
	}

	public ArrayList<Composition> getCompositions() {
		return compositions;
	}
	
	
	public void BrancheNoRes(Node root)
	{
		if (root==null) {
			//FeuilleXML(root, null);
			return;
		}
		for(int i=root.getIndex()+1;i<this.num;i++) 
		{
			Composition composition=new Composition();
			Composition rootComposition=root.getComposition();
			for(int p=0;p<rootComposition.ProjectCount();p++)
			{
				composition.AddProject(rootComposition.GetProject(p));
			}
			composition.AddProject(portfolio.getProject(i));
			compositions.add(composition);
			//BrancheXML(root, root.insertNode(composition,i));
			branche(root.insertNode(composition,i));
		}
	}
	
	public static void CreateXML(Tree t, int i)
	{
		 final String xmlFilePath = "Branch.xml";
		 try {
			 
	            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
	 
	            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
	 
	            document = documentBuilder.newDocument();
	 
	            // tree element
	            Element tree = document.createElement("tree");
	            document.appendChild(tree);
	 
	            // declarations element
	            Element declarations = document.createElement("declarations");
	 
	            tree.appendChild(declarations);
	            
	            //attributeDecl element
	            Element attributeDecl = document.createElement("attributeDecl");
	 
	            //
	            Attr name = document.createAttribute("name");
	            Attr type = document.createAttribute("type");
	            Attr value = document.createAttribute("value");
	            
	            name.setValue("name");
	            type.setValue("String");
	            
	            attributeDecl.setAttributeNode(name);
	            attributeDecl.setAttributeNode(type);
	            
	            declarations.appendChild(attributeDecl);
	            
	            Element branch = document.createElement("branch");
	            
	            //Element leaf = document.createElement("leaf");
	            
	            Element attribute = document.createElement("attribute");
	            
	            Attr name1 = document.createAttribute("name");
	            name1.setValue("name");
	            
	            value.setValue("value");
	            
	            attribute.setAttributeNode(name1);
	            attribute.setAttributeNode(value);
	            
	            //t.getRoot().setBranch(branch);
	            
	            //ElementList.add(branch);
	            
	            tree.appendChild(branch);
	            
	            //branch.appendChild(attribute);
	            
	            //Tree.CreateList(t.getRoot());
	            
	            NodeXmlTest(t.getRoot(), branch, i);
	            
	 
	            // create the xml file
	            //transform the DOM Object to an XML File
	            TransformerFactory transformerFactory = TransformerFactory.newInstance();
	            Transformer transformer = transformerFactory.newTransformer();
	            DOMSource domSource = new DOMSource(document);
	            StreamResult streamResult = new StreamResult(new File(xmlFilePath));
	 
	            // If you use
	            // StreamResult result = new StreamResult(System.out);
	            // the output will be pushed to the standard output ...
	            // You can use that for debugging 
	 
	            transformer.transform(domSource, streamResult);
	 
	            System.out.println("Done creating XML File");
	 
	        } catch (ParserConfigurationException | TransformerException pce) {
	            pce.printStackTrace();
	        }
	}
	
	public static void CreateList(Node root)
	{
			if (root==null) return;
			for(Node node:root.getBranches()) {
				if(ElementList.indexOf(node.getBranch()) == -1) {
					Element branch = document.createElement("branch");
					node.setBranch(branch);
					ElementList.add(node.getBranch());
				}
				/*NodeXml(node,i);*/}
	}
	
	public static void NodeXmlTest(Node root, Element BranchPere, int i)
	{
			if (root==null) return;
			for(Node node:root.getBranches()) { 
				
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
				
				NodeXmlTest(node, BranchFils,i);}
	}
	
	public static void NodeXml(Node root, int i) 
	{
			if (root==null) return;
			for(Node node:root.getBranches()) { 
				//root.getComposition().display();
				BrancheXML(root, node, document, i);
				NodeXml(node,i);}
	}
	
	public static void BrancheXML(Node pere , Node fils, Document document, int i)
	{
		Element attribute = document.createElement("attribute");
		
		Attr name = document.createAttribute("name");
		Attr value = document.createAttribute("value");
		
		name.setValue("name");
		value.setValue(fils.getComposition().GetCompositionString(i));
		
		attribute.setAttributeNode(name);
		attribute.setAttributeNode(value);
		
		//ElementList.get(ElementList.size()-1).appendChild(branch);
		
		ElementList.get(ElementList.indexOf(pere.getBranch())).appendChild(fils.getBranch());
		
		fils.getBranch().appendChild(attribute);
		
		
	}
	
	public void FeuilleXML(Node pere , Node fils)
	{
		
	}
	
	public Node getRoot() {
		return root;
	}
	
	public Portfolio getPortfolio() {
		return portfolio;
	}
	
	
}