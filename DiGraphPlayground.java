package A6_Dijkstra;


public class DiGraphPlayground {

	public static void main (String[] args) {

		// thorough testing is your responsibility
		//
		// you may wish to create methods like 
		//    -- print
		//    -- sort
		//    -- random fill
		//    -- etc.
		// in order to convince yourself your code is producing
		// the correct behavior
		exTest();
	}

	public static void exTest(){
		DiGraph d = new DiGraph();
		d.addNode(0, "0");
		d.addNode(1, "1");
		d.addNode(2, "2");
		d.addNode(3, "3");
		d.addNode(4, "4");
		d.addNode(5, "5");
		d.addNode(6, "6");
		d.addEdge(0, "4", "5", 2, null);
		d.addEdge(1, "0", "5", 3, null);
		d.addEdge(2, "3", "2", 6, null);
		d.addEdge(3, "6", "1", 4, null);
		d.addEdge(4, "4", "0", 1, null);
		

/*
addNode(0, 0)
addNode(1, 1)
addNode(2, 2)
addNode(3, 3)
addNode(4, 4)
addNode(5, 5)
addNode(6, 6)
addEdge(0, 4, 5, 2, null)
addEdge(1, 0, 5, 3, null)
addEdge(2, 3, 2, 6, null)
addEdge(3, 6, 1, 4, null)
addEdge(4, 4, 0, 1, null)
shortestPath(0)

correct answer:
shortest Path List:
(0, 0)
(1, -1)
(2, -1)
(3, -1)
(4, -1)
(5, 3)
(6, -1)

		 * 
		 */


//		System.out.println("numEdges: "+d.numEdges());
//		System.out.println("numNodes: "+d.numNodes()); 
		printDijkstra(d.shortestPath("0"));
		// printTOPO(d.topoSort());

	}
	public static void printTOPO(String[] toPrint){
		System.out.print("TOPO Sort: ");
		for (String string : toPrint) {
			System.out.print(string+" ");
		}
		System.out.println();
	}
	
	public static void printDijkstra(ShortestPathInfo[] toPrint) {
		for (ShortestPathInfo string : toPrint) {
			System.out.print(string+" ");
		}
		System.out.println();
	}

}