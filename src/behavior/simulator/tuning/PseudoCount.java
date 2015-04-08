package behavior.simulator.tuning;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class PseudoCount {
	//***PARAMETERS:***
	static String House 	="HouseA"; 	//Options: "HouseA" || "HouseB"

	static int 	person 		= 20;		//Options: 20: person 1, 21: person 2
	static int 	format 		= 3;		//Print layout, options: 1: vertical, 2: horizontal, 3: horizontal skipped
	static int 	print 		= 2;		//Array to print, options: 1: ADLSmoothing, 2: ADLNotSmoothed, 3:ADLNormalizedTo1
	static float days		= 7.0f;	//30: full dataset, 23: test, 7: validation

	static String oFile		= "data/InputTD_light.txt";	
	//InputTD.txt 			: system input; 
	//InputTD_light.txt 	: system input without unused;
	//data/t/norm1_7d.txt 	: test 7days
	//data/t/norm1_23d.txt	: test 23days
	//***END PARAMETERS***

	static int R = 86400;
	static int C = 27;

	static BufferedReader reader = null;
	static float [][] ADLs 				= new float [R][C];
	static float [][] ADLSmoothing 		= new float [R][C];
	static float [][] ADLNotSmoothed 	= new float [R/60][C];
	static float [][] ADLNormalizedTo1 	= new float [R/60][C];
	static float p, pCount;

	static int rowNumber = 0;
	static String[] splitted;

	public static void main (String[] args) {

//				readFile("data/ARAS/"+House+"/DAY_1.txt");
//				readFile("data/ARAS/"+House+"/DAY_2.txt");
//				readFile("data/ARAS/"+House+"/DAY_3.txt");
//				readFile("data/ARAS/"+House+"/DAY_4.txt");
//				readFile("data/ARAS/"+House+"/DAY_5.txt");
//				readFile("data/ARAS/"+House+"/DAY_6.txt");
//				readFile("data/ARAS/"+House+"/DAY_7.txt");
//				readFile("data/ARAS/"+House+"/DAY_8.txt");
//				readFile("data/ARAS/"+House+"/DAY_9.txt");		
//				readFile("data/ARAS/"+House+"/DAY_10.txt");
//		
//				readFile("data/ARAS/"+House+"/DAY_11.txt");
//				readFile("data/ARAS/"+House+"/DAY_12.txt");
//				readFile("data/ARAS/"+House+"/DAY_13.txt");
//				readFile("data/ARAS/"+House+"/DAY_14.txt");
//				readFile("data/ARAS/"+House+"/DAY_15.txt");
//				readFile("data/ARAS/"+House+"/DAY_16.txt");
//				readFile("data/ARAS/"+House+"/DAY_17.txt");
//				readFile("data/ARAS/"+House+"/DAY_18.txt");
//				readFile("data/ARAS/"+House+"/DAY_19.txt");		
//				readFile("data/ARAS/"+House+"/DAY_20.txt");
//		
//				readFile("data/ARAS/"+House+"/DAY_21.txt");
//				readFile("data/ARAS/"+House+"/DAY_22.txt");
//				readFile("data/ARAS/"+House+"/DAY_23.txt");

//		readFile("data/ARAS/"+House+"/DAY_24.txt");
//		readFile("data/ARAS/"+House+"/DAY_25.txt");
//		readFile("data/ARAS/"+House+"/DAY_26.txt");
//		readFile("data/ARAS/"+House+"/DAY_27.txt");
//		readFile("data/ARAS/"+House+"/DAY_28.txt");
//		readFile("data/ARAS/"+House+"/DAY_29.txt");
//		readFile("data/ARAS/"+House+"/DAY_30.txt");

		computationADLs(1); //Merge of unused ADLs

		//ADLNotSmoothed
		computationADLs(2);	//ADLNotSmoothed: reduce dimensionality and normalization to 30

		//The sum of all the elements of each ADL is 1.0
		computationADLs(3); //not smoothed
		//computationADLs(8); //smoothed

		//ADLSmoothed
		computationADLs(4);	//ADLSmoothed: reduce dimensionality with overlap (60+60)
		computationADLs(5);	//Smoothing

		//computationADLs(6);	//Normalization to 1 and minimum value 0.05
		computationADLs(7);		//Normalization (120*30)


		writeADLs(oFile, format, print);
		//writeADLs("data/histResults.txt", format, print);
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
						if (i==person) { 
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

	private static void computationADLs(int operation) {
		switch (operation) {

		case 1: //Merge of ADLs
			for (int i=0; i<R; i++) {
				for (int j=0; j<C; j++) {
					ADLs [i][3] += ADLs[i][2];
					ADLs[i][2] = 0;

					ADLs [i][5] += ADLs[i][4];
					ADLs[i][4] = 0;

					ADLs [i][7] += ADLs[i][6];
					ADLs[i][6] = 0;

					ADLs [i][13] += ADLs[i][8] + 
							ADLs [i][19] + ADLs [i][20];
					ADLs[i][8] = 0;
					ADLs[i][19] = 0;
					ADLs[i][20] = 0;

					ADLs [i][21] += ADLs [i][24] + 
							ADLs [i][25];
					ADLs[i][24] = 0;
					ADLs[i][25] = 0;					
				}
			}
			break;

		case 2: //UnderSampling into ADLNot Smoothed
			for (int i=0; i<R; i+=60) {
				for (int j=0; j<C; j++) {
					ADLNotSmoothed[i/60][j] = ADLs[i][j];
				}
			}			
			for (int i=0; i<R/60; i++) {
				for (int j=0; j<C; j++) {
					ADLNotSmoothed[i][j] /= days;		//# of days			
				}
			}
			break;

		case 3: //Normalization to 1 as MAX Value
			float [] sum= new float[C];
			for (int i=0; i<R; i+=60) {
				for (int j=0; j<C; j++) {
					ADLNormalizedTo1[i/60][j] = ADLs[i][j];
					sum[j] += ADLs[i][j];
				}				
			}			
			for (int i=0; i<R/60; i++) {
				for (int j=0; j<C; j++) {
					if (sum[j] > 0){
						ADLNormalizedTo1[i][j] /= sum[j];		//The sum of all the elements of the histogram is 1.0
					}
				}
			}
			break;	

		case 4: //Reduce dimensionality with overlap (60+60)
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
						ADLSmoothing[(int) Math.floor(i/120)+k][j] += ADLs[i][j];
						if (i>60) {
							ADLSmoothing[(int) Math.floor((60+i)/120)+p][j] += ADLs[i][j];							
						}
					}
				}
			}
			break;

		case 5: //Smoothing		
			float[][] temp = new float [R][C];

			for (int j=0; j<C; j++) {
				for (int i=0; i< R/60; i++) {
					temp [i][j] = ADLSmoothing[i][j];
				}
			}

			for (int j=0; j<C; j++) {
				for (int i=0; i< R/60; i++) {
					ADLSmoothing[i][j] = 0;
					for (int w = Math.max(i-8, 0); ((i+8)<(R/16)) ? w<i+8 : w<R/16-1; w++) {
						ADLSmoothing[i][j] += (float) temp [w][j];
					}
					ADLSmoothing[i][j] /= 16.0f;
				}
			}
			break;

		case 6: //Normalization to 1 and minimum value 0.05 {unused}
			for (int j=0; j<C; j++) {
				float max=0;
				for (int i=0; i<R/60; i++) {
					max = Math.max(ADLSmoothing[i][j], max);  
				}

				for (int i=0; i<R/60; i++) {
					ADLSmoothing[i][j] = (max>0) ? ADLSmoothing[i][j]/max : ADLSmoothing[i][j];
					ADLSmoothing[i][j] = Math.min(ADLSmoothing[i][j] + 0.05f, 1.0f); 				//Adds a minimum value for each ADL
				}
			}
			break;

		case 7: //Normalization (120*30)
			for (int i=0; i<R/60; i++){
				for (int j=0; j<C; j++) {
					ADLSmoothing[i][j] /= 120.0f * days;
				}
			}
			break;
		case 8: //Normalization to 1 as MAX Value
			float [] sum1= new float[C];
			int k1,p1;
			for (int j=0; j<C; j++) {
				k1=0;
				p1=0;
				for (int i=0; i<R; i++) {
					if ((i>0)&&(i%120==0))
						k1++;
					if ((i>120)&&((60+i)%120==0))
						p1++;
					if((int) Math.floor(i/120)+k1 < 1440) {
						ADLNormalizedTo1[(int) Math.floor(i/120)+k1][j]+=ADLs[i][j];
						if (i>60) {
							ADLNormalizedTo1[(int) Math.floor((60+i)/120)+p1][j]+=ADLs[i][j];							
						}
					}
				}
			}	
			for (int i=0; i<R/60; i++) {
				for (int j=0; j<C; j++) {
					ADLNormalizedTo1[i][j] /= sum1[j];		//The sum of all the elements of the histogram is 1.0 			
				}
			}
			break;
		}
	}


	private static void writeADLs(String outputFile, int target, int printing) {
		PrintWriter out;
		switch(target) {
		case 1: //Print ADL Smoothing VERTICAL skipped
			try {
				out = new PrintWriter(new FileWriter(outputFile));
				for (int i=0; i<R/60; i++) {		    	
					for (int j=0; j<C; j++) {
						if ((j!=2)&&(j!=4)&&(j!=6)&&(j!=8)&&(j!=19)
								&&(j!=20)&&(j!=24)&&(j!=25)){
							if (printing == 1)
								out.print(ADLSmoothing[i][j]+"\t");
							else if (printing == 2)
								out.print(ADLNotSmoothed[i][j]+"\t");
							else
								out.print(ADLNormalizedTo1[i][j]+"\t");
						}
					}
					out.println();
				}
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case 2: //Print ADLS HORIZONTAL
			try {
				out = new PrintWriter(new FileWriter(outputFile));
				for (int j=0; j<C; j++) {
					for (int i=0; i<R/60; i++) {
						if (printing == 1)
							out.print(ADLSmoothing[i][j]+	"\t");
						else if (printing == 2)
							out.print(ADLNotSmoothed[i][j]+	"\t");
						else {
							out.print(ADLNormalizedTo1[i][j]+"\t");
						}
					}
					out.println();


				}

				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;		
		case 3: //Print ADLS HORIZONTAL skipped
			try {
				out = new PrintWriter(new FileWriter(outputFile));
				for (int j=0; j<C; j++) {
					if ((j!=2)&&(j!=4)&&(j!=6)&&(j!=8)&&(j!=19)
							&&(j!=20)&&(j!=24)&&(j!=25)&&
							(j!=12)&&(j!=26)){
						for (int i=0; i<R/60; i++) {
							if (printing == 1)
								out.print(ADLSmoothing[i][j]+	"\t");
							else if (printing == 2)
								out.print(ADLNotSmoothed[i][j]+	"\t");
							else {
								out.print(ADLNormalizedTo1[i][j]+"\t");
							}
						}
						out.println();
					}
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
