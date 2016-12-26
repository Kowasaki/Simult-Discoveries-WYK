import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

//returns correctly formatted DOI input for pdf extraction
public class FormatDOI {
	public static void main(String[] args) {
		try {
			BufferedReader br = new BufferedReader(new FileReader("doi.txt"));
			FileWriter fw = new FileWriter("doiFormatted.txt");
			BufferedWriter bw = new BufferedWriter(fw);

			String line;
			while(br.ready()) {
				line = br.readLine();
				String doi = line.substring(line.indexOf("D:") + 2, line.indexOf("URL")).trim();
				if(!doi.equals("End DI")) {
					if (doi.startsWith("ARTN")) {
						doi = doi.substring(doi.indexOf("10.")).trim();
					}
					bw.write(doi);
					bw.newLine();
				}
				else {
					String title = line.substring(line.indexOf("T:") +2).trim();
					bw.write(title);
					bw.newLine();
				}
			}
			bw.close();
			fw.close();
			br.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
