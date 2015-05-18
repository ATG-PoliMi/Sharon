package sharon;

import sharon.xml.HomeMap;

/**
 * This class prints the map matrix.
 * 0: Floor
 * 1: Wall
 * 2: Sensor
 */
public class MapPrintTest {
	public static void main (String[] args) {
		HomeMap.getInstance().printMap();		
	}
}
