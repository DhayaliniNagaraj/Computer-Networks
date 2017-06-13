package dijkstra;

import static dijkstra.Display.getDestinationRouter;
import static dijkstra.Display.getFileInput;
import static dijkstra.Display.getSourceRouter;
import static dijkstra.Display.printConnectionTable;
import static dijkstra.Display.printExceptionError;
import static dijkstra.Display.printGoodBye;
import static dijkstra.Display.printMainMenu;
import static dijkstra.Display.printUserInputError;
import static dijkstra.Display.getRouterToRemove;
import static dijkstra.Display.printSourceRouterError;
import static dijkstra.Display.printDestinationRouterError;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * Calculates the shortest path for the nodes based on user input.
 * 
 * @author
 *
 */
public class Algorithm {

	static Graph graph = null;
	static Graph modifiedGraph = null;
	static String sourceRouter = null;
	static String destinationRouter = null;
	static String routerToRemove = null;
	static String originalSourceRouter = null;
	HashMap<String, Edge[]> shortestPathMap = null;
	static boolean isFirstTimeLoading = true;
	static boolean isFileLoaded = false;
	static boolean isTopologyModified = false;
	static boolean isProcess = true;
	static int minimumDistance = 0;
	static Set<String> visitedNodes = new HashSet<String>();
	static Set<String> indirectNodes = new HashSet<String>();
	static Set<String> directNodes = new HashSet<String>();
	String path;	

	/**
	 * Main method used for execution.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		Algorithm algorithm = new Algorithm();
		processUserInput(algorithm);
	}

	/**
	 * Displays the output based on the user input.
	 */
	private static void processUserInput(Algorithm algorithm ) {
		
		do {
			printMainMenu();
			System.out.println("Enter the command: ");
			Scanner userCommand = new Scanner(System.in);
			try {
				
				int userInput = userCommand.nextInt();
				if(userInput != 1 && !isFileLoaded){
					System.out.println("Please enter 1 to load the file ");
				}
				if (userInput >= 1 && userInput <= 5) {
					if (userInput == 1) {
						processInputOne(algorithm,userCommand);
					} else if (userInput == 2 && isFileLoaded) {
						processInputTwo(algorithm,userCommand);
					} else if (userInput == 3 && isFileLoaded) {
						processInputThree(algorithm,userCommand);
					} else if (userInput == 4 && isFileLoaded ) {
						if(!isTopologyModified){
							processInputFour(algorithm,userCommand);
						}else{
							System.out.println("Topology is already modified... ");	
						}
					} else if (userInput == 5) {
						processInputFive(userCommand);
					}
				} else {
					printUserInputError();
				}
			}catch (InputMismatchException e) {
				printExceptionError();
			}catch (Exception e) {
				e.printStackTrace();				
			}

		} while (isProcess);
	}
	
	/**
	 * Loads the file.
	 * 
	 * @param algorithm
	 * @param userCommand
	 */
	private static void processInputOne(Algorithm algorithm, Scanner userCommand) {
		String fileName = getFileInput(userCommand);
		graph = null;
		routerToRemove = null;
		isTopologyModified = false;
		if (fileName != null) {
			algorithm.loadFile(fileName, true);
			 
		}

	}
	
	/**
	 * Prepares the connection table for the given router.
	 * 
	 * @param algorithm
	 * @param userCommand
	 */
	private static void processInputTwo(Algorithm algorithm, Scanner userCommand) {
		sourceRouter = getSourceRouter(userCommand);
		if (sourceRouter != null) {
			originalSourceRouter = sourceRouter;
			if (sourceRouter.equalsIgnoreCase(routerToRemove)) {
				sourceRouter = null;
				printSourceRouterError();
			} else {
				algorithm.connectionTable(graph, sourceRouter);
			}
		}
	}
	
