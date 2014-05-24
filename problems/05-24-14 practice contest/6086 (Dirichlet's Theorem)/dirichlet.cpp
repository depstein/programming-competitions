#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

// maximum prime trial divisor we need (up to sqrt of max a*U+b)
const long long MAX_PRIME = 1000000;

// maximum range of values of n to consider (U-L+1)
const long long MAX_RANGE = 1000000+1;

vector<int> primes;
bool prime[MAX_PRIME+1];

long long gcd(long long a, long long b)
{
  if (!b) return a;
  return gcd(b, a%b);
}

long long gcd(long long a, long long b, long long &s, long long &t)
{
  long long r, r1, r2, a1, a2, b1, b2, q;

  a1 = b2 = 1;
  a2 = b1 = 0;

  while (b) {
    q = a / b;
    r = a % b;
    r1 = a1 - q*b1;
    r2 = a2 - q*b2;
    a = b;
    a1 = b1;
    a2 = b2;
    b = r;
    b1 = r1;
    b2 = r2;
  }

  s = a1;
  t = a2;
  return a;
}

// generate all primes up to max(sqrt(a*U+b))
void genprimes()
{
  fill(prime, prime+MAX_PRIME+1, true);
  prime[0] = prime[1] = false;

  // use a sieve
  for (long long p = 2; p < MAX_PRIME+1; p++) {
    if (!prime[p]) continue;
    primes.push_back(p);
    for (long long k = p*p; k < MAX_PRIME+1; k += p) {
      prime[k] = false;
    }
  }
}

bool isprime(long long n)
{
  // if it's below max prime, just check the sieve
  if (n <= MAX_PRIME) {
    return prime[n];
  }

  // trial division for the rest
  for (unsigned int i = 0; i < primes.size(); i++) {
    if (n % primes[i] == 0) return false;
  }
  return true;
}

bool solve(int id)
{
  long long a, b, L, U;
  cin >> a;
  if (!a) return false;
  cin >> b >> L >> U;
  cout << "Case " << id << ": ";

  // special case: if gcd(a, b) > 1, then the only potential prime is when
  // n == 0 and b is prime.
  if (gcd(a, b) > 1) {
    if (L == 0 && isprime(b)) {
      cout << 1 << endl;
    } else {
      cout << 0 << endl;
    }
    return true;
  }
  
  // gcd(a, b) = 1, do a sieve on range of n
  bool nprime[MAX_RANGE];
  int range = U - L + 1;
  fill(nprime, nprime + range, true);

  // special cases
  //
  // if a*n+b <= 2 we will have to account for that
  //
  // n = 0 (only occurs if L = 0)
  // n = 1 (if L <= 1)
  if (L == 0) {
    nprime[0] = isprime(b);
  }
  if (L <= 1) {
    nprime[1-L] = isprime(a+b);
  }
  
  long long maxval = a * U + b;
  for (unsigned int i = 0; i < primes.size(); i++) {
    long long p = primes[i];

    // p cannot divide b since gcd(a,b) = 1
    if (a % p == 0) continue;

    // p cannot divide more a*n+b
    if (p*p > maxval) break;

    // solve for n in a*n+b = 0 mod p
    long long s, t;
    gcd(a, p, s, t);
    int n_start =  (-(b*s)) %p;
    if (n_start < 0) n_start += p;
    int Lrem = L % p;
    n_start -= Lrem;
    if (n_start < 0) n_start += p;

    // get past the first multiple which is prime!
    while (a*(n_start + L)+b <= p) {
      n_start += p;
    }

    // finally do the sieve
    while (n_start < range) {
      nprime[n_start] = false;
      n_start += p;
    }
  }

#ifdef DEBUG
  for (int i = 0; i < range; i++) {
    if (nprime[i]) {
      cout << "  " << a*(i+L)+b << endl;
    }
  }
#endif

  cout << count(nprime, nprime+range, true) << endl;
  
  return true;
}

int main()
{
  genprimes();

  int id = 1;
  while (solve(id++))
    ;
  
  return 0;
}
