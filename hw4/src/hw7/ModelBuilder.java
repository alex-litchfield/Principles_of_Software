package hw7;

import hw4.Graph;
import hw4.Location;
import hw4.Node;

import java.io.IOException;
import java.util.*;

public class ModelBuilder {
    //Abstraction function:
    public Graph<Location, Double> campusGraph;
    public Map<Integer, Location> locationsMap; //Keys are ID's and values are the corresponding Location object

    //EDGESMAP IS LEGOMAP in LEGOPATHS
    public Map<Integer, Set<Integer>> edgesMap; //Keys are ID's and values are a HashSet of connecting ID's
    public Set<Integer> IDs; //Set of ID's

    public HashMap<String, Integer> nameToIDs; //Keys are Building names and Values are ID's

    public ModelBuilder() {
        campusGraph = new Graph<Location, Double>();
        locationsMap = new HashMap<Integer, Location>();
        edgesMap = new HashMap<Integer, Set<Integer>>();
        IDs = new HashSet<Integer>();
        nameToIDs = new HashMap<String, Integer>();
    }

    private void checkRep() {
        //The representation invariant is that the graph and number set of profs cannot be null
        if (locationsMap == null) {
            throw new RuntimeException("Rep check failed because locationsMap is null");
        }
        else if (edgesMap == null) {
            throw new RuntimeException("Rep check failed because edgesMap is null");
        }
        else if (campusGraph == null) {
            throw new RuntimeException("Rep check failed because campusGraph is null");
        }
        else if (IDs == null) {
            throw new RuntimeException("Rep check failed because campusGraph is null");
        }
    }

