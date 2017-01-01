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
      File temp = new File("temp");
      // unix
      // File output = new File("../csv/"+args[0].replace(".xml","") + ".csv");

      BufferedReader br = null;
      FileReader fr = null;
      String line;
      br = new BufferedReader(new FileReader(csvSource));
      int f = 1;

      temp.createNewFile();
      BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
      // bw.write("reference title,reference first author,reference year,reference DOI,reference journal,parent title,parent all authors,parent year,parent DOI,parent journal,reference ID\n");
      // int j = 0;
      int lineNum = 0;
      String currParent = "";
      ArrayList<String[]> potential = new ArrayList<String[]>();
      HashMap<String, String[]> lookup = new HashMap<String, String[]>();
      HashMap<String, ArrayList<String>>pairs = new HashMap<String, ArrayList<String>>();
      while ((line = br.readLine()) != null){
        lineNum++;
        System.out.println("No: " + lineNum);
        String[] lineArr = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        // System.out.println(currParent);
        if(lineNum == 1) currParent = lineArr[10].split(":")[1];
        System.out.println("ID: " + lineArr[10]);

        String twin1 = lineArr[10].split(":")[0];
        lookup.put(twin1,lineArr);
        for(String id : lookup.keySet()){
          System.out.println(id);
        }
        // System.out.println(twin1);
        System.out.println("Size: " + lookup.size());

        if(lineArr[10].split(":")[1].equals(currParent)){
          // System.out.println("same parent: " +lineArr[1]);
          for(int i = 0; i < potential.size(); i++){
            if(!potential.get(i)[1].equals(lineArr[1]) && checkYear(potential.get(i)[2], lineArr[2])){
              StringBuilder twinCode = new StringBuilder("");

              String twin2 = potential.get(i)[10].split(":")[0];
              twinCode.append(twin1 +":" + twin2);

              // lookup.put(twin2.toString(),potential.get(i));

              if(pairs.containsKey(twin2)){
                for(int j = 0; j <pairs.get(twin2).size(); j++ ){
                  String[] childCodes = pairs.get(twin2).get(j).split(":");
                  // System.out.println(childCodes.length);
                  for(int k = 0; k < childCodes.length; k++){
                    String[] entry = lookup.get(childCodes[k]);
                    System.out.println(childCodes[k]);
                    System.out.println(entry[1]);
                    System.out.println(entry[2]);
                    if(entry[1].equals(lineArr[1]) && !checkYear(entry[2], lineArr[2])){
                      break;


                    }else{
                      twinCode.append(childCodes[k]);
                      ArrayList<String> a = pairs.get(twin1);
                      ArrayList<String> b = pairs.get(twin2);
                      if (a == null) a = new ArrayList<String>();
                      a.add(twinCode.toString());
                      b.add(twinCode.toString());
                    }
                  }
                  pairs.put(twin1, a);
                  pairs.put(twin2, b);
                }
              }else{
                ArrayList<String> a = new ArrayList<String>();
                a.add(twinCode.toString());
                pairs.put(twin1, a);
                pairs.put(twin2, a);

              }
            }
          }
        }else{
          for(String id : pairs.keySet()){
            for(int i = 0; i< pairs.get(id).size(); i++){
              String[] twins = pairs.get(id).get(i).split(":");
              for(int j = 0; j < twins.length; j++){
                bw.write(lookup.get(twins[j])+","+ pairs.get(id).get(i) + "," + currParent + "\n");
              }
            }
          }
          // bw.write(Arrays.toString(lineArr)+","+ twinCode + "," + currParent + "\n");
          // bw.write(Arrays.toString(potential.get(i))+","+ twinCode + "," + currParent+"\n");
          currParent = lineArr[10].split(":")[1];
          potential = new ArrayList<String[]>();
          lookup = new HashMap<String, String[]>();
          pairs = new HashMap<String, ArrayList<String>>();
        }
        potential.add(lineArr);

      }
      bw.close();

      File output = new File("pairs.csv");
      bw = new BufferedWriter(new FileWriter(output));

      while(output.exists()) {
        output = new File("pairs-" + Integer.toString(f) + ".csv");
        f++;
      }
      // System.out.println(j);
      bw.close();
      // temp.delete();
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
