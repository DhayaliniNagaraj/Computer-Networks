package dijkstra;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Graph {

	HashMap<String, Edge[]> nodes = new HashMap<String, Edge[]>();
	int graphSize= 0;
	Iterator iterator = nodes.entrySet().iterator();

	public HashMap<String, Edge[]> getNodes() {
		return nodes;
	}

	public void setNodes(HashMap<String, Edge[]> nodes) {
		this.nodes = nodes;
	}

	public int getGraphSize() {
		return graphSize;
	}

	public void setGraphSize(int graphSize) {
		this.graphSize = graphSize;
	}

	public void addNode(String fromRouter, Edge[] edges){
		nodes.put(fromRouter, edges);
		this.graphSize++;
	}
	
	public void displayGraph(){
		Iterator iterator = nodes.entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry pair = (Map.Entry)iterator.next();
			String node = (String) pair.getKey();
			Edge[] edges = (Edge[]) pair.getValue();
			System.out.println(node+":");
	        for (Edge edge : edges) {
				System.out.println(edge.getFromNode()+" "+edge.getToNode()+" "+edge.getWeight());
			}
	        //iterator.remove();
		}
	}
	
	
	
	public Iterator getNodesIterator(){
		
			iterator = this.nodes.entrySet().iterator();
		return iterator;
	}
}
