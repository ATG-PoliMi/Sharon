package behavior.simulator.xml;

import java.util.ArrayList;
import java.util.Arrays;

import behavior.simulator.planner.LowLevelADL;
import behavior.simulator.planner.Station;




public class LLADLDB {

	public static ArrayList<LowLevelADL> addLLADL() {
		ArrayList<LowLevelADL> p = new ArrayList<LowLevelADL>();

		p.add(new LowLevelADL(1, "Breakfast", new ArrayList<Station>(Arrays.asList(new Station(1, 0.3), new Station(2, 0.7)))));
		p.add(new LowLevelADL(2, "Out", new ArrayList<Station>(Arrays.asList(new Station(3, 0.3), new Station(0, 0.7)))));
		p.add(new LowLevelADL(3, "Shower", new ArrayList<Station>(Arrays.asList(new Station(4, 0.3), new Station(5, 0.7)))));
		p.add(new LowLevelADL(4, "Launder", new ArrayList<Station>(Arrays.asList(new Station(4, 0.3), new Station(6, 0.7)))));
		p.add(new LowLevelADL(5, "CleanUp", new ArrayList<Station>(Arrays.asList(new Station(6, 0.3), new Station(7, 0.7)))));
		p.add(new LowLevelADL(6, "Toilet", new ArrayList<Station>(Arrays.asList(new Station(8, 0.3), new Station(9, 0.7)))));
		p.add(new LowLevelADL(7, "Phone", new ArrayList<Station>(Arrays.asList(new Station(10, 0.3), new Station(4, 0.7)))));
		p.add(new LowLevelADL(8, "Lunch", new ArrayList<Station>(Arrays.asList(new Station(9, 0.3), new Station(5, 0.7)))));
		p.add(new LowLevelADL(9, "TV", new ArrayList<Station>(Arrays.asList(new Station(7, 0.3), new Station(6, 0.7)))));
		p.add(new LowLevelADL(10, "Tea", new ArrayList<Station>(Arrays.asList(new Station(11, 0.3), new Station(7, 0.7)))));
		p.add(new LowLevelADL(11, "ReadBook1", new ArrayList<Station>(Arrays.asList(new Station(13, 0.3), new Station(12, 0.7)))));
		p.add(new LowLevelADL(12, "ReadBook2", new ArrayList<Station>(Arrays.asList(new Station(15, 0.3), new Station(11, 0.7)))));
		p.add(new LowLevelADL(13, "WashingMachine", new ArrayList<Station>(Arrays.asList(new Station(1, 0.3), new Station(6, 0.7)))));
		p.add(new LowLevelADL(14, "Dinner", new ArrayList<Station>(Arrays.asList(new Station(3, 0.3), new Station(8, 0.7)))));
		p.add(new LowLevelADL(15, "Sleeping1", new ArrayList<Station>(Arrays.asList(new Station(5, 1.0)))));
		p.add(new LowLevelADL(16, "Sleeping2", new ArrayList<Station>(Arrays.asList(new Station(6, 1.0)))));
		return p;
	}
}