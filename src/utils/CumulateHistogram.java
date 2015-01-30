package utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import behavior.simulator.extractor.Day;

public class CumulateHistogram {
	
	final static int timeGranularity = 1440; //HH: 24, MM: 1440
	static float[][] ADLsCount = new float[timeGranularity][Constants.ADLCOUNT];
	
	public CumulateHistogram(){
		
	}
	
	//days to check
	public void updateHistogramH(double tick, int id) {
		Time t = new Time(tick % 86400);
		ADLsCount[t.getHour()][id-1]++;		
	}
	
	public void updateHistogramM(double tick, int id) {
		int t = (int) (tick % 86400);
		ADLsCount[(int)t/60][id-1]++;		
	}
	
	public void old_refineHistogram () {
		float[][] temp = new float [timeGranularity][Constants.ADLCOUNT];
		
		//copying ADLsCount into temp
		for (int j=0; j<Constants.ADLCOUNT; j++) {
			for (int i=0; i< timeGranularity; i++) {
				temp [i][j] = ADLsCount[i][j];
			}
		}
		/*	
		for (int j=0; j<Constants.ADLCOUNT; j++) {
			for (int i=0; i< timeGranularity; i++) {
				ADLsCount[i][j] = 0;
				for (int w = Math.max(i-30, 0); ((i+30)<(timeGranularity)) ? w<i+30 : w<timeGranularity; w++) {
					ADLsCount[i][j] += (float) temp [w][j];
				}
				ADLsCount[i][j] /= 60.0f;
			}
		}
		*/
		for (int j=0; j<Constants.ADLCOUNT; j++) {
			float max=0;
			for (int i=0; i<timeGranularity; i++) {
				max = Math.max(ADLsCount[i][j], max);  
			}

			for (int i=0; i<timeGranularity; i++) {
				ADLsCount[i][j] = (max>0) ? ADLsCount[i][j]/max : ADLsCount[i][j];
				ADLsCount[i][j] = (ADLsCount[i][j]+0.05f<=1.0f) ? ADLsCount[i][j]+0.05f : 1.0f;
			}
		}
	}
	
	public void refineHistogram (float mean) {
		for (int i=0; i<timeGranularity; i++) {
			for (int j=0; j<Constants.ADLCOUNT; j++) {
				ADLsCount[i][j] /= mean;
			}
		}
	}
	public void printHistogram() {
		for (int i=0; i<timeGranularity; i++) {
			for (int j=0; j<Constants.ADLCOUNT; j++) {
				System.out.print(
						//(int)Math.ceil(ADLsCount[i][j]/(1))
						//(int)Math.ceil(ADLsCount[i][j]/(100))
						(float)ADLsCount[i][j]
						+"\t");
			}
			System.out.println();
		}
	}
	
	public void printToFile (String outputFile, int target) {		
			PrintWriter out;
			switch(target) {
			case 1: //Print ADL Smoothing VERTICAL
				try {
					out = new PrintWriter(new FileWriter(outputFile));
					for (int i=0; i<timeGranularity; i++) {		    	
						for (int j=0; j<Constants.ADLCOUNT; j++) {	    		 
							out.print(ADLsCount[i][j]+"\t");	    		
						}
						out.println();
					}
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 2: //Print ADLSmoothing HORIZONTAL
				try {
					out = new PrintWriter(new FileWriter(outputFile));
					for (int j=0; j<Constants.ADLCOUNT; j++) {
						for (int i=0; i<timeGranularity; i++) {
							out.print(ADLsCount[i][j]+" ");	    		
						}
						out.println();
					}
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;		
			}
			System.out.println("END!");
	}
}