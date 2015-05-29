package atg.polimi.sharon.configs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import atg.polimi.sharon.data.Station;
import atg.polimi.sharon.engine.LowLevelADL;

public class LLADLDB {

	public static Map<Integer, LowLevelADL> addLLADL() {
		Map<Integer, LowLevelADL> p = new HashMap<>();

		p.put(1, new LowLevelADL(1, "Other1", new ArrayList<Station>(Arrays.asList(new Station(11, 1.0)))));
		p.put(2, new LowLevelADL(2, "Other2", new ArrayList<Station>(Arrays.asList(new Station(12, 1.0)))));
		p.put(3, new LowLevelADL(3, "GoingOut1", new ArrayList<Station>(Arrays.asList(new Station(1, 0.2), new Station(8, 0.8)))));
		p.put(4, new LowLevelADL(4, "GoingOut2", new ArrayList<Station>(Arrays.asList(new Station(8, 1.0)))));
		p.put(5, new LowLevelADL(5, "Breakfast1", new ArrayList<Station>(Arrays.asList(new Station(19, 0.25), new Station(17, 0.20), new Station(9, 0.35), new Station(16, 0.20)))));
		//p.put(5, new LowLevelADL(5, "Breakfast1", new ArrayList<Station>(Arrays.asList(new Station(19, 0.1), new Station(17, 0.4), new Station(9, 0.2), new Station(16, 0.3)))));
		p.put(6, new LowLevelADL(6, "Breakfast2", new ArrayList<Station>(Arrays.asList(new Station(18, 0.1), new Station(19, 0.1), new Station(17, 0.4), new Station(10, 0.2), new Station(16, 0.2)))));
		p.put(7, new LowLevelADL(7, "Lunch1", new ArrayList<Station>(Arrays.asList(new Station(18, 0.1), new Station(19, 0.1), new Station(17, 0.4), new Station(10, 0.2), new Station(16, 0.2)))));
		p.put(8, new LowLevelADL(8, "Lunch2", new ArrayList<Station>(Arrays.asList(new Station(18, 0.1), new Station(19, 0.1), new Station(17, 0.3), new Station(10, 0.3), new Station(16, 0.2)))));
		p.put(9, new LowLevelADL(9, "Dinner1", new ArrayList<Station>(Arrays.asList(new Station(18, 0.1), new Station(19, 0.1), new Station(17, 0.4), new Station(10, 0.2), new Station(16, 0.2)))));
		p.put(10, new LowLevelADL(10, "Dinner2", new ArrayList<Station>(Arrays.asList(new Station(18, 0.1), new Station(19, 0.1), new Station(17, 0.3), new Station(10, 0.3), new Station(16, 0.2)))));

		p.put(11, new LowLevelADL(11, "Excretion1", new ArrayList<Station>(Arrays.asList(new Station(4, 0.9), new Station(5, 0.1)))));
		p.put(12, new LowLevelADL(12, "Excretion2", new ArrayList<Station>(Arrays.asList(new Station(4, 1.0)))));
		p.put(13, new LowLevelADL(13, "Shower1", new ArrayList<Station>(Arrays.asList(new Station(3, 1.0)))));
		p.put(14, new LowLevelADL(14, "Shower2", new ArrayList<Station>(Arrays.asList(new Station(3, 0.9), new Station(1, 0.1)))));
		p.put(15, new LowLevelADL(15, "Sleeping1", new ArrayList<Station>(Arrays.asList(new Station(0, 1.0)))));
		p.put(16, new LowLevelADL(16, "Sleeping2", new ArrayList<Station>(Arrays.asList(new Station(0, 0.1), new Station(1, 0.9)))));
		p.put(17, new LowLevelADL(17, "WatchingTV1", new ArrayList<Station>(Arrays.asList(new Station(14, 1.0)))));
		p.put(18, new LowLevelADL(18, "WatchingTV2", new ArrayList<Station>(Arrays.asList(new Station(14, 0.1), new Station(13, 0.9)))));
		p.put(19, new LowLevelADL(19, "Snack1", new ArrayList<Station>(Arrays.asList(new Station(18, 0.2), new Station(9, 0.8)))));
		p.put(20, new LowLevelADL(20, "Snack2", new ArrayList<Station>(Arrays.asList(new Station(19, 0.2), new Station(9, 0.8)))));

		p.put(21, new LowLevelADL(21, "Napping1", new ArrayList<Station>(Arrays.asList(new Station(13, 1.0)))));
		p.put(22, new LowLevelADL(22, "Napping2", new ArrayList<Station>(Arrays.asList(new Station(0, 1.0)))));
		p.put(23, new LowLevelADL(23, "Internet1", new ArrayList<Station>(Arrays.asList(new Station(10, 1.0)))));
		p.put(24, new LowLevelADL(24, "Internet2", new ArrayList<Station>(Arrays.asList(new Station(9, 1.0)))));
		p.put(25, new LowLevelADL(25, "ReadingBook1", new ArrayList<Station>(Arrays.asList(new Station(13, 1.0)))));
		p.put(26, new LowLevelADL(26, "ReadingBook2", new ArrayList<Station>(Arrays.asList(new Station(11, 0.1), new Station(13, 0.8), new Station(11, 0.1)))));
		p.put(27, new LowLevelADL(27, "HavingConversation1", new ArrayList<Station>(Arrays.asList(new Station(12, 1.0)))));
		p.put(28, new LowLevelADL(28, "HavingConversation2", new ArrayList<Station>(Arrays.asList(new Station(13, 1.0)))));
		p.put(29, new LowLevelADL(29, "Music1", new ArrayList<Station>(Arrays.asList(new Station(12, 1.0)))));
		p.put(30, new LowLevelADL(30, "Music2", new ArrayList<Station>(Arrays.asList(new Station(13, 1.0)))));
		
		p.put(31, new LowLevelADL(31, "Cleaning1", new ArrayList<Station>(Arrays.asList(new Station(0, 0.2), new Station(1, 0.2), new Station(18, 0.2), new Station(17, 0.2), new Station(15, 0.2)))));
		p.put(32, new LowLevelADL(32, "Cleaning2", new ArrayList<Station>(Arrays.asList(new Station(4, 0.2), new Station(5, 0.2), new Station(9, 0.2), new Station(10, 0.2), new Station(12, 0.2)))));
		p.put(33, new LowLevelADL(33, "Laundry1", new ArrayList<Station>(Arrays.asList(new Station(6, 1.0)))));
		p.put(34, new LowLevelADL(34, "Laundry2", new ArrayList<Station>(Arrays.asList(new Station(6, 0.9), new Station(1, 0.1)))));

		return p;		
	}
}