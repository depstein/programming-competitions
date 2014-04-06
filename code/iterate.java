import java.io.*;
import java.util.*;

public class iterate {

  public static void main(String[] args) {
    ArrayList<Integer> list1 = new ArrayList<Integer>(Arrays.asList(new Integer[]{1, 2, 3}));
    ArrayList<Integer> list2 = new ArrayList<Integer>(Arrays.asList(new Integer[]{6, 1, 3, 5, 2, 8, 9}));
    // Remove all occurrences of elements in list1 from list2
    for(int i=0;i<list2.size();i++) {
      for(int j=0;j<list1.size(); j++) {
        if(list2.get(i) == list1.get(j)) {
          list2.remove(i);
          break;
        }
      }
    }
    System.out.println(list2); //[6, 3, 5, 8, 9] -- whaa?
  }
}
