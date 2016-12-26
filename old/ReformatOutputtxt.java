import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class ReformatOutputtxt {
	public static void main (String[] args){
		try {
			BufferedReader br = new BufferedReader(new FileReader("output.txt"));
			BufferedWriter bw = new BufferedWriter(new FileWriter("reformattedOutput.txt"));
			while (br.ready()) {
				String line = br.readLine();
				//			if(line.startsWith("PT")) {
				line = line.trim();
				if (line.contains("﻿VR ")){
					System.out.println("Found error");
					while (!line.startsWith("EFPT")){
						br.readLine();
					}
					br.readLine();
					bw.write(line);
					bw.newLine();
				}	
				else {
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
