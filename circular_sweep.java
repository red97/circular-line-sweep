import java.util.*;

class point {
	double x, y;
	int as_line;
	double ang;

	point(double x, double y, int i) {
		this.x = x;
		this.y = y;
		as_line = i;
		if(i != -2)
			ang = circular_sweep.angle(new point(x, y, -2));
	}
}

class line {
	point p1, p2;
	double m, c;
	int ix;

	line(point p1, point p2) {
		this.p1 = p1;
		this.p2 = p2;
		m = (p2.y - p1.y) / (p2.x - p1.x);
		c = ((p1.y * p2.x) - (p1.x * p2.y)) / (p2.x - p1.x);
		ix = -1;
	}
}

public class circular_sweep 
{

	static point p, a_inf;
	static ArrayList<line> s;
	static PriorityQueue<point> pq;
	static line cur_line;

	public static void main(String[] args) {

		// comparator for priority queue to sort by angle
		Comparator<point> angle_comparator = new Comparator<point>() {
			@Override
			public int compare(point p1, point p2) {
				return ((p1.ang - p2.ang) > 0.0) ? 1 : ((p1.ang - p2.ang) == 0) ? ( (ec_dist(p, p1) < ec_dist(p, p2))?-1:(ec_dist(p, p1)  == ec_dist(p, p2))?0:1) : -1;
			}
		};
		Comparator<line> distance_comparator = new Comparator<line>() {
			@Override
			public int compare(line l1, line l2) {
				return ((distance(l1) - distance(l2)) > 0.0) ? 1 : ((distance(l1) - distance(l2)) == 0) ? 0 : -1;
			}
		};

		s = new ArrayList<line>();
		pq = new PriorityQueue<point>(angle_comparator);
		cur_line = null;

		Scanner sc = new Scanner(System.in);

		// taking input of the point p
		System.out.print("Enter the point p(x, y) : ");
		p = new point(sc.nextDouble(), sc.nextDouble(), -2);
		a_inf = new point((double) Integer.MIN_VALUE, p.y, -2);

		// taking input of all the lines
		int kx = 0;
		HashMap<line, line> line_map = new HashMap<line, line>();
		System.out.print("Enter the number of lines : ");
		int n = sc.nextInt();
		System.out.println("Enter the lines as x1 y1 x2 y2 :-");
		for (int i = 0; i < n; i++) {

			point p1 = new point(sc.nextDouble(), sc.nextDouble(), kx);
			point p2 = new point(sc.nextDouble(), sc.nextDouble(), kx);

			line l1 = new line(p1, p2);
			l1.ix = i;

			double a1 = p1.ang;
			double a2 = p2.ang;

			if (qd(a1, a2, 1) || qd(a1, a2, 2) || qd(a1, a2, 3) || qd(a1, a2, 4)) 
			{
				s.add(l1);
				pq.add(p1);
				pq.add(p2);
				line_map.put(l1, l1);
				kx++;
			} 
			else if ((qd(a1, 1) && qd(a2, 4)) || (qd(a1, 2) && qd(a2, 3)) || (qd(a1, 4) && qd(a2, 1))|| (qd(a1, 3) && qd(a2, 2)))
			{

				double y = p.y;
				double x = (p1.x == p2.x)?p1.x:(y - l1.c) / l1.m;

				point tmp_point = new point(x, y, kx);
				
				if(tmp_point.ang == 0 && qd(a1, 4))
					tmp_point.ang = 360;
				double tmp_ang = tmp_point.ang;//remove and make angle 360 for req
				
				line l11 = new line(p1, tmp_point);
				s.add(l11);
				pq.add(p1);
				pq.add(tmp_point);
				line_map.put(l11, l1);
				kx++;

				tmp_point = new point(x, y, kx);
				p2.as_line = kx;
				
				if(tmp_point.ang == 0 && qd(a2, 4))
					tmp_point.ang = 360;
				tmp_ang = tmp_point.ang;
				
				line l12 = new line(p2, tmp_point);
				s.add(l12);
				pq.add(p2);
				pq.add(tmp_point);
				line_map.put(l12, l1);
				kx++;

			} 
			else if ((qd(a1, 1) && qd(a2, 2)) || (qd(a1, 3) && qd(a2, 4)) || (qd(a1, 2) && qd(a2, 1)) || (qd(a1, 4) && qd(a2, 3)))
			{

				double x = p.x;
				double y = (l1.m * x) + l1.c;

				point tmp_point = new point(x, y, kx);
				line l11 = new line(p1, tmp_point);
				s.add(l11);
				pq.add(p1);
				pq.add(tmp_point);
				line_map.put(l11, l1);
				kx++;

				tmp_point = new point(x, y, kx);
				p2.as_line = kx;
				line l12 = new line(p2, tmp_point);
				s.add(l12);
				pq.add(p2);
				pq.add(tmp_point);
				line_map.put(l12, l1);
				kx++;

			} 
			else 
			{

				double y1 = p.y;
				double x1 = (y1 - l1.c) / l1.m;
				double x2 = p.x;
				double y2 = (l1.m * x2) + l1.c;
				line tmp_line1, tmp_line2, tmp_line3;

				point p11 = new point(x1, y1, kx);
				point p12 = new point(x1, y1, kx + 1);
				point p21 = new point(x2, y2, kx + 1);
				point p22 = new point(x2, y2, kx + 2);

				if (ec_dist(p1, p11) < ec_dist(p2, p11)) 
				{
					
					if(p11.ang == 0 && qd(a1, 4))
						p11.ang = 360;
					if(p22.ang == 0 && qd(a2, 4))
						p22.ang = 360;
					if(p12.ang == 0 && p21.ang == 270)
						p12.ang = 360;
					if(p21.ang == 0 && p12.ang == 270)
						p21.ang = 360;
					
					p1.as_line = kx;
					p2.as_line = kx + 2;
						
					tmp_line1 = new line(p1, p11);
					tmp_line2 = new line(p12, p21);
					tmp_line3 = new line(p22, p2);
				} 
				else
				{
					
					if(p11.ang == 0 && qd(a2, 4))
						p11.ang = 360;
					if(p22.ang == 0 && qd(a1, 4))
						p22.ang = 360;
					if(p12.ang == 0 && p21.ang == 270)
						p12.ang = 360;
					if(p21.ang == 0 && p12.ang == 270)
						p21.ang = 360;
					
					p1.as_line = kx + 2;
					p2.as_line = kx;
					
					tmp_line1 = new line(p2, p11);
					tmp_line2 = new line(p12, p21);
					tmp_line3 = new line(p22, p1);
				}

				s.add(tmp_line1);
				s.add(tmp_line2);
				s.add(tmp_line3);
				pq.add(p1);
				pq.add(p11);
				pq.add(p12);
				pq.add(p21);
				pq.add(p22);
				pq.add(p2);
				line_map.put(tmp_line1, l1);
				line_map.put(tmp_line2, l1);
				line_map.put(tmp_line3, l1);
				kx += 3;

			}
		}
		
		for(int i=0;i<s.size();i++)
		{
			s.get(i).ix = i;
			s.get(i).p1.as_line = i;
			s.get(i).p2.as_line = i;
		}

		PriorityQueue<line> cs = new PriorityQueue<line>(distance_comparator);
		ArrayList<line> vl = new ArrayList<line>();
		int tp = 0;
		HashMap<line, Integer> map = new HashMap<line, Integer>();
		HashMap<line, Integer> v_map = new HashMap<line, Integer>();
		while (!pq.isEmpty()) 
		{

			point tmp_point = pq.poll();
			line tmp_line = s.get(tmp_point.as_line);
			
			if(!map.containsKey(tmp_line)) 
			{
				map.put(tmp_line, 0);
				cur_line = new line(tmp_point, p);
				cs.add(tmp_line);
				if(!v_map.containsKey(cs.peek())) 
				{ 
					v_map.put(cs.peek(), 0); 
					vl.add(cs.peek());
					tp++;
				}
			} 
			else 
			{
				map.remove(tmp_line);
				cur_line = new line(p, tmp_point);
				cs.remove(tmp_line);
				if(!cs.isEmpty() && !(cs.peek().p1.ang == tmp_point.ang || cs.peek().p2.ang == tmp_point.ang) && !v_map.containsKey(cs.peek())) 
				{
					v_map.put(cs.peek(), 0);
					vl.add(cs.peek());
					tp++; 
				}
			}
				
		}
		
		HashMap<Integer, Integer> r_map = new HashMap<Integer, Integer>();
		for(int i=0;i<tp;i++)
		{
			int val = line_map.get(vl.get(i)).ix;
			if(!r_map.containsKey(val))
			{
				System.out.println("line "+val);
				r_map.put(val, 1);
			}
		}
		
	}

