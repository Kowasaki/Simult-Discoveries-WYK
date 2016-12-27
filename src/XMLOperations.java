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
      String[] listofjournals = new String[]{"<title type=\"source\">NATURE</title>",
      "<title type=\"source\">SCIENCE</title>", "<title type=\"source\">CELL</title>",
      "<title type=\"source\">NEW ENGLAND JOURNAL OF MEDICINE</title>",
      "<title type=\"source\">JAMA","<title type=\"source\">LANCET",
      "<title type=\"source\">NATURE MEDICINE</title>","<title type=\"source\">NATURE GENERTICS</title>",
      "<title type=\"source\">NATURE MATERIALS</title>","<title type=\"source\">NATURE IMMUNOLOGY</title>",
      "<title type=\"source\">NATURE NANOTECHNOLOGY</title>","<title type=\"source\">NATURE BIOTECHNOLOGY</title>",
      "<title type=\"source\">CANCER CELL</title>","<title type=\"source\">CELL STEM CELL</title>",
      "<title type=\"source\">CA-A CANCER JOURNAL FOR CLINICIANS</title>" };

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
        // findCommonRef(outputName);

      }
      catch (Exception e){
	       e.printStackTrace();
      }
  }

  // private static void findCommonRef(String output)throws Exception{
  //   BufferedWriter bw = new BufferedWriter(new FileWriter(output));
  //   System.out.println("TODO");
  //   bw.close();
  // }


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
    // ArrayList<String> citedAuthorArr = new ArrayList<String>();
    // ArrayList<String> citedTitleArr = new ArrayList<String>();
    // ArrayList<String> citedWorkArr = new ArrayList<String>();
    // ArrayList<String> citedYearArr = new ArrayList<String>();
    // ArrayList<String> citedDoiArr = new ArrayList<String>();
    // ArrayList<String> refID = new ArrayList<String>();

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
              journalArr.add(eElement.getElementsByTagName("title").item(0).getTextContent().replaceAll(",", " ").replaceAll("\"", " "));
              // if(journalArr.get(0).contains("Type 1")) System.out.println(titleArr.get(0));
              if (ti >=6){
                // String article =  eElement.getElementsByTagName("title").item(5).getTextContent();
                // System.out.println("Article : " + article);
                // String test = eElement.getElementsByTagName("title").item(5).getTextContent().replaceAll(",", " ").replaceAll("\"", " ");
                // if(test.contains("\"")||test.contains(",")) System.out.println(test);
                titleArr.add(eElement.getElementsByTagName("title").item(5).getTextContent().replaceAll(",", " ").replaceAll("\"", " "));
                // if(titleArr.get(0).contains("Fibrosis")) System.out.println(titleArr.get(0));
              }
          }
          StringBuilder pAuthor = new StringBuilder("");
          HashSet<String> names = new HashSet<String>();
          NodeList authorList = e.getElementsByTagName("name");
          // System.out.println(authorList.getLength());
          for (int i = 0; i < authorList.getLength(); i++){
            Node author = authorList.item(i);
            if (author.getNodeType() == Node.ELEMENT_NODE) {
              Element eElement = (Element) author;
              // String frstAuth =  eElement.getElementsByTagName("display_name").item(0).getTextContent();
              // System.out.println("Author : " + frstAuth);
              if(eElement.getAttribute("role").equals("author") && !names.contains(eElement.getElementsByTagName("display_name").item(0).getTextContent())){
                // System.out.println(eElement.getElementsByTagName("display_name").item(0).getTextContent());
                pAuthor.append(eElement.getElementsByTagName("display_name").item(0).getTextContent()) ;
                pAuthor.append(",");
                names.add(eElement.getElementsByTagName("display_name").item(0).getTextContent());
                // if(eElement.getElementsByTagName("display_name").item(0).getTextContent().equals("Haider, AS")){
                //   System.out.println(titleArr.get(0));
                // }
              }
            }
          }
          // if(pAuthor.capacity() > 1){
            // System.out.println(pAuthor.length());
          if(pAuthor.length() > 0 && pAuthor.charAt(pAuthor.length() - 1) == ',') pAuthor.deleteCharAt(pAuthor.length() - 1);
          // }
          authorArr.add("\""+pAuthor.toString()+"\"");


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
                doiArr.add(eElement.getAttribute("value").replaceAll(",","."));
              }
            }
          }

          NodeList referenceList = e.getElementsByTagName("reference");
          // System.out.println("ref: " + referenceList.getLength());
          // if(referenceList.getLength() > 0){
          for(int r = 0; r < referenceList.getLength(); r++){
            Node ref = referenceList.item(r);
            if (ref.getNodeType() == Node.ELEMENT_NODE) {
              Element eElement = (Element) ref;
              int cA = eElement.getElementsByTagName("citedAuthor").getLength();
              int cT = eElement.getElementsByTagName("citedTitle").getLength();
              int cW = eElement.getElementsByTagName("citedWork").getLength();
              int cY = eElement.getElementsByTagName("year").getLength();
              int cD = eElement.getElementsByTagName("doi").getLength();
              StringBuilder Authors = new StringBuilder("");

              if(cT > 0){
                // String citedTitle =  eElement.getElementsByTagName("citedTitle").item(0).getTextContent();
                // System.out.println("Cited Title : " + citedTitle);
                  // citedTitleArr.add(eElement.getElementsByTagName("citedTitle").item(0).getTextContent());
                  // output.write(citedTitleArr.get(0)+",");
                  output.write(eElement.getElementsByTagName("citedTitle").item(0).getTextContent().replaceAll("," ," ")+",");

              }else{
                output.write(" ,");
              }
              if(cA > 0){
                for ( int i = 0; i < cA; i ++ ){
                  // String citedAuthor =  eElement.getElementsByTagName("citedAuthor").item(i).getTextContent();
                  // System.out.println("Cited Author : " + citedAuthor);
                  Authors.append("\"");
                  Authors.append(eElement.getElementsByTagName("citedAuthor").item(i).getTextContent());
                  // Authors = "\"" + Authors;
                  if(i < cA-1) Authors.append(",");
                }
                Authors.append("\"");
                // citedAuthorArr.add(Authors);
                output.write(Authors.append(",").toString());
              }else{
                output.write(" ,");
              }
              if(cY > 0){
                // String citedYear = eElement.getElementsByTagName("year").item(0).getTextContent();
                // System.out.println("Year : " + citedYear);
                // citedYearArr.add(eElement.getElementsByTagName("year").item(0).getTextContent());
                output.write(eElement.getElementsByTagName("year").item(0).getTextContent()+",");
              }else{
                output.write(" ,");
              }

              if(cD > 0){
                // String citedDOI = eElement.getElementsByTagName("doi").item(0).getTextContent();
                // System.out.println("Cited DOI : " + citedDOI);
                // citedDoiArr.add(eElement.getElementsByTagName("doi").item(0).getTextContent());
                // if(eElement.getElementsByTagName("doi").item(0).getTextContent().contains(",")){
                //   System.out.println(eElement.getElementsByTagName("doi").item(0).getTextContent());
                //   System.out.println(eElement.getElementsByTagName("doi").item(0).getTextContent().replaceAll(",","."));
                // }
                output.write(eElement.getElementsByTagName("doi").item(0).getTextContent().replaceAll(",",".")+",");
              }else{
                output.write(" ,");
              }
              if (cW > 0){
                // String citedWork =  eElement.getElementsByTagName("citedWork").item(0).getTextContent();
                // System.out.println("Cited Work : " + citedWork);
                // citedWorkArr.add(eElement.getElementsByTagName("citedWork").item(0).getTextContent());
                output.write("\"" + eElement.getElementsByTagName("citedWork").item(0).getTextContent()+ "\"" +",");
              }else{
                output.write(" ,");
              }

              if(titleArr.size() > 0){output.write(titleArr.get(0) + ",");} else {output.write(" ,");}
              if(authorArr.size() > 0){output.write(authorArr.get(0) + ",");} else {output.write(" ,");}
              if(yearArr.size() > 0){output.write(yearArr.get(0) + ",");} else {output.write(" ,");}
              if(doiArr.size() > 0){output.write(doiArr.get(0) + ",");} else {output.write(" ,");}
              if(journalArr.size() > 0){output.write(journalArr.get(0) + ",");} else {output.write(" ,");}
              output.write(" \n");
            }
          }
          // }
        }
      }
      // for(int i = 0; i < 1; i++){
        // if(titleArr.size() > i){output.write(titleArr.get(i)+",");} else {output.write(",");}
        // if (authorArr.size() > i){output.write(authorArr.get(i)+",");} else {output.write(",");}
        // if (yearArr.size() > i){output.write(yearArr.get(i)+",");} else {output.write(",");}
        // if (doiArr.size() > i){output.write(doiArr.get(i)+",");} else {output.write(",");}
        // if (journalArr.size() > i){output.write(journalArr.get(i)+",");} else {output.write(",");}
        // if (citedTitleArr.size() > i){output.write(citedTitleArr.get(i)+","); } else {output.write(",");}
        // if (citedAuthorArr.size() > i){output.write(citedAuthorArr.get(i)+","); } else {output.write(",");}
        // if (citedYearArr.size() > i){ output.write(citedYearArr.get(i)+",");} else {output.write(",");}
        // if (citedDoiArr.size() > i) {output.write(citedDoiArr.get(i)+",");} else {output.write(",");}
        // if (citedWorkArr.size() > i) {output.write(citedWorkArr.get(i)+",");} else {output.write(",");}
        // if (refID.size() > i) {output.write(refID.get(i)+"\n");} else {output.write("\n");};
      // }

    }

}
