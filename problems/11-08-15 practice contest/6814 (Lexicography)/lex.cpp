// lex.cpp
// Lexicography, MCPC 2014, Problem C
// C++ version by Michael Goldwasser

#include <iostream>
#include <fstream>
#include <map>
using namespace std;

const int MAX_LEN = 16;

ifstream fin("lex.in");

long long factorial(int k) {
    long long ans = 1;
    for (int j=2; j <= k; j++)
        ans *= j;
    return ans;
}

// returns the number of distinct anagrams for given frequency profile
long long count(int length, const map<char,int>& freq) {
    long long answer = factorial(length);
    for (map<char,int>::const_iterator it = freq.begin(); it != freq.end(); ++it)
        if (it->second > 1)
            answer /= factorial(it->second);
    return answer;
}

// produces alphabetical frequency count in form of a map
map<char,int> profile(const string& s) {
    map<char,int> counts;
    for (int j=0; j < s.size(); j++)
        counts[s[j]]++;
    return counts;
}

string solve(const string& original, long long rank) {
    string answer;
    map <char,int> freq = profile(original);

    for (int length = original.size(); length > 0; --length) {
        for (map<char,int>::iterator it = freq.begin(); it != freq.end(); ++it) {
            if (it->second > 0) {
                // consider it->first as next char of answer
                freq[it->first]--;
                long long sub = count(length-1, freq);
                if (sub >= rank) {
                    answer += it->first;
                    break;    // exit innermost loop
                } else {
                    freq[it->first]++;
                    rank -= sub;
                }
            }
        }
    }
    return answer;
}

// used only by judges to validate input for test set
void validateInput(const string& original, long long K) {
    if (original.size() > MAX_LEN)
        cerr << "ERROR: string is too long" << endl;
    for (int j=0; j<original.size(); j++) {
        if (original[j] < 'A' || original[j] > 'Z')
            cerr << "ERROR: illegal character in string";
    }
    if (K < 1 || K > count(original.size(), profile(original)))
        cerr << "ERROR: illegal rank " << K << endl;
}

int main() {
    while (true) {
        string orig;
        long long K;
        fin >> orig >> K;
        if (K == 0) break;
        cout << solve(orig, K) << endl;

        validateInput(orig,K);   // sanity check for juding only
    }
}

