package hw6;
import hw4.Node;
import hw4.Graph;
import hw5.ProfessorParser;

import java.io.IOException;
import java.util.*;

public class LegoPaths {
	//Abstraction Function: The legoGraph is represented by an instance of graph, which is
	//a hashmap of parent nodes that point to child node that point to a hashset of edges (a single weight value in this case)
    public Graph<String, Double> legoGraph;
	public Map<String, Set<String>> legoMap; //IN THE MAP THE KEY IS THE lego set AND THE VALUE IS THE LIST OF lego pieces in the set
	public Set<String> legoPieces;
	public HashMap<Node<String>, HashMap<Node<String>, HashSet<String>>> edgeStrings; //In this map, the key is the parent node and the map inside's key is the child node and the value is a hashset of edges/lego sets


	public LegoPaths() {
		legoGraph = new Graph<String, Double>();
		legoMap = new HashMap<String, Set<String>>();
		legoPieces = new HashSet<String>();
		edgeStrings = new HashMap<Node<String>, HashMap<Node<String>, HashSet<String>>>();
	}

	private void checkRep() {
		//The representation invariant is that the graph and number set of legoPieces cannot be null
		if (legoGraph == null) {
			throw new RuntimeException("Rep check failed because graph is null");
		}
		else if (legoPieces == null) {
			throw new RuntimeException("Rep check failed because legoPieces is null");
		}
	}

    public void createNewGraph(String filename) {
		//copies code from createNewGraph() of professorPaths
		//Clears any data upon repeat calls of createNewGraph()
		legoMap.clear();
		legoPieces.clear();

		//Adds data into the legoGraph using professorParser
		try {
			ProfessorParser.readData(filename, legoMap, legoPieces);
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (String legoPiece: legoPieces) {
			Node<String> node1 = new Node<String>(legoPiece);
			legoGraph.addNode(node1);
			edgeStrings.put(node1, new HashMap<Node<String>, HashSet<String>>());
		}
		//Filling our data structure that finds the unique edges between each 2 pieces
		for (Map.Entry<String, Set<String>> entry: legoMap.entrySet()) {
			for (String legoPiece1: entry.getValue()) {
				for (String legoPiece2: entry.getValue()) {
					if (!legoPiece1.equals(legoPiece2)) {
						Node<String> node1 = legoGraph.findNode(legoPiece1);
						Node<String> node2 = legoGraph.findNode(legoPiece2);
						//All this under the parent node's information
						HashMap<Node<String>, HashSet<String>> childrenData = edgeStrings.get(node1);
						if (childrenData == null) {
							childrenData = new HashMap<Node<String>, HashSet<String>>();
						}
						HashSet<String> labels = new HashSet<>();
						labels.add(entry.getKey());
						if (childrenData.get(node2) != null) { //there IS an existing edge between the parent and child
							labels.addAll(childrenData.get(node2));
						}
						childrenData.put(node2, labels);
						//Under the child node's information
						childrenData = edgeStrings.get(node2);
						if (childrenData == null) {
							childrenData = new HashMap<Node<String>, HashSet<String>>();
						}
						labels.clear();
						labels.add(entry.getKey());
						if (childrenData.get(node1) != null) { //there IS an existing edge between the parent and child
							labels.addAll(childrenData.get(node1));
						}
						childrenData.put(node1, labels);
					}
				}
			}
		}
		//Populates our graph with the weights based on the previous data structure
		for (Map.Entry<Node<String>, HashMap<Node<String>, HashSet<String>>> entry : edgeStrings.entrySet()) {
			Node<String> parentNode = entry.getKey();
			for (Map.Entry<Node<String>, HashSet<String>> innerEntry : entry.getValue().entrySet()) {
				Node<String> childNode = innerEntry.getKey();
				double weight = ((double) 1 /edgeStrings.get(parentNode).get(childNode).size());
				legoGraph.addEdge(parentNode, childNode, weight);
			}
		}
        //checkRep();
    }

	//Finds the path between 2 nodes and returns it as a string
	//Parameters are the names of the 2 nodes
	public String findPath(String PART1, String PART2) {
		return legoGraph.findPath(PART1, PART2);
	}
}