#include <iostream>
#include <algorithm>
#include <string>
#include <queue>

using namespace std;

const int MAX_N = 18;

int n;
string lt[MAX_N];

// we do DP on subsets of {0, ..., n-1} of increasing size
//
// f[s] = the minimum number of inversions we can achieve by putting
//        the element of s into the the last k = |s| positions in the
//        permutation
// first[s] = the first element of the permutation of s leading with min
//            inversions
int f[(1 << MAX_N)];
int first[(1 << MAX_N)];

int compute(int subset)
{
  if (subset == 0) {
    // no inversions
    return f[subset] = 0;
  }

  // already computed
  if (f[subset] >= 0) {
    return f[subset];
  }

  // try to fix each element as the first element in that range
  int best_f = n*n;
  for (int i = 0; i < n; i++) {
    int mask = 1 << i;
    if ((subset & mask) == 0) continue;
    int val = compute(subset - mask);
    for (int j = 0; j < n; j++) {
      int mask2 = 1 << j;
      if (i != j && ((subset & mask2) != 0) && lt[j][i] == '1') {
	val++;
      }
    }
    if (val < best_f) {
      best_f = val;
      first[subset] = i;
    }
  }

  return f[subset] = best_f;
}

void print(int subset, bool start = true)
{
  if (subset == 0) {
    return;
  }
  
  if (!start) {
    cout << ' ';
  }
  cout << first[subset];
  print(subset - (1 << first[subset]), false);
}

bool solve()
{
  cin >> n;
  if (!n) return false;

  for (int i = 0; i < n; i++) {
    cin >> lt[i];
  }
  
  // initialize single element subsets
  fill(f, f + (1 << n), -1);

  int inv = compute((1 << n) - 1);
  print((1 << n) - 1);
  cout << endl;
  cout << inv << endl;

  return true;
}

int main()
{
  while (solve())
    ;
  return 0;
}
  
