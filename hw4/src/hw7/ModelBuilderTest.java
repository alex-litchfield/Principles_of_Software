package hw7;
import hw4.Location;
import hw4.Node;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
public class ModelBuilderTest {
    @Test
    public void testConstructors() {
        ModelBuilder model = new ModelBuilder();
        assert model.campusGraph != null;
        assert model.IDs != null;
        assert model.edgesMap != null;
        assert model.locationsMap != null;
        assertEquals(model.edgesMap.size(), 0);
        assertEquals(model.IDs.size(), 0);
        assertEquals(model.locationsMap.size(), 0);
    }

    @Test
    public void testCreateNewGraph() {
        ModelBuilder model = new ModelBuilder();
        //remove ../../ when submitting
        //Checks that locationParser brings in data correctly
        model.createNewGraph("data/RPI_map_data_Nodes.csv", "data/RPI_map_data_Edges.csv");
        assertTrue(model.IDs.contains(1)); //contains an id it should
        assertFalse(model.IDs.contains(183)); //doesn't contain an id it shouldn't
        Set<Integer> IDs = model.edgesMap.get(1);
        assertEquals(IDs.size(), 4); //ID 1 has 4 edges

        //checks that edgeCollection stores data correctly and can access children
        HashMap<Node<Location>, HashMap<Node<Location>, HashSet<Double>>> edgeCollection = model.campusGraph.getEdgeCollection();
        Node<Location> node1 = model.campusGraph.findNode(model.locationsMap.get(1));
        Node<Location> node2 = model.campusGraph.findNode(model.locationsMap.get(2));
        Node<Location> node3 = model.campusGraph.findNode(model.locationsMap.get(3));
        Node<Location> node4 = model.campusGraph.findNode(model.locationsMap.get(8000));
        assertTrue(edgeCollection.containsKey(node1));
        assertTrue(edgeCollection.containsKey(node2));
        assertTrue(edgeCollection.containsKey(node3));
        Iterator<Node<Location>> itr1 = model.campusGraph.listChildren(node1);
        LinkedList<String> childrenLL = new LinkedList<>();
        assertTrue(itr1.hasNext()); //Ensures there are children
        while (itr1.hasNext()) {
            Node<Location> currChild = itr1.next();
            HashSet<Double> labels = model.campusGraph.getEdgeCollection().get(node1).get(currChild);
            for (Double label : labels) {
                childrenLL.add(currChild.getName().getId()+"("+String.format("%.3f", label)+")");
            }
        }
        Collections.sort(childrenLL);
        Iterator<String> itr2 = childrenLL.iterator();
        assertTrue(itr2.hasNext()); //Ensures there are children with edges
        assertEquals("130(69.296)", itr2.next());
        assertEquals("3(78.588)", itr2.next());
        assertEquals("93(116.469)", itr2.next());
        assertEquals("94(56.045)", itr2.next());
        assertFalse(itr2.hasNext()); //Ensures we have seen every child with an edge
        itr1 = model.campusGraph.listChildren(node4);
        assertFalse(itr1.hasNext()); //No false children for non-existent nodes
    }

    @Test
    public void testListBuildings() {
        ModelBuilder model = new ModelBuilder();
        //remove ../../ when submitting
        model.createNewGraph("data/RPI_map_data_Nodes.csv", "data/RPI_map_data_Edges.csv");
        String expected = "133 Sunset Terrace,71\n200 Sunset Terrace,54\n2021 Peoples Avenue,33\n2144 Burdett Avenue,51\n" +
                "41 Ninth Street,27\n87 Gymnasium,11\nAcademy Hall,67\nAdmissions,34\nAlumni House,32\n" +
                "Alumni Sports & Recreation Center,37\nAmos Eaton Hall,26\nBarton Hall,73\nBeman Park Firehouse,69\n" +
                "Blaw-Knox 1 & 2,29\nBlitman Residence Commons,85\nBoiler House at 11th Street,77\n" +
                "Boiler House at Sage Avenue,5\nBray Hall,48\nBryckwyck,61\nBurdett Avenue Residence Hall,50\nCBIS,74\n" +
                "CII,14\nCarnegie Building,3\nCary Hall,47\nChapel and Cultural Center,49\nCogswell Laboratory,20\n" +
                "Colonie Apartments,66\nCommons Dining Hall,39\nCrockett Hall,40\nDCC,17\nDavison Hall,42\nE Complex,9\n" +
                "EMPAC,76\nEast Campus Athletic Village Arena,89\nEast Campus Athletic Village Stadium,90\nEmpire State Hall,68\n" +
                "Engineering Center,18\nField House Houston,52\nFolsom Library,23\nGreene Building,24\nGreenhouses and Grounds Barn,57\n" +
                "H Building,31\nHall Hall,46\nJ Building,30\nJava++ Cafe,80\nLINAC Facility,58\nLally Hall,25\n" +
                "Louis Rubin Memorial Approach,79\nMRC,21\nMueller Center,72\nNason Hall,41\nNorth Hall,8\nNugent Hall,44\nOGE,91\n" +
                "Parking Garage,75\nPatroon Manor,65\nPittsburgh Building,1\nPlayhouse,15\nPolytechnic Residence Commons,86\n" +
                "Public Safety,36\nQuadrangle Complex,12\nRPI Ambulance,81\nRadio Club,60\nRensselaer Apartment Housing Project RAHP A Site,53\n" +
                "Rensselaer Apartment Housing Project RAHP B Site,62\nRensselaer Union,35\nRicketts Building,10\nRobison Swimming Pool,38\n" +
                "Russell Sage Dining Hall,13\nRussell Sage Laboratory,6\nScience Center,19\nSeismograph Laboratory,55\nService Building,28\n" +
                "Sharp Hall,43\nStacwyck Apartments,59\nTroy Building,7\nVCC,22\nWalker Laboratory,4\nWarren Hall,45\nWest Hall,2\nWinslow Building,78";
        assertEquals(model.listBuildings(), expected);
    }

    @Test
    public void testFindPath() {
        ModelBuilder model = new ModelBuilder();
        //remove ../../ when submitting
        model.createNewGraph("data/RPI_map_data_Nodes.csv", "data/RPI_map_data_Edges.csv");
        //Tests a path between buildings given by name
        String expected = "Path from Nason Hall to Davison Hall:\n" +
                "\tWalk West to (Intersection 123)\n" +
                "\tWalk South to (Davison Hall)\n" +
                "Total distance: 120.479 pixel units.\n";
        assertEquals(model.findPath("Nason Hall", "Davison Hall"), expected);
        //Tests a path between buildings given by ID
        assertEquals(model.findPath("41", "42"), expected);
        //No path between 2 buildings given by ID/names
        expected = "Unknown building: [Linac Facility]\n";
        assertEquals(model.findPath("Linac Facility", "60"), expected);
        //One building is not found
        expected = "Unknown building: [cbis]\n";
        assertEquals(model.findPath("60", "cbis"), expected);
        //Both buildings not found
        expected = "Unknown building: [Grannie's]\nUnknown building: [800]";
        assertEquals(model.findPath("Grannie's", "800"), expected);
        //A path from a building to itself
        expected = "Path from Pittsburgh Building to Pittsburgh Building:\nTotal distance: 0.000 pixel units.\n";
        assertEquals(model.findPath("1", "Pittsburgh Building"), expected);
    }

}