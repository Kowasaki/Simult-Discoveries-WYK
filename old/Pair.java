import java.util.ArrayList;

/*Pair object*/
public class Pair implements Comparable {
	Article one;
	Article two;
	int cocite;
	double jacc;
//	ArrayList<String> comParents;
	ArrayList<Integer> comParents;
	ArrayList<ParentArticles> finalParents;
	boolean inline;


	public Pair(Article one, Article two) {
		if (one == null || two == null) {
			throw new NullPointerException();
		}
		this.one = one;
		this.two = two;
		cocite = 0;
		jacc = 0;
//		comParents = new ArrayList<String>();
		comParents = new ArrayList<Integer>();
		finalParents = new ArrayList<ParentArticles>();
		inline = false;
	}

	@Override
	public int compareTo(Object o) {
		if (o == null) {
			throw new NullPointerException();
		}
		if (o.getClass() != Pair.class) {
			throw new ClassCastException();
		}
		Pair other = (Pair) o;
		if (one.compareTo(other.one) + two.compareTo(other.two) == 0) {
			return 0;
		} else if (one.compareTo(other.one) == 0 && two.compareTo(other.two) > 0) {
			return 2;
		} else if (one.compareTo(other.one) == 0 && two.compareTo(other.two) < 0){
			return -2;
		} else if (one.compareTo(other.one) > 0 && two.compareTo(other.two) == 0) {
			return 1;
		} else if (one.compareTo(other.one) < 0 && two.compareTo(other.two) == 0) {
			return -1;
		} else if (one.compareTo(other.one) + two.compareTo(other.two) >= 2) {
			return 3;
		} else {
			return -3;
		}

	}
}