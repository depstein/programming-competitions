// cloudMichael.java
// Word Cloud, MCPC 2014, Problem E
// Java Version translated from Michael Goldwasser C++ version by R. Pilgrim

import java.io.*;
import java.util.Scanner;
public class cloudMichael
{
  public static long PAD = 10l;           // interspace padding
  public static int MIN_C = 5;
  public static int MAX_C = 1000;
  public static int MAX_W = 5000;
  public static int MAX_N = 1000;
        
  public static item[] box = new item[MAX_N];

  public static void main(String[] args)throws FileNotFoundException
  {
    FileReader reader = new FileReader("cloud.in");
    Scanner in = new Scanner(reader);
  
    int trial=0;
    while (true) 
      {
        int W = in.nextInt();
        int N = in.nextInt();
        if (W == 0) break;
        String toss = in.nextLine();
        if (W>MAX_W) System.err.println("ILLEGAL W");
        if (N>MAX_N) System.err.println("ILLEGAL N");

        int maxC = 0;
        String prev = "";
        for (int j=0; j<N; j++) 
          {
            String line[] = in.nextLine().split("\\s+");
            String S = line[0];
            int C = Integer.parseInt(line[1]);
            if (C<MIN_C || C>MAX_C) System.err.println("ILLEGAL C");
            if ((S.compareTo(prev))<0) System.err.println("WARNING: NOT ALPHABETIZED");
                                
            prev = S;  // for style only
            box[j] = new item();
            box[j].wordlength = S.length();
            box[j].count = C;
            maxC = Math.max(maxC, C);
          }

        for (int j=0; j<N; j++) 
          {
            box[j].height = 8 + ceil(40*(box[j].count-4), maxC-4);
            box[j].width = ceil(9*box[j].wordlength*box[j].height, 16);
            if (box[j].width > W) System.err.println("WORD TOO LONG");
          }

        int total = 0;
        int lineWidth = box[0].width;
        int lineHeight = box[0].height;
        for (int j=1; j<N; j++) 
          {
            if (lineWidth + PAD + box[j].width <= W) 
              {
                lineWidth += PAD + box[j].width;
                lineHeight = Math.max(lineHeight, box[j].height);
              } 
            else 
              {
                total += lineHeight;
                lineHeight = box[j].height;
                lineWidth = box[j].width;
              }
          }
        total += lineHeight;  // don't forget the last line
        System.out.println("CLOUD " + ++trial + ": " + total);
      }
  }
         
  public static int ceil(int num, int denom) 
  {
    if (num % denom > 0)
      return (num/denom + 1);
    else
      return (num/denom);
  }
}

class item 
{
  int wordlength,count,width,height;
}
