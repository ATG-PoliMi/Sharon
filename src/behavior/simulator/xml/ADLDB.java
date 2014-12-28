package behavior.simulator.xml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import behavior.simulator.extractor.ADL;
import behavior.simulator.extractor.ADLEffect;

public class ADLDB {

	public static Map<Integer, ADL> addADL() {
		
		Map<Integer, ADL> adl = new HashMap<>();
		
		ArrayList <Integer> days = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7));
		ArrayList <Integer> marketDays = new ArrayList<Integer>(Arrays.asList(4,6));
		ArrayList <Integer> sitcomDays1 = new ArrayList<Integer>(Arrays.asList(1,3,5));
		ArrayList <Integer> sitcomDays2 = new ArrayList<Integer>(Arrays.asList(2,4));
		ArrayList <Integer> weekdays = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5));
		ArrayList <Integer> holidays = new ArrayList<Integer>(Arrays.asList(6,7));

		//ID, NAME, DAYS, WEATHER, TMEAN, TVARIABILITY, MANDATORY, BESTTIME, RANGETIME, CYCLICALITY, 
		//NEEDS, 
		//EFFECTS, BETADISTRA, BETADISTRB
		//Morning
		adl.put(101, new ADL (101, "Breakfast", days, 0, 10*60, 5*60, 1, 8*3600, 2*3600, 1,  
				new ArrayList<String>(Arrays.asList("hunger")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hunger", -0.9), new ADLEffect("bladder", +0.3))), 2.0,5.0));
		adl.put(102, new ADL (102, "MorningSitcom1", weekdays, 0, 60*60, 10*60, 1, 9*3600, 2*3600, 3, 
				new ArrayList<String>(Arrays.asList("comfort", "fun")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("comfort", -0.2), new ADLEffect("fun", -0.3))), 2.0,5.0));
		adl.put(103, new ADL (103, "MorningSitcom2", sitcomDays2, 0, 30*60, 10*60, 1, 10*3600, 2*3600, 2,
				new ArrayList<String>(Arrays.asList("comfort", "fun")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("comfort", -0.2), new ADLEffect("fun", -0.3))), 2.0,5.0));
		adl.put(104, new ADL (104, "Shopping", days, 0, 120*60, 40*60, 1, 10*3600, 2*3600, 4,
				null,
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("energy", +0.2), new ADLEffect("hygiene", +0.3))), 2.0,5.0));
		adl.put(105, new ADL (105, "Garden", days, 3, 180*60, 60*60, 1, 10*3600, 2*3600, 3,
				new ArrayList<String>(Arrays.asList("fun")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hunger", +0.3), new ADLEffect("energy", +0.3), new ADLEffect("hygiene", +0.3))), 2.0,5.0));
		adl.put(106, new ADL (106, "Shower", days, 0, 40*60, 10*60, 1, 11*3600, 2*3600, 2,
				new ArrayList<String>(Arrays.asList("hygiene")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hygiene", -1.0))), 2.0,5.0));
		adl.put(107, new ADL (107, "TakeAWalk", days, 3, 60*60, 15*60, 1, 10*3600, 2*3600, 3,
				null,
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hunger", +0.3), new ADLEffect("energy", +0.3), new ADLEffect("hygiene", +0.3))), 2.0,5.0));
		adl.put(108, new ADL (108, "Launder", days, 3, 60*60, 15*60, 1, 9*3600, 2*3600, 7,
				null,
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("energy", +0.2))), 2.0,5.0));
		adl.put(109, new ADL (109, "CleanUp", days, 0, 80*60, 20*60, 1, 10*3600, 2*3600, 5,
				null,
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("energy", +0.2))), 2.0,5.0));
		adl.put(110, new ADL (110, "Toilet", days, 0, 5*60, 1*60, 1, 9*3600, 2*3600, 1,
				new ArrayList<String>(Arrays.asList("bladder")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("bladder", -1.0))), 2.0,5.0));
		adl.put(111, new ADL (111, "Toilet", days, 0, 5*60, 1*60, 1, 11*3600, 1*3600, 1,
				new ArrayList<String>(Arrays.asList("bladder")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("bladder", -1.0))), 2.0,5.0));		
		adl.put(112,new ADL (112, "Sleep", days, 0, 7*3600, 120*60, 1, 3*3600, 2*3600, 1,
				new ArrayList<String>(Arrays.asList("energy")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("energy", -2.0), new ADLEffect("comfort", -1.0))), 10.0,1.0));
		adl.put(113, new ADL (113, "MARKET", marketDays, 3, 60*60, 15*60, 1, 9*3600, 2*3600, 3,
				null,
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hunger", +0.1), new ADLEffect("energy", +0.3), new ADLEffect("hygiene", +0.1))), 2.0,5.0));
		adl.put(114, new ADL (114, "Phone", days, 0, 5*60, 1*60, 1, 10*3600, 2*3600, 1,
				null,
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("fun", -0.1))), 2.0,5.0));


		adl.put(100, new ADL (100, "Lunch", days, 0, 50*60, 5*60, 1, 12*3600, 2*3600, 1,
				new ArrayList<String>(Arrays.asList("hunger")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hunger", -0.8), new ADLEffect("energy", +0.1))), 10.0,1.0));
		//Afternoon		

		adl.put(201, new ADL (201, "Sitcom1", holidays, 0, 60*60, 10*60, 0, 16*3600, 2*3600, 3,
				new ArrayList<String>(Arrays.asList("comfort", "fun")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("comfort", -0.2), new ADLEffect("fun", -0.3))), 2.0,5.0));
		adl.put(202, new ADL (202, "Sitcom2", sitcomDays1, 0, 60*60, 10*60, 0, 14*3600, 2*3600, 2,
				new ArrayList<String>(Arrays.asList("comfort", "fun")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("comfort", -0.2), new ADLEffect("fun", -0.3))), 2.0,5.0));
		adl.put(203, new ADL (203, "Tea", days, 0, 20*60, 5*60, 1, 17*3600, 2*3600, 1,
				new ArrayList<String>(Arrays.asList("hunger", "energy")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hunger", -0.2), new ADLEffect("energy", -0.1))), 2.0,5.0));
		adl.put(204, new ADL (204, "Garden", days, 3, 120*60, 60*60, 1, 16*3600, 2*3600, 3,
				new ArrayList<String>(Arrays.asList("fun")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hunger", +0.3), new ADLEffect("energy", +0.3), new ADLEffect("hygiene", +0.3))), 2.0,5.0));
		adl.put(205, new ADL (205, "ReadBook", days, 0, 30*60, 20*60, 1, 15*3600, 2*3600, 2,
				new ArrayList<String>(Arrays.asList("comfort", "fun")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("comfort", -0.3), new ADLEffect("fun", -0.3))), 2.0,5.0));
		adl.put(206, new ADL (206, "Shower", days, 0, 40*60, 10*60, 1, 17*3600, 2*3600, 1,
				new ArrayList<String>(Arrays.asList("hygiene")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hygiene", -1.0))), 2.0,5.0));
		adl.put(207, new ADL (207, "WashingMachine", days, 1, 10*60, 5*60, 1, 15*3600, 2*3600, 4,
				null,
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("energy", +0.2))), 2.0,5.0));
		adl.put(208, new ADL (208, "TakeAWalk", days, 3, 60*60, 15*60, 1, 16*3600, 2*3600, 2,
				null,
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hunger", +0.3), new ADLEffect("energy", +0.3), new ADLEffect("hygiene", +0.3))), 2.0,5.0));
		adl.put(209, new ADL (209, "CleanUp", days, 0, 80*60, 20*60, 1, 15*3600, 2*3600, 5,
				null,
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("energy", +0.1),new ADLEffect("hygiene", +0.1))), 2.0,5.0));
		adl.put(210, new ADL (210, "Toilet", days, 0, 5*60, 1*60, 1, 18*3600, 2*3600, 1,
				new ArrayList<String>(Arrays.asList("bladder")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("bladder", -1.0))), 2.0,5.0));
		adl.put(211, new ADL (211, "Phone", days, 0, 5*60, 1*60, 1, 16*3600, 2*3600, 1,
				null,
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("fun", -0.1))), 2.0,5.0));

		adl.put(200, new ADL (200, "Dinner", days, 0, 50*60, 5*60, 1, 20*3600, 2*3600, 1,
				new ArrayList<String>(Arrays.asList("hunger")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hunger", -0.8), new ADLEffect("energy", +0.1))), 10.0,1.0));

		//Evening

		adl.put(301, new ADL (301, "Cinema", days, 0, 180*60, 60*60, 1, 20*3600, 2*3600, 4,
				new ArrayList<String>(Arrays.asList("comfort", "fun")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("comfort", -0.5), new ADLEffect("fun", -0.3))), 2.0,5.0));
		adl.put(302, new ADL (302, "EveningTV", days, 0, 180*60, 30*60, 1, 21*3600, 2*3600, 3,
				new ArrayList<String>(Arrays.asList("comfort", "fun")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("comfort", -0.5), new ADLEffect("fun", -0.3))), 2.0,5.0));
		adl.put(303, new ADL (303, "ReadBook", days, 0, 180*60, 60*60, 1, 20*3600, 2*3600, 3,
				new ArrayList<String>(Arrays.asList("comfort", "fun")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("comfort", -0.5), new ADLEffect("fun", -0.3))), 2.0,5.0));

		adl.put(300, new ADL (300, "Sleep", days, 0, 9*3600, 120*60, 1, 21*3600, 2*3600, 1,
				new ArrayList<String>(Arrays.asList("energy")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("energy", -2.0), new ADLEffect("comfort", -1.0))), 10.0,1.0));
		return adl;
	}
}
