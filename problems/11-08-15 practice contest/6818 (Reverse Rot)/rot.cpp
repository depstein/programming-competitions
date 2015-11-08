// rot.cpp
// Reverse Rot, MCPC 2014, Problem G
// C++ solution by James Feher

#include <fstream>
#include <iostream>
#include <cstring>
using namespace std;

ifstream fin( "rot.in" );
bool DEBUG = false;      // Make sure to set to false on submission

int main() {   

  int N, i, inLength;
  string inString, outString;

  fin >> N;             // Get the amount to rotate the string

  while (N > 0) {
    // Get the entire line, this would still work if the string included
    // white space, where >> would not have worked
    fin >> inString;
    DEBUG && cout << "Got Data -- N: " << N
                  << "\tString: " << inString << endl;

    // make outstring the same size
    outString = inString;

    // Reverse the string, could use reverse in the STL, but simple enough here
    inLength = inString.length();
    for (i=0; i < inLength; i++) {
      outString[inLength -1 -i] = inString[i];
    }
    DEBUG && cout << "REV: " << outString << endl;

    // shift the letters now
    for (i=0; i < inLength; i++) {
      
      // input is a letter
      if (outString[i] >= 'A' && outString[i] <= 'Z') {
        // output is a letter
        if (int(outString[i] - 'A' + N) % 28 < 26) {
          outString[i] = 'A' + (outString[i] - 'A' + N) % 28;
        // output is an underscore
        } else if ((outString[i] - 'A' + N) % 28 == 26) {
          outString[i] = '_';
        // output is a .
        } else {
          outString[i] = '.';
        }

      // input is an underscore  
      } else if (outString[i] == '_') {
        if (N == 1) { 
          outString[i] = '.';
        } else {
          outString[i] = 'A' + N - 2;
        }
      
      // input is a .
      } else {
        if (N == 27) {
          outString[i] = '_';
        } else { 
          outString[i] = 'A' + N - 1;
        }
      }
    } // end for

    DEBUG && cout << "OUT: " << outString << "\nIN : " << inString << endl;

    cout << outString << endl;
    fin >> N;
  }
  return 0;
}
