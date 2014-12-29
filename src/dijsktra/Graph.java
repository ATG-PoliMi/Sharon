package dijsktra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class Graph {
	private Map<String,Vertex> vertices = new HashMap<String, Vertex>();
	public PriorityQueue<Vertex> pq = new PriorityQueue<Vertex>(2000, new Vertex());
	private ArrayList<String> path = new ArrayList<String>();
	public ArrayList<String> getPath() {
		return path;
	}
	public void setPath(ArrayList<String> path) {
		this.path = path;
	}
	public void addVertex(String source, String dest, int weight) {
		Vertex v = getVertex(source);
		Vertex w = getVertex(dest);
		v.adjacentD.add(new Edge(w, weight));
		w.adjacentD.add(new Edge(v, weight));
	}
	public Vertex getVertex(String name) {
		Vertex v = (Vertex) vertices.get(name);
		if(v == null) {
			v = new Vertex(name);
			vertices.put(name, v);
		}
		return v;
	}
	public void dijkstra (String init) {
		Vertex current;
		Vertex start = (Vertex) vertices.get(init);
		start.setDistance(0);
		pq.add(start);
		int handled = 0;
		while (handled < vertices.size()) {
			current = pq.poll();
			//weight associated with the current vertex
			int vertWeight = current.getDistance();
			if(current.known == false) {
				handled++;
				current.known = true;
				compAdjEdges(current, vertWeight);

			}
		}
	}
	public void compAdjEdges(Vertex s, int w) {
		Vertex source = s;
		int vertWeight = w;
		int tempDist;
		int origDist;
		/* Each adjacent edge to the source Vertex,
		 * (if it has not yet been handled) 
		 * has a weight which is added to the current pathWeight.
		 * If this pathWeight is smaller than the one associated with
		 * the given edge's vertex (destination.getDistance())
		 * then the vertex's distance is updated and this path is 
		 * added to the priority queue
		 */
		for(Edge e : source.adjacentD) {
			Edge curEdge = e;
			Vertex curVer = e.getDestination();
			origDist = curVer.getDistance();
			if (curVer.known == false) {
				tempDist = curEdge.getWeight();
				tempDist = tempDist + vertWeight;
				if (tempDist < origDist) {
					curVer.setDistance(tempDist);
					curVer.previous = source;
					pq.add(curVer);
				}
			}
		}
	}
	public void printPath(Vertex c) {
		Vertex current = c;
		if (current.previous != null) {
			printPath(current.previous);

			//System.out.println("PATH: "+ current.name+ " Not null");
			//path.add(current.name);
		}
//		if (current.previous == null)
//			System.out.println("PATH: "+ current.name+ " Null");
		path.add(current.name);		
	}
}