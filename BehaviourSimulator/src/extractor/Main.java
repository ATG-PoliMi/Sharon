package extractor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class Main {

	static ArrayList <ADL> adl= new ArrayList<ADL>();
	static ADL badl = new ADL (0, "Foo", new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7)), 0, 0, 0, 0, 0, 0, 1);
	static int day, minute;
	static int usedTime;
	static int positionBadl;
	static Day d;
	

	public static void main (String[] args) {
		
		System.out.println("Simulation start!");
		addADL();	//Adds some ADLs examples

	    RandomGaussian gaussian = new RandomGaussian();

		for (day=0; day>-1; day++) {
			newDay();
			dayInitADL();
			for (minute=0; minute <=1440; minute ++) {
				minute += minute == 0 ? wake() : 0; 
				computeADLValue(minute*60);
				badl.setRank(-1000);
				for (ADL a:adl) {
					
					if ((a.getDoneToday()==0) && 
							(a.getRank() > badl.getRank()) &&
							(Day.getInstance().getWeather() >= a.getWeather()) &&
							(a.getDays().contains(Day.getInstance().getWeekDay())))
							{						
						badl=a;
						positionBadl = adl.indexOf(a);
					}
				}
				adl.get(positionBadl).setDoneToday(1);
				usedTime = (int) gaussian.getGaussian (badl.getTmean(), badl.getTvariability());
				System.out.println ("ADL: "+ badl.getName() +" with rank "+ badl.getRank() + " day hour: " +(int) minute/60 +" time: "+ (int)usedTime/60);
				
				minute+= (int) usedTime/60;

			}			
			System.out.println("ENDDAY!");
			try {
				System.in.read();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	private static void newDay() {
		Day.getInstance().nextDay();
		System.out.print("Today is "+ Day.getInstance().getWeekDay() + " and the weather is ");
		switch (Day.getInstance().getWeather()) {
			case 1: System.out.print("Rainy\n"); break;
			case 3: System.out.print("Sunny\n"); break;
			default: System.out.print("Error\n");
		}
	}


	private static int wake() {
		RandomGaussian gaussian = new RandomGaussian();
		return (int) gaussian.getGaussian (7*60, 60);
	}


	private static void dayInitADL() {
		for (ADL a : adl)  {
			a.setDoneToday(0); //In this way I avoid duplication of activities
		}
	}


	private static void computeADLValue(int minute) {

		for (ADL a : adl) {
			a.setRank((double) (a.getCyclicality() + 
					a.isMandatory() + 
					1.0 + 
					(1- (Math.abs(minute - a.getBestTime()))/100)));
		}

	}


	private static void addADL() {
		ArrayList <Integer> days = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7));
		ArrayList <Integer> marketDays = new ArrayList<Integer>(Arrays.asList(4,6));

		//ID, NAME, DAYS, WEATHER, TMEAN, TVARIABILITY, MANDATORY, BESTTIME, TYPE, CYCLICALITY
		//Morning
		adl.add(new ADL (101, "Breakfast", days, 0, 10*60, 5*60, 1, 8*3600, 0, 1));
		adl.add(new ADL (102, "MorningSitcom1", days, 0, 60*60, 10*60, 1, 9*3600, 0, 1));
		adl.add(new ADL (103, "MorningSitcom2", days, 0, 30*60, 10*60, 1, 10*3600, 0, 1));
		adl.add(new ADL (104, "Shopping", days, 0, 120*60, 60*60, 1, 9*3600, 0, 1));
		adl.add(new ADL (105, "Garden", days, 3, 180*60, 60*60, 1, 10*3600, 0, 1));
		adl.add(new ADL (106, "Shower", days, 0, 20*60, 10*60, 1, 11*3600, 0, 1));
		adl.add(new ADL (107, "TakeAWalk", days, 3, 60*60, 15*60, 1, 10*3600, 0, 1));
		adl.add(new ADL (108, "MARKET", marketDays, 3, 60*60, 15*60, 1, 9*3600, 0, 1));
		

		adl.add(new ADL (100, "Lunch", days, 0, 40*60, 10*60, 1, 12*3600, 0, 1));
		//Afternoon		

		adl.add(new ADL (201, "Sitcom1", days, 0, 60*60, 10*60, 0, 16*3600, 0, 1));
		adl.add(new ADL (202, "Sitcom2", days, 0, 60*60, 10*60, 0, 14*3600, 0, 1));
		adl.add(new ADL (203, "Tea", days, 0, 20*60, 5*60, 1, 17*3600, 0, 1));
		adl.add(new ADL (204, "Garden", days, 3, 120*60, 60*60, 1, 16*3600, 0, 1));
		adl.add(new ADL (205, "ReadBook", days, 0, 30*60, 20*60, 1, 15*3600, 0, 1));
		adl.add(new ADL (206, "Shower", days, 0, 20*60, 10*60, 1, 17*3600, 0, 1));
		adl.add(new ADL (207, "WashingMachine", days, 1, 10*60, 5*60, 1, 15*3600, 0, 1));
		adl.add(new ADL (208, "TakeAWalk", days, 3, 60*60, 15*60, 1, 16*3600, 0, 1));
		
		adl.add(new ADL (200, "Dinner", days, 0, 50*60, 10*60, 1, 19*3600, 0, 1));
		//Evening

		adl.add(new ADL (301, "Cinema", days, 0, 180*60, 60*60, 1, 20*3600, 0, 1));
		adl.add(new ADL (302, "EveningTV", days, 0, 180*60, 30*60, 1, 21*3600, 0, 1));
		adl.add(new ADL (303, "ReadBook", days, 0, 180*60, 60*60, 1, 20*3600, 0, 1));

		adl.add(new ADL (300, "Sleep", days, 0, 480*60, 120*60, 1, 21*3600, 0, 1));

	}

}
