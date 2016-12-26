import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;


/*Removes articles with 0 Cited References*/

public class ExtractInfo {
	public static void main (String [] args){
		final int ALLREFS = 100;

		File folder = new File("C:\\Users\\Yuming\\Desktop\\Research_Assistant_Python\\textfilesfirst100ex11_23_44_57");
		File[] listOfFiles = folder.listFiles();
		String[] fileNames = new String[listOfFiles.length];
		for (int i = 0; i < listOfFiles.length; i++) {
			fileNames[i] = listOfFiles[i].getName();
		}

		int[] results = new int[fileNames.length];

		for (int i = 0; i < fileNames.length; i++) {
			try {
				results[i] = Integer.parseInt(fileNames[i]);
			} catch (NumberFormatException nfe) {};
		}

		int[] baseline = new int[ALLREFS];
		int count = 1;
		for (int i = 0; i < ALLREFS; i++) {
			baseline[i] = count;
			count++;
		}


		try {
			FileReader fr = new FileReader("combined.txt");
			BufferedReader br = new BufferedReader(fr);

			FileWriter writer = new FileWriter("test100.txt"); 
			BufferedWriter bw = new BufferedWriter (writer);

			int counter = 1;

			String line = br.readLine();
			bw.write(line);
			line = br.readLine();

			while (br.ready()){
				if (!line.startsWith("PT")){
					System.out.println(line);
					bw.write(line);
					bw.newLine();
				} else {
					bw.write(results[counter -1]);
				}
				counter++;
				line = br.readLine();
			}

			bw.close();
			br.close();
			fr.close();
			writer.close();



		} catch (Exception e) {
			System.out.println("error" + e.getMessage());
		}     

	}

}
