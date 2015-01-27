package behavior.simulator.tuning;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.media.jai.Histogram;


public class PseudoCount {
	static int R = 86400;
	static int C = 27;

	static BufferedReader reader = null;
	static float [][] ADLs = new float [R][C];
	static float [][] ADLSmoothing = new float [R][C];
	static float [][] ADLUnderSampling = new float [R/60][C];
	static float p, pCount;

	static int rowNumber = 0;
	static String[] splitted;



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
		readFile("data/ARAS/DAY_10.txt");

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

		//smoothing ();
		computationADLs(5);
		computationADLs(6);

		//computationADLs(2);
		//computationADLs(3);

		writeADLs("data/histResults.txt", 1);
		System.out.println("END!");
	}

	private static void readFile (String nameFile) {
		rowNumber=0;
		try {
			reader = new BufferedReader(new FileReader(nameFile));
			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					splitted = line.split("\\s+");					
					for (int i=0; i<splitted.length; i++) {
						if (i==20) { //i=20: person 1, i=21: person 2
							ADLs[rowNumber][Integer.parseInt(splitted[i])-1]++;
							Float.parseFloat(splitted[i]);							
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

	private static void readFileX (String nameFile) {
		rowNumber=0;
		int k=0,p=0;
		try {
			reader = new BufferedReader(new FileReader(nameFile));
			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					splitted = line.split("\\s+");					
					for (int i=0; i<splitted.length; i++) {						
						if (i==20) { //i=20: person 1, i=21: person 2
							if((int) Math.floor(rowNumber/120)+k < 1440) {
								ADLUnderSampling[(int) Math.floor(rowNumber/120)+k][Integer.parseInt(splitted[i])]++;
								System.out.print("A:");
								System.out.print((int) Math.floor(rowNumber/120)+k+" ");

								if ((i>60)&&(i<1440)) {
									ADLUnderSampling[(int) Math.floor((60+rowNumber)/120)+p][Integer.parseInt(splitted[i])]++;
									System.out.print("B:");
									System.out.println((int) Math.floor((60+rowNumber)/120)+p);
								}
							}
						}
						if (i%120==0)
							k++;
						if (i%180==0)
							p++;
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
	private static void smoothing() {
		for (int j=0; j<C; j++) {
			for (int i=0; i<R; i++) {
				p=0;		//Partial sum of elements			
				pCount=0;	//# of elements summed

				for (int k = (i>=60) ? i-60 : 0; (i+60<R) ? k<i+60 : k<R; k++){
					p += ADLs[i][j];
					pCount++;
				}
				ADLSmoothing[i][j] = p/(pCount * 30f);
				if (i>60) {
					//System.out.println();
				}
			}
		}		
	}

	private static void computationADLs(int operation) {
		switch (operation) {
		case 1:
			for (int i=0; i<R; i++) {		    	
				for (int j=0; j<C; j++) {	    		 
					ADLs[i][j] = ADLs[i][j] / 30.0f;
				}
			}
			break;
		case 2:
			for (int j=0; j<C; j++) {
				for (int i=0; i<R; i++) {
					ADLs[i][j] = (int) Math.ceil(ADLs[i][j] / 30.0f);					
				}
			}
			break;
		case 3: //Histogram (Unused)
			int [] array = new int[1440];
			double [] arrayD = new double [1440];

			for (int i=0; i<R; i++){
				array[i] 	= 	(ADLs[i][5]<=0) ? 1 	: (int) ADLs[i][5];
				arrayD[i] 	=	(ADLs[i][5]<=0) ? 1.0 	:  		ADLs[i][5];

			}
			Histogram h = new Histogram(array, arrayD, arrayD);
			System.out.println(h.getSmoothed(true, 1));
			break;
		case 4: //UnderSampling
			for (int i=0; i<R; i+=60) {
				for (int j=0; j<C; j++) {
					ADLUnderSampling[i/60][j] = ADLSmoothing[i][j];
				}
			}			
			for (int i=0; i<R/60; i++) {
				for (int j=0; j<C; j++) {
					ADLUnderSampling[i/60][j] /= 60.0f;
				}
			}
			break;
		case 5:
			int k,p;
			for (int j=0; j<C; j++) {
				k=0;
				p=0;
				for (int i=0; i<R; i++) {
					if ((i>0)&&(i%120==0))
						k++;
					if ((i>120)&&((60+i)%120==0))
						p++;
					if((int) Math.floor(i/120)+k < 1440) {
						ADLSmoothing[(int) Math.floor(i/120)+k][j]+=ADLs[i][j];
						//						if (i%20==0) {
						//							System.out.print(i+"A");
						//							System.out.print((int) Math.floor(i/120)+k+" ");
						//						}
						if (i>60) {
							ADLSmoothing[(int) Math.floor((60+i)/120)+p][j]+=ADLs[i][j];
							//							if (i%20==0) {
							//								System.out.print("B");
							//								System.out.println((int) Math.floor((60+i)/120)+p);
							//							}
						}
					}
				}
			}
			break;
		case 6: //Mean with 120 samples			

			for (int j=0; j<C; j++) {
				float max=0;
				for (int i=0; i<R/60; i++) {
					max = (ADLSmoothing[i][j]>max) ? ADLSmoothing[i][j] : max;  
				}
				System.out.println(max);
				//max /= 0.95;
				for (int i=0; i<R/60; i++) {
					ADLSmoothing[i][j] = (max>0) ? ADLSmoothing[i][j]/max : ADLSmoothing[i][j];
					ADLSmoothing[i][j] = (ADLSmoothing[i][j]+0.05f<=1.0f) ? ADLSmoothing[i][j]+0.05f : 1.0f;
				}
			}
			break;
		}
	}



	private static void writeADLs(String outputFile, int target) {
		PrintWriter out;
		switch(target) {
		case 1: //Print ADL 
			try {
				out = new PrintWriter(new FileWriter(outputFile));
				for (int i=0; i<R/60; i++) {		    	
					for (int j=0; j<C; j++) {	    		 
						out.print(ADLSmoothing[i][j]+"\t");	    		
					}
					out.println();
				}
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 2: //Print ADLSmoothing

			try {
				out = new PrintWriter(new FileWriter(outputFile));
				for (int i=0; i<R; i++) {		    	
					for (int j=0; j<C; j++) {	    		 
						out.print(ADLSmoothing[i][j]+"\t");	    		
					}
					out.println();
				}
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 3: //Print ADLSampled

			try {
				out = new PrintWriter(new FileWriter(outputFile));
				for (int i=0; i<R/60; i++) {		    	
					for (int j=0; j<C; j++) {	    		 
						out.print(ADLUnderSampling[i][j]+"\t");	    		
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
	}
}
