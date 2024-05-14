package hw4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public final class NodeTest {
    @Test
    public void testConstructors() {
		//Create nodes using name and vertex constructors
    	Node<String> wordNode = new Node<String>("bobby");
		//Assertions for each constructor
    	assertEquals("bobby", wordNode.getName());
    }
    @Test
    public void testEquals() {
        //Create nodes using name and vertex constructors
    	Node<String> wordNode1 = new Node<String>("bobby");
		Node<String> wordNode2 = new Node<String>("bobby");
        //Assertions comparing each name node to each other
        assertTrue(wordNode1.equals(wordNode2));
    }

}