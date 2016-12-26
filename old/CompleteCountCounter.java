import java.io.BufferedReader;
import java.io.FileReader;

public class CompleteCountCounter {
	public static void main(String[] args) {
	try {
	BufferedReader br = new BufferedReader(new FileReader("citations_output.txt"));
	String line = br.readLine();
	int count = 0;
	int newcount = 0;
	while(br.ready()) {
		if (line.contains("CompleteCount")) {
			String num = line.substring(line.indexOf("CompleteCo"), line.indexOf("CompleteCo") + 19);
			System.out.println(num);
			count++;
		}
		
		if (line.contains("?PT")) {
			newcount++;
		}
		line = br.readLine();
	}
	System.out.println("COUNT: " + count);
	System.out.println("NEW COUNT: " + newcount);
	br.close();
	} catch(Exception e) {
		e.printStackTrace();
	}
	}
}
