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
      String line;
      br = new BufferedReader(new FileReader(csvSource));
      int f = 1;

      temp.createNewFile();
      BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
      int lineNum = 0;
      String currParent = "";
      LinkedHashMap<String, String[]> lookup = new LinkedHashMap<String, String[]>();
      HashSet<String> pairs = new HashSet<String>();
      HashMap<String, Integer> counts = new HashMap<String, Integer>();
      while ((line = br.readLine()) != null){
        lineNum++;
        String[] lineArr = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

        if(lineNum == 1) currParent = lineArr[10].split(":")[1];
        String childID = lineArr[10].split(":")[0];
        lookup.put(childID,lineArr);
        counts.put(childID,Integer.parseInt(lineArr[13]));
        if(!lineArr[10].split(":")[1].equals(currParent)){
          int ind = 0;
          int curr = 0;
          for(String id1 : lookup.keySet()){
            ArrayList<ArrayList<String>> possiblities = new ArrayList<ArrayList<String>>();
            ind++;
            curr = 0;
            for(String id2 : lookup.keySet()){
              if(curr < ind){
                curr++;
              } else{
                ArrayList<String> twins = new ArrayList<String>();
                String twin1 = lookup.get(id1)[10].split(":")[0];
                String twin2 = lookup.get(id2)[10].split(":")[0];

                if(!lookup.get(id1)[1].equals(lookup.get(id2)[1]) && checkYear(lookup.get(id1)[2], lookup.get(id2)[2])){
                  twins.add(twin1);
                  twins.add(twin2);
                  if(possiblities.size() == 0){
                    possiblities.add(twins);
                  }else{
                    for(int i = 0; i < possiblities.size(); i++){
                      for(int j = 0; j < possiblities.get(i).size(); j++){
                        if(!possiblities.get(i).get(j).equals(id1) && !lookup.get(id2)[1].equals(lookup.get(possiblities.get(i).get(j))[1]) &&
                          checkYear(lookup.get(id2)[2], lookup.get(possiblities.get(i).get(j))[2])){
                            if(!isRepeat(possiblities.get(i).get(j), twins)){
                              twins.add(possiblities.get(i).get(j));
                            }
                        }
                      }
                    }
                    possiblities.add(twins);
                  }
                }
              }
            }
            // put in hashset
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
          }
          for(String code : pairs){
            bw.write(code + "," + currParent +"\n");
          }
          currParent = lineArr[10].split(":")[1];
          lookup = new LinkedHashMap<String, String[]>();
          pairs = new HashSet<String>();
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

      while ((line = br.readLine()) != null){
        String[] ids = line.split(",");
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
        // Screen for useful groups here
        if(childArr.length <= 4 && jacc >= 0.5 ){
          StringBuilder allP = new StringBuilder("\"");
          for(int i = 0; i < childParents.get(pairCode).size(); i++){
            allP.append(childParents.get(pairCode).get(i));
            allP.append(",");
          }
          allP.deleteCharAt(allP.length()-1);
          allP.append("\"");
          bw.write(pairCode + "," + allP.toString() +"," + jacc + "\n");
        }
      }
      bw.close();
      temp.delete();
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
    for(int i = 0; i < in.length; i++){
      str.append(in[i]);
      str.append(",");
    }
    str.deleteCharAt(str.length()-1);
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
