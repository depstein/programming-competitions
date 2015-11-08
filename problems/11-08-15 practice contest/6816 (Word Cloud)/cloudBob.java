// cloud.java
// Word Cloud, MCPC 2014, Problem E
// Java version by Robert Pilgrim

import java.io.*;
import java.util.Scanner;
public class cloudBob
{
    public static void main(String[] args) throws FileNotFoundException
    {
        FileReader file = new FileReader("cloud.in");
        Scanner in = new Scanner(file);
        String[] word = new String[100];
        int[] height = new int[100];             // height of word in points
        int[] count = new int[100];              // number of occurences of word
        int[] width = new int[100];              // width of word in points
        
        int set = 0;
        int cloudcount = 0;
        while(true)
        {
           int w = in.nextInt();
           int n = in.nextInt();
           if(w==0) break;
           cloudcount += 1;
           System.err.println("w = " + w + " n = " + n);
           int cmax = 0;
           for(int i=0;i<n;i++)
           {
               word[i] = in.next();
               count[i] = in.nextInt();
               if(count[i]>cmax)
                   cmax = count[i];
           }
           for(int i=0;i<n;i++)
           {
              int P = (int)Math.ceil(8.0+ (40.0*(count[i]-4.0)/(cmax-4.0)));
              height[i] = P;
              width[i] = (int)Math.ceil(9.0/16.0*word[i].length()*P);
              System.err.println(word[i] + " " + count[i] + " " + height[i] + " " + width[i]);
           }
           int lineheight = height[0];
           int linewidth = width[0];
           int cloudheight = 0;
           for(int i=1;i<n;i++)
           {
              if((linewidth + 10 + width[i])<=w)
              {
                 linewidth += 10 + width[i];
                 if(height[i] > lineheight)
                     lineheight = height[i];
              }
              else
              {
                 cloudheight += lineheight;
                 lineheight = height[i];
                 linewidth = width[i];
              }
           }
           cloudheight += lineheight;
           System.out.println("CLOUD " + cloudcount + ": " + cloudheight);
        }
        in.close();
    }
}