import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Main {

	/**
	 * @param args
	 */

	public static boolean good = true;
	public static HashSet<Character> chars = new HashSet<Character>();
	public static Expression ROOT = null;
	public static final String goodChars = "CNKADEJ";
	public static String result = "";
	public static int count = 0;

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);

		ArrayList<String> lines = new ArrayList<String>();

		while (true) {
			String line = in.nextLine();
			if (line.length() == 0)
				break;
			lines.add(line);
		}

		for (String line : lines) {
			good = true;
			chars.clear();
			result = "";
			int charsRead = read(line, null);
			if(!good) {
				System.out.println(line + " is invalid: " + result);
				continue;
			}
			if(charsRead < line.length()) {
				System.out.println(line + " is invalid: extraneous text");
				continue;
			}
			if(!DFS(ROOT)) {
				System.out.println(line + " is invalid: insufficient operands");
				continue;
			}
			count = 0;
			ArrayList<Character> charList = new ArrayList<Character>();
			for(Character c : chars)
				charList.add(c);
			makeMap(charList, 0, new HashMap<Character, Boolean>());
			//System.out.println(line + " " +  count + " " + chars.size());
			if(count == 0) {
				System.out.println(line + " is valid: contradiction");
			} else if(count == 1<<charList.size()) {
				System.out.println(line + " is valid: tautology");
			} else {
				System.out.println(line + " is valid: contingent");
			}
		}
	}

	private static void makeMap(ArrayList<Character> chars2, int index, HashMap<Character, Boolean> map) {
		if(index >= chars2.size()) {
			count += ROOT.evaluate(map)?1:0;
			return;
		}
		Character c = chars2.get(index);
		map.put(c, true);
		makeMap(chars2, index+1, map);
		map.put(c, false);
		makeMap(chars2, index+1, map);
	}

	private static boolean DFS(Expression rOOT2) {
		if(rOOT2 == null)
			return false;
		if(Character.isLowerCase(rOOT2.c))
			return true;
		if(rOOT2.c == 'N')
			return DFS(rOOT2.left);
		return DFS(rOOT2.left) && DFS(rOOT2.right);
	}

	private static int read(String line, Expression parent) {
		if (!good || line.length() == 0)
			return 0;
		char cur = line.charAt(0);
		Expression e = new Expression(cur, false);
		if (parent == null)
			ROOT = e;
		int charsUsed = 1;
		if (Character.isLowerCase(cur)) {
			chars.add(cur);
		} else if (!goodChars.contains("" + cur)) {
			result = "invalid character";
			good = false;
			return 0;
		} else {
			if (cur == 'N') {
				if(ROOT == e) {
					e = new Expression(cur, true);
					ROOT = e;
				} else {
					e = new Expression(cur, true);
				}
				charsUsed += read(line.substring(1), e);
				if (!good)
					return 0;
			} else {
				charsUsed += read(line.substring(1), e);
				if (!good)
					return 0;
				charsUsed += read(line.substring(charsUsed), e);
				if (!good)
					return 0;
			}
		}
		if (parent != null) {
			if (parent.left == null)
				parent.left = e;
			else if (parent.right == null)
				parent.right = e;
			else {
				result = "extraneous text";
				good = false;
				return 0;
			}
		}
		return charsUsed;
	}

}

class Expression {
	boolean negate;
	char c;
	Expression left;
	Expression right;

	public Expression(char c, boolean negate) {
		this.c = c;
		this.negate = negate;
	}

	public boolean evaluate(HashMap<Character, Boolean> map) {
		boolean result = false;
		switch (c) {
		case 'N':
			result = left.evaluate(map);
			break;
		case 'C':
			result = !left.evaluate(map) || right.evaluate(map);
			break;
		case 'K':
			result = left.evaluate(map) && right.evaluate(map);
			break;
		case 'A':
			result =  left.evaluate(map) || right.evaluate(map);
			break;
		case 'D':
			result = !(left.evaluate(map) && right.evaluate(map));
			break;
		case 'E':
			result = left.evaluate(map) == right.evaluate(map);
			break;
		case 'J':
			result = (left.evaluate(map) && !right.evaluate(map))
					|| (!left.evaluate(map) && right.evaluate(map));
			break;
		}
		// single lowercase char
		if(Character.isLowerCase(c)) {
			result = map.get(c);
		}
		return negate ? !result : result;
	}
}