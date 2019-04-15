package  FrontEnd.Graph;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class XMLCreator
{
  public static final String xmlFilePath = "C:\\\\Users\\\\HP\\\\Desktop\\\\Apps\\\\Java workspace\\\\Prefuse-master\\\\data\\\\BranchAndBound.xml";
  
  public XMLCreator() {}
  
  public static void main(String[] argv)
  {
    try
    {
      DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
      
      DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
      
      Document document = documentBuilder.newDocument();
      

      Element tree = document.createElement("tree");
      document.appendChild(tree);
      

      Element declarations = document.createElement("declarations");
      
      tree.appendChild(declarations);
      

      Element attributeDecl = document.createElement("attributeDecl");
      

      Attr name = document.createAttribute("name");
      Attr type = document.createAttribute("type");
      Attr value = document.createAttribute("value");
      
      name.setValue("name");
      type.setValue("type");
      
      attributeDecl.setAttributeNode(name);
      attributeDecl.setAttributeNode(type);
      
      declarations.appendChild(attributeDecl);
      
      Element branch = document.createElement("branch");
      
      Element leaf = document.createElement("leaf");
      
      Element attribute = document.createElement("attribute");
      


      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      javax.xml.transform.Transformer transformer = transformerFactory.newTransformer();
      DOMSource domSource = new DOMSource(document);
      StreamResult streamResult = new StreamResult(new java.io.File("C:\\\\Users\\\\HP\\\\Desktop\\\\Apps\\\\Java workspace\\\\Prefuse-master\\\\data\\\\BranchAndBound.xml"));
      





      transformer.transform(domSource, streamResult);
      
      System.out.println("Done creating XML File");
    }
    catch (ParserConfigurationException pce) {
      pce.printStackTrace();
    } catch (TransformerException tfe) {
      tfe.printStackTrace();
    }
  }
}
