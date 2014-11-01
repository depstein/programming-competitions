#include <iostream>
#include <cstdlib>
using namespace std;

const int MAXS = 1000;
const int EMPTY = -1;

int t[2][MAXS];
int n[2];
int s[2];

int hash(int val, int itab)
{
	return val%s[itab];
}

int main()
{
	int m, icase=0;;
	cin >> s[0] >> s[1] >> m;
	while (s[0] != 0) {
		cout << "Case " << ++icase << ":" << endl;
		for(int i=0; i<s[0]; i++)
			t[0][i] = EMPTY;
		for(int i=0; i<s[1]; i++)
			t[1][i] = EMPTY;
		n[0] = n[1] = 0;
		for(int i=0; i<m; i++) {
			int val;
			cin >> val;
			int j = 0;
			int hval = hash(val, j);
			int count=0;
			while (t[j][hval] != EMPTY) {
				if (++count > s[0]+s[1]) {
					cout << "ERROR: need to rehash" << endl;
					exit(-1);
				}
				int tmp = t[j][hval];
				t[j][hval] = val;
				val = tmp;
				j = 1-j;
				hval = hash(val, j);
			}
			t[j][hval] = val;
			n[j]++;
		}
		if (n[0] > 0) {
			cout << "Table 1" << endl;
			for(int i=0; i<s[0]; i++) {
				if (t[0][i] != EMPTY)
					cout << i << ":" << t[0][i] << endl;
			}
		}
		if (n[1] > 0) {
			cout << "Table 2" << endl;
			for(int i=0; i<s[1]; i++) {
				if (t[1][i] != EMPTY)
					cout << i << ":" << t[1][i] << endl;
			}
		}

		cin >> s[0] >> s[1] >> m;
	}
}
