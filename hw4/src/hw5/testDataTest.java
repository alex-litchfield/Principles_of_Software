
package hw5;

import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class testDataTest {
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
        profPath.createNewGraph("data/testData.csv");
        Set<String> courseProfs = profPath.profsTeaching.get("BIOL-2624");
        assertEquals(courseProfs.size(), 2);
        assertTrue(courseProfs.contains("Colton Rogers"));
        assertTrue(courseProfs.contains("Aaron Barnes"));
    }

    @Test
    public void testFindPath() {
        //LARGE DATASET
        ProfessorPaths profPath = new ProfessorPaths();
        //remove ../../ when submitting
        profPath.createNewGraph("data/testData.csv");
        //A path is found. The lexicographically (alphabetically) least path is returned.
        String expected = "path from Aaron Alexander to Aaron Bennett:\n" +
                "Aaron Alexander to Brooks Hernandez via ANSC-4451\n" +
                "Brooks Hernandez to Noah White via ARTS-3808\n" +
                "Noah White to Angel Bailey via ANTH-3227\n" +
                "Angel Bailey to William Anderson via BEHS-1585\n" +
                "William Anderson to Jordan Gonzalez via DATA-5295\n" +
                "Jordan Gonzalez to Aaron Bennett via ARBC-3244\n";
        assertEquals(profPath.findPath("Aaron Alexander", "Aaron Bennett"), expected);
        //No path exists.
        expected = "path from Aaron Alexander to Xavier Wood:\nno path found\n";
        assertEquals(profPath.findPath("Aaron Alexander", "Xavier Wood"), expected);
        //Professor not found.
        expected = "unknown professor Johnny Appleseed\n";
        assertEquals(profPath.findPath("Johnny Appleseed", "Xavier Wood"), expected);
        //Both professors not found.
        expected = "unknown professor Johnny Appleseed\nunknown professor Ben Franklin\n";
        assertEquals(profPath.findPath("Johnny Appleseed", "Ben Franklin"), expected);
        //A path to the professor themselves.
        expected = "path from Aaron Alexander to Aaron Alexander:\n";
        assertEquals(profPath.findPath("Aaron Alexander", "Aaron Alexander"), expected);
        //A path to themselves for an unknown professor.
        expected = "unknown professor Johnny Appleseed\n";
        assertEquals(profPath.findPath("Johnny Appleseed", "Johnny Appleseed"), expected);
    }
}