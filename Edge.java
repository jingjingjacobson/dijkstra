package A6_Dijkstra;



public class Edge {
	long id;
	Vertex source;
	Vertex destination;
	String label;
	long weight;


	public Edge(long id, Vertex source, Vertex destination, long weight, String label) {
		this.id = id;
		this.source = source;
		this.destination = destination;
		this.weight = weight;
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public String getSLabel() {
		return source.getLabel();
	}

	public String getDLabel() {
		return destination.getLabel();
	}





}
