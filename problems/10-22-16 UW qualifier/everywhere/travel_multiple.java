import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class travel_multiple {

	private void work() {
		Scanner sc = new Scanner(System.in);
		int cases = sc.nextInt();
		for(int casei=0;casei<cases;casei++) {
			Set<String> s = new HashSet<String>();
			int nc = sc.nextInt();
			while(nc-- > 0){
				int n = sc.nextInt();
				s.clear();
				while(n-- > 0){
					s.add(sc.next());
				}
				System.out.println(s.size());
			}
		}
		sc.close();
	}

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		new travel_multiple().work();
		long t = System.currentTimeMillis() - startTime;
		/*
		System.err.printf("Elapsed time: %02d:%02d:%02d.%03d\n",
				t / 1000 / 3600, ((t / 1000) % 3600) / 60, (t / 1000) % 60,
				t % 1000);
		*/
	}

}
