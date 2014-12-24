package behavior.simulator.extractor;

import java.util.Random;

public final class RandomGaussian {
	private Random fRandom = new Random();

	double getGaussian(double aMean, double aVariance){
		return aMean + fRandom.nextGaussian() * aVariance;
	}

	private static void log(Object aMsg){
		System.out.println(String.valueOf(aMsg));
	}
} 