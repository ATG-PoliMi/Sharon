package dijsktra;

import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Dijkstra {
	
	public static void computePaths(Vertex source) {
		
		source.minDistance = 0.;
		PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
		vertexQueue.add(source);

		while (!vertexQueue.isEmpty()) {
			Vertex u = vertexQueue.poll();

			// Visit each edge exiting u
			for (Edge e : u.adjacencies)
			{
				Vertex v = e.target;
				double weight = e.weight;
				double distanceThroughU = u.minDistance + weight;
				if (distanceThroughU < v.minDistance) {
					vertexQueue.remove(v);

					v.minDistance = distanceThroughU ;
					v.previous = u;
					vertexQueue.add(v);
				}
			}
		}
	}

	public static List<Vertex> getShortestPathTo(Vertex target)	{
		List<Vertex> path = new ArrayList<Vertex>();
		for (Vertex vertex = target; vertex != null; vertex = vertex.previous)
			path.add(vertex);

		Collections.reverse(path);
		return path;
	}

	public static void main(String[] args) {
		Vertex v0 = new Vertex("Harrisburg");
		Vertex v1 = new Vertex("Baltimore");
		//...
		v0.adjacencies = new Edge[]{ new Edge(v1,  79.83), new Edge(v5,  81.15) };
		v1.adjacencies = new Edge[]{ new Edge(v0,  79.75), new Edge(v2,  39.42),
		Vertex[] vertices = { v0, v1};
		
		//...
		computePaths(v0);
		for (Vertex v : vertices)
		{
			System.out.println("Distance to " + v + ": " + v.minDistance);
			List<Vertex> path = getShortestPathTo(v);
			System.out.println("Path: " + path);
		}
	}
}
