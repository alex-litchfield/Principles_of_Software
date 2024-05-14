package hw4;


import java.util.*;

public class Graph<T extends Comparable<T>, U> {
    private HashMap<Node<T>, HashMap<Node<T>, HashSet<U>>> edgeCollection;
    private HashMap<T, Node<T>> nameToNodeMap;
    //Abstraction Function: The graph is represented by a hashMap which contains
    //parentNodes as keys and another hashMap as the value
    //This inner hashMap's keys are the childNodes and a hashSet of edges as the value
    //No edges can be null
    public Graph() {
        this.edgeCollection = new HashMap<Node<T>, HashMap<Node<T>, HashSet<U>>>();
        this.nameToNodeMap = new HashMap<T, Node<T>>();
        //this.checkRep();
    }
    private void checkRep() {
        HashSet<Node<T>> nodes = new HashSet<Node<T>>();
        HashSet<U> labelsForNode = new HashSet<U>();
        for (Map.Entry<Node<T>, HashMap<Node<T>, HashSet<U>>> entry: this.edgeCollection.entrySet()) {
            if (!nodes.add(entry.getKey())) {
                throw new RuntimeException("Failed rep check: duplicate node present");
            }

            for (Map.Entry<Node<T>, HashSet<U>> innerEntry : entry.getValue().entrySet()) {
                for (U label: innerEntry.getValue()) {
                    if(!labelsForNode.add(label)) {
                        throw new RuntimeException("Failed rep check: duplicate label present");
                    }
                }
                labelsForNode.clear();
            }
        }
    }

    //Adds a node to the graph (so edgeCollection and nameToNodeMap)
    public void addNode(Node<T> currNode) {
        if (this.edgeCollection != null) {
            this.edgeCollection.put(currNode, new HashMap<Node<T>, HashSet<U>>());
            this.nameToNodeMap.put(currNode.getName(), currNode);
        }
        //checkRep();
    }

    //Adds an edge to the map (so to edgeCollection)
    public void addEdge(Node<T> parentNode, Node<T> childNode, U label) {
        if (this.edgeCollection != null) {
            //All this under the parent node's information
            HashMap<Node<T>, HashSet<U>> childrenData = this.edgeCollection.get(parentNode);
            if (childrenData == null) {
                childrenData = new HashMap<Node<T>, HashSet<U>>();
            }
            HashSet<U> labels = new HashSet<>();
            labels.add(label);
            if (childrenData.get(childNode) != null) { //there IS an existing edge between the parent and child
                labels.addAll(childrenData.get(childNode));
            }
            childrenData.put(childNode, labels);
            //Under the child node's information
            childrenData = this.edgeCollection.get(childNode);
            if (childrenData == null) {
                childrenData = new HashMap<Node<T>, HashSet<U>>();
            }
            labels.clear();
            labels.add(label);
            if (childrenData.get(parentNode) != null) { //there IS an existing edge between the parent and child
                labels.addAll(childrenData.get(parentNode));
            }
            childrenData.put(parentNode, labels);
        }
    }

    //Returns an iterator to a linked list of all the node names
    public Iterator<T> listNodes() {
        LinkedList<T> nodeLL = new LinkedList<T>();
        nodeLL.addAll(this.nameToNodeMap.keySet());
        Collections.sort(nodeLL);
        return nodeLL.iterator();
    }

    //Returns an iterator to a linked list of all children/label combos for a parent node in the format childName(label)
    public Iterator<Node<T>> listChildren(Node<T> parent) {
        LinkedList<Node<T>> childrenLL = new LinkedList<Node<T>>();
        HashMap<Node<T>, HashSet<U>> childInfo = this.edgeCollection.get(parent);
        if (childInfo != null) {
            for (Map.Entry<Node<T>, HashSet<U>> entry : childInfo.entrySet()) {
                childrenLL.add(entry.getKey());
            }
        }
        //Sorts the child nodes alphabetically by name
        Comparator<Node<T>> nodeComparator = new Comparator<Node<T>>() {
            @Override
            public int compare(Node<T> node1, Node<T> node2) {
                return node1.getName().compareTo(node2.getName());
            }
        };
        childrenLL.sort(nodeComparator);
        return childrenLL.iterator();
    }

