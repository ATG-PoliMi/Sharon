package utils;

import behavior.simulator.extractor.Day;

public class CumulateHistogram {
	int[][] ADLsCount = new int[24][Constants.ADLCOUNT];
	
	public CumulateHistogram(){
		
	}
	
	//days to check
	public void updateHistogram(double tick, int id) {
		Time t = new Time(tick % 86400);
		ADLsCount[t.getHour()][id-1]++;		
	}
	
	public void printHistogram() {
		for (int i=0; i<24; i++) {
			for (int j=0; j<Constants.ADLCOUNT; j++) {
				System.out.print(
						(int)Math.ceil(ADLsCount[i][j]/(1))
						//(int)Math.ceil(ADLsCount[i][j]/(100))
						//(float)ADLsCount[i][j]/(100)
						+"\t");
			}
			System.out.println();
		}
	}
}