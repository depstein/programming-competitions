#include <iostream>
#include <cmath>
#include <cstdio>
#include <queue>
using namespace std;

const int MAXB = 10;

const double TOL = 0.000001;
const double PI = 4.0*atan(1.0);

bool isZero(double d)
{
	return (abs(d) < TOL);
}

bool gEqZero(double d)
{
	if (isZero(d))
		return true;
	return (d > 0.0);
}

bool between(double a, double b1, double b2)
{
	if (b1 < b2)
		return (a>=b1 && a<=b2);
	else
		return (a>=b1 || a<=b2);
}

class segment {
public:
	double x[2], y[2];	// endpoints
	double a, b, c, d;	// x = at + b, y = ct+d

	segment(double xx1=0.0, double yy1=0.0, double xx2=0.0, double yy2=0.0)
	{
		x[0] = xx1;
		y[0] = yy1;
		x[1] = xx2;
		y[1] = yy2;
		calcParametric();
	}

	void output(ostream &out)
	{
		out << x[0] << ',' << y[0] << ' ' << x[1] << ',' << y[1];
	}

	void calcParametric()
	{
		a = x[1]-x[0];
		b = x[0];
		c = y[1]-y[0];
		d = y[0];
	}

	bool findInt(segment s, double& t, double &u)	// t - this seg, u - passed in seg
	{
		double den = s.a*c - a*s.c;
		if (isZero(den))
			return false;
		double bb = s.b-b;
		double dd = s.d-d;
		t = (s.a*dd-s.c*bb)/den;
		u = (a*dd - c*bb)/den;
		return true;
	}
};

class beam {
public:
	segment e1, e2;
	double x0, y0;				// origin of beam
	double alpha1, alpha2;		// angles beam edges make with the x-axis
	int iw;						// reflecting barrier

	void output(ostream &out)
	{
		out << " origin = " << x0 << ',' << y0;
		out << " angles =  " << alpha1 << ',' << alpha2;
		out << " barrier # =  " << iw;
	}

};

class barrier {
public:
	segment e;
	bool isRefl;

	void output(ostream &out)
	{
		e.output(out);
		out << ' ' << isRefl;
	}
};
barrier bars[MAXB+1];

ostream& operator<<(ostream& out, segment s)
{
	s.output(out);
	return out;
}
ostream& operator<<(ostream& out, beam b)
{
	b.output(out);
	return out;
}
ostream& operator<<(ostream& out, barrier w)
{
	w.output(out);
	return out;
}

class interval
{
public:
	double a, b;
	interval *next;

	interval(double aa = 0, double bb = 0, interval* nn = 0)
	{
		a = aa;
		b = bb;
		next = nn;
	}
} intervals;

queue<beam> beamQ;

struct barpt
{
	double angle;			// angle relative to beam origin
	int iw;					// barrier index
	int ip;					// point index
	bool start;				// true if start pt of barrier
} barpts[2*MAXB+2];
int nbp = 0;

double dist(double x1, double y1, double x2, double y2)
{
	double dx = x2 - x1;
	double dy = y2 - y1;
	return sqrt(x2*x2+y2+y2);
}

bool checkDist(barrier w, beam bm, double len, double &t1, double &t2)
{
	double a = w.e.a;
	double b = w.e.b - bm.x0;
	double c = w.e.c;
	double d = w.e.d - bm.y0;
	double qa = a*a + c*c;
	double qb = 2*(a*b + c*d);
	double qc = b*b + d*d - len*len;
	double disc = qb*qb-4*qa*qc;
	if (disc < 0.0 || isZero(disc))
		return false;
	t1 = (-qb - sqrt(disc))/qa/2.0;
	t2 = (-qb + sqrt(disc))/qa/2.0;
	return true;
}

void insertBp(barpt bp)
{
	int i=nbp;
	while (i > 0 && barpts[i-1].angle > bp.angle) {
		barpts[i] = barpts[i-1];
		i--;
	}
	barpts[i] = bp;
	nbp++;
}

void swap(double &a, double &b)
{
	double tmp = a;
	a = b;
	b = tmp;
}

