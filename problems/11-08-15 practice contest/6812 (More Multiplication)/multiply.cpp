// multiply.cpp
// (More) Multiplication, MCPC 2014, Problem A
// C++ solution by James Feher

#include <fstream>
#include <iostream>
#include <cstring>
using namespace std;

ifstream fin( "multiply.in" );
bool DEBUG = false;      // Make sure to set to false on submission

void printChars(char c, int num2print) {
// INPUT  : character to print, number of times to print it
// OUTPUT : None
// PURPOSE: Print char c num2print times
//          Make it easier to print spaces and lines for output
  int i;
  for (i=0; i<num2print; i++)
    cout << c;
} // end printChars

void printLine(int numXdig) {
// INPUT  : number of X digits
// OUTPUT : None
// PURPOSE: Print a line the appropriate length
  cout << "| ";
  cout << "+";
  for (int j=0; j < numXdig; j++) {
      printChars('-', 3);
      cout << "+";
  }
  cout << " |" << endl;
} // end printLine

int main() {   

  // numRdigits contains the number of result digits
  int X, Y, i, j, result, numXdigits, numYdigits, numRdigits;
  int Xdigits[5], Ydigits[5], Rdigits[10];
  bool firstDigitZero;

  fin >> X >> Y;             // Get the numbers to multiply

  while (X > 0) {
    DEBUG && cout << "Got Data -- X: " << X << "\tY: " << Y << endl;
  
    // determing the number of digits required for each number
    for (i=1, numXdigits=0; i<=X; i*=10) numXdigits++;
    for (i=1, numYdigits=0; i<=Y; i*=10) numYdigits++;
    // Now get the individual digits
    for (i=0, j=X; j>0; i++) {
      Xdigits[i] = j%10;
      j /= 10;
    }
    for (i=0, j=Y; j>0; i++) {
      Ydigits[i] = j%10;
      j /= 10;
    }
    // Do the same for the result
    result = X*Y;
    for (i=1, numRdigits=0; i<=result; i*=10) numRdigits++;
    for (i=0, j=result; j>0; i++) {
      Rdigits[i] = j%10;
      j /= 10;
    }

    DEBUG && cout << "  Num digits X: " << numXdigits 
                  << "\tNum digits Y: " << numYdigits
                  << "\tNum digits R: " << numRdigits 
                  << "\tResult: " << result << endl;

    // print top row
    cout << "+";
    printChars('-', 3+4*numXdigits);
    cout << "+" << endl;

    // print X formatted appropriately
    cout << "|   ";            // start out with | and 3 spaces
    for (i=0; i<numXdigits; i++) {
      cout << Xdigits[numXdigits-i-1];
      printChars(' ', 3);
    }
    cout << "|" << endl;      // end with | 
    
    // Now print each row of the results
    firstDigitZero = false;
    printLine(numXdigits);
    for (i=numYdigits-1; i>=0; i--) {
      int topDigit, botDigit;
      // if top row see if the highest order digit is zero
      if (i==numYdigits-1) 
        if ( (Xdigits[numXdigits-1]*Ydigits[i]) /10 <= 0 ) 
          firstDigitZero = true;

      // print the top digits
      // first line or second line with zero as top digit
      if (i==numYdigits-1 || (i==numYdigits-2 && firstDigitZero))
        cout << "| |";
      else 
        cout << "|/|";
      for (j=numXdigits-1; j>=0; j--) {
        topDigit = Xdigits[j]*Ydigits[i] / 10;  
        cout << topDigit << " /|";  
      }
      cout << " |" << endl;

      // now print the middle
      cout << "| |";
      for (j=numXdigits-1; j>=0; j--) 
        cout << " / |"; 
      cout << Ydigits[i] << "|" << endl;

      // now print the bottom digits
      cout << "|";
      if ((i == numYdigits-1) && firstDigitZero) 
        cout << " |";
      else 
        cout << Rdigits[i+numXdigits] << "|";
      for (j=numXdigits-1; j>=0; j--) {
        botDigit = Xdigits[j]*Ydigits[i] % 10;
        cout << "/ " << botDigit << "|";  
      }
      cout << " |" << endl;

      printLine(numXdigits);
    }

    // print Result formatted appropriately
    cout << "|";            // start out with |
    for (i=0; i<numXdigits; i++) {
      if (firstDigitZero && numYdigits == 1) {
        cout << "  ";
      } else {
        cout << "/ ";
      }
      cout << Rdigits[numXdigits-i-1] << " ";
    }
    cout << "   |" << endl;      // end with three spaces and | 
    
    // print bottom row
    cout << "+";
    printChars('-', 3+4*numXdigits);
    cout << "+" << endl;

    fin >> X >> Y;             // Get the numbers to multiply
  }
  return 0;
}
