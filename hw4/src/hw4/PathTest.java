package hw4;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class PathTest {
    @Test
    public void testConstructors() {
        //Constructor 1
        Node<String> node1 = new Node<String>("node1");
        Path<String> legoPath = new Path<String>(node1);
        assertEquals(legoPath.getDestNode(), node1);
        assertEquals(legoPath.getTotalWeight(), 0.0, 0.001);
        Iterator<Node<String>> itr = legoPath.getNodes();
        assertTrue(itr.hasNext());
        assertEquals(itr.next(), node1);
        assertFalse(itr.hasNext());

        //Constructor 2
        Node<String> node2 = new Node<String>("node2");
        Path<String> legoPath2 = new Path<String>(node2, 1.0, legoPath);
        assertEquals(legoPath2.getDestNode(), node2);
        assertEquals(legoPath2.getTotalWeight(), 1.0, 0.001);
        itr = legoPath2.getNodes();
        assertTrue(itr.hasNext());
        assertEquals(itr.next(), node1);
        assertTrue(itr.hasNext());
        assertEquals(itr.next(), node2);
        assertFalse(itr.hasNext());
    }
}
