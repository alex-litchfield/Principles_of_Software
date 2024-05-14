package hw4;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class LocationTest {
    @Test
    public void testConstructors() {
		//Create locations via constructor
    	Location loc1 = new Location("Mom's House", 1, 82.6, 549.5);
		//Testing that values are accurate with each other
    	assertEquals(loc1.getId(), 1);
        assertEquals(loc1.getBuildingName(), "Mom's House");
        assertEquals(loc1.getXCord(), 82.6, 0.0001);
        assertEquals(loc1.getYCord(), 549.5, 0.0001);

    }
    @Test
    public void testGetCardinalDirections() {
        Location loc1 = new Location("Mom's House", 1, 82.6, 549.5);
        Location loc2 = new Location("Dad's House", 2, 134.3, 344.8);
        assertEquals(loc1.getCardinalDirectionTo(loc2), "North");
    }

}