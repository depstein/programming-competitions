#include <iostream>
#include <iomanip>
#include <cmath>
#include <cassert>

using namespace std;

const int MAX_N = 200;
const double PI = acos(-1.0);

struct Circle
{
  int x, y, r;
};

istream &operator>>(istream &is, Circle &c)
{
  return is >> c.x >> c.y >> c.r;
}

bool solve(int id)
{
  int N;
  cin >> N;
  if (!N) return false;

  Circle c[MAX_N];
  for (int i = 0; i < N; i++) {
    cin >> c[i];
  }

  double best_shade = 0.0;
  //  int best_time;

  for (int t = 0; t < 24*60; t++) {
    double shade = 0.0;
    double angle = 2*PI*t/(24*60);
    double sun_x = 500.0 * sin(angle);
    double sun_y = 500 * cos(angle);

    for (int i = 0; i < N; i++) {
      double proj = (sun_x * c[i].x + sun_y * c[i].y) /
	sqrt(sun_x * sun_x + sun_y * sun_y);
      if (proj < 0) continue;
      double dist_sq = c[i].x*c[i].x + c[i].y*c[i].y - proj*proj;
      if (dist_sq > c[i].r*c[i].r) continue;

      shade += 2*sqrt(c[i].r*c[i].r - dist_sq);
    }

    if (shade > best_shade) {
      best_shade = shade;
      //      best_time = t;
    }
  }

  /*
  cout << "Case " << id << ": " << setw(2) << setfill('0')
       << best_time / 60 << ":" << setw(2) << setfill('0')
       << best_time % 60 << endl;
  */

  assert((int)(best_shade*1000000) % 1000 != 500);
  cout << fixed << setprecision(3) << best_shade << endl;

  return true;
}

int main()
{
  int id = 1;
  while (solve(id++))
    ;

  return 0;
}
