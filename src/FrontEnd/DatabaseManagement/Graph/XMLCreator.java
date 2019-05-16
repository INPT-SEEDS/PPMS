package FrontEnd.DatabaseManagement.Graph;
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
 
public class XMLCreator {
 
    public static final String xmlFilePath = "BranchAndBound.xml";
 
    public static void main(String[] argv) {

        try {
 
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
 
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
 
            Document document = documentBuilder.newDocument();
 
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
            type.setValue("type");
            
            attributeDecl.setAttributeNode(name);
            attributeDecl.setAttributeNode(type);
            
            declarations.appendChild(attributeDecl);
            
            Element branch = document.createElement("branch");
            
            Element leaf = document.createElement("leaf");
            
            Element attribute = document.createElement("attribute");
 
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
}