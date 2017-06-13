package dijkstra;

public class Edge {

	String fromNode;
	String toNode;
	int weight;
	
	
	
	public Edge() {
		
	}
	
	public Edge(String fromNode, String toNode, int weight) {
		super();
		this.fromNode = fromNode;
		this.toNode = toNode;
		this.weight = weight;
	}
	public String getFromNode() {
		return fromNode;
	}
	public void setFromNode(String fromNode) {
		this.fromNode = fromNode;
	}
	public String getToNode() {
		return toNode;
	}
	public void setToNode(String toNode) {
		this.toNode = toNode;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	
}
