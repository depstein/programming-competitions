#include <iostream>
#include <cctype>
#include <string>
using namespace std;

char grid[5][5];
struct rc {
	int r,c;
} loc[26];

void fillGrid(string s)
{
	bool used[26] = {0};
	int k=0;

	for(int i=0; i<s.length(); i++) {
		if (isalpha(s[i])) {
			char ch = toupper(s[i]);
			if (ch == 'J')
				ch = 'I';
			if (!used[ch-'A']) {
				grid[k/5][k%5] = ch;
				used[ch-'A'] = true;
				loc[ch-'A'].r = k/5;
				loc[ch-'A'].c = k%5;
				k++;
			}
		}
	}
	used[9] = true;				// no 'J' in the grid
	for(int i=0; i<26; i++) {
		if (!used[i]) {
			grid[k/5][k%5] = ('A'+i);
			loc[i].r = k/5;
			loc[i].c = k%5;
			k++;
		}
	}
	loc[9] = loc[8];
}

void encrypt(char p1, char p2, char &e1, char &e2)
{
	int i1 = p1-'A';
	int i2 = p2-'A';
	if (loc[i1].r == loc[i2].r) {
		e1 = grid[loc[i1].r][(loc[i1].c+1)%5];
		e2 = grid[loc[i2].r][(loc[i2].c+1)%5];
	}
	else if (loc[i1].c == loc[i2].c) {
		e1 = grid[(loc[i1].r+1)%5][loc[i1].c];
		e2 = grid[(loc[i2].r+1)%5][loc[i2].c];
	}
	else {
		e1 = grid[loc[i1].r][loc[i2].c];
		e2 = grid[loc[i2].r][loc[i1].c];
	}
}

char nextInsert(char ch, int &i)
{
	if (ch-'A' == i)
		i = (i+1)%26;
	if (i == 9)			// skip over 'J'
		i++;
	char ans = 'A'+i;
	i = (i+1)%26;
	if (i == 9)			// skip over 'J'
		i++;
	return ans;
}

int main()
{
	int ncase;
	string key, plain, tmp;

	cin >> ncase;
	getline(cin, key);
	for(int icase=1; icase<=ncase; icase++) {
		getline(cin, key);
		fillGrid(key);
		getline(cin, tmp);
		plain = "";
		for(int i=0; i<tmp.length(); i++) {
			if (isalpha(tmp[i]))
				plain += toupper(tmp[i]);
			if (plain[i] == 'J')
				plain[i] = 'I';
		}
		cout << "Case " << icase << ": ";
		int insert = 0;
		char e1, e2;
		for(int i=0; i<plain.length(); i+=2) {
			if (i+2 > plain.length())
				encrypt(plain[i], nextInsert(plain[i], insert), e1, e2);
			else if (plain[i] == plain[i+1]) {
				encrypt(plain[i], nextInsert(plain[i], insert), e1, e2);
				i--;
			}
			else
				encrypt(plain[i], plain[i+1], e1, e2);
			cout << e1 << e2;
		}
		cout << endl;
	}
}
