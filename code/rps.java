/*
 * "Rock, Paper, or Scissors?", 2007 ICPC Pacific Northwest Regionals problem A
 * LiveArchive problem #4065
 * Determine which of two players win more games of Rock, Paper, Scissors
 * Daniel Epstein, depstein AT cs DOT washington DOT edu, @daepstein
 */

import java.io.*;
import java.util.*;

public class Main {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    int t = in.nextInt();
    for(int a = 0; a < t; a++) {
      int n = in.nextInt();
      int p1score = 0, p2score = 0;
      for(int i = 0; i < n; i++) {
        char p1 = in.next().charAt(0);
        char p2 = in.next().charAt(0);
        if(p1 == 'R') {
          if(p2 == 'P') {
            p2score++;
          } else if(p2 == 'S') {
            p1score++;
          }
        } else if(p1 == 'P') {
          if(p2 == 'R') {
            p1score++;
          } else if(p2 == 'S') {
            p2score++;
          }
        } else { //'S'
          if(p2 == 'R') {
            p2score++;
          } else if(p2 == 'P') {
            p1score++;
          }
        }
      }
      System.out.println(p1score==p2score?"TIE":(p1score>p2score?"Player 1":"Player 2"));
    }
  }
}
