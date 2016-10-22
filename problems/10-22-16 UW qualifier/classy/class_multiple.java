import java.util.Arrays;
import java.util.Scanner;

public class class_multiple {

	class Blah implements Comparable<Blah> {
		String name, stuff;

		Blah(String line) {
			String[] spl = line.split("\\s+");
			name = spl[0].substring(0, spl[0].length() - 1);
			StringBuilder sb = new StringBuilder();
			for (String c : spl[1].split("-"))
				sb.insert(0, c.charAt(0));
			while (sb.length() < 10)
				sb.append('m');
			stuff = sb.toString();
		}

		public int compareTo(Blah b) {
			int comp = b.stuff.compareTo(stuff);
			if (comp == 0)
				comp = name.compareTo(b.name);
			return comp;
		}
	}

	private void work() {
		Scanner sc = new Scanner(System.in);
		int cases = sc.nextInt();
		for(int casei=0;casei<cases;casei++) {
			int nc = sc.nextInt();
			for (int tc = 1; tc <= nc; tc++) {
				int n = sc.nextInt();
				sc.nextLine();
				Blah[] b = new Blah[n];
				for (int i = 0; i < b.length; i++) {
					b[i] = new Blah(sc.nextLine().trim());
				}
				Arrays.sort(b);
				for (Blah blah : b) {
					System.out.println(blah.name);
				}
				System.out.println("==============================");
			}
		}
		sc.close();
	}

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		new class_multiple().work();
		long t = System.currentTimeMillis() - startTime;
		/*
		System.err.printf("Elapsed time: %02d:%02d:%02d.%03d\n",
				t / 1000 / 3600, ((t / 1000) % 3600) / 60, (t / 1000) % 60,
				t % 1000);
		*/
	}

}
