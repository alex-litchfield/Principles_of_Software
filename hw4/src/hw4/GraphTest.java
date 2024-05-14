package hw4;

import org.junit.Test;

import java.util.HashSet;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GraphTest {
    @Test
    public void testConstructor() {
        Graph<String, String> graph1 = new Graph<String, String>();
        assertFalse(graph1.listNodes().hasNext()); //Node list has no more elements
        assertEquals(0, graph1.getEdgeCollection().size()); //Graph has 0 nodes
    }
    @Test
    public void testAddNode() {
        Graph<String, String> graph1 = new Graph<String, String>();
        Node<String> node1 = new Node<String>("node1");
        graph1.addNode(node1);
        assertTrue(graph1.listNodes().hasNext()); //Node list has a future element for iterator
        assertEquals(1, graph1.getEdgeCollection().size()); //Node list is size 1
        assertEquals(node1.getName(), graph1.listNodes().next()); //Next element in Node list is node1
    }

    @Test
    public void testAddEdge() {
        Graph<String, String> graph1 = new Graph<String, String>();
        //Adds edge to node1 with node 2 - no previous edges
        Node<String> node1 = new Node<String>("node1");
        graph1.addNode(node1);
        Node<String> node2 = new Node<String>("node2");
        graph1.addNode(node2);
        graph1.addEdge(node1, node2, "1");
        //Adds new edge to node 1 with child of node2
        graph1.addEdge(node1, node2, "2");
        assertEquals(2, graph1.getEdgeCollection().size()); //node 1 and node 2 should be keys
        //NODE 1 TESTING
        assertEquals(1, graph1.getEdgeCollection().get(node1).size()); //node1 should only have 1 child
        HashSet<Node<String>> children = new HashSet<Node<String>>();
        children.add(node2);
        assertEquals(children, graph1.getEdgeCollection().get(node1).keySet()); //only child of node1 is node2
        HashSet<String> labels = new HashSet<String>();
        labels.add("1");
        labels.add("2");
        assertEquals(labels, graph1.getEdgeCollection().get(node1).get(node2)); //edges between the 2 nodes are "1" and "2"
        //NODE 2 TESTING
        assertEquals(1, graph1.getEdgeCollection().get(node2).size()); //node2 should only have 1 child
        children.clear();
        children.add(node1);
        assertEquals(children, graph1.getEdgeCollection().get(node2).keySet()); //only child of node2 is node1
        assertEquals(labels, graph1.getEdgeCollection().get(node2).get(node1)); //edges between the 2 nodes are "1" and "2"

        //System.out.println(graph1.getEdgeCollection().get(node1));
    }

    @Test
    public void testListNodes() {
        Graph<String, String> graph1 = new Graph<String, String>();
        Node<String> node2 = new Node<String>("node2");
        graph1.addNode(node2);
        assertTrue(graph1.listNodes().hasNext()); //Node list has a future element for iterator
        assertEquals(node2.getName(), graph1.listNodes().next()); //Next element in Node list is node2
        Node<String> node1 = new Node<String>("node1");
        graph1.addNode(node1);
        assertEquals(node1.getName(), graph1.listNodes().next()); //Next element in Node list is node1 meaning nodes are in correct order
    }

    @Test
    public void testListChildren() {
        //Creating graph, nodes, and edges
        Graph<String, String> graph1 = new Graph<String, String>();
        //Adding nodes and edges
        Node<String> node5 = new Node<String>("node5");
        Node<String> node4 = new Node<String>("node4");
        Node<String> node3 = new Node<String>("node3");
        Node<String> node2 = new Node<String>("node2");
        Node<String> node1 = new Node<String>("node1");
        graph1.addNode(node4);
        graph1.addNode(node3);
        graph1.addNode(node2);
        graph1.addNode(node1);
        graph1.addEdge(node1, node2, "5");
        graph1.addEdge(node1, node3, "20");
        graph1.addEdge(node1, node4, "10");
        graph1.addEdge(node2, node1, "15");
        graph1.addEdge(node1, node2, "5");
        graph1.addEdge(node1, node2, "7");
        assertEquals("node4", node4.getName()); //Ensure node equals itself
        Iterator<Node<String>> itr1 = graph1.listChildren(node1);
        assertTrue(itr1.hasNext()); //Ensures there are children
        //assertEquals(node2, itr1.next());
        System.out.println(itr1.next().getName());
        System.out.println(itr1.next().getName());
        System.out.println(itr1.next().getName());

        //assertEquals(node3, itr1.next());
        //assertEquals(node4, itr1.next());
        assertFalse(itr1.hasNext()); //Ensures we have seen every child
        Iterator<Node<String>> itr2 = graph1.listChildren(node5);
        assertFalse(itr2.hasNext()); //Ensures nodes with no children do not have false children
    }
}
