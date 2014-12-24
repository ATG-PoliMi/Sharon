package behavior.simulator.planner;

import java.util.ArrayList;

public class ADLMatcher {
	private int HLadl;
	private ArrayList<Integer> LLadl = new ArrayList<Integer>();
	private ArrayList<Double> LLadlProbability = new ArrayList<Double>();
	
	
	public ADLMatcher(int hLadl, ArrayList<Integer> lLadl,
			ArrayList<Double> lLadlProbability) {
		super();
		HLadl = hLadl;
		LLadl = lLadl;
		LLadlProbability = lLadlProbability;
	}
}
