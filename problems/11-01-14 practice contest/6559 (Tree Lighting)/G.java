import java.util.*;

public class G
{
	
	public static final int MAXB = 10;
	public static final double TOL = 0.000001;
	public static final double PI = 4.0*Math.atan(1.0);
	public static barrier[] bars = new barrier[MAXB+1];
	public static interval intervals = new interval();
	public static Queue<beam> beamQ = new LinkedList<beam>();
	public static barpt[] barpts = new barpt[2*MAXB+2];
	public static int nbp = 0;
	public static int[] activeBar = new int[MAXB];
	public static int nActive;
	
	
	public static boolean isZero(double d)
	{
		return (Math.abs(d) < TOL);
	}
	
	public static boolean gEqZero(double d)
	{
		if (isZero(d))
			return true;
		return (d > 0.0);
	}
	
	public static boolean between(double a, double b1, double b2)
	{
		if (b1 < b2)
			return (a>=b1 && a<=b2);
		else
			return (a>=b1 || a<=b2);
	}
	
	public static double dist(double x1, double y1, double x2, double y2)
	{
		double dx = x2 - x1;
		double dy = y2 - y1;
		return Math.sqrt(x2*x2+y2+y2);
	}
	
	public static tuRet checkDist(barrier w, beam bm, double len)
	{
		tuRet ans = new tuRet();
		ans.t = ans.u = 0.0;
		double a = w.e.a;
		double b = w.e.b - bm.x0;
		double c = w.e.c;
		double d = w.e.d - bm.y0;
		double qa = a*a + c*c;
		double qb = 2*(a*b + c*d);
		double qc = b*b + d*d - len*len;
		double disc = qb*qb-4*qa*qc;
		if (disc < 0.0 || isZero(disc)) {
			ans.found = false;
			return ans;
		}
		ans.t = (-qb - Math.sqrt(disc))/qa/2.0;
		ans.u = (-qb + Math.sqrt(disc))/qa/2.0;
		ans.found = true;
		return ans;
	}
	
	public static void insertBp(barpt bp)
	{
		int i=nbp;
		while (i > 0 && barpts[i-1].angle > bp.angle) {
			barpts[i] = barpts[i-1];
			i--;
		}
		barpts[i] = bp;
		nbp++;
	}
	
	public static void swap(Double a, Double b)
	{
		double tmp = a;
		a = b;
		b = tmp;
	}
	
