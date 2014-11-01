import java.util.*;

public class A
{
	public static final int MAXB = 50;
	public static final char EMPTY = ' ';
	public static final char WATER = '.';
	public static final char[] SHAPE = {'w', 'X', '<', '^', '>', 'v', 'O'};

	public static ship[] ships = new ship[11];

	public static board b = new board();
	public static long numSol;

	public static char[][] sq1 = new char[100][7];
	public static char[][][][] sq2 = new char[100][100][7][7];

	public static void init()
	{
		for(int i=0; i<11; i++)
			ships[i] = new ship();
		ships[0].len = 4;
		ships[0].hparts[0] = 2;
		ships[0].hparts[1] = ships[0].hparts[2] = 1;
		ships[0].hparts[3] = 4;
		ships[0].vparts[0] = 3;
		ships[0].vparts[1] = ships[0].vparts[2] = 1;
		ships[0].vparts[3] = 5;
		ships[1].len = 3;
		ships[1].hparts[0] = 2;
		ships[1].hparts[1] = 1;
		ships[1].hparts[2] = 4;
		ships[1].vparts[0] = 3;
		ships[1].vparts[1] = 1;
		ships[1].vparts[2] = 5;
		ships[2] = ships[1];
		ships[3].len = 2;
		ships[3].hparts[0] = 2;
		ships[3].hparts[1] = 4;
		ships[3].vparts[0] = 3;
		ships[3].vparts[1] = 5;
		ships[4] = ships[3];
		ships[5] = ships[3];
		ships[6].len = 1;
		ships[6].hparts[0] = 6;
		ships[7] = ships[6];
		ships[8] = ships[6];
		ships[9] = ships[6];
		ships[10] = ships[6];		// dummy ship, makes place routine easier
	}

	public static boolean canPlaceHor(int npiece, int r, int c, int[] rcount, int[] ccount)
	{
		int len = ships[npiece].len;
		if (rcount[r] < len)
			return false;
		if (len > 11-c)
			return false;
		for(int i=0; i<len; i++) {
			if (ccount[c+i] == 0)
				return false;
		}
		for(int i=-1; i<=1; i++) {
			for(int j=-1; j<=len; j++) {
				if (b.grid[r+i][c+j] > 0)
					return false;
			}
		}
		return true;
	}

	public static boolean canPlaceVer(int npiece, int r, int c, int[] rcount, int[] ccount)
	{
		int len = ships[npiece].len;
		if (ccount[c] < len)
			return false;
		if (len > 11-r)
			return false;
		for(int i=0; i<len; i++) {
			if (rcount[r+i] == 0)
				return false;
		}
		for(int i=-1; i<=1; i++) {
			for(int j=-1; j<=len; j++) {
				if (b.grid[r+j][c+i] > 0)
					return false;
			}
		}
		return true;
	}