void addInterval(double t1, double t2, double u1, double u2)
{
	if (u1 <=0 && u2 <= 0)		// barrier behind beam
		return;
	if (u1 <= 0 && t1 < 0)
		t1 = 1.0;
	if (t2 < t1) {
		double tmp = t1;
		t1 = t2;
		t2 = tmp;
	}
	if (t2 < 0.0 || t1 > 1.0)
		return;
	if (t1 < 0.0)
		t1 = 0.0;
	if (t1 < u1)
		t1 = u1;
	if (t2 > 1.0)
		t2 = 1.0;
	if (t2 > u2)
		t2 = u2;
	if (t1 >= t2)
		return;
	interval *p, *q;
	for(p = &intervals; p->next != 0; p = p->next)
		if (p->next->b > t1)
			break;
	if (p->next == 0 || p->next->a > t2) {
		p->next = new interval(t1, t2, p->next);
		return;
	}
	if (p->next->a > t1) 
		p->next->a = t1;
	for(q = p->next; q->next != 0;) {
		if (q->next->a > t2)
			break;
		p->next->b = q->next->b;
		interval *sav = q->next;
		p->next->next = q->next->next;
		delete sav;
	}
	if (p->next->b < t2)
		p->next->b = t2;
}

beam getReflection(double beta1, double beta2, double x1, double y1, double x2, double y2)
{
	beam newb;
	double t, u;
	double alpha = atan2(y2-y1, x2-x1);
	double angle = 2*alpha - beta1;
	while (angle < 0)
		angle += 2*PI;
	newb.alpha1 = angle;
	angle = 2*alpha - beta2;
	while (angle < 0)
		angle += 2*PI;
	newb.alpha2 = angle;
	segment e1(x1, y1, x1+cos(newb.alpha1), y1+sin(newb.alpha1));
	newb.e1 = e1;
	segment e2(x2, y2, x2+cos(newb.alpha2), y2+sin(newb.alpha2));
	newb.e2 = e2;
	e1.findInt(e2, t, u);
	newb.x0 = e2.a*u + e2.b;
	newb.y0 = e2.c*u + e2.d;
	return newb;
}

int activeBar[MAXB];
int nActive;

bool inFrontOf(double x0, double y0, barrier w1, barrier w2)
{
	double t, u;
	if (w2.e.a == 0) {
		segment e(x0, y0, w2.e.x[0], w2.e.y[0]);
		e.calcParametric();
		if (e.findInt(w1.e, t, u)) {
			if (t < 1.0)
				return true;
		}
		e.x[1] = w2.e.x[1];
		e.y[1] = w2.e.y[1];
		e.calcParametric();
		if (e.findInt(w1.e, t, u)) {
			if (t < 1.0)
				return true;
		}
		return false;
	}
	else {
		double slope = w2.e.c/w2.e.a;
		double yint = w2.e.d - w2.e.b*slope;
		double val0 = y0 - x0*slope - yint;
		double val1 = w1.e.y[0] - w1.e.x[0]*slope - yint;
		if (val0*val1 <= 0)
			return false;
		val1 = w1.e.y[1] - w1.e.x[1]*slope - yint;
		if (val0*val1 <= 0)
			return false;
	}
	return true;
}

void addToActive(int iw, beam b)
{
	int i = nActive;
	while (i > 0 && !inFrontOf(b.x0, b.y0, bars[activeBar[i-1]], bars[iw])) {
		activeBar[i] = activeBar[i-1];
		i--;
	}
	activeBar[i] = iw;
	nActive++;
}

void removeFromActive(int iw)
{
	int i=0;
	while (activeBar[i] != iw)
		i++;
	for(; i<nActive-1; i++)
		activeBar[i] = activeBar[i+1];
	nActive--;
}

