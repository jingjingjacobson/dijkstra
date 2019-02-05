package A6_Dijkstra;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.PriorityQueue;

import A6_Dijkstra.Edge;
import A6_Dijkstra.Vertex;

public class DiGraph implements DiGraph_Interface{

	HashMap edgeMap;
	HashMap labelMap;
	HashMap idMap;
	HashSet ids;


	public DiGraph() {
		// if you then write others, this one will still be there
		HashMap<String,Edge> edgeMap = new HashMap<String,Edge>();
		HashMap<String,Vertex> labelMap = new HashMap<String,Vertex>();
		HashMap<String,Vertex> idMap = new HashMap<String,Vertex>();
		HashSet<Long> ids = new HashSet<Long>();

		this.edgeMap = edgeMap;
		this.labelMap = labelMap;
		this.idMap = idMap;
		this.ids = ids;
	}

	@Override
	public boolean addNode(long idNum, String label) {
		if(idNum >= 0 && labelMap.get(label) == null && idMap.get(Long.toString(idNum)) == null) {
			Vertex node = new Vertex(idNum, label);
			labelMap.put(label,node);
			idMap.put(Long.toString(idNum), node);
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public boolean addEdge(long idNum, String sLabel, String dLabel, long weight, String eLabel) {
		Vertex source = (Vertex) labelMap.get(sLabel);
		Vertex destination = (Vertex) labelMap.get(dLabel);

		if (source == null || destination == null) {
			return false;
		}

		String sourceID = Long.toString(source.getID());
		String destinationID = Long.toString(destination.getID());

		String label = sourceID + "->" + destinationID;



		if(idNum >= 0 && edgeMap.get(label) == null && !ids.contains(idNum)) {


			Edge newEdge = new Edge(idNum,source,destination,weight,eLabel);
			edgeMap.put(label, newEdge);
			ids.add(idNum);

			source.addOut(label,newEdge);
			destination.addIn(label,newEdge);


			return true;

		}
		else {
			return false;
		}
	}

	@Override
	public boolean delNode(String label) {
		if(labelMap.get(label) == null) {
			return false;
		}
		else {
			Vertex v = (Vertex) labelMap.get(label); // this node
			String id = Long.toString(v.getID()); // vertex id

			Iterator iterIn = v.edgesIn.entrySet().iterator(); // iterators


			while(iterIn.hasNext()) {
				HashMap.Entry pair = (HashMap.Entry)iterIn.next();
				String key = (String) pair.getKey(); // get edge key
				Edge e = (Edge) edgeMap.get(key); // get the edge from edgeMap
				delEdge(e.getSLabel(), e.getDLabel()); // delete edge



			}

			Iterator iterOut = v.edgesOut.entrySet().iterator();
			while(iterOut.hasNext()) {
				HashMap.Entry pair = (HashMap.Entry)iterOut.next();
				String key = (String) pair.getKey();
				Edge e = (Edge) edgeMap.get(key);
				delEdge(e.getSLabel(), e.getDLabel());

				//				iterOut.remove();
			}

			labelMap.remove(label); // remove node from the labelMap and idMap
			idMap.remove(id);

			return true;
		}
	}

	@Override
	public boolean delEdge(String sLabel, String dLabel) {
		Vertex source = (Vertex) labelMap.get(sLabel); // edge source
		Vertex destination = (Vertex) labelMap.get(dLabel); // edge destination

		if (source == null || destination == null) {
			return false;
		}

		String key = Long.toString(source.getID()) + "->" + Long.toString(destination.getID()); // this will be the edge key, which is just "sLabel -> dLabel"


		if(edgeMap.get(key) == null) {
			return false;
		}
		else {
			Edge e = (Edge) edgeMap.get(key);
			edgeMap.remove(key);
			source.delOut(key);
			destination.delIn(key);
			ids.remove(e.id);
			return true;
		}
	}

	@Override
	public long numNodes() {
		return labelMap.size();
	}

	@Override
	public long numEdges() {
		return edgeMap.size();
	}

	@Override
	public String[] topoSort() {
		int visitedNodes = 0;
		String sorted[] = new String[(int)numNodes()];
		Queue<Vertex> q = new LinkedList<Vertex>();
		HashMap<Vertex,Integer> inDegrees = new HashMap<Vertex,Integer>();
		Iterator iterVertex = labelMap.entrySet().iterator();
		while(iterVertex.hasNext()) {
			HashMap.Entry pair = (HashMap.Entry)iterVertex.next();
			Vertex v = (Vertex) pair.getValue(); 
			inDegrees.put(v, v.getInDegree());
			if(v.getInDegree() == 0) {
				q.add(v);
			}


		}
		while(!q.isEmpty()) {
			Vertex v = q.poll();
			sorted[visitedNodes] = v.getLabel();
			visitedNodes += 1;
			Iterator neighbors = v.edgesOut.entrySet().iterator();
			while(neighbors.hasNext()) {
				HashMap.Entry pair = (HashMap.Entry)neighbors.next();
				Edge nextEdge = (Edge) pair.getValue();
				Vertex neighborNode = nextEdge.destination;
				int newInDegree = inDegrees.get(neighborNode) - 1;
				inDegrees.remove(neighborNode);

				if(newInDegree > 0) {
					inDegrees.put(neighborNode, newInDegree);
				}
				else {
					q.add(neighborNode);
				}
			}
		}
		if(visitedNodes != numNodes()) {
			return null;
		}
		else {

			return sorted;

		}
	}

	@Override
	public ShortestPathInfo[] shortestPath(String label) {
		HashSet<Vertex> sptSet = new HashSet<>();
		HashSet<Vertex> sptComplementSet = new HashSet<>();
		PriorityQueue<Vertex> minBinHeap = new PriorityQueue<Vertex>(new Compare());
		ShortestPathInfo[] shortestPathArray = new ShortestPathInfo[(int) numNodes()];
		int i = 0;
		Vertex source = (Vertex) labelMap.get(label);
		Iterator iterInitialDistances = labelMap.entrySet().iterator();
		while(iterInitialDistances.hasNext()) {
			HashMap.Entry pair = (HashMap.Entry)iterInitialDistances.next();
			Vertex thisNode = (Vertex) pair.getValue();

			if(pair.getKey() == label) {
				thisNode.distance = 0;
				minBinHeap.add(thisNode);
				sptComplementSet.add(thisNode);
			}
			else {
				thisNode.distance = Long.MAX_VALUE;
				sptComplementSet.add(thisNode);
			}
		}
		while(!minBinHeap.isEmpty()){ 
			Vertex u = (Vertex) minBinHeap.poll();
			if(u != null && !sptSet.contains(u)) {
				sptSet.add(u);
				sptComplementSet.remove(u);
				shortestPathArray[i] = new ShortestPathInfo(u.label,u.distance);
				i +=1;

				Iterator iterOut = u.edgesOut.entrySet().iterator();
				while(iterOut.hasNext()) {
					HashMap.Entry pair2 = (HashMap.Entry)iterOut.next();
					Edge thisEdge = (Edge) pair2.getValue();
					Vertex v = (Vertex) thisEdge.destination;
					if(v.distance > thisEdge.weight + u.distance) {
						v.distance = thisEdge.weight + u.distance;
						minBinHeap.add(v);
					}
				}	
			}

		}

		for(Vertex v: sptComplementSet) {
			v.distance = -1;
			shortestPathArray[i] = new ShortestPathInfo(v.label,v.distance);
			i += 1;
		} 
		return shortestPathArray;


	}
}


