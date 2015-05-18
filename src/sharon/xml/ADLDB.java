package sharon.xml;

import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import sharon.engine.ADL;
import sharon.engine.ADLEffect;
import utils.Constants;

public class ADLDB {

	private static ArrayList <Float[]> TimeDependancy = new ArrayList<Float[]>();

	public static Map<Integer, ADL> addADL() {
		
		Map<Integer, ADL> adl = new HashMap<>();
		
		//Days Array
		double days [] 			= {0.8, 0.8, 0.8, 0.8, 0.8, 0.8, 0.8};
		double weekdays [] 		= {0.8, 0.8, 0.8, 0.8, 0.8, 0.1, 0.1};
		double holydays [] 		= {0.1, 0.1, 0.1, 0.1, 0.1, 0.8, 0.8};
		double walkdays [] 		= {0.5, 0.5, 0.5, 0.5, 0.5, 0.8, 0.8};
		double marketDays [] 	= {0.0, 0.8, 0.0, 0.8, 0.0, 0.5, 0.5};
		double sitcomDays1 [] 	= {0.8, 0.0, 0.0, 0.8, 0.0, 0.0, 0.0};
		double sitcomDays2 [] 	= {0.0, 0.8, 0.0, 0.0, 0.0, 0.8, 0.0};
		double shoppingDays [] 	= {0.8, 0.0, 0.0, 0.8, 0.0, 0.0, 0.0};
		double evenDays [] 		= {0.8, 0.0, 0.0, 0.8, 0.0, 0.0, 0.0};
		double oddDays [] 		= {0.8, 0.0, 0.0, 0.8, 0.0, 0.0, 0.0};

		//Weather Array
		double sunny []			= {1.0, 0.7, 0.0};
		double rainy []			= {0.0, 0.0, 1.0};		
		double independent []	= {1.0, 1.0, 1.0};
		
		TimeDependancy = loadTimeDependancy("data/InputTD.txt");
		updateTimeDependancy (14, 0.15f);
		
		//ID, NAME, DAYS, WEATHER, TIMEDESCRIPTIONARRAY, MINTIME,  
		//NEEDS, EFFECTS
		//Extra: Garden, CleanUp, Having Guests, Shaving, ChangingClothes		
		adl.put(1, new ADL (1, "Other", days, independent, TimeDependancy.get(0), 10,
				new ArrayList<String>(Arrays.asList("stress")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("stress", -0.05)))));
		adl.put(2, new ADL (2, "Going Out", days, independent, TimeDependancy.get(1), 45,
				new ArrayList<String>(Arrays.asList("asociality","stock")),
				new ArrayList<ADLEffect>(Arrays.asList(
						new ADLEffect("stress", 	-Constants.STRESS/4), 
						new ADLEffect("hunger", 	-Constants.HUNGER/3), 
						new ADLEffect("boredom", 	-Constants.BOREDOM/3), 
						new ADLEffect("sweat", 		-Constants.SWEAT/3), 
						new ADLEffect("toileting", 	-Constants.TOILETING/3), 
						new ADLEffect("stock", 		-Constants.OUTOFSTOCK/3), 
						new ADLEffect("dirtiness", 	-Constants.DIRTINESS/3)))));
		adl.put(3, new ADL (3, "Breakfast", days, independent, TimeDependancy.get(3), 20,
				new ArrayList<String>(Arrays.asList("hunger")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hunger", -0.05)))));
		adl.put(4, new ADL (4, "Lunch", days, independent, TimeDependancy.get(5), 30,
				new ArrayList<String>(Arrays.asList("hunger")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hunger", -0.03)))));
		adl.put(5, new ADL (5, "Dinner", days, independent, TimeDependancy.get(7), 30,
				new ArrayList<String>(Arrays.asList("hunger")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hunger", -0.03)))));
		adl.put(6, new ADL (6, "Snack", days, independent, TimeDependancy.get(9), 5,
				new ArrayList<String>(Arrays.asList("hunger")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hunger", -0.04)))));
		adl.put(7, new ADL (7, "Sleeping", days, independent, TimeDependancy.get(10), 20,
				new ArrayList<String>(Arrays.asList("tirediness")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("tirediness", -(Constants.TIREDINESS + 0.0015)),
						new ADLEffect("stress", 	-Constants.STRESS), 
						new ADLEffect("hunger", 	-Constants.HUNGER), 
						new ADLEffect("boredom", 	-Constants.BOREDOM), 
						new ADLEffect("sweat", 		-Constants.SWEAT), 
						new ADLEffect("toileting", 	-Constants.TOILETING), 
						new ADLEffect("stock", 		-Constants.OUTOFSTOCK), 
						new ADLEffect("dirtiness", 	-Constants.DIRTINESS)))));
		adl.put(8, new ADL (8, "WatchingTV", days, independent, TimeDependancy.get(11), 50,
				new ArrayList<String>(Arrays.asList("boredom","stress")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("boredom", -0.05), new ADLEffect("stress", -0.02)))));		
		/*adl.put(9, new ADL (9, "Studying", days, independent, TimeDependancy.get(7), 45,
				new ArrayList<String>(Arrays.asList("boredom","stress")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("boredom", -0.05), new ADLEffect("stress", -0.02)))));	*/			
		adl.put(9,new ADL (9, "Shower", days, independent, TimeDependancy.get(13), 20,
				new ArrayList<String>(Arrays.asList("sweat")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("sweat", -0.05)))));
		adl.put(10, new ADL (10, "Toileting", days, independent, TimeDependancy.get(14), 9,
				new ArrayList<String>(Arrays.asList("toileting")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("toileting", -0.1)))));
		adl.put(11, new ADL (11, "Napping", days, independent, TimeDependancy.get(15), 10,
				new ArrayList<String>(Arrays.asList("tirediness")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("tirediness", -0.01),new ADLEffect("stress", -0.01)))));
		adl.put(12, new ADL (12, "Internet", days, independent, TimeDependancy.get(16), 20,
				new ArrayList<String>(Arrays.asList("boredom")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("boredom", -0.02)))));
		adl.put(13, new ADL (13, "Reading", days, independent, TimeDependancy.get(17), 20,
				new ArrayList<String>(Arrays.asList("boredom", "stress")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("boredom", -0.02), new ADLEffect("stress", -0.001)))));
		adl.put(14, new ADL (14, "Laundry", days, independent, TimeDependancy.get(18), 30,
				new ArrayList<String>(Arrays.asList("dirtiness")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("dirtiness", -0.02)))));		
		adl.put(15, new ADL (15, "Phone", days, independent, TimeDependancy.get(21), 5,
				new ArrayList<String>(Arrays.asList("asociality")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("asociality", -0.03)))));
		adl.put(16, new ADL (16, "Music", days, independent, TimeDependancy.get(22), 15,
				new ArrayList<String>(Arrays.asList("boredom")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("boredom", -0.01)))));
		adl.put(17, new ADL (17, "Cleaning", days, independent, TimeDependancy.get(23), 30,
				new ArrayList<String>(Arrays.asList("dirtiness")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("dirtiness", -0.02)))));
		return adl;
	}

	private static void updateTimeDependancy(int index, float d) {
		Float[] updateNeed = TimeDependancy.get(index);
		for (int i=0; i<updateNeed.length; i++) {
			updateNeed [i] += d;
		}
		TimeDependancy.set(index, updateNeed);		
	}

	private static ArrayList<Float[]> loadTimeDependancy(String fileName) {
				
		ArrayList<Float[]> timeDependancyAL = new ArrayList<Float[]>();		
	    BufferedReader reader = null;
	    Float[] td = new Float[1440];
	    String[] splited;
	    
	    try {
	        reader = new BufferedReader(new FileReader(fileName));
	        String line = null;
	        try {
				while ((line = reader.readLine()) != null) {
					splited = line.split("\\s+");
					
					for (int i=0; i<splited.length; i++) {
						td[i] = Float.parseFloat(splited[i]);
						//System.out.print(td[i]+" ");
					}
					timeDependancyAL.add(td.clone());					
					//System.out.println("");
				    
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
		return timeDependancyAL;
	}
}
