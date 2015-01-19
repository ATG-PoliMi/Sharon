package behavior.simulator.tuning;

import org.apache.commons.math3.distribution.BetaDistribution;

import behavior.simulator.extractor.ADL;

/***
 * 
 * @author Daniele Proserpio
 * The aim of this class is to provide a custom description of an ADL. 
 * Each ADL is described, over the day, with a combination of Beta distribution.
 * 
 * timeDependence(double currentMinute, double betaA, double betaB, 
			double centralTime, double rangeTime );
 *
 */
public class ComputeADLTimeDescription {

	public static double description [] = new double [1440];

	public static void main(String[] args) {
		for (int i=0; i<description.length;i++) {
			if (i==700) {
				System.out.println("");		
			}
			description[i]=0.1;
			description [i] += timeDependence(i, 2.0, 1.0, 12*60, 60);
			//description [i] += timeDependence(i, 1.0, 1.0, 12*60, 60);
			description [i] += timeDependence(i, 2.0, 1.0, 9*60, 60);
			description [i] += timeDependence(i, 2.0, 1.0, 15*60, 60);
			description [i] += timeDependence(i, 2.0, 1.0, 19*60, 60);
			
			
			System.out.print(description [i]+", ");
			description [i] = description [i] > 1.0 ? 1.0 : description [i]; //Saturation to 1.0
			
		}
		System.out.println("");
		for (int i=0; i< description.length; i++) {
				System.out.print(i+": ");
				for (int j=0; j< (description[i]*10); j++) {
					System.out.print("*");
				}
				System.out.println();
			}
	}

	private static double timeDependence(double currentMinute, double betaA, double betaB, 
			double centralTime, double rangeTime ) {

		BetaDistribution b = new BetaDistribution(betaA, betaB);		
		double test = b.density((0.5 + (((double)currentMinute - centralTime) / (rangeTime)))); 
		return test;
	}
}
