package behavior.simulator.tuning;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;



public class PseudoCount {
	static BufferedReader reader = null;
	static float [][] ADLs = new float [1441][28];
	static int rowNumber = 0;
	static String[] splited;

	static int R = 1440;
	static int C = 27;

	public static void main (String[] args) {

		readFile("data/ARAS/DAY_1.txt");
		readFile("data/ARAS/DAY_2.txt");
		readFile("data/ARAS/DAY_3.txt");
		readFile("data/ARAS/DAY_4.txt");
		readFile("data/ARAS/DAY_5.txt");
		readFile("data/ARAS/DAY_6.txt");
		readFile("data/ARAS/DAY_7.txt");
		readFile("data/ARAS/DAY_8.txt");
		readFile("data/ARAS/DAY_9.txt");		
		readFile("data/ARAS/DAY_10.txt")
		;
		readFile("data/ARAS/DAY_11.txt");
		readFile("data/ARAS/DAY_12.txt");
		readFile("data/ARAS/DAY_13.txt");
		readFile("data/ARAS/DAY_14.txt");
		readFile("data/ARAS/DAY_15.txt");
		readFile("data/ARAS/DAY_16.txt");
		readFile("data/ARAS/DAY_17.txt");
		readFile("data/ARAS/DAY_18.txt");
		readFile("data/ARAS/DAY_19.txt");		
		readFile("data/ARAS/DAY_20.txt");

		readFile("data/ARAS/DAY_21.txt");
		readFile("data/ARAS/DAY_22.txt");
		readFile("data/ARAS/DAY_23.txt");
		readFile("data/ARAS/DAY_24.txt");
		readFile("data/ARAS/DAY_25.txt");
		readFile("data/ARAS/DAY_26.txt");
		readFile("data/ARAS/DAY_27.txt");
		readFile("data/ARAS/DAY_28.txt");
		readFile("data/ARAS/DAY_29.txt");
		readFile("data/ARAS/DAY_30.txt");

		computationADLs(1);

		writeADLs("data/histResults.txt");
		System.out.println("END!");
	}

	private static void computationADLs(int operation) {
		switch (operation) {
		case 1:
			for (int i=0; i<R; i++) {		    	
				for (int j=0; j<C; j++) {	    		 
					ADLs[i][j] = ADLs[i][j] / 30;
				}
			}
			break;
		case 2:
			for (int j=0; j<C; j++) {
				for (int i=0; i<R; i++) {
					
					ADLs[i][j] = ADLs[i][j] / 30;
				}
			}
		}


	}

	private static void readFile (String nameFile) {
		rowNumber=0;
		try {
			reader = new BufferedReader(new FileReader(nameFile));
			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					splited = line.split("\\s+");					
					for (int i=0; i<splited.length; i++) {
						if (i==21) { //i=20: person 1, i=21: person 2
							ADLs[(int) Math.floor(rowNumber/60)][Integer.parseInt(splited[i])]++;
							Float.parseFloat(splited[i]);							
						}
					}  
					rowNumber++;
				}
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
	}

	private static void writeADLs(String outputFile) {
		PrintWriter out;
		try {
			out = new PrintWriter(new FileWriter(outputFile));
			for (int i=0; i<R; i++) {		    	
				for (int j=0; j<C; j++) {	    		 
					out.print(ADLs[i][j]+"\t");	    		
				}
				out.println();
			}
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
