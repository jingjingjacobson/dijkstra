package A6_Dijkstra;

import java.util.Comparator;

public class Compare implements Comparator<Vertex> {

	@Override
	public int compare(Vertex v1, Vertex v2) {
		if(v1.distance > v2.distance) {
			return 1;
		}
		else if(v1.distance < v2.distance) {
			return -1;
		}
		else {
			return 0;
		}
	}

}
