import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.InputSource;
import java.io.StringReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import org.w3c.dom.*;
import java.util.*;


public class XMLOperations{

  public static void main(String args[]) throws Exception {
      String xmlFile = args[0];
      HashSet<String> filter = new HashSet<String>();
      String[] listofjournals = new String[]{"<citedWork>NATURE</citedWork>",
      "<citedWork>SCIENCE</citedWork>", "<citedWork>CELL</citedWork>",
      "<citedWork>NEW ENGLAND JOURNAL OF MEDICINE</citedWork>",
      "<citedWork>JAMA</citedWork>","<citedWork>LANCET",
      "<citedWork>NATURE MEDICINE</citedWork>","<citedWork>NATURE GENERTICS</citedWork>",
      "<citedWork>NATURE MATERIALS</citedWork>","<citedWork>NATURE IMMUNOLOGY</citedWork>",
      "<citedWork>NATURE NANOTECHNOLOGY</citedWork>","<citedWork>NATURE BIOTECHNOLOGY</citedWork>",
      "<citedWork>CANCER CELL</citedWork>","<citedWork>CELL STEM CELL</citedWork>","<citedWork>CA: A CANCER JOURNAL FOR CLINICIANS</citedWork>" };
      filter.addAll(Arrays.asList(listofjournals));


      try{
        File xmlSource = new File(xmlFile);
        File output = new File("out0.csv");
        BufferedReader br = null;
        FileReader fr = null;
        boolean nodeComplete = false;
        StringBuilder node= new StringBuilder();;
        String line;
        br = new BufferedReader(new FileReader(xmlSource));
        int f = 1;
        String outputName = "";
        while(output.exists()) {
          output = new File("out" + Integer.toString(f) + ".csv");
          outputName = "out" + Integer.toString(f) + ".csv";
          f++;
        }
        output.createNewFile();
        BufferedWriter bw = new BufferedWriter(new FileWriter(output));
        bw.write("reference title,reference first author,reference year,reference DOI,reference journal,parent title,parent all authors,parent year,parent DOI,parent journal,reference ID\n");

        while ((line = br.readLine()) != null){
            if(line.contains("<REC ")){
              // System.out.println("new node");
              node = new StringBuilder();
              node.append(line);
              nodeComplete = false;
            } else if(line.contains("</REC>")){
              node.append(line);
              nodeComplete = true;
              for(String journal : filter){
                // System.out.println(journal);
                if( node.toString().contains(journal)){
                  parseNode(node.toString(),bw);
                  break;
                }
              }

            } else if (!nodeComplete){
              node.append(line);
            }
        }
        bw.close();
        findCommonRef(outputName);

      }
      catch (Exception e){
	       e.printStackTrace();
      }
  }

  private static void findCommonRef(String output){
    BufferedWriter bw = new BufferedWriter(new FileWriter(output));


  }


