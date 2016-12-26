public class Tester {
	public Tester() {
		
		
	}
	
	public static void stepOne() {
		int[] arra = {1, 2, 3};
		stepTwo(arra);
		
		System.out.println(arra[0] + arra[1] + arra[2] + " ");
	}
	
	public static int[] stepTwo(int[] arr) {
		arr[1] = 5;
		return arr;
	}
	
	public static void main (String[] args) {
		stepOne();
	}
}