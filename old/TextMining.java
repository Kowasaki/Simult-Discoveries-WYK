import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;

//lots of formatting
public class TextMining {

	//isolate the sets of parentheses and the text in between in the text files
	public static TreeMap<Integer, ArrayList<String>> createDictionaries() {
		try {
			BufferedReader br = new BufferedReader(new FileReader("dictionaries.txt"));
			String line = br.readLine();
			TreeMap<Integer, ArrayList<String>> inline = new TreeMap<Integer, ArrayList<String>>();
			ArrayList<String> dict = new ArrayList<String>();

			while (br.ready()) {
				if (line.startsWith("DICTION")) {
					String num = line.substring(line.indexOf(":") + 2, line.length());
					dict = new ArrayList<String>();
					int number = Integer.parseInt(num);
					inline.put(number, dict);
				} else if (!line.isEmpty()) {
					dict.add(line);
				}
				line = br.readLine();
			}

			for (Map.Entry<Integer, ArrayList<String>> entry : inline.entrySet()) {
				//System.out.println(entry.getKey() + "/" + entry.getValue());
			}

			br.close();
			return inline;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	

	public static ArrayList<CC> isolate(TreeMap<Integer, ArrayList<String>> dicts) {
		TreeMap<String, Integer> nombres = new TreeMap<String, Integer>();
		ArrayList<CC> allCocitations = new ArrayList<CC>();
		
		for (Map.Entry<Integer, ArrayList<String>> entry : dicts.entrySet()) {
			//one dictionary
			ArrayList<String> authors = new ArrayList<String>();
			int key = entry.getKey(); //key = dictionaryNumber
			ArrayList<String> values = entry.getValue();
			int ccNum = 0;
			//sets of ()s = 1 CC
			for (String val: values) {
				//all names/years within each set of ()
				String[] names = val.split(";");
				TreeMap<String,Integer> entries = new TreeMap<String,Integer>();
				
				for (String value : names) {
					if (value.startsWith("(")){
						value = value.substring(1);
						if (value.contains("(")){
							value = value.substring(value.indexOf("(") +1);
						}
					}
					String[] attr = value.split(",");
					String name = attr[0].trim();
										
					try{
						String year = attr[1].trim();
						if (year.endsWith(")")){
							year = year.substring(0, year.length() - 1);
						}
												
						int yr = Integer.parseInt(year);
						entries.put(name, yr);
					}
					catch (Exception e){
						System.out.println(attr[0]);
						e.printStackTrace();;
					}
				}
					
				ccNum++;
				CC cc = new CC(entries, key, ccNum);
				allCocitations.add(cc);
			}
}
		return allCocitations;
	}
	
	public static void checkAuthors(ArrayList<CC> ccList){		
		for (CC cc : ccList){
			for (Map.Entry<String, Integer> entry : cc.names.entrySet()) {
				//System.out.println(entry.getKey() + "/" + entry.getValue());
				String realName = entry.getKey().substring(0, entry.getKey().indexOf(" "));
				int realYear = entry.getValue();
				
				
				
			}
		}
	}
	 
	public static void main(String[] args) {
		isolate(createDictionaries());

	}

}