	public static double distance(line l1) {
		double x = (cur_line.c - l1.c) / (l1.m - cur_line.m);
		double y = ((cur_line.m * l1.c) - (l1.m * cur_line.c)) / (cur_line.m - l1.m);
		
		if(l1.p1.x == l1.p2.x)
		{
			x = l1.p1.x;
			y = (cur_line.m*x)+cur_line.c;
		}
		if(cur_line.p1.x == cur_line.p2.x)
		{
			x = cur_line.p1.x;
			y = (l1.m*x)+l1.c;
			
		}
		
		return Math.sqrt(Math.pow((y - p.y), 2) + Math.pow((x - p.x), 2));
	}

	public static double angle(point p1) {
		double slope1 = Math.atan((p.y - a_inf.y) / (p.x - a_inf.x));
		double slope2 = Math.atan((p.y - p1.y) / (p.x - p1.x));
		double angle = Math.toDegrees(slope2 - slope1);// Math.atan((slope1 - slope2) / (1 - (slope1 * slope2)));
		if (p1.y > p.y) {
			if (p1.x <= p.x)
				angle = 0 - angle;
			else
				angle = 180 - angle;
		} else {
			if (p1.x <= p.x)
				angle = 360 - angle;
			else
				angle = 180 - angle;
		}
		return angle%360;
	}

	public static boolean qd(double a1, double a2, int q) {
		if (q == 1)
			return (a1 >= 0.0 && a1 < 90.0 && a2 >= 0.0 && a2 < 90.0);
		if (q == 2)
			return (a1 >= 90.0 && a1 < 180.0 && a2 >= 90.0 && a2 < 180.0);
		if (q == 3)
			return (a1 >= 180.0 && a1 < 270.0 && a2 >= 180.0 && a2 < 270.0);
		if (q == 4)
			return (a1 >= 270.0 && a1 < 360.0 && a2 >= 270.0 && a2 < 360.0);

		return false;
	}

	public static boolean qd(double a1, int q) {
		if (q == 1)
			return (a1 >= 0.0 && a1 < 90.0);
		if (q == 2)
			return (a1 >= 90.0 && a1 < 180.0);
		if (q == 3)
			return (a1 >= 180.0 && a1 < 270.0);
		if (q == 4)
			return (a1 >= 270.0 && a1 < 360.0);

		return false;

	}

	public static double ec_dist(point p1, point p2) {
		return Math.sqrt(Math.pow((p1.y - p2.y), 2) + Math.pow((p1.x - p2.x), 2));
	}
}

/*

0 0
5
-6 2 -6 4
-5 -5 -5 5
-3 6 2 6
2 3 -1 -1
2 -6 -4 3

ans line 3, line 4

*/