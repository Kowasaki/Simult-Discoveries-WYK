import java.util.ArrayList;
import java.util.TreeMap;

//CC = one set of Parentheses
//example: (Deliagina et al., 2000; Drew et al., 1986)
public class CC {
	TreeMap<String, Integer> names;
	int dictionaryNumber;
	int numCCs;

	public CC(TreeMap<String, Integer> nombres, int dictNum, int nc){
		names = nombres;	// TreeMap of all authors and year, such that key = authorName, value = year
		// example: Deliagina = 2000
		dictionaryNumber = dictNum; // same as the parent article #
		numCCs = nc;	// number of parentheses sets per dictionary/per parent article
		// aka number of parenthetical cocitations per parent article
	}
}
