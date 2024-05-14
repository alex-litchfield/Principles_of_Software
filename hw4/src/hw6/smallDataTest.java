package hw6;

import hw4.Node;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.util.*;

public class smallDataTest {
    @Test
    public void testConstructors() {
        LegoPaths legoPath = new LegoPaths();
        assert legoPath.legoGraph != null;
        assert legoPath.legoMap != null;
        assert legoPath.legoPieces != null;
        assertEquals(legoPath.legoMap.size(), 0);
        assertEquals(legoPath.legoPieces.size(), 0);
    }

    @Test
    public void testCreateNewGraph() {
        LegoPaths legoPath = new LegoPaths();
        //remove ../../ when submitting
        //Checks that professorParser brings in data correctly
        legoPath.createNewGraph("data/smallTestDataSet.csv");
        Set<String> legoSetPieces = legoPath.legoMap.get("ASL-1236");
        assertEquals(legoSetPieces.size(), 2);
        assertTrue(legoSetPieces.contains("Jack Cooper"));
        assertTrue(legoSetPieces.contains("Jack Anderson"));

        //checks that edgeCollection stores data correctly and can access children
        HashMap<Node<String>, HashMap<Node<String>, HashSet<Double>>> edgeCollection = legoPath.legoGraph.getEdgeCollection();
        Node<String> node1 = legoPath.legoGraph.findNode("Jack Bell");
        Node<String> node2 = legoPath.legoGraph.findNode("Logan Garcia");
        Node<String> node3 = legoPath.legoGraph.findNode("Jack Clark");
        Node<String> node4 = legoPath.legoGraph.findNode("Superman Duperman Set83");
        assertTrue(edgeCollection.containsKey(node1));
        assertTrue(edgeCollection.containsKey(node2));
        assertTrue(edgeCollection.containsKey(node3));
        Iterator<Node<String>> itr1 = legoPath.legoGraph.listChildren(node1);
        LinkedList<String> childrenLL = new LinkedList<>();
        assertTrue(itr1.hasNext()); //Ensures there are children
        while (itr1.hasNext()) {
            Node<String> currChild = itr1.next();
            HashSet<Double> labels = legoPath.legoGraph.getEdgeCollection().get(node1).get(currChild);
            for (Double label : labels) {
                childrenLL.add(currChild.getName()+"("+label+")");
            }
        }
        Collections.sort(childrenLL);
        Iterator<String> itr2 = childrenLL.iterator();
        assertTrue(itr2.hasNext()); //Ensures there are children with edges
        assertEquals("Logan Garcia(1.0)", itr2.next());
        assertFalse(itr2.hasNext()); //Ensures we have seen every child with an edge
        itr1 = legoPath.legoGraph.listChildren(node4);
        assertFalse(itr1.hasNext()); //No false children for non-existent nodes*/
    }

    @Test
    public void testFindPath() {
        LegoPaths legoPath = new LegoPaths();
        //remove ../../ when submitting
        legoPath.createNewGraph("data/smallTestDataSet.csv");
        //A path is found. The lexicographically (alphabetically) least path is returned.
        String expected = "path from Jack Bell to Jack Clark:\n"+
                "Jack Bell to Logan Garcia with weight 1.000\n"+
                "Logan Garcia to Logan Murphy with weight 1.000\n"+
                "Logan Murphy to Jack Bennett with weight 1.000\n"+
                "Jack Bennett to Jack Clark with weight 1.000\n"+
                "total cost: 4.000\n";
        assertEquals(legoPath.findPath("Jack Bell", "Jack Clark"), expected);
        //No path exists.
        expected = "path from Jack Bell to Rowan Sunderland:\n" +
                "no path found\n";
        assertEquals(legoPath.findPath("Jack Bell", "Rowan Sunderland"), expected);
        //Piece not found.
        expected = "unknown part UnknownPiece\n";
        assertEquals(legoPath.findPath("UnknownPiece", "Jack Clark"), expected);
        //Both pieces not found.
        expected = "unknown part UnknownPiece1\nunknown part UnknownPiece2\n";
        assertEquals(legoPath.findPath("UnknownPiece1", "UnknownPiece2"), expected);
        //A path to the piece itself.
        expected = "path from Jack Bell to Jack Bell:\ntotal cost: 0.000\n";
        assertEquals(legoPath.findPath("Jack Bell", "Jack Bell"), expected);
        //A path to themselves for an unknown piece.
        expected = "unknown part UnknownPiece1\n";
        assertEquals(legoPath.findPath("UnknownPiece1", "UnknownPiece1"), expected);
    }
}
