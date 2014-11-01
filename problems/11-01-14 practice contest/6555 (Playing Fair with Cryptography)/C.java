import java.util.*;

public class C
{
	public static char[][] grid = new char[5][5];
	public static rc[] loc = new rc[26];
	public static int insert;
	public static char e1, e2;
	
	public static void fillGrid(String s)
	{
		boolean [] used = new boolean[26];
		int k=0;
	
		for(int i=0; i<s.length(); i++) {
			if (Character.isLetter(s.charAt(i))) {
				char ch = Character.toUpperCase(s.charAt(i));
				if (ch == 'J')
					ch = 'I';
				if (!used[ch-'A']) {
					grid[k/5][k%5] = ch;
					used[ch-'A'] = true;
					loc[ch-'A'].r = k/5;
					loc[ch-'A'].c = k%5;
					k++;
				}
			}
		}
		used[9] = true;				// no 'J' in the grid
		for(int i=0; i<26; i++) {
			if (!used[i]) {
				grid[k/5][k%5] = (char)('A'+i);
				loc[i].r = k/5;
				loc[i].c = k%5;
				k++;
			}
		}
		loc[9] = loc[8];
	}
	
	public static void encrypt(char p1, char p2)
	{
		int i1 = p1-'A';
		int i2 = p2-'A';
		if (loc[i1].r == loc[i2].r) {
			e1 = grid[loc[i1].r][(loc[i1].c+1)%5];
			e2 = grid[loc[i2].r][(loc[i2].c+1)%5];
		}
		else if (loc[i1].c == loc[i2].c) {
			e1 = grid[(loc[i1].r+1)%5][loc[i1].c];
			e2 = grid[(loc[i2].r+1)%5][loc[i2].c];
		}
		else {
			e1 = grid[loc[i1].r][loc[i2].c];
			e2 = grid[loc[i2].r][loc[i1].c];
		}
	}
	
	public static char nextInsert(char ch)
	{
		if (ch-'A' == insert)
			insert = (insert+1)%26;
		if (insert == 9)			// skip over 'J'
			insert++;
		char ans = (char)('A'+insert);
		insert = (insert+1)%26;
		if (insert == 9)			// skip over 'J'
			insert++;
		return ans;
	}
	
	public static void main(String [] args)
	{
		int ncase;
		String key, plain, tmp;
		Scanner in = new Scanner(System.in);
	
		for(int i=0; i<26; i++)
			loc[i] = new rc();
		ncase = in.nextInt();
		in.nextLine();
		for(int icase=1; icase<=ncase; icase++) {
			key = in.nextLine();
			key = key.toUpperCase();
			fillGrid(key);
			tmp = in.nextLine();
			tmp = tmp.toUpperCase();
			plain = "";
			for(int i=0; i<tmp.length(); i++) {
				if (Character.isLetter(tmp.charAt(i)))
					plain += tmp.charAt(i);
			}
			System.out.print("Case " + icase + ": ");
			insert = 0;
			for(int i=0; i<plain.length(); i+=2) {
				if (i+2 > plain.length())
					encrypt(plain.charAt(i), nextInsert(plain.charAt(i)));
				else if (plain.charAt(i) == plain.charAt(i+1)) {
					encrypt(plain.charAt(i), nextInsert(plain.charAt(i)));
					i--;
				}
				else
					encrypt(plain.charAt(i), plain.charAt(i+1));
				System.out.print("" + e1 +  e2);
			}
			System.out.println();
		}
	}
}

class rc {
	public int r,c;
}