    public HashMap<Node<T>, HashMap<Node<T>, HashSet<U>>> getEdgeCollection() {
        return this.edgeCollection;
    }

    //returns null if it cannot find the node with a matching name
    public Node<T> findNode(T nodeName) {
        return this.nameToNodeMap.get(nodeName);
    }

    public class PathComparator implements Comparator<Path<T>> {
        @Override
        public int compare(Path<T> path1, Path<T> path2) {
            double weight1 = path1.getTotalWeight();
            double weight2 = path2.getTotalWeight();

            //Compares total weight
            int weightComparison = Double.compare(weight1, weight2);
            if (weightComparison != 0) {
                return weightComparison;
            }
            else { //If the weights are the same, we now compare by destination node names
                if (path1.getDestNode().getName() instanceof Location && path2.getDestNode().getName() instanceof Location) {
                    String destName1 = ((Location) path1.getDestNode().getName()).getBuildingName();
                    String destName2 = ((Location) path2.getDestNode().getName()).getBuildingName();
                    return destName1.compareTo(destName2);
                }
                else {
                    T destName1 = path1.getDestNode().getName();
                    T destName2 = path2.getDestNode().getName();
                    return destName1.compareTo(destName2);
                }
                //return destName1.compareTo(destName2);
            }
        }
    }