  private static void parseNode(String node,BufferedWriter output) throws Exception{

    InputSource is = new InputSource();
    is.setCharacterStream(new StringReader(node));

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document doc = builder.parse(is);
    doc.getDocumentElement().normalize();

    ArrayList<String> titleArr = new ArrayList<String>();
    ArrayList<String> journalArr = new ArrayList<String>();
    ArrayList<String> authorArr = new ArrayList<String>();
    ArrayList<String> yearArr = new ArrayList<String>();
    ArrayList<String> doiArr = new ArrayList<String>();
    ArrayList<String> citedAuthorArr = new ArrayList<String>();
    ArrayList<String> citedTitleArr = new ArrayList<String>();
    ArrayList<String> citedWorkArr = new ArrayList<String>();
    ArrayList<String> citedYearArr = new ArrayList<String>();
    ArrayList<String> citedDoiArr = new ArrayList<String>();
    ArrayList<String> refID = new ArrayList<String>();

    NodeList recList = doc.getElementsByTagName("REC");

    for (int temp = 0; temp < recList.getLength(); temp++) {
      Node nNode = recList.item(temp);
      if(nNode.getNodeType() == Node.ELEMENT_NODE) {
          Element e = (Element) nNode;
          NodeList titleList = e.getElementsByTagName("titles");

            Node title = titleList.item(0);
            if (title.getNodeType() == Node.ELEMENT_NODE) {
              Element eElement = (Element) title;
                int ti =  eElement.getElementsByTagName("title").getLength();
                // String journal =  eElement.getElementsByTagName("title").item(0).getTextContent();
                // System.out.println("Journal : " + journal);
                journalArr.add(eElement.getElementsByTagName("title").item(0).getTextContent().replaceAll(",", " "));
                if (ti >=6){
                  // String article =  eElement.getElementsByTagName("title").item(5).getTextContent();
                  // System.out.println("Article : " + article);
                  titleArr.add(eElement.getElementsByTagName("title").item(5).getTextContent());
                }
            }

          NodeList authorList = e.getElementsByTagName("names");
          Node author = authorList.item(0);
          if (author.getNodeType() == Node.ELEMENT_NODE) {
            Element eElement = (Element) author;
            // String frstAuth =  eElement.getElementsByTagName("display_name").item(0).getTextContent();
            // System.out.println("Author : " + frstAuth);
            authorArr.add("\""+eElement.getElementsByTagName("display_name").item(0).getTextContent()+"\"");

          }

          NodeList dateList = e.getElementsByTagName("pub_info");
          Node date = dateList.item(0);
          if (date.getNodeType() == Node.ELEMENT_NODE) {
            Element eElement = (Element) date;
            if(eElement.getAttribute("pubyear")!= null){
              // String pubyear =  eElement.getAttribute("pubyear");
              // System.out.println("pubyear : " + pubyear);
              yearArr.add(eElement.getAttribute("pubyear"));
            }
          }


          NodeList ids = e.getElementsByTagName("identifier");
          for (int j = 0; j < ids.getLength(); j++) {
            Node id = ids.item(j);
            // System.out.println("\nCurrent Element :" + id.getNodeName());
            if (id.getNodeType() == Node.ELEMENT_NODE) {
              Element eElement = (Element) id;
              if(eElement.getAttribute("type").equals("doi")){
                // String doi =  eElement.getAttribute("value");
                // System.out.println("DOI : " + doi);
                doiArr.add(eElement.getAttribute("value"));
              }
            }
          }

          NodeList referenceList = e.getElementsByTagName("reference");
          System.out.println("ref: " + referenceList.getLength());
          if(referenceList.getLength() > 0){
              Node ref = referenceList.item(0);
              if (ref.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) ref;
                int cA = eElement.getElementsByTagName("citedAuthor").getLength();
                int cT = eElement.getElementsByTagName("citedTitle").getLength();
                int cW = eElement.getElementsByTagName("citedWork").getLength();
                int cY = eElement.getElementsByTagName("year").getLength();
                int cD = eElement.getElementsByTagName("doi").getLength();
                String Authors = "";
                if(cA > 0){
                  for ( int i = 0; i < cA; i ++ ){
                    // String citedAuthor =  eElement.getElementsByTagName("citedAuthor").item(i).getTextContent();
                    // System.out.println("Cited Author : " + citedAuthor);
                    Authors = Authors + eElement.getElementsByTagName("citedAuthor").item(i).getTextContent();
                    Authors = "\"" + Authors;
                    if(i < cA) Authors = Authors + ",";
                  }
                  Authors = Authors + "\"";
                  citedAuthorArr.add(Authors);
                }
                if(cT > 0){
                  // String citedTitle =  eElement.getElementsByTagName("citedTitle").item(0).getTextContent();
                  // System.out.println("Cited Title : " + citedTitle);
                  citedTitleArr.add(eElement.getElementsByTagName("citedTitle").item(0).getTextContent());
                }
                if (cW > 0){
                  String citedWork =  eElement.getElementsByTagName("citedWork").item(0).getTextContent();
                  System.out.println("Cited Work : " + citedWork);
                  citedWorkArr.add(eElement.getElementsByTagName("citedWork").item(0).getTextContent());
                }
                if(cY > 0){
                  // String citedYear = eElement.getElementsByTagName("year").item(0).getTextContent();
                  // System.out.println("Year : " + citedYear);
                  citedYearArr.add(eElement.getElementsByTagName("year").item(0).getTextContent());
                }
                if(cD > 0){
                  // String citedDOI = eElement.getElementsByTagName("doi").item(0).getTextContent();
                  // System.out.println("Cited DOI : " + citedDOI);
                  citedDoiArr.add(eElement.getElementsByTagName("doi").item(0).getTextContent());
                }
              }
          }
        }
      }
      for(int i = 0; i < 1; i++){
        if(titleArr.size() > i){output.write(titleArr.get(i)+",");} else {output.write(",");}
        if (authorArr.size() > i){output.write(authorArr.get(i)+",");} else {output.write(",");}
        if (yearArr.size() > i){output.write(yearArr.get(i)+",");} else {output.write(",");}
        if (doiArr.size() > i){output.write(doiArr.get(i)+",");} else {output.write(",");}
        if (journalArr.size() > i){output.write(journalArr.get(i)+",");} else {output.write(",");}
        if (citedTitleArr.size() > i){output.write(citedTitleArr.get(i)+","); } else {output.write(",");}
        if (citedAuthorArr.size() > i){output.write(citedAuthorArr.get(i)+","); } else {output.write(",");}
        if (citedYearArr.size() > i){ output.write(citedYearArr.get(i)+",");} else {output.write(",");}
        if (citedDoiArr.size() > i) {output.write(citedDoiArr.get(i)+",");} else {output.write(",");}
        if (citedWorkArr.size() > i) {output.write(citedWorkArr.get(i)+",");} else {output.write(",");}
        if (refID.size() > i) {output.write(refID.get(i)+"\n");} else {output.write("\n");};
      }

    }

}
