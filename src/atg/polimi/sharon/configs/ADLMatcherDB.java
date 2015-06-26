package atg.polimi.sharon.configs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import atg.polimi.sharon.engine.ADLMatcher;


public class ADLMatcherDB {

	public static Map<Integer, ADLMatcher> addADLMatch() {
		
		Map<Integer, ADLMatcher> m = new HashMap<>();

		m.put(1, new ADLMatcher(1, new ArrayList<Integer>(Arrays.asList(1,2)), new ArrayList<Double>(Arrays.asList(0.5,0.5))));
		m.put(2, new ADLMatcher(2, new ArrayList<Integer>(Arrays.asList(3,4)), new ArrayList<Double>(Arrays.asList(0.5,0.5))));
		m.put(3, new ADLMatcher(3, new ArrayList<Integer>(Arrays.asList(5,6)), new ArrayList<Double>(Arrays.asList(0.5,0.5))));
		m.put(4, new ADLMatcher(4, new ArrayList<Integer>(Arrays.asList(7,8)), new ArrayList<Double>(Arrays.asList(0.5,0.5))));
		m.put(5, new ADLMatcher(5, new ArrayList<Integer>(Arrays.asList(9,10)), new ArrayList<Double>(Arrays.asList(0.5,0.5))));
		
		m.put(6, new ADLMatcher(6, new ArrayList<Integer>(Arrays.asList(11,12)), new ArrayList<Double>(Arrays.asList(0.5,0.5))));
		m.put(7, new ADLMatcher(7, new ArrayList<Integer>(Arrays.asList(13,14)), new ArrayList<Double>(Arrays.asList(0.5,0.5))));
		m.put(8, new ADLMatcher(8, new ArrayList<Integer>(Arrays.asList(15,16)), new ArrayList<Double>(Arrays.asList(0.5,0.5))));
		m.put(9, new ADLMatcher(9, new ArrayList<Integer>(Arrays.asList(17,18)), new ArrayList<Double>(Arrays.asList(0.5,0.5))));
		m.put(10, new ADLMatcher(10, new ArrayList<Integer>(Arrays.asList(19,20)), new ArrayList<Double>(Arrays.asList(0.5,0.5))));
		
		m.put(11, new ADLMatcher(11, new ArrayList<Integer>(Arrays.asList(21,22)), new ArrayList<Double>(Arrays.asList(0.5,0.5))));
		m.put(12, new ADLMatcher(12, new ArrayList<Integer>(Arrays.asList(23,24)), new ArrayList<Double>(Arrays.asList(0.5,0.5))));
		m.put(13, new ADLMatcher(13, new ArrayList<Integer>(Arrays.asList(25,26)), new ArrayList<Double>(Arrays.asList(0.5,0.5))));
		m.put(14, new ADLMatcher(14, new ArrayList<Integer>(Arrays.asList(27,28)), new ArrayList<Double>(Arrays.asList(0.5,0.5))));
		m.put(15, new ADLMatcher(15, new ArrayList<Integer>(Arrays.asList(29,30)), new ArrayList<Double>(Arrays.asList(0.5,0.5))));
		
		m.put(16, new ADLMatcher(16, new ArrayList<Integer>(Arrays.asList(31,32)), new ArrayList<Double>(Arrays.asList(0.5,0.5))));
		m.put(17, new ADLMatcher(17, new ArrayList<Integer>(Arrays.asList(33,34)), new ArrayList<Double>(Arrays.asList(0.5,0.5))));
		
		return m;
	}
}