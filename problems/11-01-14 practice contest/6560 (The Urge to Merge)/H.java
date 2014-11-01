// Tom Wexler

import java.util.*;
import java.io.*;

public class H {
    
    public static void main(String[] args) {
        
        long time = System.currentTimeMillis();
        Scanner input = new Scanner(System.in);
        int n = input.nextInt();
        input.nextLine();
        int[][] A = new int[1][1];
        int caseNum = 1;
        while(n > 0) {
            A = new int[n][3];
            for(int y = 0; y < 3; y++) {
                for(int x = 0; x < n; x++) {
                    A[x][y] = input.nextInt();
                }
            }
            int[] xxx = new int[n];
            int[] xoo = new int[n];
            int[] oxo = new int[n];
            int[] oox = new int[n];
            int[] xxo = new int[n];
            int[] xox = new int[n];
            int[] oxx = new int[n];
            xoo[0] = 0;
            oxo[0] = 0;
            oox[0] = 0;
            xxo[0] = A[0][0]*A[0][1];
            xox[0] = 0;
            oxx[0] = A[0][1]*A[0][2];
            xxx[0] = max(A[0][1]*A[0][2], A[0][0]*A[0][1]);
            for(int i = 1; i < n; i++) {
                xoo[i] = max(xxx[i-1], oxx[i-1]+A[i-1][0]*A[i][0]);
                oxo[i] = max(xxx[i-1], xox[i-1]+A[i-1][1]*A[i][1]);
                oox[i] = max(xxx[i-1], xxo[i-1]+A[i-1][2]*A[i][2]);
                xxo[i] = max(xxx[i-1]+A[i][0]*A[i][1],
                             xoo[i],
                             oxo[i],
                             oox[i-1]+A[i-1][0]*A[i][0]+A[i-1][1]*A[i][1]);
                xox[i] = max(xxx[i-1],
                             xoo[i],
                             oox[i],
                             oxo[i-1]+A[i-1][0]*A[i][0]+A[i-1][2]*A[i][2]);
                oxx[i] = max(xxx[i-1]+A[i][1]*A[i][2],
                             oxo[i],
                             oox[i],
                             xoo[i-1]+A[i-1][1]*A[i][1]+A[i-1][2]*A[i][2]);
                if(i > 1) 
                    xxx[i] = max(xxo[i],
                                 xox[i],
                                 oxx[i],
                                 xxx[i-2]+A[i][0]*A[i-1][0]+A[i][1]*A[i-1][1]+A[i][2]*A[i-1][2],
                                 xxo[i-1]+A[i][0]*A[i][1]+A[i][2]*A[i-1][2],
                                 oxx[i-1]+A[i][1]*A[i][2]+A[i][0]*A[i-1][0]);
                else
                    xxx[i] = max(xxo[i],
                                 xox[i],
                                 oxx[i],
                                 A[i][0]*A[i-1][0]+A[i][1]*A[i-1][1]+A[i][2]*A[i-1][2],
                                 xxo[i-1]+A[i][0]*A[i][1]+A[i][2]*A[i-1][2],
                                 oxx[i-1]+A[i][1]*A[i][2]+A[i][0]*A[i-1][0]);
            }
            System.out.println("Case "+caseNum+": "+xxx[n-1]);
            caseNum++;
            n = input.nextInt();
            input.nextLine();
            
        }
    }
    
    public static int max(int a, int b) {
        return Math.max(a,b);
    }
    
    public static int max(int a, int b, int c) {
        return max(a,max(b,c));
    }
    
    public static int max(int a, int b, int c, int d) {
        return max(a,max(b,c,d));
    }
    
    public static int max(int a, int b, int c, int d, int e) {
        return max(a,max(b,c,d,e));
    }
    
    public static int max(int a, int b, int c, int d, int e, int f) {
        return max(a,max(b,c,d,e,f));
    }
}
