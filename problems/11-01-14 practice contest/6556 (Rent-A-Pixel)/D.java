// Solution to RentAPixel

import java.util.*;

class Block {
    public int row,col;
    public Block(int r, int c) {
        row = r; col = c;
    }
    public String toString() {
        return "("+row+","+col+")";
    }
}

public class D {
    public static int n;
    public static Block[] req;
    public static int caseNum;
    public static Scanner in;
    public static LinkedList<Block> convex;

    public static void main(String[] args) {
        in = new Scanner(System.in);
        caseNum = 0;
        while (true) {
            n = in.nextInt();
            if (n==0) break;
            caseNum++;
            req = new Block[n];
            for (int i = 0; i < n; i++) {
                req[i] = new Block(in.nextInt(),in.nextInt());
            }
            Arrays.sort(req, new Comparator<Block>() {
                public int compare(Block p1, Block p2) {
                    if (p1.row < p2.row) return -1;
                    else if (p1.row > p2.row) return 1;
                    else return p1.col - p2.col;
                }
            } );
            convex = new LinkedList<Block>();
            System.out.print("Case " + caseNum + ":");
            buildlist();
        }
    }

    public static void buildlist() {
        // count rows; there can be no gaps without causing
        // a disconnected convex hull. Keep track of
        // first and last cell in each row:
        int rowcount = 1; // must be at least one
        int currow = req[0].row;
        ArrayList<Block> first = new ArrayList<Block>();
        ArrayList<Block> last = new ArrayList<Block>();
        first.add(req[0]); // first in first row
        for (int i=1; i < n; i++) {
            if (req[i].row != currow) {
                currow = req[i].row;
                rowcount++;
                first.add(req[i]); // first in this row
                last.add(req[i-1]); // last in previous row
            }
        }
        last.add(req[n-1]); // last in last row

        // Go down the right side, adding points to the hull
        // when right side increases:
        convex.add(req[0]); // top left = first block
        int max=last.get(0).col; // top right = second block
        convex.add(new Block(req[0].row,max+9));
        for (int i=1; i < rowcount; i++) { // succeeding rows...
            if (last.get(i).col > max) { // hull moves to right:
                convex.add(new Block(last.get(i).row,max+9));
                max = last.get(i).col;
                convex.add(new Block(last.get(i).row,max+9));
            }
        }
        // Now go UP right side, adding points to a stack;
        // later, pop stack onto convex hull:
        Stack<Block> st = new Stack<Block>();
        max=last.get(rowcount-1).col; // last cell in bottom row
        st.push(new Block(last.get(rowcount-1).row+9,max+9));
        for (int i = rowcount-2; i >= 0; i--) { //preceding rows
            if (last.get(i).col > max) {
                st.push(new Block(last.get(i).row+9,max+9));
                max = last.get(i).col;
                st.push(new Block(last.get(i).row+9,max+9));
            }
        }
        while (!st.isEmpty()) {
            convex.add(st.pop());
        }

        // Reverse the process to do left side:
        // Go up the left side, adding points to the hull
        // when left  side decreases:
        convex.add(new Block(first.get(rowcount-1).row+9,
                              first.get(rowcount-1).col)); // bottom left 
        int min=first.get(rowcount-1).col; // top right = second block
        for (int i= rowcount-2; i>0; i--) { // preceding rows...
            if (first.get(i).col < min) { // hull moves to left:
                convex.add(new Block(first.get(i).row+9,min));
                min = first.get(i).col;
                convex.add(new Block(first.get(i).row+9,min));
            }
        }

        // Now go DOWN right side, adding points to a stack;
        // later, pop stack onto convex hull:
        st = new Stack<Block>();
        min=first.get(0).col; // first cell in top row
        for (int i = 1; i < rowcount; i++) {
            if (first.get(i).col < min) {
                st.push(new Block(first.get(i).row,min));
                min = first.get(i).col;
                st.push(new Block(first.get(i).row,min));
            }
        }
        while (!st.isEmpty()) {
            convex.add(st.pop());
        }
            
        for (Block b: convex) {
            System.out.print(" "+b.row+" "+b.col);
        }
        System.out.println();
    }
}
