import java.io.File;
import java.io.StringReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

public class CSVParser{


  public static void main(String args[]) throws Exception {
    String csv = args[0];

    try{
      File csvSource = new File(csv);
      // windows
      File output = new File("parsed.csv");
      // unix
      // File output = new File("../csv/"+args[0].replace(".xml","") + ".csv");

      BufferedReader br = null;
      FileReader fr = null;
      String line;
      br = new BufferedReader(new FileReader(csvSource));
      int f = 1;
      while(output.exists()) {
        output = new File("parsed-" + Integer.toString(f) + ".csv");
        f++;
      }
      output.createNewFile();
      BufferedWriter bw = new BufferedWriter(new FileWriter(output));
      int j = 0;
      while ((line = br.readLine()) != null){
        String[] lineArr = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        if(lineArr.length  == 11 && checkNoSpace(lineArr) == true){
          StringBuilder child = new StringBuilder(lineArr[0]);
          child.append(lineArr[1]);
          child.append(lineArr[2]);
          child.append(lineArr[3]);
          child.append(lineArr[4]);
          StringBuilder parent = new StringBuilder(lineArr[5]);
          parent.append(lineArr[6]);
          parent.append(lineArr[7]);
          parent.append(lineArr[8]);
          parent.append(lineArr[9]);
          String id = Integer.toString(child.toString().hashCode()) + ":" + Integer.toString(parent.toString().hashCode());
          // if(!same.contains(id)){
          bw.write(line + id + "\n");
            // same.add(id);
          j++;
          // }
        }
      }
      System.out.println(j);
      bw.close();
    }
    catch (Exception e){
       e.printStackTrace();
    }
  }

  private static boolean checkNoSpace(String[] lineArr ){
    for(int i = 0; i < lineArr.length-1; i++){
      if(lineArr[i].trim().length() == 0){
        return false;
      }
    }
    return true;
    }
  }