	/**
	 * Calculates the shortest path for the given nodes.
	 * 
	 * @param algorithm
	 * @param userCommand
	 */
	private static void processInputThree(Algorithm algorithm, Scanner userCommand) {
		if (sourceRouter == null) {
			sourceRouter = getSourceRouter(userCommand);
			if (sourceRouter != null) {
				originalSourceRouter = sourceRouter;
			} else {
				return;
			}
		}
		destinationRouter = getDestinationRouter(userCommand);
		if (destinationRouter != null) {
			if (destinationRouter
					.equalsIgnoreCase(originalSourceRouter)) {
				System.out.println("The shortest path from router "
						+ originalSourceRouter + " to "
						+ destinationRouter
						+ " is 0, the total cost is 0");
			} else if(sourceRouter.equalsIgnoreCase(routerToRemove)){
				sourceRouter = null;
				printSourceRouterError();
			}else if(destinationRouter.equalsIgnoreCase(routerToRemove)){
				destinationRouter=null;
				printDestinationRouterError();
			}else {
				sourceRouter = originalSourceRouter;
				reintialiseValues();
				algorithm.calculateShortestPath(graph);
				sourceRouter=originalSourceRouter;
			}
		}
	}
	
	/**
	 * Prepares the alternate path if a router is not available.
	 * 
	 * @param algorithm
	 * @param userCommand
	 */
	private static void processInputFour(Algorithm algorithm, Scanner userCommand) {
		isTopologyModified = true;
		routerToRemove = getRouterToRemove(userCommand);
		if (routerToRemove != null) {
			algorithm.modifyGraph();
			if (sourceRouter != null || routerToRemove.equalsIgnoreCase(sourceRouter)) {
				algorithm.connectionTable(modifiedGraph,sourceRouter);
			}
			if (destinationRouter !=null || routerToRemove.equalsIgnoreCase(destinationRouter)) {
				algorithm.connectionTable(modifiedGraph,destinationRouter);
				originalSourceRouter  = sourceRouter;
			}
			if(sourceRouter != null && destinationRouter !=null && !sourceRouter.equalsIgnoreCase(routerToRemove) && !destinationRouter.equalsIgnoreCase(routerToRemove)){
				sourceRouter = originalSourceRouter;
				reintialiseValues();
				algorithm.calculateShortestPath(graph);
				sourceRouter=originalSourceRouter;
			}
			if (sourceRouter == null && destinationRouter ==null){
				algorithm.connectionTable(modifiedGraph,routerToRemove);
			}
			//modifiedGraph = null;
		}
	}
	
	/**
	 * Exists from the program.
	 * 
	 * @param userCommand
	 */
	private static void processInputFive(Scanner userCommand) {
		printGoodBye();
		userCommand.close();
		isProcess = false;
		System.exit(0);
	}

	private void calculateShortestPath(Graph graph) {
		int graphSize = graph.getGraphSize();
		// HashMap<Node, Edge[]> shortestPathMap = graph.getNodes();
		for (; graphSize > 1; graphSize--) {
			if (sourceRouter != null) {
				sourceRouter = updateRouteValues(sourceRouter, true, graph);
			} else {
				break;
			}
		}
	}

