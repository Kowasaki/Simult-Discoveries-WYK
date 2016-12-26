import java.awt.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Comparator;


// (references) Article object class
public class Article implements Comparable{

	//article attributes
	String title;
	int year;
	ArrayList<String> authors;	//reference article's authors
	String id;	//each reference has a unique String id in the format "ParentArticleNumber.ReferenceNumber"
	ArrayList<Integer> parentArticle;	//all numbers of parent articles the reference is cited in
	int parentNumber;	//unique identifying number of the parent article
	int numCited = 0;	//number of times the reference is cited
	String journal; //added later!
	ArrayList<Article> references; //added later for parent articles

	//this constructor is for references
	public Article(String t, int y, ArrayList<String> aut, String ID, ArrayList<Integer> pa, int nc, int parentNum){
		title = t.toUpperCase().trim();
		year = y;
		authors = aut;
		id = ID;
		parentArticle = pa;
		numCited = nc;		
		parentNumber = parentNum;
	}

	//this constructor is a later edited version for references
	public Article(String t, int y, ArrayList<String> aut, String ID, ArrayList<Integer> pa){
		title = t.toUpperCase().trim();
		year = y;
		authors = aut;
		id = ID;
		parentArticle = pa;
	}

	//this constructor is for the parent article
	public Article(String t, int y, ArrayList<String> aut, String ID, int parentNum, String journ, ArrayList<Article> refs) {
		title = t.toUpperCase().trim();
		year = y;
		authors = aut;
		id = ID;
		parentNumber = parentNum;
		journal = journ;
		references = refs;
	}


	public String toStringParent() {
		String result = "";
		String authString = "";
		for(int i = 0; i < authors.size(); i++) {
			String auth = authors.get(i);
			authString += auth + "; ";
		}

		String refMaster = "<References for ParArticle> " + "\n";
		for(int i = 0; i < references.size(); i++) {
			String refTitle = references.get(i).title;
			int refYr = references.get(i).year;
			String refAuths = "";
			for(int j = 0; j < references.get(i).authors.size(); j++) {
				String auth = references.get(i).authors.get(j);
				refAuths += auth + "; ";
			}
			String refID = references.get(i).id;
			String refParArticles = "";
			for(int k = 0; k < references.get(i).parentArticle.size(); k++) {
				String refPar = references.get(i).parentArticle.get(k).toString();
				refParArticles += refPar + "; ";
			}
			refMaster += "<Reference> " + "\n" + "<Ref Title> " + refTitle + "\n" + "<Ref Year> " + refYr + "\n" + 
					"<Ref Authors> " + refAuths + "\n" + "<Ref ID> " + refID + "\n" + "<Ref Parent Articles> " +
					refParArticles + "\n" + "</Reference> " + "\n" + "\n";

		}
		result = "<Parent Number> " + parentNumber + "\t" + "<Parent Journal> " + journal + "\t" +
				"<Parent Title> " + title + "\t" + "<Parent Year> " + year + "\t" + "<Parent Authors> " + 
				authString + "\t" + "<Parent ID> " + id + "\n" + refMaster + "</End Par References>" +
				"\n" + "\n";
		return result;
	}
	//	public Article(String t, int y, ArrayList<String> aut, String ID, ArrayList<Integer> pa){
	//		title = t.toUpperCase().trim();
	//		year = y;
	//		authors = aut;
	//		id = ID;
	//		parentArticle = pa;
	//	}

	//used for sorting/transversing purposes
	@Override
	public int compareTo(Object o) {
		if (o == null) {
			throw new NullPointerException();
		}
		if (o.getClass() != Article.class) {
			throw new ClassCastException();
		}
		Article other = (Article) o;
		if (other.year == this.year) {
			//does not account for articles that have the same
			//title but are inherently different
			return this.title.compareTo(other.title);
		} else if (this.year > other.year) {
			return 1;
		} else {
			return -1;
		}
	}

	//used for sorting/transversing purposes
	@Override
	public boolean equals(Object o) {
		if (o == null) {
			throw new NullPointerException();
		}
		if (o.getClass() != Article.class) {
			throw new ClassCastException();
		}
		Article other = (Article) o;
		if (other.year == this.year && other.title.equals(this.title) && other.id.equals(this.id)) {
			return true;
		} else {
			return false;
		}
	}

}

