package sharon.data;

import java.util.ArrayList;

public class Targets {
	ArrayList <Integer> LLADLTicks;
	
	public Targets() {
		
	}
	
	public Targets (ArrayList<Integer> LLADLTicks) {
		this.LLADLTicks = LLADLTicks;
	}

	public ArrayList<Integer> getLLADLTicks() {
		return LLADLTicks;
	}

	public void setLLADLTicks(ArrayList<Integer> lLADLTicks) {
		LLADLTicks = lLADLTicks;
	} 

}
