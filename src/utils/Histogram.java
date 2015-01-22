package utils;

public class Histogram {
	int[][] ADLsCount = new int[24][Constants.ADLCOUNT];
	
	public Histogram(){
		
	}
	
	//days to check
	public void updateHistogram(double tick, int id) {
		Time t = new Time(tick % 86400);
		ADLsCount[t.getHour()][id]++;		
	}
	
	public void printHistogram() {
		for (int i=0; i<24; i++) {
			for (int j=0; j<Constants.ADLCOUNT; j++) {
				System.out.print(ADLsCount[i][j]+"\t");
			}
			System.out.println();
		}
	}

}
