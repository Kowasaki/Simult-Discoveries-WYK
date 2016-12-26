import java.awt.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;

public class createArticles {

	//formats all appropriate fields for the references
	//see Article class

	static ArrayList<Article> allRefs = new ArrayList<Article>();	//stores master list of all references
	static int parentNumber;

	public static void main(String [] args){
		ArrayList<Article> refs = createObs();
		try {

			//@param = input text file with ALL references of all input articles
			FileWriter writer = new FileWriter("test100.txt"); 
			BufferedWriter bw = new BufferedWriter (writer);

			//formatting
			for (Article a: refs){
				Collections.sort(a.authors);
				writer.write(a.id + " \t" + a.year + "\t" + a.title + "\t" + a.authors + "\n");
			}
			bw.close();
			writer.close();

		} catch(Exception e) {
		}
	}

	public static ArrayList<Article> createObs(){
		FileReader fr;
		try {
			//@param = input text file with ALL references of all input articles
			fr = new FileReader("1-478real.txt"); 

			BufferedReader br = new BufferedReader(fr);


			String line = br.readLine();
			line = br.readLine();
			int counter = 1;

			//formatting and relevant information extraction
			while (br.ready()){
				if (line.startsWith("?PT")){
					line = br.readLine();
				}
				if (line.startsWith("Article Number:")){
					int index = line.indexOf(": ");
					String number = line.substring(index+2, line.indexOf("Title"));
					number = number.trim();
					parentNumber = Integer.valueOf(number);
					line = br.readLine();
					counter = 1;
				}

				String t;
				int y;
				ArrayList<String> a = new ArrayList<String>();
				String id = null; 
				ArrayList<String> pa = new ArrayList<String>();
				int nc = 0;

//				J	Title:The neuronal background K-2P channels: focus on TREK1	Year:2007	Authors:Honore, Eric

				int ti = line.indexOf("Title:");

				int yr = line.indexOf("Year:");
		
				int ar = line.indexOf("\"Authors:");
				
				try {
				t = line.substring(ti + 6,yr);
				
				try {
					y = Integer.valueOf((line.substring(yr + 5, ar)).trim());
				} catch(Exception e) {
					ar = line.indexOf("Authors:");
					y = Integer.valueOf((line.substring(yr + 5, ar)).trim());
					System.out.println("this author didn't have quotes");
				}
				id = parentNumber + "." + counter;
				counter++;
				String rest = line.substring(ar + 8);

				//formatting all authors of a given reference for comparison and cocitation check at the end of the process
				//adding all authors of a given reference to an ArrayList<String>
				String [] arr = rest.split(";");
				for (String name : arr){
					String nombre = name;
					name = name.toLowerCase().trim();
					try{
						if (name.contains(",")){
							name = name.substring(0, name.indexOf(","));
						}
						else if (name.contains(".")){
							name = name.substring(0, name.indexOf("."));
						}
						else if (name.contains(" ")){
							name = name.substring(0, name.indexOf(" "));
						}
					}
					catch (Exception e){
						e.printStackTrace();
						System.out.println(nombre);
						System.out.println("ERROR: " + name);
					}
					a.add(name);
				}

				line = br.readLine();
				//finally creating the (reference) Article objects and adding them to the master reference list- "allRefs"
				allRefs.add(new Article(t,y,a,id,pa,nc, parentNumber));
				} catch(Exception e) {
					e.printStackTrace();
					System.out.println("ERROR- year not found probably");
					line = br.readLine();
				}
			}

			br.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		//returns master reference list- "allRefs"
		return allRefs;
	}
}