	public static void addInterval(double t1, double t2, double u1, double u2)
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
		interval p, q;
		for(p = intervals; p.next != null; p = p.next)
			if (p.next.b > t1)
				break;
		if (p.next == null || p.next.a > t2) {
			p.next = new interval(t1, t2, p.next);
			return;
		}
		if (p.next.a > t1) 
			p.next.a = t1;
		for(q = p.next; q.next != null;) {
			if (q.next.a > t2)
				break;
			p.next.b = q.next.b;
			p.next.next = q.next.next;
		}
		if (p.next.b < t2)
			p.next.b = t2;
	}
	
	public static beam getReflection(double beta1, double beta2, double x1, double y1, double x2, double y2)
	{
		beam newb = new beam();
		double t, u;
		double alpha = Math.atan2(y2-y1, x2-x1);
		double angle = 2*alpha - beta1;
		while (angle < 0)
			angle += 2*PI;
		newb.alpha1 = angle;
		angle = 2*alpha - beta2;
		while (angle < 0)
			angle += 2*PI;
		newb.alpha2 = angle;
		segment e1 = new segment(x1, y1, x1+Math.cos(newb.alpha1), y1+Math.sin(newb.alpha1));
		newb.e1 = e1;
		segment e2 = new segment(x2, y2, x2+Math.cos(newb.alpha2), y2+Math.sin(newb.alpha2));
		newb.e2 = e2;
		tuRet ans = e1.findInt(e2);
		t = ans.t;
		u = ans.u;
		newb.x0 = e2.a*u + e2.b;
		newb.y0 = e2.c*u + e2.d;
		return newb;
	}
	
	public static boolean inFrontOf(double x0, double y0, barrier w1, barrier w2)
	{
		double t, u;
		if (w2.e.a == 0) {
			segment e = new segment(x0, y0, w2.e.x[0], w2.e.y[0]);
			e.calcParametric();
			tuRet ans = e.findInt(w1.e);
			t = ans.t;
			u = ans.u;
			if (ans.found) {
				if (t < 1.0)
					return true;
			}
			e.x[1] = w2.e.x[1];
			e.y[1] = w2.e.y[1];
			e.calcParametric();
			ans = e.findInt(w1.e);
			t = ans.t;
			u = ans.u;
			if (ans.found) {
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
	
	public static void addToActive(int iw, beam b)
	{
		int i = nActive;
		while (i > 0 && !inFrontOf(b.x0, b.y0, bars[activeBar[i-1]], bars[iw])) {
			activeBar[i] = activeBar[i-1];
			i--;
		}
		activeBar[i] = iw;
		nActive++;
	}
	
	public static void removeFromActive(int iw)
	{
		int i=0;
		while (activeBar[i] != iw)
			i++;
		for(; i<nActive-1; i++)
			activeBar[i] = activeBar[i+1];
		nActive--;
	}
	
	public static void checkBarrier(int iw, beam b, double len)
	//
	//  add barrier to barrier intersection list
	//
	//  iw = index of barrier to check
	//  b = beam
	//  len = maximum length of beam
	//
	{
		barpt bp = new barpt();
		double t, u;
		if (b.iw == iw)				// return if this is barrier that reflected beam
			return;
		barrier w = bars[iw];
		if (b.iw != -1) {			// if a barrier reflected b, don't process barriers
									//    between beam origin and the source barrier
			segment e0 = new segment(b.x0, b.y0, bars[b.iw].e.x[0], bars[b.iw].e.y[0]);
			tuRet ans = e0.findInt(w.e);
			t = ans.t;
			u = ans.u;
			if (!ans.found)
				return;
			if (gEqZero(t) && gEqZero(1.0-t) && gEqZero(u) && gEqZero(1.0-u))
				return;
			segment e1 = new segment(b.x0, b.y0, bars[b.iw].e.x[1], bars[b.iw].e.y[1]);
			ans = e1.findInt(w.e);
			t = ans.t;
			u = ans.u;
			if (!ans.found)
				return;
			if (gEqZero(t) && gEqZero(1.0-t) && gEqZero(u) && gEqZero(1.0-u))
				return;
		}
									// now check if any part of barrier spans beam
										// first, get angles from beam origin to edges of barrier
		double ang0 = Math.atan2(w.e.y[0]-b.y0, w.e.x[0]-b.x0);
		double ang1 = Math.atan2(w.e.y[1]-b.y0, w.e.x[1]-b.x0);
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
		boolean b1 = between(ang0, b.alpha1, b.alpha2);
		boolean b2 = between(ang1, b.alpha1, b.alpha2);
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
		barpt bp2 = new barpt();
		bp2.angle = ang1;
		bp2.iw = iw;
		bp2.ip = 1-start;
		bp2.start = false;
		insertBp(bp2);
	}
	
	public static void process(double x0, double y0, double ang0, double ang1, int iw, int n, double len, int ip)
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
		beam b = new beam();
		b.alpha1 = ang0;
		b.alpha2 = ang1;
		segment e1 = new segment(x0, y0, x0+Math.cos(b.alpha1), y0+Math.sin(b.alpha1));
		b.e1 = e1;
		segment e2 = new segment(x0, y0, x0+Math.cos(b.alpha2), y0+Math.sin(b.alpha2));
		b.e2 = e2;
		b.x0 = x0;
		b.y0 = y0;
		b.iw = -1;
		barrier w = bars[iw];
		double t1, t2, u1, u2;
		tuRet ans = w.e.findInt(b.e1);		// find intersection params
		t1 = ans.t;
		u1 = ans.u;
		ans = w.e.findInt(b.e2);
		t2 = ans.t;
		u2 = ans.u;
		ans = checkDist(w, b, len);
		if (!ans.found)	// adjust params based on max beam length...
			return;							//   ...and return if barrier too far
		u1 = ans.t;
		u2 = ans.u;
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
			beta1 = Math.atan2(y1-b.y0, x1-b.x0);
			beta2 = Math.atan2(y2-b.y0, x2-b.x0);
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
			beamQ.add(newb);
		}
	}
	
	public static void process(beam b, int n, double len)		// process beam b
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
	
	public static void main(String [] args)
	{
		int n, icase = 0;
		double alpha, len;
		Scanner in = new Scanner(System.in);
	
		n = in.nextInt();
		alpha = in.nextDouble();
		len = in.nextDouble();
		while (len != 0) {
			icase++;
			alpha = alpha*PI/180.0;
			intervals.next = null;
			for(int i=0; i<n; i++) {
				bars[i] = new barrier();
				bars[i].e.x[0] = in.nextDouble();
				bars[i].e.y[0] = in.nextDouble();
				bars[i].e.x[1] = in.nextDouble();
				bars[i].e.y[1] = in.nextDouble();
				int b = in.nextInt();
				bars[i].isRefl = (b==1);
				bars[i].e.calcParametric();
			}
			bars[n] = new barrier();
			bars[n].e.x[0] = in.nextDouble();
			bars[n].e.y[0] = in.nextDouble();
			bars[n].e.x[1] = in.nextDouble();
			bars[n].e.y[1] = in.nextDouble();
			bars[n].isRefl = false;
			bars[n].e.calcParametric();
	
			beam b = new beam();
			b.alpha1 = PI/2 - alpha/2;
			b.alpha2 = PI/2 + alpha/2;
			segment e1 = new segment(0.0, 0.0, Math.sin(alpha/2), Math.cos(alpha/2));
			b.e1 = e1;
			segment e2 = new segment(0.0, 0.0, -Math.sin(alpha/2), Math.cos(alpha/2));
			b.e2 = e2;
			b.x0 = b.y0 = 0.0;
			b.iw = -1;
				
			beamQ.add(b);
			while (!beamQ.isEmpty()) {
				b = beamQ.remove();
				process(b, n, len);
			}
	
			double ans = 0;
			for(interval p = intervals.next; p != null; p = p.next) {
				ans += p.b - p.a;
			}
			System.out.printf("Case %d: %.2f\n", icase, 100*ans);
			n = in.nextInt();
			alpha = in.nextDouble();
			len = in.nextDouble();
		}
	}
}	

