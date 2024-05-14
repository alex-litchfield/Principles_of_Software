package hw5;

import hw4.GraphWrapper;

import java.io.IOException;
import java.util.*;
import java.util.Set;

public class ProfessorPaths {
	//Abstraction Function: The profGraph is represented by an instance of graphWrapper
	//This is a hashMap which contains parentNodes as keys and another hashMap as the value
	//This inner hashMap's keys are the childNodes and a hashSet of edges as the value
    public GraphWrapper profGraph;
    public Map<String, Set<String>> profsTeaching;
	public Set<String> profs;

	public ProfessorPaths() {
		profGraph = new GraphWrapper();
		profsTeaching = new HashMap<String, Set<String>>();
		profs = new HashSet<String>();
	}

	private void checkRep() {
		//The representation invariant is that the graph and number set of profs cannot be null
		if (profGraph == null) {
			throw new RuntimeException("Rep check failed because graph is null");
		}
		else if (profs == null) {
			throw new RuntimeException("Rep check failed because profs is null");
		}
	}

    public void createNewGraph(String filename) {
		//Clears any data upon repeat calls of createNewGraph()
		profsTeaching.clear();
		profs.clear();
		try {
			ProfessorParser.readData(filename, profsTeaching, profs);
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (String prof: profs) {
			profGraph.addNode(prof);
		}
		for (Map.Entry<String, Set<String>> entry: profsTeaching.entrySet()) {
			for (String prof1: entry.getValue()) {
				for (String prof2: entry.getValue()) {
					if (!prof1.equals(prof2)) {
						profGraph.addEdge(prof1, prof2, entry.getKey());
					}
				}
			}
		}
        //checkRep();
		//IN THE MAP THE KEY IS THE COURSE CODE AND THE VALUE IS THE LIST OF PROFS WHO TAUGHT THAT COURSE
    }

    public String findPath(String node1, String node2) {
        //node 1 is starting node
		//node 2 is ending node
		String output = "";
		if (!profs.contains(node1)) {
			if (!profs.contains(node2) && !node1.equals(node2)) {
				output = "unknown professor " + node1 + "\nunknown professor " + node2 + "\n";
			}
			else {
				output = "unknown professor " + node1 + "\n";
			}
		}
		else if (!profs.contains(node2)) {
			output = "unknown professor " + node2 + "\n";
		}
		else {
			Queue<String> queue = new LinkedList<>(); //queue or "worklist", of nodes to visit: initially empty
			queue.add(node1); //Add start to Q
			//Map from nodes to paths; initially empty
			//Each key in M is a visited node.
			//Each value is a path from start to that node
			//A path is a list is a list of nodes
			Map<String, String> pathsMap = new HashMap<String, String>();
            pathsMap.put(node1, "path from " + node1 + " to " + node2 + ":\n");
			while (!queue.isEmpty()) {
				String destroyedNode = queue.remove();
				if (destroyedNode.equals(node2)) {
					output = pathsMap.get(destroyedNode);
					return output;
				}
				Iterator<String> itr1 = profGraph.listChildren(destroyedNode);
				while (itr1.hasNext()) {
					String next_itr = itr1.next();
					String label = next_itr .substring(next_itr .indexOf("(") + 1, next_itr .indexOf(")"));
					if (!pathsMap.containsKey(next_itr .substring(0, next_itr .indexOf("(")))) {
						String newEdge = pathsMap.get(destroyedNode) + destroyedNode + " to " + next_itr .substring(0, next_itr .indexOf("(")) + " via " + label + "\n";
						pathsMap.put(next_itr.substring(0, next_itr .indexOf("(")), newEdge);
						queue.add(next_itr .substring(0, next_itr .indexOf("(")));
					}
				}
			}
	        output = "path from " + node1 + " to " + node2 + ":\nno path found\n";
        }
		return output;
    }
}