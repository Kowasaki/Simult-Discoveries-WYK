import java.io.File;
import java.io.StringReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;
import java.text.DecimalFormat;


public class PairFinder{


  public static void main(String args[]) throws Exception {
    String csv = args[0];
    HashSet<String> parents = new HashSet<String>();
    HashSet<String> children = new HashSet<String>();

    try{
      File csvSource = new File(csv);
      File temp = new File("temp");

      BufferedReader br = null;
      FileReader fr = null;
      String line;
      br = new BufferedReader(new FileReader(csvSource));
      int f = 1;

      temp.createNewFile();
      BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
      // int j = 0;
      int lineNum = 0;
      String currParent = "";
      LinkedHashMap<String, String[]> lookup = new LinkedHashMap<String, String[]>();
      HashSet<String> pairs = new HashSet<String>();
      HashMap<String, Integer> counts = new HashMap<String, Integer>();
      while ((line = br.readLine()) != null){
        lineNum++;
        // System.out.println("No: " + lineNum);
        String[] lineArr = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

        if(lineNum == 1) currParent = lineArr[10].split(":")[1];
        String childID = lineArr[10].split(":")[0];
        lookup.put(childID,lineArr);
        counts.put(childID,Integer.parseInt(lineArr[13]));
        if(lineArr[10].split(":")[1].equals(currParent)){
          // System.out.println(currParent);
          // System.out.println("No: " + lineNum);
          // System.out.println("ID: " + lineArr[10]);
          // for(String id : lookup.keySet()){
            // System.out.println("in lookup: " + id);
          // }
          // System.out.println("Lookup Size: " + lookup.size());
        }else{
          // System.out.println("same parent: " +lineArr[1]);
          int ind = 0;
          int curr = 0;
          for(String id1 : lookup.keySet()){
            ArrayList<ArrayList<String>> possiblities = new ArrayList<ArrayList<String>>();
            ind++;
            curr = 0;
            // System.out.println("ID1: " + id1);
            for(String id2 : lookup.keySet()){
              if(curr < ind){
                // System.out.println("ind: " + ind);
                // System.out.println("curr: " + curr);
                // System.out.println("skipped: " + id2);
                curr++;
                // continue;
              } else{
                // System.out.println("ID2: " + id2);
                ArrayList<String> twins = new ArrayList<String>();
                String twin1 = lookup.get(id1)[10].split(":")[0];
                String twin2 = lookup.get(id2)[10].split(":")[0];
                // System.out.println("Twin1: " + twin1);
                // System.out.println("Twin2: " + twin2);
                // System.out.println("prev poss size: "+ possiblities.size());

                if(!lookup.get(id1)[1].equals(lookup.get(id2)[1]) && checkYear(lookup.get(id1)[2], lookup.get(id2)[2])){
                  // System.out.println(id1 + " and " + id2 + " are twins!");
                  twins.add(twin1);
                  twins.add(twin2);
                  if(possiblities.size() == 0){
                    possiblities.add(twins);
                  }else{
                    // System.out.println("possibilities size: "+ possiblities.size());
                    // System.out.println(possiblities.get(0).size());
                    for(int i = 0; i < possiblities.size(); i++){
                      for(int j = 0; j < possiblities.get(i).size(); j++){
                        // System.out.println("in poss: " + possiblities.get(i).get(j));
                        if(!possiblities.get(i).get(j).equals(id1) && !lookup.get(id2)[1].equals(lookup.get(possiblities.get(i).get(j))[1]) &&
                          checkYear(lookup.get(id2)[2], lookup.get(possiblities.get(i).get(j))[2])){
                            // System.out.println(possiblities.get(i).get(j));
                            // System.out.println("another twin: " + possiblities.get(i).get(j));
                            // System.out.println(lookup.get(id2)[1]);
                            // System.out.println(lookup.get(possiblities.get(i).get(j))[1]);
                            // System.out.println(lookup.get(id2)[2]);
                            // System.out.println(lookup.get(possiblities.get(i).get(j))[2]);
                            if(!isRepeat(possiblities.get(i).get(j), twins)){
                              // System.out.println("another twin: " + possiblities.get(i).get(j));
                              // System.out.println(lookup.get(id2)[1]);
                              // System.out.println(lookup.get(possiblities.get(i).get(j))[1]);
                              // System.out.println(lookup.get(id2)[2]);
                              // System.out.println(lookup.get(possiblities.get(i).get(j))[2]);
                              twins.add(possiblities.get(i).get(j));
                            }
                            // System.out.println("Twin size: " + twins.size());
                            // if(twins.size() > 10) System.out.println(twins.get(10000000));
                        }
                      }
                    }
                    possiblities.add(twins);
                  }
                }
                // System.out.println("end of id2 loop");
              }
            }
            // put in hashset
            // System.out.println("End poss size: " + possiblities.size());

            for(int i = 0; i < possiblities.size(); i++){
              Collections.sort(possiblities.get(i));
              StringBuilder twinCode = new StringBuilder("");
              for(int j = 0; j < possiblities.get(i).size(); j++){
                twinCode.append(possiblities.get(i).get(j));
                twinCode.append(":");
              }
              twinCode.deleteCharAt(twinCode.length()-1);
              pairs.add(twinCode.toString());
              twinCode = new StringBuilder("");
            }
            // System.out.println("end of id1 loop");
          }
          for(String code : pairs){
            // System.out.println(code);
            // String[] entries = code.split(":");
            // for(int i = 0; i < entries.length; i++){
              // String entry = backToString(lookup.get(entries[i]));
              // bw.write(entry +","+ code + "," + currParent + "\n");
              // bw.write(lookup.get(entries[i])[10] +","+ code + "," + currParent +"\n");
            // }
            bw.write(code + "," + currParent +"\n");

          }
          currParent = lineArr[10].split(":")[1];
          lookup = new LinkedHashMap<String, String[]>();
          pairs = new HashSet<String>();
          // System.out.println(lineArr[10000000]);
        }
      }

      bw.close();
      File output = new File("pairs.csv");
      bw = new BufferedWriter(new FileWriter(output));
      br = new BufferedReader(new FileReader(temp));
      HashMap<String, ArrayList<String>> childParents = new HashMap<String, ArrayList<String>>();

      while(output.exists()) {
        output = new File("pairs-" + Integer.toString(f) + ".csv");
        f++;
      }
      // System.out.println(j);

      while ((line = br.readLine()) != null){
        String[] ids = line.split(",");
        // System.out.println(ids[0]);
        if(childParents.containsKey(ids[0])){
          ArrayList<String> p = childParents.get(ids[0]);
          p.add(ids[1]);
          childParents.put(ids[0],p);
        }else{
          ArrayList<String> p = new ArrayList<String>();
          p.add(ids[1]);
          childParents.put(ids[0],p);
        }
      }

      for(String pairCode : childParents.keySet()){
        String[] childArr = pairCode.split(":");
        double sum = 0;
        for(int i = 0; i < childArr.length; i++){
          sum += (double)counts.get(childArr[i]);
        }
        double jacc = calculateJaccard(sum , childParents.get(pairCode).size());
        StringBuilder allP = new StringBuilder("\"");
        for(int i = 0; i < childParents.get(pairCode).size(); i++){
          allP.append(childParents.get(pairCode).get(i));
          allP.append(",");
        }
        allP.deleteCharAt(allP.length()-1);
        allP.append("\"");
        bw.write(pairCode + "," + allP.toString() +"," + jacc + "\n");
      }
      bw.close();
      // temp.delete();
    }
    catch (Exception e){
       e.printStackTrace();
    }
  }

  private static double calculateJaccard(double childCount, double coCite ){
    DecimalFormat df = new DecimalFormat("#.##");
    double jacc = coCite/(childCount-coCite);
    return Double.valueOf(df.format(jacc));

  }

  private static boolean isRepeat(String id ,ArrayList<String> twins){
    boolean repeat = false;
    for(int i = 0; i < twins.size(); i++){
      if(twins.get(i).equals(id)) return true;
    }
    return false;
  }

  private static String backToString(String[] in){
    StringBuilder str = new StringBuilder("");
    // System.out.println(in.length);
    for(int i = 0; i < in.length; i++){
      str.append(in[i]);
      str.append(",");
    }
    str.deleteCharAt(str.length()-1);
    // System.out.println(str.toString());
    return str.toString();
  }

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
