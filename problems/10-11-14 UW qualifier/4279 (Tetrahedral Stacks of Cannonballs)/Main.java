import java.util.Scanner;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		
		int cases = in.nextInt();
		
		for(int aaa=1;aaa<=cases;aaa++) {
			int n = in.nextInt();
			int sum = 0;
			int layer = 0;
			for(int i=1;i<=n;i++) {
				layer += i;
				sum += layer;
			}
			System.out.printf("%d: %d %d\n", aaa, n, sum);
		}

	}

}
