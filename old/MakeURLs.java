import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MakeURLs {
	/*Makes URLS for Macros via the WOS id*/
	//formats

	public static void main (String [] args){

		String beginning =  "http://apps.webofknowledge.com/InboundService.do?SID=2FmBpICBDXy4g74gXLu&product=WOS&UT=WOS%3A";
		String end = "&SrcApp=EndNote&DestFail=http%3A%2F%2Fwww.webofknowledge.com&Init=Yes&action=retrieve&SrcAuth=ResearchSoft&customersID=ResearchSoft&Func=Frame&IsProductCode=Yes&mode=FullRecord";


		try {
			FileReader fr = new FileReader("sample.txt");
			BufferedReader br = new BufferedReader(fr);

			FileWriter writer = new FileWriter("URLsNatureGeneticsAll.txt"); 
			BufferedWriter bw = new BufferedWriter (writer);

			while (br.ready()){
				String line = br.readLine();
				if (line.contains("WOS:")){
					String code = line.substring(line.indexOf("WOS:") + 4);
					System.out.println(code);
					bw.write(beginning + code + end);	
					bw.newLine();
				}
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