void checkBarrier(int iw, beam b, double len)
//
//  add barrier to barrier intersection list
//
//  iw = index of barrier to check
//  b = beam
//  len = maximum length of beam
//
{
	barpt bp;
	double t, u;
	if (b.iw == iw)				// return if this is barrier that reflected beam
		return;
	barrier w = bars[iw];
	if (b.iw != -1) {			// if a barrier reflected b, don't process barriers
								//    between beam origin and the source barrier
		segment e0(b.x0, b.y0, bars[b.iw].e.x[0], bars[b.iw].e.y[0]);
		if (!e0.findInt(w.e, t, u))
			return;
		if (gEqZero(t) && gEqZero(1.0-t) && gEqZero(u) && gEqZero(1.0-u))
			return;
		segment e1(b.x0, b.y0, bars[b.iw].e.x[1], bars[b.iw].e.y[1]);
		if (!e1.findInt(w.e, t, u))
			return;
		if (gEqZero(t) && gEqZero(1.0-t) && gEqZero(u) && gEqZero(1.0-u))
			return;
	}
								// now check if any part of barrier spans beam
									// first, get angles from beam origin to edges of barrier
	double ang0 = atan2(w.e.y[0]-b.y0, w.e.x[0]-b.x0);
	double ang1 = atan2(w.e.y[1]-b.y0, w.e.x[1]-b.x0);
	if (ang0 < 0.0)
		ang0 += 2*PI;
	if (ang1 < 0.0)
		ang1 += 2*PI;
	double spread = ang0-ang1;
	if (isZero(spread))				// skip barriers parallel to beam
		return;
									// determine which barrier edge/angle is first
	int start = 0;
	if ((0 <=  spread && spread < PI) || spread < -PI) {
		start = 1;
		double temp = ang0;
		ang0 = ang1;
		ang1 = temp;
	}
	bool b1 = between(ang0, b.alpha1, b.alpha2);
	bool b2 = between(ang1, b.alpha1, b.alpha2);
	if (b1) {						// barrier starts inside of beam
		ang0 -= b.alpha1;
		if (ang0 < 0.0)
			ang0 += 2*PI;
		ang1 -= b.alpha1;
		if (ang1 < 0.0)
			ang1 += 2*PI;
	}
	else if (!b1 && b2) {			// barrier starts outside and ends inside of beam
		ang0 -= b.alpha1;
		if (ang0 > 0.0)
			ang0 -= 2*PI;
		ang1 -= b.alpha1;
		if (ang1 < 0.0)
			ang1 += 2*PI;
	}
	else if (between(b.alpha1, ang0, ang1) && between(b.alpha2, ang0, ang1)) {
									// beam within span of barrier
		ang0 -= b.alpha1;
		if (ang0 > 0.0)
			ang0 -= 2*PI;
		ang1 -= b.alpha1;
		if (ang1 < 0.0)
			ang1 += 2*PI;
	}
	else							// otherwise, no intersection with beam and barrier
		return;
									// add two beam intersections to list
	bp.angle = ang0;
	bp.iw = iw;
	bp.ip = start;
	bp.start = true;
	insertBp(bp);
	bp.angle = ang1;
	bp.ip = 1-start;
	bp.start = false;
	insertBp(bp);
}

void process(double x0, double y0, double ang0, double ang1, int iw, int n, double len, int ip)
//
//	process beam and intersection
//		x0, y0 = beam origin
//		ang0, ang1 = angles of beam sides
//		iw = index of intersecting barrior
//		n = index of house barrier
//		len = max length for a beam
//		ip = index of start point for barrier
//
{
							// create beam
	beam b;
	b.alpha1 = ang0;
	b.alpha2 = ang1;
	segment e1(x0, y0, x0+cos(b.alpha1), y0+sin(b.alpha1));
	b.e1 = e1;
	segment e2(x0, y0, x0+cos(b.alpha2), y0+sin(b.alpha2));
	b.e2 = e2;
	b.x0 = x0;
	b.y0 = y0;
	b.iw = -1;
	barrier w = bars[iw];
	double t1, t2, u1, u2;
	w.e.findInt(b.e1, t1, u1);			// find intersection params
	w.e.findInt(b.e2, t2, u2);
	if (!checkDist(w, b, len, u1, u2))	// adjust params based on max beam length...
		return;							//   ...and return if barrier too far
	if (iw == n) {
		addInterval(t1, t2, u1, u2);	// hit the house
	}
	if (w.isRefl) {
		double beta1, beta2;
										// determine intersection points on barrier
		double x1 = w.e.x[1-ip], y1 = w.e.y[1-ip], x2 = w.e.x[ip], y2 = w.e.y[ip];
		double x = w.e.a*t1 + w.e.b;
		double y = w.e.c*t1 + w.e.d;
		if ((x-x1)*(x-x2) <= 0.0 && (y-y1)*(y-y2) <= 0.0) {
			x1 = x;
			y1 = y;
		}
		x = w.e.a*t2 + w.e.b;
		y = w.e.c*t2 + w.e.d;
		if ((x-x1)*(x-x2) <= 0.0 && (y-y1)*(y-y2) <= 0.0) {
			x2 = x;
			y2 = y;
		}
											// determine beam angles of intersection
		beta1 = atan2(y1-b.y0, x1-b.x0);
		beta2 = atan2(y2-b.y0, x2-b.x0);
		if (beta1 < 0.0)
			beta1 += 2*PI;
		if (beta2 < 0.0)
			beta2 += 2*PI;
		if (beta1 < beta2 && beta2 - beta1 > PI) {
			swap(beta1, beta2);
			swap(x1, x2);
			swap(y1, y2);
		}
		else if (beta2 < beta1 && beta1 - beta2 < PI) {
			swap(beta1, beta2);
			swap(x1, x2);
			swap(y1, y2);
		}
												// created reflected beam and store in
												//   beam queue
		beam newb = getReflection(beta2, beta1, x2, y2, x1, y1);
		newb.iw = iw;
		beamQ.push(newb);
	}
}

