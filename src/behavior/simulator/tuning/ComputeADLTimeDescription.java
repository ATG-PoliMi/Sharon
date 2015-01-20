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
			//description[i] = ((i<420)||(i>1310)) ? 1.0 : 0.0;
			description[i] = ((i>460)&&(i<1300)) ? 0.0 : 0.00; //Only day
			//description [i] += timeDependence(i, 2.0, 2.0, 15*60, 60)/3;
			//description [i] += timeDependence(i, 2.0, 2.0, 16*60, 90)/3;
			//description [i] += timeDependence(i, 2.0, 2.0, 14.5*60, 30)/2;
			//description [i] += timeDependence(i, 2.0, 2.0, 11*60, 60)/2;
			//description [i] += timeDependence(i, 2.0, 2.0, 9*60, 90)/2;			
			//description [i] += timeDependence(i, 2.0, 2.0, 15*60, 90)/2;
			//description [i] += timeDependence(i, 2.0, 2.0, 21*60, 90)/2;
			//description [i] += timeDependence(i, 1.0, 1.0, 16*60, 180)/3;
			//description [i] += timeDependence(i, 2.0, 2.0, 15*60, 120)/2;
			//description [i] += timeDependence(i, 1.0, 1.0, 12*60, 60);
			//description [i] += timeDependence(i, 2.0, 1.0, 9*60, 60);
			//description [i] += timeDependence(i, 2.0, 1.0, 15*60, 60);
			//description [i] += timeDependence(i, 2.0, 1.0, 19*60, 60);
			
			
			
			description [i] = description [i] > 1.0 ? 1.0 : description [i]; //Saturation to 1.0
			System.out.print(description [i]+" ");
			
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
