package hw4;

import java.util.*;

public class Path <T>{
    private double totalWeight;
    private Node<T> destNode;
    private LinkedList<Node<T>> nodes;

    public Path(Node<T> startNode) {
        this.totalWeight = 0.0;
        this.destNode = startNode;
        this.nodes = new LinkedList<Node<T>>();
        nodes.add(startNode);
        //this.checkRep();
    }

    public Path(Node<T> newDestNode, Double newWeight, Path<T> oldPath) {
        this.totalWeight = oldPath.getTotalWeight() + newWeight;
        this.destNode = newDestNode;
        this.nodes = new LinkedList<Node<T>>();
        Iterator<Node<T>> itr = oldPath.getNodes();
        while (itr.hasNext()) {
            Node<T> node = itr.next();
            this.nodes.add(node);
        }
        this.nodes.add(newDestNode);
        //this.checkRep();
    }

    private void checkRep() {
        HashSet<Node<T>> nodes = new HashSet<Node<T>>();
        Iterator<Node<T>> itr = this.getNodes();
        while (itr.hasNext()) {
            Node<T> node = itr.next();
            if (!nodes.add(node)) {
                throw new RuntimeException("Failed rep check: duplicate node present");
            }
        }
    }

    public Node<T> getDestNode() {
        return this.destNode;
    }

    public double getTotalWeight() {
        return this.totalWeight;
    }

    public Iterator<Node<T>> getNodes() {
        return this.nodes.iterator();
    }
}