    public void createNewGraph(String filename1, String filename2) {
        locationsMap.clear();
        edgesMap.clear();
        IDs.clear();
        nameToIDs.clear();
        try {
            LocationParser.readNodes(filename1, locationsMap, IDs);
            LocationParser.readEdges(filename2, edgesMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Map.Entry<Integer, Location> entry : locationsMap.entrySet()) {
            Node<Location> node1 = new Node<Location>(entry.getValue()); //Each node name is LOCATION OBJECT
            campusGraph.addNode(node1);
        }
        for (Map.Entry<Integer, Set<Integer>> entry: edgesMap.entrySet()) {
            Integer id1 = entry.getKey();
            for (Integer id2: entry.getValue()) {
                if (!id1.equals(id2)) {
                    double distance = Math.sqrt(Math.pow((locationsMap.get(id2).getXCord() - locationsMap.get(id1).getXCord()), 2) +
                            Math.pow((locationsMap.get(id2).getYCord() - locationsMap.get(id1).getYCord()), 2));
                    Node<Location> node1 = campusGraph.findNode(locationsMap.get(id1));
                    Node<Location> node2 = campusGraph.findNode(locationsMap.get(id2));
                    campusGraph.addEdge(node1, node2, distance);
                }
            }
        }
        for (Map.Entry<Integer, Location> entry: locationsMap.entrySet()) {
            String buildingName = locationsMap.get(entry.getKey()).getBuildingName();
            if (!buildingName.isEmpty()) {
                nameToIDs.put(buildingName, entry.getKey());
            }
        }
    }

    public String findPath(String firstBuilding, String secondBuilding) {
        int firstBuildingID; //holds 0 for if a name is provided or ID
        int secondBuildingID; //holds 0 for if a name is provided or ID
        Location location1 = null;
        Location location2 = null;
        String output = "";
        //Checks if either of the buildings are integers (therefore IDs)
        try {
            firstBuildingID = Integer.parseInt(firstBuilding);
        } catch (NumberFormatException e) {
            firstBuildingID = 0;
        }
        try {
            secondBuildingID = Integer.parseInt(secondBuilding);
        } catch (NumberFormatException e) {
            secondBuildingID = 0;
        }
        //If a name was provided for firstBuilding and not an ID
        if (firstBuildingID == 0) {
            //First building provides a name and it is not valid
            if (nameToIDs.get(firstBuilding) == null) {
                output = output + "Unknown building: [" + firstBuilding + "]\n";
                if (firstBuilding.equals(secondBuilding)) {
                    return output;
                }
            }
            //First building provides a valid name
            else {
                location1 = locationsMap.get(nameToIDs.get(firstBuilding));
            }
        }
        //If firstBuilding DOES provide an ID
        else {
            //First building provides a valid id
            if (locationsMap.get(firstBuildingID) != null) {
                //If the ID provided shows us an intersection
                if (locationsMap.get(firstBuildingID).getBuildingName().isEmpty()) {
                    output = output + "Unknown building: [" + firstBuildingID + "]\n";
                    if (firstBuildingID == secondBuildingID) {
                        return output;
                    }
                }
                //ID does not show an intersection
                else {
                    location1 = locationsMap.get(firstBuildingID);
                }
            }
            //First building does not provide a valid ID
            else {
                output = output + "Unknown building: [" + firstBuildingID + "]\n";
                if (firstBuildingID == secondBuildingID) {
                    return output;
                }
            }
        }
        //If a name was provided for secondBuilding and not an ID
        if (secondBuildingID == 0) {
            //Second building provides a name and it is not valid
            if (nameToIDs.get(secondBuilding) == null) {
                output = output + "Unknown building: [" + secondBuilding + "]\n";
            }
            //Second building provides a valid name
            else {
                location2 = locationsMap.get(nameToIDs.get(secondBuilding));
            }
        }
        //If SecondBuilding DOES provide an ID
        else {
            //Second building provides a valid id
            if (locationsMap.get(secondBuildingID) != null) {
                //If the ID provided shows us an intersection
                if (locationsMap.get(secondBuildingID).getBuildingName().isEmpty()) {
                    output = output + "Unknown building: [" + secondBuildingID + "]";
                }
                //ID does not show an intersection
                else {
                    location2 = locationsMap.get(secondBuildingID);
                }
            }
            //Second building does not provide a valid ID
            else {
                output = output + "Unknown building: [" + secondBuildingID + "]";
            }
        }
        //Calling findpath
        if (location1 != null && location2 != null) {
            output = output + campusGraph.findPath(location1, location2);
        }
        return output;
    }

    static class BuildingComparator implements Comparator<Integer> {
        private final Map<Integer, Location> locationsMap;
        public BuildingComparator(Map<Integer, Location> locationsMap) {
            this.locationsMap = locationsMap;
        }
        @Override
        public int compare(Integer id1, Integer id2) {
            String name1 = locationsMap.get(id1).getBuildingName();
            String name2 = locationsMap.get(id2).getBuildingName();
            return name1.compareTo(name2);
        }
    }

    //Returns a string which lists all the buildings and their ids in lexicographical order
    public String listBuildings() {
        //BUILDINGS WITH NO NAME HAVE EMPTY NAMES
        BuildingComparator comparator = new BuildingComparator(locationsMap);
        LinkedList<Integer> sortedIDs = new LinkedList<Integer>();
        //Adds IDs to the list only if their name is not ""
        for (int currID: IDs) {
            if (!locationsMap.get(currID).getBuildingName().isEmpty()) {
                sortedIDs.add(currID);
            }
        }
        //sorts by lexicographical order by name
        sortedIDs.sort(comparator);
        //Creates output string which lists all the buildings and their ids in lexicographical order
        Iterator<Integer> itr = sortedIDs.iterator();
        StringBuilder output = new StringBuilder();
        while (itr.hasNext()) {
            Integer currID = itr.next();
            output = new StringBuilder(output + locationsMap.get(currID).getBuildingName() + "," + currID);
            if (itr.hasNext()) {
                output = new StringBuilder(output + "\n");
            }
        }
        return output.toString();
    }

}
