import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.InputSource;
import java.io.StringReader;
import java.io.BufferedReader;
import java.io.FileReader;
import org.w3c.dom.*;
import java.util.*;


public class XMLOperations{

  public static void main(String args[]) throws Exception {
      String xmlFile = args[0];


      try{
        File xmlSource = new File(xmlFile);
        BufferedReader br = null;
        FileReader fr = null;
        boolean nodeComplete = false;
        StringBuilder node= new StringBuilder();;
        String line;
        br = new BufferedReader(new FileReader(xmlSource));
        while ((line = br.readLine()) != null){
            // String line = br.readLine();
            // System.out.println(line);
            if(line.contains("<REC ")){
              // System.out.println("new node");
              node = new StringBuilder();
              node.append(line);
              nodeComplete = false;
            } else if(line.contains("</REC>")){
              node.append(line);
              nodeComplete = true;
              // System.out.println(node.toString());
              parseNode(node.toString());
            } else if (!nodeComplete){
              node.append(line);
            }
        }


      }
      catch (Exception e){
	       e.printStackTrace();
      }
  }

  private static void parseNode(String node) throws Exception{
    HashSet<String> filter = new HashSet<String>();
    String[] listofjournals = new String[]{"NATURE", "SCIENCE", "CELL", "NEW ENGLAND JOURNAL OF MEDICINE", "JAMA","LANCET","CA-","NATURE GENERTICS","NATURE MATERIALS","NATURE IMMUNOLOGY","NATURE NANOTECHNOLOGY","NATURE BIOTECHNOLOGY","CANCER CELL","CELL STEM CELL" };
    filter.addAll(Arrays.asList(listofjournals));
    InputSource is = new InputSource();
    is.setCharacterStream(new StringReader(node));

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document doc = builder.parse(is);
    doc.getDocumentElement().normalize();

    System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
    NodeList recList = doc.getElementsByTagName("REC");

    for (int temp = 0; temp < recList.getLength(); temp++) {
      System.out.println(temp);
      Node nNode = recList.item(temp);
      if(nNode.getNodeType() == Node.ELEMENT_NODE) {
          Element e = (Element) nNode;
          NodeList titleList = e.getElementsByTagName("titles");
          // System.out.println("titles : " + titleList.getLength());

          // for (int i = 0; i < titleList.getLength(); i++) {
            Node title = titleList.item(0);
            // System.out.println("\nCurrent Element :" + nNode.getNodeName());
            if (title.getNodeType() == Node.ELEMENT_NODE) {
              Element eElement = (Element) title;
                int ti =  eElement.getElementsByTagName("title").getLength();
                String journal =  eElement.getElementsByTagName("title").item(0).getTextContent();
                System.out.println("Journal : " + journal);

                if (ti >=6){
                  String article =  eElement.getElementsByTagName("title").item(5).getTextContent();
                  System.out.println("Article : " + article);
                }
            }
          // }

          NodeList authorList = e.getElementsByTagName("names");
          Node author = authorList.item(0);
          if (author.getNodeType() == Node.ELEMENT_NODE) {
            Element eElement = (Element) author;
            String frstAuth =  eElement.getElementsByTagName("display_name").item(0).getTextContent();
            System.out.println("Author : " + frstAuth);

          }

          NodeList dateList = e.getElementsByTagName("pub_info");
          Node date = dateList.item(0);
          if (date.getNodeType() == Node.ELEMENT_NODE) {
            Element eElement = (Element) date;
            if(eElement.getAttribute("pubyear")!= null){
              String pubyear =  eElement.getAttribute("pubyear");
              System.out.println("pubyear : " + pubyear);
            }
          }


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

          NodeList referenceList = e.getElementsByTagName("reference");
          System.out.println("ref: " + referenceList.getLength());
          if(referenceList.getLength() > 0){
            // for (int j = 0; j < referenceList.getLength(); j++) {
              Node ref = referenceList.item(0);
                // System.out.println("\nCurrent Element :" + nNode.getNodeName());
              if (ref.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) ref;
                int cA = eElement.getElementsByTagName("citedAuthor").getLength();
                int cT = eElement.getElementsByTagName("citedTitle").getLength();
                int cW = eElement.getElementsByTagName("citedWork").getLength();
                int cY = eElement.getElementsByTagName("year").getLength();
                int cD = eElement.getElementsByTagName("doi").getLength();
                if(cA > 0){
                  for ( int i = 0; i < cA; i ++ ){
                    String citedAuthor =  eElement.getElementsByTagName("citedAuthor").item(i).getTextContent();
                    System.out.println("Cited Author : " + citedAuthor);
                  }
                }
                if(cT > 0){
                  String citedTitle =  eElement.getElementsByTagName("citedTitle").item(0).getTextContent();
                  System.out.println("Cited Title : " + citedTitle);
                }
                if (cW > 0){
                  String citedWork =  eElement.getElementsByTagName("citedWork").item(0).getTextContent();
                  System.out.println("Cited Work : " + citedWork);
                }
                if(cY > 0){
                  String citedYear = eElement.getElementsByTagName("year").item(0).getTextContent();
                  System.out.println("Year : " + citedYear);
                }
                if(cD > 0){
                  String citedDOI = eElement.getElementsByTagName("doi").item(0).getTextContent();
                  System.out.println("Cited DOI : " + citedDOI);
                }

              }
            // }
          }
      }

    }

  }
}
