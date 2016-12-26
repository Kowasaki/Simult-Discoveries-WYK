import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class Numbering {
	ArrayList<String> lines;

	public Numbering() {
		lines = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader("alljournalsExcel.txt"));

			String line = br.readLine();
			while(br.ready()) {
				lines.add(line);
				line = br.readLine();
			}
		} catch(Exception e) {
			System.out.println("error: " + e.getMessage());
		}
	}

	public ArrayList<String> getLines() {
		return lines;
	}

	public static void main(String args[]) {

	}
}