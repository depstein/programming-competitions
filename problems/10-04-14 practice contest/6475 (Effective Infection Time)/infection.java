/*
 * Name:    EIT.java
 * Author:  Sam Battat
 * Date:    Jan 20, 2013
 */
import java.util.*;

public class Sam {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int cases = input.nextInt();

        for(int i = 0; i < cases; i++){
            //reads in input
            input.nextLine();
            //stores purchase mont and year
            int month_purchased = input.nextInt();
            int year_purchased = input.nextInt();

            //proceeds to next line
            input.nextLine();
            //stores migration month and year
            int month_migrated = input.nextInt();
            int year_migrated = input.nextInt();

            //calculates EIT
            double eit = getEIT(year_purchased, month_purchased, year_migrated, month_migrated);
            //formats result and displays the string
            System.out.println(String.format("%.4f", eit));
        }
    }

    public static double getEIT(int year_p, int month_p, int year_m, int month_m){
        double eit = 0.0;
        //if asset was purchased and migrated in the same month of the same year, then return 0
        if(year_p == year_m && month_p == month_m){
            eit = 0.0;
        }
        //if the purchase year and migrated year is the same, calculate eit based on months
        else if(year_p == year_m){
            eit = ((month_m - month_p)/(12.0 - month_p+1)) * 0.5;
        }
        //if the purchase year and migrated year are different, then calculate based on years
        else{
            eit = 0.5 + ((year_m - year_p)-1) + (month_m - 1)/12.0;
        }
        return eit;
    }
}
