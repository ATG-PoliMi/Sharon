package it.polimi.deib.atg.sharon.utils;

import java.io.BufferedReader;
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
		Float [] distanceEM 	= new Float [P.size()];

		for (int i=0; i < Math.min(P.size(), Q.size()); i++) {			
			divergenceKL[i] 	= kullbackLeibler		(P.get(i),	Q.get(i));
			distanceBC	[i] 	= bhattacharyya			(P.get(i),	Q.get(i));
			distanceEM	[i] 	= earthMoverDistance	(P.get(i),	Q.get(i));
		}
		printDistribution(divergenceKL, "data/distributions/KL.txt");
		printDistribution(distanceBC, 	"data/distributions/BC.txt");
		printDistribution(distanceEM, 	"data/distributions/EM.txt");

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
		float p,q;
		if (P.length == Q.length) {
			float divergence = 0;
			//Somm(Pi*ln(Pi/Qi))
			for (int i=0; i<P.length; i++) {
				p = (float) Math.max(P[i], 0.00000001);
				q = (float) Math.max(Q[i], 0.00000001);
					divergence += p * Math.log(p/q);				
			}
			return divergence;
		} else {
			System.out.println("Distribution with different lengths");
			return 0;
		}
	}
	public static float bhattacharyya (Float [] P, Float [] Q){
		if (P.length == Q.length) {
			float distance, Ps=0, Qs=0, den=0, num=0;
			
			for (int i=0; i<P.length; i++) {
				num += (float) Math.sqrt(P[i]*Q[i]);
				Ps += P[i];
				Qs += Q[i];
			}
			den = (float) Math.sqrt(Ps*Qs);
			distance = (den == 0) ? 0 : (float) Math.sqrt(1-(num/den));		
			return distance;
		} else {
			System.out.println("Distribution with different lengths");
			return 0;			
		}
	}
	public static float earthMoverDistance (Float [] P,Float [] Q){
		if (P.length == Q.length) {
			Float [] D = new Float[P.length];
			float distance = 0;
			for (int i=0; i<P.length; i++) {
				if (i>0)
					D[i] = D[i-1] + P[i] - Q[i];
				else
					D[i] = 0 + P[i] - Q[i];
			}
			for (int i=0; i<P.length; i++) {
				distance += Math.max(-D[i], D[i]);
			}			
			return distance;
		} else {
			System.out.println("Distribution with different lengths");
			return 0;			
		}
		
	}
	//EMD0 = 0
	//EMDi+1 = ( Ai + EMDi ) - Bi
	//TotalDistance = S | EMDi |

}