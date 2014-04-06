import java.util.*;
import java.io.*;

public class cells {
  public static final double SAMPLE = 1000;

  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    while(true) {
      int N = in.nextInt();
      if(N==0)
        break;
      double X = in.nextDouble(), Y = in.nextDouble(), R = in.nextDouble();
      double[] x = new double[N-1], y = new double[N-1], r = new double[N-1];
      for(int i=0;i<N-1;i++) {
        x[i] = in.nextDouble();
        y[i] = in.nextDouble();
        r[i] = in.nextDouble();
      }
      int inRange = 0;
      int covered = 0;
      for(double a = X-R; a<=X+R; a+= R/SAMPLE)
        for(double b = Y-R; b<=Y+R; b+= R/SAMPLE) {
          if(distSQ(X, Y, a, b) <= R*R) {
            inRange++;
            for(int i=0;i<N-1;i++) {
              if(distSQ(x[i],y[i], a, b) <= r[i]*r[i]) {
                covered++;
                break;
              }
            }
          }
        }
      System.out.printf("%.02f\n", ((double)covered)/inRange);
    }
  }

  public static double distSQ(double x1, double y1, double x2, double y2) {
    double dx = x1-x2, dy=y1-y2;
    return dx*dx+dy*dy;
  }
}
