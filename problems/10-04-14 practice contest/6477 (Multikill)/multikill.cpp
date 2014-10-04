#include <iostream>
#include <vector>
#include <math.h>
#include <assert.h>
#include <cmath>
using namespace std;

//For two doubles d1,d2, if abs(d1-d2)<EPSILON we assume d1=d2.
double EPSILON = pow(10.0,-9.0);

typedef pair<double,double> point;
typedef pair<point,point> centers;

void writepoint(point p1) {
	cout << "(" << p1.first << "," << p1.second << ")" << endl;
}

//Gives the slope of the line passing through p1 and p2.
double slope(point p1, point p2) {
	return (p2.second - p1.second)/(p2.first-p1.first);
}


//Gives the distance between p1 and p2.
double D(point p1, point p2) {
	double a = p1.first - p2.first;
	double b = p1.second - p2.second;
	return pow(a*a+b*b, 0.5);
}


//Gives the midpoint of p1 and p2.
point midpoint(point p1, point p2) {
	return point( (p1.first + p2.first)/2, (p1.second + p2.second)/2);
}

//Returns the centers of the two circles with given radius passing through
//the two points by using the right triangles formed by p1, the midpoint
//of p1 and p2, and the center of one of the circles.
//
//Note:This assumes that p1 and p2 are distinct. If it happens that they are
//     the same, then there are an infinite number of circles of radius r
//     through that point, and this function just returns 2. This should not affect
//     the output of the overall algorithm, I just thought it was worth noting.
centers circle(point p1, point p2, double r) {
	point p3 = midpoint(p1,p2),p4,p5;

	//d is the distance from p1 to the midpoint.
	//a is the length of the other leg of the triangle.
	double d = D(p1,p3), a = pow(r*r-d*d,.5);

	//Handle the cases where the points are vertically or horizontally aligned.
	if ( abs(p1.first - p2.first) < EPSILON ) {
		p4 = point(p3.first + a, p3.second);
		p5 = point(p3.first - a, p3.second);
	} else if ( abs(p1.second - p2.second) < EPSILON) {
		p4 = point(p3.first, p3.second + a);
		p5 = point(p3.first, p3.second - a);

	} else {

		//m is the slope of the line through p1 and p2.
		double m = slope(p1,p2);

		//offset is the absolute value of the difference between the x-coordinates
		//of p3 and the centers of the circles.
		//
		//This is found by finding the points on the line with slope -1/m through p3
		//that are distance a from p3.
		double offset = pow(a*a*m*m/(1+m*m),0.5);

		//x1 and x2 are the centers of the circles.
		double x1 = p3.first + offset, x2 = p3.first - offset;

		//The line through p3 with slope -1/m has equation
		//y = p3.second - 1/m * (x-p3.first)
		//So we just plug x1 and x2 into that equation to find the associated
		//y values.
		p4 = point(x1, p3.second - 1/m * (x1 - p3.first));
		p5 = point(x2, p3.second - 1/m * (x2 - p3.first));
	}
	return centers(p4,p5);
}

int findBest(vector<point> rocks, double d) {
	int i,j,k,count=0,best=0;
	point p1,p2,p4,p5;
	for (i=0; i<rocks.size(); ++i) {
		p1 = rocks[i];
		for (j=0; j<i; ++j) {
			p2 = rocks[j];

			//There only exist circles of radius d intersecting p1 & p2 if
			//the distance between p1 and p2 is less than 2*d.
			if ( D(p1,p2) < 2*d ) {
				centers c = circle(p1,p2,d);
				p4 = c.first;
				p5 = c.second;
				count=0;
				for (k=0; k<rocks.size(); ++k) {
					if ( D(rocks[k],p4) < d + EPSILON) {
						++count;
					}
				}
				if (count > best) best = count;
				count=0;
				for (k=0; k<rocks.size(); ++k) {
					if ( D(rocks[k],p5) < d + EPSILON) {
						++count;
					}
				}
				if (count > best) best = count;
			}
		}
	}
  if (best < 1 && rocks.size () == 0)
    return 0;
  if (best < 1 && rocks.size () >= 1)
    return 1;
	return best;
}



int main() {
  int cases;
	cin >> cases;
  while (cases-->0)
  {
    double d,N,x,y;
    vector<point> rocks;
    cin >> d >> N;
    while (N-->0) {
      cin >> x >> y;
      rocks.push_back(point(x,y));
    }
    cout << findBest(rocks,d) << endl;
  }
}