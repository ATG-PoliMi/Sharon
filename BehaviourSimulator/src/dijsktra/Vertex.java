package dijsktra;

import java.util.*;
public class Vertex implements Comparator<Vertex>{
	public String name;
	public LinkedList<Edge> adjacentD;
	public LinkedList<Vertex> adjacentT;
	public int distance;
	public Vertex previous;    //previous vertex on shortest path
	public boolean known;
	public int defaultDis = Integer.MAX_VALUE;
	public Vertex() {
		name = null;
		distance = defaultDis;
		previous = null;
		known = false;
	}
	public Vertex(String argName) {
		this.name = argName;
		adjacentD = new LinkedList<Edge>();
		adjacentT = new LinkedList<Vertex>();
		distance = defaultDis;
		previous = null;
		known = false;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int w) {
		distance = w;
	}
	public int compare(Vertex v1, Vertex v2) {
		int w1 = v1.getDistance();
		int w2 = v2.getDistance();
		if(w1 > w2)
			return 1;
		else if (w1 < w2)
			return -1;
		else
			return 0;
	}
}