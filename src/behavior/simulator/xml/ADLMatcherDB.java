package behavior.simulator.xml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import behavior.simulator.planner.ADLMatcher;


public class ADLMatcherDB {
	

	public static Map<Integer, ADLMatcher> addADLMatch() {
		
		Map<Integer, ADLMatcher> m = new HashMap<>();
//NEW:		
		m.put(1, new ADLMatcher(1, new ArrayList<Integer>(Arrays.asList(8)), new ArrayList<Double>(Arrays.asList(1.0))));
//OLD:		
		m.put(100, new ADLMatcher(100, new ArrayList<Integer>(Arrays.asList(8)), new ArrayList<Double>(Arrays.asList(1.0))));
		m.put(101, new ADLMatcher(101, new ArrayList<Integer>(Arrays.asList(1)), new ArrayList<Double>(Arrays.asList(1.0))));
		m.put(102, new ADLMatcher(102, new ArrayList<Integer>(Arrays.asList(9)), new ArrayList<Double>(Arrays.asList(1.0))));
		m.put(103, new ADLMatcher(103, new ArrayList<Integer>(Arrays.asList(9)), new ArrayList<Double>(Arrays.asList(1.0))));
		m.put(104, new ADLMatcher(104, new ArrayList<Integer>(Arrays.asList(2)), new ArrayList<Double>(Arrays.asList(1.0))));
		m.put(105, new ADLMatcher(105, new ArrayList<Integer>(Arrays.asList(2)), new ArrayList<Double>(Arrays.asList(1.0))));
		m.put(106, new ADLMatcher(106, new ArrayList<Integer>(Arrays.asList(3)), new ArrayList<Double>(Arrays.asList(1.0))));
		m.put(107, new ADLMatcher(107, new ArrayList<Integer>(Arrays.asList(2)), new ArrayList<Double>(Arrays.asList(1.0))));
		m.put(108, new ADLMatcher(108, new ArrayList<Integer>(Arrays.asList(4)), new ArrayList<Double>(Arrays.asList(1.0))));
		m.put(109, new ADLMatcher(109, new ArrayList<Integer>(Arrays.asList(5)), new ArrayList<Double>(Arrays.asList(1.0))));
		m.put(110, new ADLMatcher(110, new ArrayList<Integer>(Arrays.asList(6)), new ArrayList<Double>(Arrays.asList(1.0))));
		m.put(111, new ADLMatcher(111, new ArrayList<Integer>(Arrays.asList(7)), new ArrayList<Double>(Arrays.asList(1.0))));
		m.put(112, new ADLMatcher(112, new ArrayList<Integer>(Arrays.asList(15,16)), new ArrayList<Double>(Arrays.asList(0.5,0.5))));
		m.put(112, new ADLMatcher(113, new ArrayList<Integer>(Arrays.asList(15,16)), new ArrayList<Double>(Arrays.asList(0.5,0.5))));
		m.put(112, new ADLMatcher(114, new ArrayList<Integer>(Arrays.asList(15,16)), new ArrayList<Double>(Arrays.asList(0.5,0.5))));
		
		m.put(200, new ADLMatcher(200, new ArrayList<Integer>(Arrays.asList(14)), new ArrayList<Double>(Arrays.asList(1.0))));
		m.put(201, new ADLMatcher(201, new ArrayList<Integer>(Arrays.asList(9)), new ArrayList<Double>(Arrays.asList(1.0))));
		m.put(202, new ADLMatcher(202, new ArrayList<Integer>(Arrays.asList(9)), new ArrayList<Double>(Arrays.asList(1.0))));
		m.put(203, new ADLMatcher(203, new ArrayList<Integer>(Arrays.asList(10)), new ArrayList<Double>(Arrays.asList(1.0))));
		m.put(204, new ADLMatcher(204, new ArrayList<Integer>(Arrays.asList(2)), new ArrayList<Double>(Arrays.asList(1.0))));
		m.put(205, new ADLMatcher(205, new ArrayList<Integer>(Arrays.asList(11,12)), new ArrayList<Double>(Arrays.asList(0.5,0.5))));
		m.put(206, new ADLMatcher(206, new ArrayList<Integer>(Arrays.asList(3)), new ArrayList<Double>(Arrays.asList(1.0))));
		m.put(207, new ADLMatcher(207, new ArrayList<Integer>(Arrays.asList(13)), new ArrayList<Double>(Arrays.asList(1.0))));
		m.put(208, new ADLMatcher(208, new ArrayList<Integer>(Arrays.asList(2)), new ArrayList<Double>(Arrays.asList(1.0))));
		m.put(209, new ADLMatcher(209, new ArrayList<Integer>(Arrays.asList(5)), new ArrayList<Double>(Arrays.asList(1.0))));
		m.put(210, new ADLMatcher(210, new ArrayList<Integer>(Arrays.asList(6)), new ArrayList<Double>(Arrays.asList(1.0))));
		m.put(210, new ADLMatcher(211, new ArrayList<Integer>(Arrays.asList(6)), new ArrayList<Double>(Arrays.asList(1.0))));
		
		m.put(300, new ADLMatcher(300, new ArrayList<Integer>(Arrays.asList(15,16)), new ArrayList<Double>(Arrays.asList(0.5,0.5))));
		m.put(301, new ADLMatcher(301, new ArrayList<Integer>(Arrays.asList(2)), new ArrayList<Double>(Arrays.asList(1.0))));
		m.put(302, new ADLMatcher(302, new ArrayList<Integer>(Arrays.asList(9)), new ArrayList<Double>(Arrays.asList(1.0))));
		m.put(303, new ADLMatcher(303, new ArrayList<Integer>(Arrays.asList(11,12)), new ArrayList<Double>(Arrays.asList(0.5,0.5))));
		
		return m;
	}
}
