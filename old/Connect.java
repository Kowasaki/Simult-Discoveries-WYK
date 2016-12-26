import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;

/*
 *Connect the article entry to its citations 
 *
 */

public class Connect {
	public static void main(String args[]) {
		final int LENGTH_OF_RECORDS = 10000000;
		Numbering nums = new Numbering();

		File folder = new File("C:\\Users\\yumin\\Desktop\\Java Workspace\\ResearchAssistantWork\\479 sample");
		File[] listOfFiles = folder.listFiles();
		String[] fileNames = new String[listOfFiles.length];
		for (int i = 0; i < listOfFiles.length; i++) {
			int period = listOfFiles[i].getName().indexOf('.');
			fileNames[i] = listOfFiles[i].getName().substring(0, period);

		}

		int[] results = new int[LENGTH_OF_RECORDS];

		for (int i = 0; i < fileNames.length; i++) {
			try {
				results[i] = Integer.parseInt(fileNames[i]);
			} catch (NumberFormatException nfe) {
				nfe.getMessage();
			}
			
		}
		Arrays.sort(results, 0, fileNames.length);
		for (int i = 0; i < fileNames.length; i++) {
			System.out.println("RESULTS: " + results[i]);
		}
		for (int i = fileNames.length; i < results.length; i++) {
			results[i] = i + 1;
		}

		try {
			BufferedReader br = new BufferedReader(new FileReader("newfile2.txt"));
			BufferedWriter bw = new BufferedWriter(new FileWriter("citations_output.txt"));
			int count = 0;
			while (br.ready()) {
				String line = br.readLine();
				line = line.trim();
//				if(line.startsWith("?PT") || line.startsWith("PT")) {
				
				if ((line.startsWith("?PT") || (line.contains("?PT")))) {
//					bw.write("AGH");
					bw.write(line);
					String ex = (String) nums.getLines().get(count);
					bw.write(ex);
					bw.newLine();
					bw.write("Article Number: " + results[count] + "\t");

					count++;
					bw.newLine();
				} else {
					bw.write(line);
					bw.newLine();
				}

			}
			br.close();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}