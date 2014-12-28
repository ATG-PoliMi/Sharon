package behavior.simulator.xml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import behavior.simulator.planner.LowLevelADL;
import behavior.simulator.planner.Station;




public class LLADLDB {

	public static Map<Integer, LowLevelADL> addLLADL() {
		Map<Integer, LowLevelADL> p = new HashMap<>();

		p.put(1, new LowLevelADL(1, "Breakfast", new ArrayList<Station>(Arrays.asList(new Station(1, 0.3), new Station(2, 0.7)))));		
		p.put(2, new LowLevelADL(2, "Out", new ArrayList<Station>(Arrays.asList(new Station(3, 0.3), new Station(0, 0.7)))));
		p.put(3, new LowLevelADL(3, "Shower", new ArrayList<Station>(Arrays.asList(new Station(4, 0.3), new Station(5, 0.7)))));
		p.put(4, new LowLevelADL(4, "Launder", new ArrayList<Station>(Arrays.asList(new Station(4, 0.3), new Station(6, 0.7)))));
		p.put(5, new LowLevelADL(5, "CleanUp", new ArrayList<Station>(Arrays.asList(new Station(6, 0.3), new Station(7, 0.7)))));
		p.put(6, new LowLevelADL(6, "Toilet", new ArrayList<Station>(Arrays.asList(new Station(8, 0.3), new Station(9, 0.7)))));
		p.put(7, new LowLevelADL(7, "Phone", new ArrayList<Station>(Arrays.asList(new Station(10, 0.3), new Station(4, 0.7)))));
		p.put(8, new LowLevelADL(8, "Lunch", new ArrayList<Station>(Arrays.asList(new Station(9, 0.3), new Station(5, 0.7)))));
		p.put(9, new LowLevelADL(9, "TV", new ArrayList<Station>(Arrays.asList(new Station(7, 0.3), new Station(6, 0.7)))));
		p.put(10, new LowLevelADL(10, "Tea", new ArrayList<Station>(Arrays.asList(new Station(11, 0.3), new Station(7, 0.7)))));
		p.put(11, new LowLevelADL(11, "ReadBook1", new ArrayList<Station>(Arrays.asList(new Station(13, 0.3), new Station(12, 0.7)))));
		p.put(12, new LowLevelADL(12, "ReadBook2", new ArrayList<Station>(Arrays.asList(new Station(15, 0.3), new Station(11, 0.7)))));
		p.put(13, new LowLevelADL(13, "WashingMachine", new ArrayList<Station>(Arrays.asList(new Station(1, 0.3), new Station(6, 0.7)))));
		p.put(14, new LowLevelADL(14, "Dinner", new ArrayList<Station>(Arrays.asList(new Station(3, 0.3), new Station(8, 0.7)))));
		p.put(15, new LowLevelADL(15, "Sleeping1", new ArrayList<Station>(Arrays.asList(new Station(5, 1.0)))));
		p.put(16, new LowLevelADL(16, "Sleeping2", new ArrayList<Station>(Arrays.asList(new Station(6, 1.0)))));
		return p;		
	}
}