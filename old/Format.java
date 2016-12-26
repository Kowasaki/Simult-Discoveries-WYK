import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;


/*Removes articles with 0 Cited References*/

public class Format {

	public static void main (String [] args){
		//    final int ALLREFS = 100;
		final int LENGTH_OF_RECORDS = 68000;
		String init_text = "science_mod.txt";

		String beginning =  "http://apps.webofknowledge.com/InboundService.do?SID=2FmBpICBDXy4g74gXLu&product=WOS&UT=WOS%3A";
		String end = "&SrcApp=EndNote&DestFail=http%3A%2F%2Fwww.webofknowledge.com&Init=Yes&action=retrieve&SrcAuth=ResearchSoft&customersID=ResearchSoft&Func=Frame&IsProductCode=Yes&mode=FullRecord";

		File folder = new File("C:\\Users\\yumin\\Desktop\\Java Workspace\\ResearchAssistantWork\\479 sample");
		File[] listOfFiles = folder.listFiles();
		String[] fileNames = new String[listOfFiles.length];
		for (int i = 0; i < listOfFiles.length; i++) {
			int period = listOfFiles[i].getName().indexOf('.');
			fileNames[i] = listOfFiles[i].getName().substring(0, period);

		}
		int zero_count = 0;
		int[] results = new int[LENGTH_OF_RECORDS];

		for (int i = 0; i < fileNames.length; i++) {
			try {
				results[i] = Integer.parseInt(fileNames[i]);
			} catch (NumberFormatException nfe) {nfe.getMessage();};
		}
		Arrays.sort(results, 0, fileNames.length);
		System.out.println("LENGTH OF FILENAMES " + fileNames.length);
		for (int i = fileNames.length; i < results.length; i++) {
			results[i] = i + 1;
		}

		try {
			BufferedReader br = new BufferedReader(new FileReader("initalcombinedfiles.txt"));

			BufferedWriter bw = new BufferedWriter (new FileWriter("sample.txt"));

			int counter = 1;
			int completeCount = 1;

			String line = br.readLine();
			line = br.readLine();


			while (br.ready()){
				if (line.startsWith("Cited References Count:0")) {
					while(!line.startsWith("End Affiliations")&& br.ready()) {
						line = br.readLine();
					}
					line = br.readLine(); 
					line = br.readLine();
					zero_count++;


				} else if(line.startsWith("Cited References Count:")) {

					bw.newLine();
					bw.write("Counter: " + results[counter - 1] + "\t");
					bw.write("CompleteCount: " + completeCount + "\t");
					counter++;
					completeCount++;
					while (!line.startsWith("Affiliations")&& br.ready()) {
						if (line.startsWith("DI: End DI")) {
							System.out.println("started and ended with DI");
							bw.write(line + "\t");
							line = br.readLine();
						} else if (line.startsWith("DI:")) {
							while (!line.startsWith("End DI")) {
								bw.write(line + " ");
								line = br.readLine();
							}
						} else if (line.startsWith("End DI")) {
							bw.write("\t");
							line = br.readLine();
						} else if (line.startsWith("Vol: ")) { 
							String combinedLine = line;
							String orig = line;
							line = br.readLine();
							combinedLine = combinedLine + line;
							if(combinedLine.contains("Pgs: WOS:")) {
								bw.write("Vol: unknown" + "\t");
								bw.write("unknown issue" + "\t");
								String code = combinedLine.substring(combinedLine.indexOf("WOS:") + 4);
								bw.write("Pgs: N/A" + "\t" +beginning + code + end + "\t");
								System.out.println("unknown vol");
							} else {
								bw.write(orig + "\t");
								bw.write(line);
							}

							line = br.readLine();
						} else if (line.startsWith("Pgs: WOS:")) {
							bw.write("Pgs: N/A" + "\t");
							String code = line.substring(line.indexOf("WOS:") + 4);
							bw.write(beginning + code + end + "\t");
							line = br.readLine();
						} else if (line.startsWith("WOS:")) {
							String code = line.substring(line.indexOf("WOS:") + 4);
							bw.write(beginning + code + end + "\t");
							line = br.readLine();
						} else if (line.startsWith("Auths: Name: ")) {
							bw.write("No Authors found" + "\t");
							String[] parts = line.split("Auths: ");
							bw.write(parts[1] + "\t");
							line = br.readLine();
						} else {
							if(line.endsWith("\t")) {
								bw.write(line);
							} else {
								bw.write(line + "\t");
							}
							line = br.readLine();
						}
					}
					while(!line.startsWith("End Affiliations")&& br.ready()) {
						bw.write(line + " ; ");
						line = br.readLine();
					}
				} else if (line.startsWith("End Affiliations") && br.ready()) {
					bw.write("\t" + line);
					line = br.readLine();

				} else {
					while (!line.startsWith("Cited References Count:") && br.ready()) {
						line = br.readLine();
					}
				}
			}
			bw.write("\t" + "End Affiliations");
			bw.close();
			br.close();


		} catch (Exception e) {
			System.out.println("error" + e.getMessage());
			e.printStackTrace();
		}     
		System.out.println("Number of zero citations: " + zero_count);
	}

}
