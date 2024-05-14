package hw4;

import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
public class GraphWrapperTest {
    @Test
    public void testConstructor() {
        GraphWrapper graphWrapper1 = new GraphWrapper();
        assertFalse(graphWrapper1.listNodes().hasNext()); //Node list has no more elements
        assertEquals(0, graphWrapper1.graph.getEdgeCollection().size()); //Graph has 0 nodes
    }
    @Test
    public void testAddNode() {
        GraphWrapper graphWrapper1 = new GraphWrapper();
        graphWrapper1.addNode("node1");
        assertTrue(graphWrapper1.listNodes().hasNext()); //Node list has a future element for iterator
        assertEquals(1, graphWrapper1.graph.getEdgeCollection().size()); //Node list is size 1
        assertEquals("node1", graphWrapper1.listNodes().next()); //Next element in Node list is node1
    }
    @Test
    public void testAddEdge() {
        GraphWrapper graphWrapper1 = new GraphWrapper();
        graphWrapper1.addNode("node1");
        graphWrapper1.addNode("node2");
        graphWrapper1.addEdge("node1", "node2", "8");
        assertEquals(2, graphWrapper1.graph.getEdgeCollection().size()); //Edge list is size 2 because there are 2 nodes
    }
    @Test
    public void testListNodes() {
        GraphWrapper graphWrapper1 = new GraphWrapper();
        graphWrapper1.addNode("node2");
        assertTrue(graphWrapper1.listNodes().hasNext()); //Node list has a future element for iterator
        assertEquals("node2", graphWrapper1.listNodes().next()); //Next element in Node list is node2
        assertEquals(1, graphWrapper1.graph.getEdgeCollection().size()); //Node list is size 1
        graphWrapper1.addNode("node1");
        assertEquals(2, graphWrapper1.graph.getEdgeCollection().size()); //Node list is size 2
        assertEquals("node1", graphWrapper1.listNodes().next()); //Next element in Node list is node1 meaning nodes are in correct order
    }
    @Test
    public void testGetChildren() {
        GraphWrapper graphWrapper1 = new GraphWrapper();
        //Adding nodes and edges
        graphWrapper1.addNode("node4");
        graphWrapper1.addNode("node3");
        graphWrapper1.addNode("node2");
        graphWrapper1.addNode("node1");
        graphWrapper1.addEdge("node1", "node2", "5");
        graphWrapper1.addEdge("node1", "node3", "20");
        graphWrapper1.addEdge("node1", "node4", "10");
        graphWrapper1.addEdge("node2", "node1", "15");
        graphWrapper1.addEdge("node1", "node2", "5");
        graphWrapper1.addEdge("node1", "node2", "6");
        Iterator<String> itr1 = graphWrapper1.listChildren("node1");
        assertTrue(itr1.hasNext()); //Ensures there are children
        //Checks children are in order AND there is no duplicate child
        assertEquals("node2(15)", itr1.next());
        assertEquals("node2(5)", itr1.next());
        assertEquals("node2(6)", itr1.next());
        assertEquals("node3(20)", itr1.next());
        assertEquals("node4(10)", itr1.next());
        assertFalse(itr1.hasNext()); //Ensures we have seen every child
        Iterator<String> itr2 = graphWrapper1.listChildren("node8");
        assertFalse(itr2.hasNext()); //Ensures nodes with no children do not have false children
    }
}
