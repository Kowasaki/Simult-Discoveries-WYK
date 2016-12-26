import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
// import javax.xml.transform.Result;
// import javax.xml.transform.Source;
// import javax.xml.transform.Transformer;
// import javax.xml.transform.TransformerFactory;
// import javax.xml.transform.dom.DOMSource;
// import javax.xml.transform.stream.StreamResult;
// import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.*;
import java.util.*;


public class XMLOperations{

  public static void main(String args[]) throws Exception {
      String xmlFile = args[0];
      System.out.println(xmlFile);
      // File stylesheet = new File("src/main/resources/style.xsl");
      HashSet<String> filter = new HashSet<String>();
      String[] listofjournals = new String[]{"Nature", "Science", "Cell", "New England Journal of Medicine", "JAMA","Lancet","CA-","Nature Genetics","Nature Materials","Nature Immunology","Nature Nanotechnology","Nature BioTechnology","Cancer Cell","Cell Stem Cell" };
      filter.addAll(Arrays.asList(listofjournals));

      try{
        File xmlSource = new File(xmlFile);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlSource);
        doc.getDocumentElement().normalize();

        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        NodeList recList = doc.getElementsByTagName("REC");

        for (int temp = 0; temp < 3; temp++) {
          System.out.println(temp);
          Node nNode = recList.item(temp);
          if(nNode.getNodeType() == Node.ELEMENT_NODE) {
              Element e = (Element) nNode;
              NodeList titleList = e.getElementsByTagName("titles");
              // for (int i = 0; i < titleList.getLength(); i++) {
                Node title = titleList.item(0);
                // System.out.println("\nCurrent Element :" + nNode.getNodeName());
                if (title.getNodeType() == Node.ELEMENT_NODE) {
                  Element eElement = (Element) title;
                  String journal =  eElement.getElementsByTagName("title").item(0).getTextContent();
                  String article =  eElement.getElementsByTagName("title").item(5).getTextContent();
                  // System.out.println("Staff id : " + eElement.getAttribute("id"));
                  System.out.println("Journal : " + journal);
                  System.out.println("Article : " + article);
                }
              // }

              NodeList authorList = e.getElementsByTagName("names");
              // for (int j = 0; j < authorList.getLength(); j++) {
              Node author = authorList.item(0);
                // System.out.println("\nCurrent Element :" + author.getNodeName());
                // System.out.println("hi" + author.getNodeType());

              if (author.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) author;
                String frstAuth =  eElement.getElementsByTagName("display_name").item(0).getTextContent();
                System.out.println("Author : " + frstAuth);

              }
              // }

              NodeList ids = e.getElementsByTagName("identifier");
              for (int j = 0; j < ids.getLength(); j++) {
                Node id = ids.item(j);
                // System.out.println("\nCurrent Element :" + id.getNodeName());
                if (id.getNodeType() == Node.ELEMENT_NODE) {
                  Element eElement = (Element) id;
                  // System.out.println(eElement.getAttribute("type"));
                  if(eElement.getAttribute("type").equals("doi")){
                    // System.out.println("yay");
                    // System.out.println(j);
                    String doi =  eElement.getAttribute("value");
                    System.out.println("DOI : " + doi);
                  }
                }
            }

              NodeList referenceList = e.getElementsByTagName("references");
              if(referenceList.getLength() > 1){
                for (int j = 0; j < referenceList.getLength(); j++) {
                  Node ref = referenceList.item(j);
                    // System.out.println("\nCurrent Element :" + nNode.getNodeName());
                  if (ref.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) ref;
                    String reference =  eElement.getElementsByTagName("reference").item(0).getTextContent();
                    // System.out.println("Staff id : " + eElement.getAttribute("id"));
                    System.out.println("Author : " + reference);

                  }
                }
              }
          }

        }

      }
      catch (Exception e){
	       e.printStackTrace();
      }
  }


}