	private String updateRouteValues(String sourceRouter, boolean print,
			Graph graph) {
		boolean isFirst = true;
		boolean isFirstIterate = true;
		String node = null;
		Edge[] edge = null;
		Edge shortestEdge = new Edge();

		if (isFirstTimeLoading) {
			node = new String(sourceRouter);
			edge = graph.nodes.get(sourceRouter);
			shortestPathMap = new HashMap<String, Edge[]>();
			shortestPathMap.put(node, edge);
		}

		Iterator shortestPathMapIterator = shortestPathMap.entrySet()
				.iterator();
		Map.Entry pair = (Map.Entry) shortestPathMapIterator.next();
		node = (String) pair.getKey();
		edge = (Edge[]) pair.getValue();

		HashMap temp = graph.getNodes();
		Edge[] tempEdges = (Edge[]) temp.get(sourceRouter);
		if (tempEdges == null) {
			System.out.println("The given router is not available");
			return null;
		}
		for (int i = 1; i < tempEdges.length; i++) {
			if (edge[i].getWeight() < 0 && tempEdges[i].getWeight() > 0) {
				edge[i].setWeight(tempEdges[i].getWeight() + minimumDistance);
				edge[i].setFromNode(sourceRouter);

			}
			else {
				if (tempEdges[i].getWeight() + minimumDistance < edge[i]
						.getWeight() && tempEdges[i].getWeight() > 0) {
					edge[i].setWeight(tempEdges[i].getWeight()
							+ minimumDistance);
					edge[i].setFromNode(sourceRouter);
				}
			}
		}

		shortestPathMap.replace(node, edge);

		for (Edge edge2 : edge) {
			if (isFirstIterate) {
				isFirstIterate = false;
				continue;
			}
			int weight = edge2.getWeight();
			String toNode = edge2.getToNode();
			String fromNode = edge2.getFromNode();
			boolean isAlreadyVisited = false;
			if (!visitedNodes.isEmpty()) {
				for (String singleNode : visitedNodes) {
					if (singleNode.equalsIgnoreCase(toNode)) {
						isAlreadyVisited = true;
						break;
					}
				}
			}

			if (!isAlreadyVisited) {
				if (weight > 0) {
					if (isFirst) {
						shortestEdge.setWeight(weight);
						shortestEdge.setToNode(toNode);
						shortestEdge.setFromNode(fromNode);
						isFirst = false;
					}
					else if (weight < shortestEdge.getWeight()) {
						shortestEdge.setWeight(weight);
						shortestEdge.setToNode(toNode);
						shortestEdge.setFromNode(fromNode);
					}
				}
			}
		}
		minimumDistance = shortestEdge.getWeight();
		if(shortestEdge.getFromNode() != null){
			printShortestPath(shortestEdge.getFromNode(), shortestEdge.getToNode(),shortestEdge.getWeight(), print);
		}
		visitedNodes.add(shortestEdge.getToNode());
		isFirstTimeLoading = false;
		return shortestEdge.getToNode();
	}

	private void printShortestPath(String fromNode, String toNode, int weight,
			boolean print) {
		if (fromNode.equals(originalSourceRouter)) {
			directNodes.add(toNode);
			if (toNode.equalsIgnoreCase(destinationRouter) && print) {
				System.out.println("The shortest path from router "
						+ fromNode + " to "
						+ toNode + " is "
						+ fromNode + " - "
						+ toNode + " the total cost is"
						+ weight);
			}
		} else {
			indirectNodes.add(fromNode);
			StringBuffer visitedNod = new StringBuffer();
			if (!indirectNodes.isEmpty()) {
				for (String singleNode : indirectNodes) {
					visitedNod.append(singleNode);
					visitedNod.append(" - ");
				}
			}
			if (toNode.equalsIgnoreCase(destinationRouter) && print) {
				System.out.println("The shortest path from router "
						+ originalSourceRouter + " to " + toNode
						+ " is " + originalSourceRouter + " - "
						+ visitedNod.toString() + toNode
						+ " the total cost is" + weight);
			}

		}

	}

	private static void reintialiseValues() {
		isFirstTimeLoading = true;
		minimumDistance = 0;
		visitedNodes = new HashSet<String>();
		indirectNodes = new HashSet<String>();
		directNodes = new HashSet<String>();

	}

