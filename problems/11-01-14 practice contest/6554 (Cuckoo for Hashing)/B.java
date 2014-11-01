
import java.util.*;
import java.io.*;


public class B {

    public static void insert(int x, int[] T1, int n1, int[] T2, int n2) {
        int i = x%n1;
        int curr = T1[i];
        T1[i] = x;
        if (curr != -1) {
            insert(curr, T2, n2, T1, n1);
        }
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int n1 = input.nextInt();
        int n2 = input.nextInt();
        int m = input.nextInt();
        int caseNum = 1;
        while(n1 > 0) {
            int[] T1 = new int[n1];
            int[] T2 = new int[n2];
            for(int i = 0; i < n1; i++) {
                T1[i] = -1;
            }
            for(int i = 0; i < n2; i++) {
                T2[i] = -1;
            }
            for(int i = 0; i < m; i++) {
                insert(input.nextInt(), T1, n1, T2, n2);
            }
            System.out.println("Case "+caseNum+":");
            boolean entries1 = false;
            boolean entries2 = false;
            for(int i = 0; i < n1; i++) {
                if (T1[i] >= 0) entries1 = true;
            }
            for(int i = 0; i < n2; i++) {
                if (T2[i] >= 0) entries2 = true;
            }
            if(entries1) {
                System.out.println("Table 1");
                for(int i = 0; i < n1; i++) {
                    if (T1[i] >= 0) System.out.println(i+":"+T1[i]);
                }
            }
            if(entries2) {
                System.out.println("Table 2");
                for(int i = 0; i < n2; i++) {
                    if (T2[i] >= 0) System.out.println(i+":"+T2[i]);
                }
            }
            caseNum++;
            n1 = input.nextInt();
            n2 = input.nextInt();
            m = input.nextInt();
        }
    }
}
