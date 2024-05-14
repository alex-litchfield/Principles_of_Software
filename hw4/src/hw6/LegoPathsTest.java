package hw6;

import hw4.Node;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public final class LegoPathsTest {
    @Test
    public void testConstructors() {
        LegoPaths legoPath = new LegoPaths();
        assert legoPath.legoGraph != null;
        assert legoPath.legoMap != null;
        assert legoPath.legoPieces!= null;
        assertEquals(legoPath.legoMap.size(), 0);
        assertEquals(legoPath.legoPieces.size(), 0);
    }

    @Test
    public void testCreateNewGraph() {
        LegoPaths legoPath = new LegoPaths();
        //remove ../../ when submitting
        //Checks that professorParser brings in data correctly
        legoPath.createNewGraph("data/lego1960.csv");
        Set<String> legoSetPieces = legoPath.legoMap.get("1310-1 v1 1956 ESSO Filling Station");
        assertEquals(legoSetPieces.size(), 30);
        assertTrue(legoSetPieces.contains("3005 Red Brick 1 x 1"));
        assertTrue(legoSetPieces.contains("3005 White Brick 1 x 1"));
        assertTrue(legoSetPieces.contains("3065 Red Brick 1 x 2 without Bottom Tube"));

        //checks that edgeCollection stores data correctly and can access children
        HashMap<Node<String>, HashMap<Node<String>, HashSet<Double>>> edgeCollection = legoPath.legoGraph.getEdgeCollection();
        Node<String> node1 = legoPath.legoGraph.findNode("3001c Red Brick 2 x 4 without Bottom Tubes");
        Node<String> node2 = legoPath.legoGraph.findNode("3001c Trans-Clear Brick 2 x 4 without Bottom Tubes");
        Node<String> node3 = legoPath.legoGraph.findNode("3001c White Brick 2 x 4 without Bottom Tubes");
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
        assertEquals("29ac01 Red Window 1 x 1 x 2 (old type) with Extended Lip and Solid Stud, with Fixed Glass(1.0)", itr2.next());
        assertEquals("3001c Trans-Clear Brick 2 x 4 without Bottom Tubes(1.0)", itr2.next());
        assertEquals("3001c White Brick 2 x 4 without Bottom Tubes(1.0)", itr2.next());
        assertEquals("3002b Red Brick 2 x 3 without Bottom Tubes(1.0)", itr2.next());
        assertEquals("3002b White Brick 2 x 3 without Bottom Tubes(1.0)", itr2.next());
        assertEquals("3003b Red Brick 2 x 2 without Inside Support or Tubes(1.0)", itr2.next());
        assertEquals("3003b White Brick 2 x 2 without Inside Support or Tubes(1.0)", itr2.next());
        assertEquals("3007a Red Brick 2 x 8 without Bottom Tubes, with Cross Supports(1.0)", itr2.next());
        assertEquals("3035a White Plate 4 x 8 with Waffle Bottom(1.0)", itr2.next());
        assertEquals("3065 Red Brick 1 x 2 without Bottom Tube(1.0)", itr2.next());
        assertEquals("3065 White Brick 1 x 2 without Bottom Tube(1.0)", itr2.next());
        assertEquals("3081ac01 Red Window 1 x 2 x 2 (old type) with Extended Lip and Solid Studs, with Glass(1.0)", itr2.next());
        assertEquals("31ac01 Red Window 1 x 3 x 2 Classic with Solid Studs [Complete](1.0)", itr2.next());
        assertEquals("33ac01 Red Door 1 x 2 x 3 Right, Solid Stud, Fixed Glass(1.0)", itr2.next());
        assertFalse(itr2.hasNext()); //Ensures we have seen every child with an edge
        itr1 = legoPath.legoGraph.listChildren(node4);
        assertFalse(itr1.hasNext()); //No false children for non-existent nodes
    }


    @Test
    public void testFindPath() {
        LegoPaths legoPath = new LegoPaths();
        //remove ../../ when submitting
        legoPath.createNewGraph("data/lego1960.csv");
        //Provided tests by submitty (these first big 2)
        String expected = "path from 3087ac01 Red Window 1 x 1 x 1 Classic with Solid Stud [Complete] to upn0039 Red Brick 2 x 14 without Bottom Tubes, 1 End Slot:\nno path found\n";
        assertEquals(legoPath.findPath("3087ac01 Red Window 1 x 1 x 1 Classic with Solid Stud [Complete]","upn0039 Red Brick 2 x 14 without Bottom Tubes, 1 End Slot"), expected);
        expected = "path from 3005 Trans-Clear Brick 1 x 1 to 3065b Green Brick 1 x 2 without Bottom Tube, Slotted (with 1 slot):\n" +
                "3005 Trans-Clear Brick 1 x 1 to 3005 White Brick 1 x 1 with weight 1.000\n" +
                "3005 White Brick 1 x 1 to 3065b White Brick 1 x 2 without Bottom Tube, Slotted (with 1 slot) with weight 0.333\n" +
                "3065b White Brick 1 x 2 without Bottom Tube, Slotted (with 1 slot) to 3065b Green Brick 1 x 2 without Bottom Tube, Slotted (with 1 slot) with weight 1.000\n" +
                "total cost: 2.333\n";
        assertEquals(legoPath.findPath("3005 Trans-Clear Brick 1 x 1", "3065b Green Brick 1 x 2 without Bottom Tube, Slotted (with 1 slot)"), expected);
        expected = "path from 604c Red Window 1 x 6 x 3 Panorama, without Glass for Slotted Bricks to 821a Red Garage Door Frame (Old style) - No Studs, Full Wings:\n" +
                "604c Red Window 1 x 6 x 3 Panorama, without Glass for Slotted Bricks to 645c Red Window 1 x 6 x 2 3-Pane, without Glass for Slotted Bricks with weight 1.000\n" +
                "645c Red Window 1 x 6 x 2 3-Pane, without Glass for Slotted Bricks to 820 White Baseplate Raised 8 x 18 with Garage Floor Plate (Old style) with weight 1.000\n" +
                "820 White Baseplate Raised 8 x 18 with Garage Floor Plate (Old style) to 822ac01 Red Garage Door Solid Assembly - Old (Hinge Pin on Counterweights - One Side) with weight 0.200\n" +
                "822ac01 Red Garage Door Solid Assembly - Old (Hinge Pin on Counterweights - One Side) to 820 Red Baseplate Raised 8 x 18 with Garage Floor Plate (Old style) with weight 1.000\n" +
                "820 Red Baseplate Raised 8 x 18 with Garage Floor Plate (Old style) to 821a Red Garage Door Frame (Old style) - No Studs, Full Wings with weight 1.000\n" +
                "total cost: 4.200\n";
        assertEquals(legoPath.findPath("604c Red Window 1 x 6 x 3 Panorama, without Glass for Slotted Bricks", "821a Red Garage Door Frame (Old style) - No Studs, Full Wings"), expected);
        //A path is found. The lexicographically (alphabetically) least path is returned.
        expected = "path from 3001a Black Brick 2 x 4 without Cross Supports to 3001a Yellow Brick 2 x 4 without Cross Supports:\n" +
                "3001a Black Brick 2 x 4 without Cross Supports to 3001a Yellow Brick 2 x 4 without Cross Supports with weight 1.000\n" +
                "total cost: 1.000\n";
        assertEquals(legoPath.findPath("3001a Black Brick 2 x 4 without Cross Supports", "3001a Yellow Brick 2 x 4 without Cross Supports"), expected);
        //Piece not found.
        expected = "unknown part UnknownPiece\n";
        assertEquals(legoPath.findPath("UnknownPiece", "3001a Black Brick 2 x 4 without Cross Supports"), expected);
        //Both pieces not found.
        expected = "unknown part UnknownPiece1\nunknown part UnknownPiece2\n";
        assertEquals(legoPath.findPath("UnknownPiece1", "UnknownPiece2"), expected);
        //A path to the piece itself.
        expected = "path from 3001a Black Brick 2 x 4 without Cross Supports to 3001a Black Brick 2 x 4 without Cross Supports:\ntotal cost: 0.000\n";
        assertEquals(legoPath.findPath("3001a Black Brick 2 x 4 without Cross Supports", "3001a Black Brick 2 x 4 without Cross Supports"), expected);
        //A path to themselves for an unknown piece.
        expected = "unknown part UnknownPiece1\n";
        assertEquals(legoPath.findPath("UnknownPiece1", "UnknownPiece1"), expected);

        legoPath.createNewGraph("data/lego1980.csv");
        expected = "path from 3829c01 White Steering Stand 1 x 2 with Black Steering Wheel to 260pr0001 HO Medium Blue HO Scale VW Beetle (Short Version):\n" +
                "3829c01 White Steering Stand 1 x 2 with Black Steering Wheel to 3641 Black Tyre 15 x 6 Offset Tread Small with weight 0.125\n" +
                "3641 Black Tyre 15 x 6 Offset Tread Small to 3004 Blue Brick 1 x 2 with weight 0.026\n" +
                "3004 Blue Brick 1 x 2 to 3005 Blue Brick 1 x 1 with weight 0.011\n" +
                "3005 Blue Brick 1 x 1 to 260pr0001 Red HO Scale VW Beetle (Short Version) with weight 0.500\n" +
                "260pr0001 Red HO Scale VW Beetle (Short Version) to 260pr0001 HO Medium Blue HO Scale VW Beetle (Short Version) with weight 1.000\n" +
                "total cost: 1.663\n";
        assertEquals(legoPath.findPath("3829c01 White Steering Stand 1 x 2 with Black Steering Wheel", "260pr0001 HO Medium Blue HO Scale VW Beetle (Short Version)"), expected);
    }

    @Test
    public void tinyDataTest() {
        LegoPaths legoPath = new LegoPaths();
        //remove ../../ when submitting
        legoPath.createNewGraph("data/tinyData");
        String expected = "path from Tepig to Caterpie:\n" +
                "Tepig to Caterpie with weight 1.000\n" +
                "total cost: 1.000\n";
        assertEquals(legoPath.findPath("Tepig", "Caterpie"), expected);
    }
}