	/**
	 * Prepares the connection table for given router.
	 * 
	 * @param graph
	 */
	private void connectionTable(Graph graph,String sourceRouter) {
		String node = new String(sourceRouter);
		Edge[] edge = graph.nodes.get(sourceRouter);
		Edge shortestEdge = new Edge();
		HashMap<String, Edge[]> shortestPathMap = new HashMap<String, Edge[]>();
		shortestPathMap.put(node, edge);

		Iterator shortestPathMapIterator = shortestPathMap.entrySet()
				.iterator();
		Map.Entry pair = (Map.Entry) shortestPathMapIterator.next();
		node = (String) pair.getKey();
		edge = (Edge[]) pair.getValue();
		HashMap temp = graph.getNodes();
		Edge[] tempEdges = (Edge[]) temp.get(sourceRouter);
		if (tempEdges != null) {
			printConnectionTable(sourceRouter);
			for (int i = 1; i < tempEdges.length; i++) {
				if (routerToRemove != null && tempEdges[i].toNode.equals(routerToRemove)) {
					continue;
				}
				System.out.print(tempEdges[i].toNode);
				if (edge[i].getWeight() == 0) {
					System.out.println("               " + "-");
				}else {
					System.out.println("               "+ getFirstHop(sourceRouter, tempEdges[i].toNode,graph));
				}
			}
		}
		else {
			//sourceRouter = null;
			System.out.println("The given router is not available");
		}

	}

	/**
	 * Retrieves the first node for the connection table.
	 * 
	 * @param toNodeFirst
	 * @param destNode
	 * @param graph
	 * @return
	 */
	private String getFirstHop(String toNodeFirst, String destNode, Graph graph) {
		String node = null;
		reintialiseValues();
		originalSourceRouter = toNodeFirst;
		int graphSize = graph.getGraphSize();
		for (; graphSize > 1; graphSize--) {
			toNodeFirst = updateRouteValues(toNodeFirst, false, graph);
			if (toNodeFirst.equals(destNode)) {
				break;
			}
		}
		if (!directNodes.isEmpty()) {
			for (String singleNode : directNodes) {
				if (singleNode.equalsIgnoreCase(destNode)) {
					node = singleNode;
				}
			}
		}
		if (!indirectNodes.isEmpty() && node == null) {
			for (String singleNode : indirectNodes) {
				node = singleNode;
				break;
			}
		}
		return node == null ? toNodeFirst : node;
	}
	
	/**
	 * Populates the graphs based on the given input file.
	 * 
	 * @param file
	 * @param displayGraph
	 */
	private void loadFile(String file, boolean displayGraph) {
		this.graph = new Graph();
		File fileHandler = new File(file);
		int i = 1, j = 1;
		try {
			Scanner fileScanner = new Scanner(fileHandler);
			try{
				while (fileScanner.hasNext()) {
			
				String node = new String("R" + i);
				String line = fileScanner.nextLine();
				/*if (displayGraph) {
					System.out.println(line);
				}*/
				String[] tokens = line.split("[ ]");
				Edge edges[] = new Edge[tokens.length + 1];
				j = 1;
				for (String string : tokens) {
					edges[j] = new Edge("R" + i, "R" + j,
							Integer.parseInt(string));
					j++;
				
				}
				if (displayGraph) {
					System.out.println(line);
				}
				i++;
				graph.addNode(node, edges);
				
			}
			fileScanner.close();
			isFileLoaded = true;
			}catch (Exception e){
				System.out.println("Unable to read the file. Please enter a proper input file");
			}
		} catch (FileNotFoundException e) {
			System.out.println("Unable to load Input file");
		}

	}

	/**
	 * Populates the graph with the new nodes for option 4.
	 */
	private void modifyGraph() {
		modifiedGraph = new Graph();
		HashMap<String, Edge[]> nodes = graph.getNodes();
		String node = null;
		Edge[] edge = null;

		Iterator nodesIterator = nodes.entrySet()
				.iterator();
		while (nodesIterator.hasNext()) {

			Map.Entry pair = (Map.Entry) nodesIterator.next();
			node = (String) pair.getKey();
			edge = (Edge[]) pair.getValue();
			/*System.out.println(node + ":");*/
			for (int i = 1; i < edge.length; i++) {
				if (edge[i].toNode.equals(routerToRemove)
						|| node.equals(routerToRemove)) {
					edge[i].setWeight(0);

				}
				/*System.out.println(edge[i].getFromNode() + " "
						+ edge[i].getToNode() + " " + edge[i].getWeight());*/
			}
			modifiedGraph.addNode(node, edge);
		}

	}

}