class segment {
	public double[] x;
	public double[] y;	// endpoints
	public double a, b, c, d;	// x = at + b, y = ct+d
	
	public segment()
	{
		x = new double[2];
		y = new double[2];
		x[0] = 0.0;
		y[0] = 0.0;
		x[1] = 0.0;
		y[1] = 0.0;
		calcParametric();
	}

	public segment(double xx1, double yy1, double xx2, double yy2)
	{
		x = new double[2];
		y = new double[2];
		x[0] = xx1;
		y[0] = yy1;
		x[1] = xx2;
		y[1] = yy2;
		calcParametric();
	}
	
	public void calcParametric()
	{
		a = x[1]-x[0];
		b = x[0];
		c = y[1]-y[0];
		d = y[0];
	}
	
	public tuRet findInt(segment s)	// t - this seg, u - passed in seg
	{
		tuRet ans = new tuRet();
		ans.t = ans.u = 0;
		ans.found = false;
		double den = s.a*c - a*s.c;
		if (G.isZero(den))
			return ans;
		double bb = s.b-b;
		double dd = s.d-d;
		ans.t = (s.a*dd-s.c*bb)/den;
		ans.u = (a*dd - c*bb)/den;
		ans.found = true;
		return ans;
	}
}
class beam {
	public segment e1, e2;
	public double x0, y0;				// origin of beam
	public double alpha1, alpha2;		// angles beam edges make with the x-axis
	public int iw;						// reflecting barrier
}

class barrier {
	public segment e;
	public boolean isRefl;

	public barrier()
	{
		e = new segment();
	}
}
class interval
{
	public double a, b;
	public interval next;

	public interval()
	{
		a = 0;
		b = 0;
		next = null;
	}

	public interval(double aa, double bb, interval nn)
	{
		a = aa;
		b = bb;
		next = nn;
	}
}
class barpt
{
	public double angle;			// angle relative to beam origin
	public int iw;					// barrier index
	public int ip;					// point index
	public boolean start;				// true if start pt of barrier
}
class tuRet
{
	public boolean found;
	public double t, u;
}