	public static void place(int r, int c, int npiece, int[] rcount, int[] ccount)
	{
		if (npiece == 10) {
			numSol++;
			int l=0;
			for(int i=1; i<=10; i++) {
				for(int j=1; j<=10; j++) {
					int val = (b.grid[i][j] > 0) ? b.grid[i][j] : 0;
					if(sq1[l][val]< 2)
						sq1[l][val]++;
					l++;
				}
			}
			for(int i=0; i<100; i++) {
				int rr = i/10 + 1, cc = i%10 + 1;
				int val1 = (b.grid[rr][cc] > 0) ? b.grid[rr][cc] : 0;
				for(int j=i+1; j<100; j++) {
					rr = j/10 + 1; cc = j%10 + 1;
					int val2 = (b.grid[rr][cc] > 0) ? b.grid[rr][cc] : 0;
					if(sq2[i][j][val1][val2]< 2)
						sq2[i][j][val1][val2]++;
				}
			}
			return;
		}
		int len = ships[npiece].len;
		if (c > 10) {
			r++; c=1;
		}
		int savr = r, savc = c;
		while (r < 11) {
			if (canPlaceHor(npiece, r, c, rcount, ccount)) {
				rcount[r] -= len;
				for(int i=0; i<len; i++) {
					b.grid[r][c+i] = ships[npiece].hparts[i];
					ccount[c+i]--;
				}
				for(int i=-1; i<=len; i++) {
					b.grid[r-1][c+i]--;
					b.grid[r+1][c+i]--;
				}
				b.grid[r][c-1]--;
				b.grid[r][c+len]--;
	
				if (ships[npiece+1].len == len)
					place(r, c+len+1, npiece+1, rcount, ccount);
				else
					place(1, 1, npiece+1, rcount, ccount);
	
				rcount[r] += len;
				for(int i=0; i<len; i++) {
					b.grid[r][c+i] = 0;
					ccount[c+i]++;
				}
				for(int i=-1; i<=len; i++) {
					b.grid[r-1][c+i]++;
					b.grid[r+1][c+i]++;
				}
				b.grid[r][c-1]++;
				b.grid[r][c+len]++;
			}
			if (++c > 10) {
				r++;
				c=1;
			}
		}
		if (len == 1)
			return;
		r = savr; c = savc;
		while (r < 12-len) {
			if (canPlaceVer(npiece, r, c, rcount, ccount)) {
				ccount[c] -= len;
				for(int i=0; i<len; i++) {
					b.grid[r+i][c] = ships[npiece].vparts[i];
					rcount[r+i]--;
				}
				for(int i=-1; i<=len; i++) {
					b.grid[r+i][c-1]--;
					b.grid[r+i][c+1]--;
				}
				b.grid[r-1][c]--;
				b.grid[r+len][c]--;

				if (ships[npiece+1].len == len)
					place(r, c+2, npiece+1, rcount, ccount);
				else
					place(1, 1, npiece+1, rcount, ccount);

				ccount[c] += len;
				for(int i=0; i<len; i++) {
					b.grid[r+i][c] = 0;
					rcount[r+i]++;
				}
				for(int i=-1; i<=len; i++) {
					b.grid[r+i][c-1]++;
					b.grid[r+i][c+1]++;
				}
				b.grid[r-1][c]++;
				b.grid[r+len][c]++;
			}
			if (++c > 10) {
				r++;
				c=1;
			}
		}
		return;
	}

	public static void main(String [] args)
	{
		Scanner in = new Scanner(System.in);
		int ncase;
		int [] rcount = new int [11];
		int [] ccount = new int [11];
		ncase = in.nextInt();
		init();
		for(int icase=1; icase<=ncase; icase++) {
			for(int i=1; i<11; i++)
				rcount[i] = in.nextInt();
			for(int i=1; i<11; i++)
				ccount[i] = in.nextInt();
			for(int i=0; i<12; i++) {
				b.grid[0][i] = b.grid[11][i] = -1;
				b.grid[i][0] = b.grid[i][11] = -1;
			}
			for(int i=1; i<=10; i++)
				for(int j=1; j<=10; j++)
					b.grid[i][j] = 0;
			for(int i=0; i<100; i++)
				for(int l=0; l<7; l++)
					sq1[i][l] = 0;
			for(int i=0; i<100; i++)
				for(int j=0; j<100; j++)
					for(int k=0; k<7; k++)
						for(int l=0; l<7; l++)
						sq2[i][j][k][l] = 0;
			numSol = 0;
			place(1, 1, 0, rcount, ccount);
			System.out.print("Case " + icase + ": " + numSol + " ");
			if (numSol == 1)
				System.out.println(0);
			else {
				int i=0, j=0, k=0, l=0;
				while (i < 100) {
					l=0;
					while (l<7) {
						if (sq1[i][l] == 1)
							break;
						l++;
					}
					if (l<7)
						break;
					i++;
				}
				if (i < 100) {
					System.out.println(1 + " (" + (i/10+1) + ',' + (i%10+1) + ")=" + SHAPE[l]);
					continue;
				}
	
				i=0;
				while (i < 100) {
					k = 0;
					while (k<7) {
						j=i+1;
						while (j < 100) {
							l = 0;
							while (l < 7) {
								if (sq2[i][j][k][l] == 1)
									break;
								l++;
							}
							if (l < 7)
								break;
							j++;
						}
						if (j<100)
							break;
						k++;
					}
					if (k < 7)
						break;
					i++;
				}
				if (i < 100) {
					System.out.print(2 + " (" + (i/10+1) + ',' + (i%10+1) + ")=" + SHAPE[k]);
					System.out.println(" (" + (j/10+1) + ',' + (j%10+1) + ")=" + SHAPE[l]);
					continue;
				}
				else
					System.out.println("too ambiguous");
			}
		}
	}
}

class board {
	public short[][] grid;

	board()
	{
		grid = new short[12][12];
	}
}

class ship {
	public int len;
	public short[] hparts = new short[4];
	public short[] vparts = new short[4];
}
