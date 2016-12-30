import java.io.File;
import java.io.StringReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

public class PairFinder{


  public static void main(String args[]) throws Exception {
    String csv = args[0];
    HashSet<String> parents = new HashSet<String>();
    HashSet<String> children = new HashSet<String>();

    try{
      File csvSource = new File(csv);
      // windows
      File output = new File("pairs.csv");
      // unix
      // File output = new File("../csv/"+args[0].replace(".xml","") + ".csv");

      BufferedReader br = null;
      FileReader fr = null;
      String line;
      br = new BufferedReader(new FileReader(csvSource));
      int f = 1;
      while(output.exists()) {
        // output = new File("..\\csv\\"+args[0].replace(".xml","")+"-" + Integer.toString(f) + ".csv");
        output = new File("pairs-" + Integer.toString(f) + ".csv");
        f++;
      }
      output.createNewFile();
      BufferedWriter bw = new BufferedWriter(new FileWriter(output));
      // bw.write("reference title,reference first author,reference year,reference DOI,reference journal,parent title,parent all authors,parent year,parent DOI,parent journal,reference ID\n");
      // int j = 0;
      int lineNum = 0;
      String currParent = "";
      ArrayList<String[]> potential = new ArrayList<String[]>();
      while ((line = br.readLine()) != null){
        lineNum++;
        // System.out.println(lineNum);
        String[] lineArr = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        // findPairs(lineArr, lineNum, csvSource, bw);
        // System.out.println(currParent);
        // System.out.println("ID: " + lineArr[10]);
        if(lineArr[10].split(":")[1].equals(currParent)){
          // System.out.println("same parent: " +lineArr[1]);
          for(int i = 0; i < potential.size(); i++){
            if(!potential.get(i)[0].equals(lineArr[1]) && checkYear(potential.get(i)[1], lineArr[2])){
              bw.write("1," + potential.get(i)[0] + "," + potential.get(i)[1] + ",2,"+ lineArr[1] + "," + lineArr[2] + "\n");
            }
          }
          potential.add(new String[]{lineArr[1], lineArr[2]});
        }else{
          currParent = lineArr[10].split(":")[1];
          potential = new ArrayList<String[]>();
          potential.add(new String[]{lineArr[1], lineArr[2]});
        }
      }
      // System.out.println(j);
      bw.close();
    }
    catch (Exception e){
       e.printStackTrace();
    }
  }

  // private static void findPairs(String[] currLine, int lineNum, File csvSource, BufferedWriter output) throws Exception{
  //   BufferedReader  br = new BufferedReader(new FileReader(csvSource));
  //   for(int i = 0; i < lineNum; i++) br.readLine();
  //   String line = "";
  //   while ((line = br.readLine()) != null){
  //     String[] lineArr = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
  //     // System.out.println(currLine[10]);
  //     if(currLine[10].split(":")[1].equals(lineArr[10].split(":")[1]) && checkYear(currLine[2], lineArr[2]) && !currLine[1].equals(lineArr[1])){
  //       output.write("1: " + currLine[1] + ", " + "2: "+ lineArr[1] + "\n");
  //     }
  //   }
  // }

  private static boolean checkYear(String currYr, String lineYr){
    if(!isInteger(currYr) || !isInteger(lineYr)) return false;
    int diff = Integer.parseInt(currYr) - Integer.parseInt(lineYr);
    if(diff > 1 || diff < -1){
      return false;
    }
    return true;
  }

  // From http://stackoverflow.com/questions/237159/whats-the-best-way-to-check-to-see-if-a-string-represents-an-integer-in-java
  public static boolean isInteger(String str) {
    if (str == null) {
        return false;
    }
    int length = str.length();
    if (length == 0) {
        return false;
    }
    int i = 0;
    if (str.charAt(0) == '-') {
        if (length == 1) {
            return false;
        }
        i = 1;
    }
    for (; i < length; i++) {
        char c = str.charAt(i);
        if (c < '0' || c > '9') {
            return false;
        }
    }
    return true;
  }
}
