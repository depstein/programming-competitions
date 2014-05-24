#include <iostream>
#include <iomanip>
#include <cstdlib>

using namespace std;

int main()
{
  int m, s;
  char c;

  int nines[100];
  for (int i = 0; i <= 99; i++) {
    nines[i] = 0;
    if (i % 10 == 9) nines[i]++;
    if (i / 10 == 9) nines[i]++;
  }
  
  while (cin >> m >> c >> s && (m || s)) {
    int T0 = 60*m+s;
    int bestm = 0, bests = 0;
    int best9 = -1;
    int besterror = 1000000;
    
    for (int mm = 0; mm <= 99; mm++) {
      for (int ss = 0; ss <= 99; ss++) {
	int T9 = 60*mm+ss;
	int error = abs(T9 - T0);
	if (error*10 >= T0) continue;
	int num9 = nines[mm] + nines[ss];
	if (num9 > best9 ||
	    (num9 == best9 && error < besterror) ||
	    (num9 == best9 && error == besterror && mm < bestm) ||
	    (num9 == best9 && error == besterror && mm == bestm && ss < bests)) {
	  bestm = mm;
	  bests = ss;
	  best9 = num9;
	  besterror = error;
	}	
      }
    }
    cout << setw(2) << setfill('0') << bestm << ":"
	 << setw(2) << setfill('0') << bests << endl;
  }

  return 0;
}
