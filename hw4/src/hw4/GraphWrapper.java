package hw4;

import java.util.*;

public class GraphWrapper {
    public Graph<String, String> graph;
    public GraphWrapper() {
        this.graph = new Graph<String, String>();
    }

    public void addNode(String nodeData) {
        Node<String> newNode = new Node<String>(nodeData);
        this.graph.addNode(newNode);
    }

    public void addEdge(String parentNodeName, String childNodeName, String edgeLabel) {
        Node<String> parentNode = this.graph.findNode(parentNodeName);
        Node<String> childNode = this.graph.findNode(childNodeName);
        this.graph.addEdge(parentNode, childNode, edgeLabel);
    }

    public Iterator<String> listNodes() {
        return this.graph.listNodes();
    }

    public Iterator<String> listChildren(String parentNodeName) {
        Node<String> parentNode = this.graph.findNode(parentNodeName);
        Iterator<Node<String>> itr = this.graph.listChildren(parentNode);
        LinkedList<String> childrenLL = new LinkedList<>();
        while (itr.hasNext()) {
            Node<String> currNode = itr.next();
            HashSet<String> labels = this.graph.getEdgeCollection().get(parentNode).get(currNode);
            for (String label : labels) {
                childrenLL.add(currNode.getName()+"("+label+")");
            }
        }
        Collections.sort(childrenLL);
        return childrenLL.iterator();
    }
}
