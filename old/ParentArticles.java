import java.util.ArrayList;

/*Parent article object*/
public class ParentArticles implements Comparable{

	int parNum; 
	String DOI; 
	String title; 
	String url;
	ArrayList<Article> references;

	public ParentArticles(int parNum, String DOI, String title, String url){
		this.parNum = parNum;
		this.DOI = DOI;
		this.title = title;
		this.url = url;		
		this.references = new ArrayList<Article>();
	}

	@Override 
	public int compareTo(Object o) {
		if (o == null) {
			throw new NullPointerException();
		} 
		if (o.getClass() != ParentArticles.class) {
			throw new ClassCastException();
		}
		ParentArticles other = (ParentArticles) o;
		if (other.parNum == this.parNum) {
			return 0;
		} else if (this.parNum > other.parNum) {
			return 1;
		} else {
			return -1;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			throw new NullPointerException();
		} 
		if (o.getClass() != ParentArticles.class) {
			throw new ClassCastException();
		}
		ParentArticles other = (ParentArticles) o;
		return other.parNum == this.parNum;
	}
}
