package hw7;

import hw4.Location;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class LocationParser {
	public static void readNodes(String filename, Map<Integer, Location> locationsMap, Set<Integer> nodes)
			throws IOException {
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				String buildingName = parts[0];
				int id = Integer.parseInt(parts[1]);
				double xCord = Integer.parseInt(parts[2]);
				double yCord = Integer.parseInt(parts[3]);
				Location newLoc = new Location(buildingName, id, xCord, yCord);
				locationsMap.put(id, newLoc);
				nodes.add(id);
			}
		}
	}

	public static void readEdges(String filename, Map<Integer, Set<Integer>> edgesMap)
			throws IOException {
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				int id1 = Integer.parseInt(parts[0]);
				int id2 = Integer.parseInt(parts[1]);
				if (edgesMap.containsKey(id1)) { //id1 already exists as key
					edgesMap.get(id1).add(id2);
				}
				else { //id1 does not already exist in the map as key
					HashSet<Integer> idHolder= new HashSet<Integer>();
					idHolder.add(id2);
					edgesMap.put(id1, idHolder);
				}
			}
		}
	}
}
