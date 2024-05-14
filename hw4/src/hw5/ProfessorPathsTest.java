package hw5;

import hw4.Node;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.*;

public final class ProfessorPathsTest {
    @Test
    public void testConstructors() {
        ProfessorPaths profPath = new ProfessorPaths();
        assert profPath.profGraph != null;
        assert profPath.profsTeaching != null;
        assert profPath.profs!= null;
        assertEquals(profPath.profsTeaching.size(), 0);
        assertEquals(profPath.profs.size(), 0);
    }

    @Test
    public void testCreateNewGraph() {
        ProfessorPaths profPath = new ProfessorPaths();
        //remove ../../ when submitting
        profPath.createNewGraph("data/courses.csv");
        //checks professorParser data
        Set<String> courseProfs = profPath.profsTeaching.get("CSCI-4650");
        assertEquals(courseProfs.size(), 3);
        assertTrue(courseProfs.contains("David A Kotfila"));
        assertTrue(courseProfs.contains("Bernadette Mary O'Brien"));
        assertTrue(courseProfs.contains("Christian M. Price"));
        //checks that edgeCollection stores data correctly and can access children
        HashMap<Node<String>, HashMap<Node<String>, HashSet<String>>> edgeCollection = profPath.profGraph.graph.getEdgeCollection();
        Node<String> node1 = profPath.profGraph.graph.findNode("David A Kotfila");
        Node<String> node2 = profPath.profGraph.graph.findNode("Bernadette Mary O'Brien");
        Node<String> node3 = profPath.profGraph.graph.findNode("Christian M. Price");
        assertTrue(edgeCollection.containsKey(node1));
        assertTrue(edgeCollection.containsKey(node2));
        assertTrue(edgeCollection.containsKey(node3));
        Iterator<String> itr1 = profPath.profGraph.listChildren("David A Kotfila");
        assertTrue(itr1.hasNext()); //Ensures there are children
        assertEquals("Bernadette Mary O'Brien(CSCI-4650)", itr1.next());
        assertEquals("Christian M. Price(CSCI-4650)", itr1.next());
        assertEquals("Christian M. Price(CSCI-4660)", itr1.next());
        assertEquals("Robert Michael Cannistra(CSCI-4660)", itr1.next());
        assertFalse(itr1.hasNext()); //Ensures we have seen every child
        itr1 = profPath.profGraph.listChildren("Bobby Joe");
        assertFalse(itr1.hasNext()); //No false children for non-existent nodes
    }

    @Test
    public void testFindPath() {
        ProfessorPaths profPath = new ProfessorPaths();
        //remove ../../ when submitting
        profPath.createNewGraph("data/courses.csv");
        //A path is found. The lexicographically (alphabetically) least path is returned.
        System.out.println(profPath.findPath("Mohammed J. Zaki", "Wilfredo Colon"));
        assertEquals(profPath.findPath("Mohammed J. Zaki", "Wilfredo Colon"), "path from Mohammed J. Zaki to Wilfredo Colon:\nMohammed J. Zaki to David Eric Goldschmidt via CSCI-2300\nDavid Eric Goldschmidt to Michael Joseph Conroy via CSCI-4430\nMichael Joseph Conroy to Alan R Cutler via CHEM-1200\nAlan R Cutler to Wilfredo Colon via CHEM-1100\n");
        //No path exists.
        assertEquals(profPath.findPath("David Eric Goldschmidt", "Hugh Johnson"),"path from David Eric Goldschmidt to Hugh Johnson:\nno path found\n" );
        //Professor not found.
        assertEquals(profPath.findPath("Donald Knuth", "Malik Magdon-Ismail"), "unknown professor Donald Knuth\n");
        //Both professors not found.
        assertEquals(profPath.findPath("Donald Knuth", "Brian Kernighan"), "unknown professor Donald Knuth\nunknown professor Brian Kernighan\n");
        //A path to the professor themselves.
        assertEquals(profPath.findPath("Barbara Cutler", "Barbara Cutler"), "path from Barbara Cutler to Barbara Cutler:\n");
        //A path to themselves for an unknown professor.
        assertEquals(profPath.findPath("Donald Knuth", "Donald Knuth"), "unknown professor Donald Knuth\n");

    }
}