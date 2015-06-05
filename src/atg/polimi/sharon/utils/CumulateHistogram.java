package atg.polimi.sharon.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import atg.polimi.sharon.configs.Parameters;

public class CumulateHistogram {

	final static int timeGranularity = 1440; //HH: 24, MM: 1440
	static float[][] ADLsCount = new float[timeGranularity][Parameters.ADLCOUNT];

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
		float[][] temp = new float [timeGranularity][Parameters.ADLCOUNT];

		//copying ADLsCount into temp
		for (int j=0; j<Parameters.ADLCOUNT; j++) {
			for (int i=0; i< timeGranularity; i++) {
				temp [i][j] = ADLsCount[i][j];
			}
		}
		for (int j=0; j<Parameters.ADLCOUNT; j++) {
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
			for (int j=0; j<Parameters.ADLCOUNT; j++) {
				ADLsCount[i][j] /= mean;
			}
		}
	}

	public void normalizationTo1Histogram () {
		float [] sum = new float[Parameters.ADLCOUNT];
		for (int i=0; i<timeGranularity; i++) {
			for (int j=0; j<Parameters.ADLCOUNT; j++) {
				sum [j] += ADLsCount[i][j];
			}
		}   
		for (int i=0; i<timeGranularity; i++) {
			for (int j=0; j<Parameters.ADLCOUNT; j++) {
				ADLsCount[i][j] /= sum[j];
			}
		}
	}
	
	public void printHistogram() {
		for (int i=0; i<timeGranularity; i++) {
			for (int j=0; j<Parameters.ADLCOUNT; j++) {
				System.out.print(
						(float)ADLsCount[i][j]
								+"\t");
			}
			System.out.println();
		}
	}

	/**
	 * This method prints exactly ADL obtained
	 * TODO: correct the input ADL and then remove if clause
	 */
	public void printToFile (String outputFile, int target) {		
		PrintWriter out;
		switch(target) {
		case 1: //Print ADL Smoothing VERTICAL
			try {
				out = new PrintWriter(new FileWriter(outputFile));
				for (int i=0; i<timeGranularity; i++) {		    	
					for (int j=0; j<Parameters.ADLCOUNT; j++) {
						if ((j!=2)&&(j!=4)&&(j!=6)&&(j!=8)&&(j!=19)
								&&(j!=20)&&(j!=24)&&(j!=25)){
							out.print(ADLsCount[i][j]+"\t");	    		
						}
						out.println();
					}
				}
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case 2: //Print ADLSmoothing HORIZONTAL
			try {
				out = new PrintWriter(new FileWriter(outputFile));
				for (int j=0; j<Parameters.ADLCOUNT; j++) {
						for (int i=0; i<timeGranularity; i++) {
							out.print(ADLsCount[i][j]+" ");	    		
						}
						out.println();					
				}
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;		
		}
		System.out.println("END!");
	}
}