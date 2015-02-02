package utils;

public class Distributions {

	public static float kullbackLeibler (float [] P, float [] Q){
		float divergence = 0;
		//Somm(Pi*ln(Pi/Qi))
		for (int i=0; i<P.length; i++) {
			divergence += P[i] * Math.log1p((P[i]/Q[i]));
		}
		return divergence;
	}
	
	public static float bhattacharyya (float [] P, float [] Q){
		float distance = 0;
		//-ln(Somm(sqrt(Pi*Qi))
		for (int i=0; i<P.length; i++) {
			distance += Math.sqrt(P[i]*Q[i]);
		}
		distance = (float) -Math.log1p(distance);
		return distance;
	}
}
