package behavior.simulator.xml;

import java.util.ArrayList;
import java.util.Arrays;

import behavior.simulator.planner.ADLMatcher;


public class ADLMatcherDB {
	

	public static ArrayList<ADLMatcher> addADLMatch() {
		
		ArrayList<ADLMatcher> m = new ArrayList<ADLMatcher>();
		
		m.add(new ADLMatcher(100, new ArrayList<Integer>(Arrays.asList(8)), new ArrayList<Double>(Arrays.asList(1.0))));
		m.add(new ADLMatcher(101, new ArrayList<Integer>(Arrays.asList(1)), new ArrayList<Double>(Arrays.asList(1.0))));
		m.add(new ADLMatcher(102, new ArrayList<Integer>(Arrays.asList(9)), new ArrayList<Double>(Arrays.asList(1.0))));
		m.add(new ADLMatcher(103, new ArrayList<Integer>(Arrays.asList(9)), new ArrayList<Double>(Arrays.asList(1.0))));
		m.add(new ADLMatcher(104, new ArrayList<Integer>(Arrays.asList(2)), new ArrayList<Double>(Arrays.asList(1.0))));
		m.add(new ADLMatcher(105, new ArrayList<Integer>(Arrays.asList(2)), new ArrayList<Double>(Arrays.asList(1.0))));
		m.add(new ADLMatcher(106, new ArrayList<Integer>(Arrays.asList(3)), new ArrayList<Double>(Arrays.asList(1.0))));
		m.add(new ADLMatcher(107, new ArrayList<Integer>(Arrays.asList(2)), new ArrayList<Double>(Arrays.asList(1.0))));
		m.add(new ADLMatcher(108, new ArrayList<Integer>(Arrays.asList(4)), new ArrayList<Double>(Arrays.asList(1.0))));
		m.add(new ADLMatcher(109, new ArrayList<Integer>(Arrays.asList(5)), new ArrayList<Double>(Arrays.asList(1.0))));
		m.add(new ADLMatcher(110, new ArrayList<Integer>(Arrays.asList(6)), new ArrayList<Double>(Arrays.asList(1.0))));
		m.add(new ADLMatcher(111, new ArrayList<Integer>(Arrays.asList(7)), new ArrayList<Double>(Arrays.asList(1.0))));
		
		m.add(new ADLMatcher(200, new ArrayList<Integer>(Arrays.asList(14)), new ArrayList<Double>(Arrays.asList(1.0))));
		m.add(new ADLMatcher(201, new ArrayList<Integer>(Arrays.asList(9)), new ArrayList<Double>(Arrays.asList(1.0))));
		m.add(new ADLMatcher(202, new ArrayList<Integer>(Arrays.asList(9)), new ArrayList<Double>(Arrays.asList(1.0))));
		m.add(new ADLMatcher(203, new ArrayList<Integer>(Arrays.asList(10)), new ArrayList<Double>(Arrays.asList(1.0))));
		m.add(new ADLMatcher(204, new ArrayList<Integer>(Arrays.asList(2)), new ArrayList<Double>(Arrays.asList(1.0))));
		m.add(new ADLMatcher(205, new ArrayList<Integer>(Arrays.asList(11,12)), new ArrayList<Double>(Arrays.asList(0.5,0.5))));
		m.add(new ADLMatcher(206, new ArrayList<Integer>(Arrays.asList(3)), new ArrayList<Double>(Arrays.asList(1.0))));
		m.add(new ADLMatcher(207, new ArrayList<Integer>(Arrays.asList(13)), new ArrayList<Double>(Arrays.asList(1.0))));
		m.add(new ADLMatcher(208, new ArrayList<Integer>(Arrays.asList(2)), new ArrayList<Double>(Arrays.asList(1.0))));
		m.add(new ADLMatcher(209, new ArrayList<Integer>(Arrays.asList(5)), new ArrayList<Double>(Arrays.asList(1.0))));
		m.add(new ADLMatcher(210, new ArrayList<Integer>(Arrays.asList(6)), new ArrayList<Double>(Arrays.asList(1.0))));
		
		m.add(new ADLMatcher(300, new ArrayList<Integer>(Arrays.asList(15,16)), new ArrayList<Double>(Arrays.asList(0.5,0.5))));
		m.add(new ADLMatcher(301, new ArrayList<Integer>(Arrays.asList(2)), new ArrayList<Double>(Arrays.asList(1.0))));
		m.add(new ADLMatcher(302, new ArrayList<Integer>(Arrays.asList(9)), new ArrayList<Double>(Arrays.asList(1.0))));
		m.add(new ADLMatcher(303, new ArrayList<Integer>(Arrays.asList(11,12)), new ArrayList<Double>(Arrays.asList(0.5,0.5))));
		
		
		return m;
	}

}