    //Finds the shortest path between 2 nodes (utilizes weights)
    //Parameters are 2 names for nodes of type T and a Comparator of Path<T>
    public String findPath(T PART1, T PART2) {
        //T PART1 = node1.getName();
        //T PART2 = node2.getName();
        StringBuilder output = new StringBuilder();
        if (PART1 instanceof String && PART2 instanceof String) {
            PathComparator comparator = new PathComparator();
            HashSet<Node<T>> finished = new HashSet<Node<T>>();
            HashMap<Node<T>, Path<T>> paths = new HashMap<Node<T>, Path<T>>();
            Node<T> start = this.findNode(PART1);
            Node<T> destination = this.findNode(PART2);
            PriorityQueue<Path<T>> active = new PriorityQueue<Path<T>>(comparator);
            if (!this.nameToNodeMap.containsKey(PART1)) {
                if (!this.nameToNodeMap.containsKey(PART2) && !PART1.equals(PART2)) {
                    output = new StringBuilder("unknown part " + PART1 + "\nunknown part " + PART2 + "\n");
                }
                else {
                    output = new StringBuilder("unknown part " + PART1 + "\n");
                }
            }
            else if (!this.nameToNodeMap.containsKey(PART2)) {
                output = new StringBuilder("unknown part " + PART2 + "\n");
            }
            else {
                //Starting Dijkstra's algorithm
                Path<T> currPath = new Path<T>(start);
                active.add(currPath);
                paths.put(start, currPath);
                while (!active.isEmpty()) {
                    Path<T> minPath = active.poll();
                    Node<T> minDest = minPath.getDestNode();
                    if (minDest.getName().equals(PART2)) {
                        Path<T> endPath = paths.get(minDest);
                        Iterator<Node<T>> itr = endPath.getNodes();
                        Node<T> prevNode = null;
                        while (itr.hasNext()) {
                            Node<T> currNode = itr.next();
                            if (prevNode != null) {
                                Iterator<U> itr2 = this.getEdgeCollection().get(prevNode).get(currNode).iterator();
                                U edgeLabel = (itr2.next());
                                StringBuilder stringBuilder = new StringBuilder(output);
                                stringBuilder.append(prevNode.getName())
                                        .append(" to ")
                                        .append(currNode.getName())
                                        .append(String.format(" with weight %.3f\n", (double) edgeLabel));
                                output = stringBuilder;
                            } else {
                                output = new StringBuilder("path from " + start.getName() + " to " + destination.getName() + ":\n");
                            }
                            prevNode = currNode;
                        }
                        output = new StringBuilder(output + String.format("total cost: %.3f\n", endPath.getTotalWeight()));
                        return output.toString();
                    }
                    if (finished.contains(minDest)) {
                        continue;
                    }
                    Iterator<Node<T>> itr1 = this.listChildren(minDest);
                    while (itr1.hasNext()) {
                        Node<T> childNode = itr1.next();
                        Iterator<U> itr2 = this.edgeCollection.get(minDest).get(childNode).iterator();
                        U edgeLabel = (itr2.next());
                        if (!finished.contains(childNode)) {
                            if (paths.get(childNode) == null || paths.get(childNode).getTotalWeight() > paths.get(minDest).getTotalWeight() + (double) edgeLabel) {
                                Path<T> newPath = new Path<T>(childNode, (double) edgeLabel, paths.get(minDest));
                                paths.put(childNode, newPath);
                                active.add(newPath);
                            }
                        }
                    }
                    finished.add(minDest);
                }
                output = new StringBuilder("path from " + PART1 + " to " + PART2 + ":\nno path found\n");
            }
        }
        else if (PART1 instanceof Location && PART2 instanceof Location) {
            PathComparator comparator = new PathComparator();
            HashSet<Node<T>> finished = new HashSet<Node<T>>();
            HashMap<Node<T>, Path<T>> paths = new HashMap<Node<T>, Path<T>>();
            Node<T> start = this.findNode(PART1);
            Node<T> destination = this.findNode(PART2);
            PriorityQueue<Path<T>> active = new PriorityQueue<Path<T>>(comparator);
            //Starting Dijkstra's algorithm
            Path<T> currPath = new Path<T>(start);
            active.add(currPath);
            paths.put(start, currPath);
            while (!active.isEmpty()) {
                Path<T> minPath = active.poll();
                Node<T> minDest = minPath.getDestNode();
                if (minDest.getName().equals(PART2)) {
                    Path<T> endPath = paths.get(minDest);
                    Iterator<Node<T>> itr = endPath.getNodes();
                    Node<T> prevNode = null;
                    while (itr.hasNext()) {
                        Node<T> currNode = itr.next();
                        if (prevNode != null) {
                            StringBuilder stringBuilder = new StringBuilder(output);
                            Location currLocation = (Location)currNode.getName();
                            Location prevLocation = (Location)prevNode.getName();
                            String currLocName = currLocation.getBuildingName();
                            if (currLocName.isEmpty()) {
                                currLocName = "Intersection " + currLocation.getId();
                            }
                            stringBuilder.append("\tWalk ")
                                    .append(prevLocation.getCardinalDirectionTo(currLocation))
                                    .append(" to (")
                                    .append(currLocName)
                                    .append(")\n");
                            output = stringBuilder;
                        } else {
                            Location startLoc = (Location)start.getName();
                            Location destLoc = (Location)destination.getName();
                            output = new StringBuilder("Path from " + startLoc.getBuildingName() + " to " + destLoc.getBuildingName() + ":\n");
                        }
                        prevNode = currNode;
                    }
                    output = new StringBuilder(output + String.format("Total distance: %.3f pixel units.\n", endPath.getTotalWeight()));
                    return output.toString();
                }
                if (finished.contains(minDest)) {
                    continue;
                }
                Iterator<Node<T>> itr1 = this.listChildren(minDest);
                while (itr1.hasNext()) {
                    Node<T> childNode = itr1.next();
                    Iterator<U> itr2 = this.edgeCollection.get(minDest).get(childNode).iterator();
                    U edgeLabel = (itr2.next());
                    if (!finished.contains(childNode)) {
                        if (paths.get(childNode) == null || paths.get(childNode).getTotalWeight() > paths.get(minDest).getTotalWeight() + (double) edgeLabel) {
                            Path<T> newPath = new Path<T>(childNode, (double) edgeLabel, paths.get(minDest));
                            paths.put(childNode, newPath);
                            active.add(newPath);
                        }
                    }
                }
                finished.add(minDest);
            }
            output = new StringBuilder("There is no path from " + ((Location) PART1).getBuildingName() + " to " + ((Location) PART2).getBuildingName() + ".\n");
        }
        return output.toString();
    }
}
