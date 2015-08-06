package it.polimi.deib.atg.sharon.utils.dijsktra;

public class Edge {
	public Vertex destination;
	public int weight;
	public Edge() {
		destination = null;
		weight = 1;
	}
	public Edge(Vertex d, int w) {
		destination = d;
		weight = w;
	}
	public int getWeight() {
		return weight;
	}
	public Vertex getDestination() {
		return destination;
	}
}