void process(beam b, int n, double len)		// process beam b
{
	double t1, u1, t2, u2;
									// create sorted list of barriers
									//   which intersect beam
	double delta = b.alpha2 - b.alpha1;
	if (delta < 0.0)
		delta += 2*PI;
	nbp = 0;
	for(int i=0; i<=n; i++)
		checkBarrier(i, b, len);
	if (nbp == 0)
		return;							// beam did not hit anything
	nActive = 0;
	int i=0;
	double baseAng = b.alpha1;
									// process barrier list
										// first add barriers which start
										//   outside of beam
	while (barpts[i].angle < 0) {
		addToActive(barpts[i].iw, b);
		i++;
	}
	for(;i<nbp; i++) {
		if (barpts[i].start) {			// if barrier starts inside...
			addToActive(barpts[i].iw, b);
										//   ... check if it's closest, and
										//     process part of beam with 2nd
										//     closest
			if (barpts[i].iw == activeBar[0] && nActive > 1) {
				process(b.x0, b.y0, b.alpha1, baseAng+barpts[i].angle, activeBar[1], n, len, barpts[i].ip);
				b.alpha1 = baseAng+barpts[i].angle;
			}
		}
		else {							// if barrier ends inside...
										//   ... and is closest barrier
			if (barpts[i].iw == activeBar[0]) {
										//   determine angle of beam intersection
				double alpha2 = baseAng+barpts[i].angle;
				if (barpts[i].angle > delta)
					alpha2 = b.alpha2;
				process(b.x0, b.y0, b.alpha1, alpha2, activeBar[0], n, len, barpts[i].ip);
				b.alpha1 = alpha2;
			}
			removeFromActive(barpts[i].iw);
		}
		if (b.alpha1 >= baseAng + delta)
			break;
	}
}

int main()
{
	int n, icase = 0;
	double alpha, len;

	cin >> n >> alpha >> len;
	while (len != 0) {
		icase++;
		alpha = alpha*PI/180.0;
		intervals.next = 0;
		for(int i=0; i<n; i++) {
			cin >> bars[i].e.x[0] >> bars[i].e.y[0] >> bars[i].e.x[1] >> bars[i].e.y[1] >> bars[i].isRefl;
			bars[i].e.calcParametric();
		}
		cin >> bars[n].e.x[0] >> bars[n].e.y[0] >> bars[n].e.x[1] >> bars[n].e.y[1];
		bars[n].isRefl = 0;
		bars[n].e.calcParametric();

		beam b;
		b.alpha1 = PI/2 - alpha/2;
		b.alpha2 = PI/2 + alpha/2;
		segment e1(0.0, 0.0, sin(alpha/2), cos(alpha/2));
		b.e1 = e1;
		segment e2(0.0, 0.0, -sin(alpha/2), cos(alpha/2));
		b.e2 = e2;
		b.x0 = b.y0 = 0.0;
		b.iw = -1;
			
		beamQ.push(b);
		while (!beamQ.empty()) {
			b = beamQ.front();
			beamQ.pop();
			process(b, n, len);
		}

		double ans = 0;
		for(interval *p = intervals.next; p != 0; p = p->next) {
			ans += p->b - p->a;
		}
		printf("Case %d: %.2f", icase, 100*ans);
		cout << endl;
		cin >> n >> alpha >> len;
	}
}

