package behavior.simulator.tuning;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;



public class PseudoCount {
	public static void main (String[] args) {
	    BufferedReader reader = null;
	    float [][] ADLs = new float [1441][28];
	    int rowNumber = 0;
	    String[] splited;
	    
	    try {
	        reader = new BufferedReader(new FileReader("data/DAY_1.txt"));
	        String line = null;
	        try {
				while ((line = reader.readLine()) != null) {
					
					splited = line.split("\\s+");					
					for (int i=0; i<splited.length; i++) {
						if (i==20) {
							ADLs[(int) Math.floor(rowNumber/60)][Integer.parseInt(splited[i])]++;
							Float.parseFloat(splited[i]);							
						}
					}  
					rowNumber++;
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
	        try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    for (int i=0; i<1440; i++) {
	    	for (int j=0; j<27; j++) {
	    		System.out.print(ADLs[i][j]+"\t");
	    	}
	    	System.out.println();
	    }		
	}
}
