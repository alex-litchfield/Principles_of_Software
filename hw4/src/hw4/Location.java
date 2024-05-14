package hw4;

public class Location implements Comparable<Location>{
    private String buildingName;
    private int id;
    private double xCord;
    private double yCord;

    public Location(String buildingName, int id, double xCord, double yCord) {
        this.buildingName = buildingName;
        this.id = id;
        this.xCord = xCord;
        this.yCord = yCord;
    }

    @Override
    public int compareTo(Location location) {
        return Integer.compare(this.id, location.id);
    }

    public String getBuildingName() {
        return this.buildingName;
    }

    public int getId() {
        return this.id;
    }

    public double getXCord() {
        return this.xCord;
    }

    public double getYCord() {
        return this.yCord;
    }

    public String getCardinalDirectionTo(Location destLocation) {
        double deltaX = this.xCord - destLocation.xCord;
        double deltaY = this.yCord - destLocation.yCord;
        // Calculate angle in radians
        double angle = Math.atan2(deltaY, deltaX);
        // Convert angle to degrees
        double angleDegrees = Math.toDegrees(angle);
        // Normalize angle to be within [0, 360) degrees
        if (angleDegrees < 0) {
            angleDegrees += 360;
        }
        String direction;
        // Map angle to direction
        if (angleDegrees >= 22.5 && angleDegrees < 67.5) {
            direction = "NorthWest";
        } else if (angleDegrees >= 67.5 && angleDegrees < 112.5) {
            direction = "North";
        } else if (angleDegrees >= 112.5 && angleDegrees < 157.5) {
            direction = "NorthEast";
        } else if (angleDegrees >= 157.5 && angleDegrees < 202.5) {
            direction = "East";
        } else if (angleDegrees >= 202.5 && angleDegrees < 247.5) {
            direction = "SouthEast";
        } else if (angleDegrees >= 247.5 && angleDegrees < 292.5) {
            direction = "South";
        } else if (angleDegrees >= 292.5 && angleDegrees < 337.5) {
            direction = "SouthWest";
        } else {
            direction = "West";
        }
        return direction;
    }
}
