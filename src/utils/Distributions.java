package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Distributions {

	public static void loadDistributions (String fileP, String fileQ) {
		ArrayList <Float []> P 	= loadAL(fileP);
		ArrayList <Float []> Q 	= loadAL(fileQ);

		Float [] divergenceKL 	= new Float [P.size()];
		Float [] distanceBC 	= new Float [P.size()];

		for (int i=0; i < Math.min(P.size(), Q.size()); i++) {			
			divergenceKL[i] 	= kullbackLeibler	(P.get(i),	Q.get(i));
			distanceBC	[i] 	= bhattacharyya	(P.get(i),	Q.get(i));
		}
		printDistribution(divergenceKL, "data/distributions/KL.txt");
		printDistribution(distanceBC, 	"data/distributions/BC.txt");

	}


	private static void printDistribution(Float[] distribution, String outputFile) {
		PrintWriter out;
		try {
			out = new PrintWriter(new FileWriter(outputFile));
			for (int i=0; i<distribution.length; i++) {
				out.print((float)distribution[i]+" ");						
			}
			out.close();
		} catch (IOException e) {e.printStackTrace();}		
		System.out.println("END printing: "+outputFile);
	}


	public static ArrayList<Float[]> loadAL (String nameFile) {
		BufferedReader reader 	= null;
		Float [] temp			= new Float [1440];
		String[] splited;
		ArrayList<Float[]> Distribution = new ArrayList<Float[]> ();

		try {
			reader = new BufferedReader(new FileReader(nameFile));
			String line = null;
			while ((line = reader.readLine()) != null) {
				splited = line.split("\\s+");

				for (int i=0; i<splited.length; i++) {
					temp[i] = Float.parseFloat(splited[i]);
				}
				Distribution.add(temp.clone());			    
			}
		} catch (Exception e) {e.printStackTrace();} finally {
			try {reader.close();} catch (IOException e) {e.printStackTrace();}
		}		

		return Distribution;		
	}

	public static float kullbackLeibler (Float[] P, Float[] Q){
		if (P.length == Q.length) {
			float divergence = 0;
			//Somm(Pi*ln(Pi/Qi))
			for (int i=0; i<P.length; i++) {
				if ((P[i]>0)&&(Q[i]>0))
					divergence += P[i] * Math.log((P[i]/Q[i]));				
			}
			return divergence;
		} else {
			System.out.println("Distribution with different lengths");
			return 0;
		}
	}

	public static float bhattacharyya (Float [] P, Float [] Q){
		if (P.length == Q.length) {
			float distance = 0;
			float Ps=0, Qs=0, den=0, num=0;
			
			for (int i=0; i<P.length; i++) {
				num += (float) Math.sqrt(P[i]*Q[i]);
				Ps += P[i];
				Qs += P[i];
			}
			den = (float) Math.sqrt(Ps*Qs);						
			distance = (float) Math.sqrt(1-(num/den));		
			
			return distance;
		} else {
			System.out.println("Distribution with different lengths");
			return 0;			
		}
	}
}
