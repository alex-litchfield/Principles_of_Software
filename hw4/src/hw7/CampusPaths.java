package hw7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CampusPaths {
    //THIS IS THE CONTROLLER AND VIEW FILE
    public static void main(String[] argv) {
        ModelBuilder rpiModel = new ModelBuilder();
        //REMOVE WHEN TESTING ON SUBMITTY
        rpiModel.createNewGraph("data/RPI_map_data_Nodes.csv", "data/RPI_map_data_Edges.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input = "";
        while (true) {
            try {
                input = reader.readLine();
            } catch (IOException e) {
                break;
            }
            //prints a menu of all commands
            if (input.equals("m")) {
                System.out.println("b lists all buildings\n"
                    + "r prints directions for the shortest route between any two buildings\n"
                    + "q quits the program\nm prints a menu of all commands");
            }
            //quits the program
            else if (input.equals("q")) {
                break;
            }
            //lists all buildings (only buildings) in the form name,id in lexicographic (alphabetical) orderof name.
            else if (input.equals("b")) {
                System.out.println(rpiModel.listBuildings());
            }
            //prompts the user for the ids or names of two buildings (only buildings!) and prints direction for the shortest route between them.
            else if (input.equals("r")) {
                String firstBuilding;
                String secondBuilding;
                System.out.print("First building id/name, followed by Enter: ");
                try {
                    firstBuilding = reader.readLine();
                } catch (IOException e) {
                    break;
                }
                System.out.print("Second building id/name, followed by Enter: ");
                try {
                    secondBuilding = reader.readLine();
                } catch (IOException e) {
                    break;
                }
                System.out.print(rpiModel.findPath(firstBuilding, secondBuilding));
            }
            else {
                System.out.println("Unknown option");
            }
        }
        return;
    }

}