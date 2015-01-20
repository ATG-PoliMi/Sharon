package behavior.simulator.xml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import behavior.simulator.extractor.ADL;
import behavior.simulator.extractor.ADLEffect;

public class ADLDB {

	public static Map<Integer, ADL> addADL() {
		
		Map<Integer, ADL> adl = new HashMap<>();
		
		//Days Array
		double days [] 			= {0.8, 0.8, 0.8, 0.8, 0.8, 0.8, 0.8};
		double weekdays [] 		= {0.8, 0.8, 0.8, 0.8, 0.8, 0.1, 0.1};
		double holydays [] 		= {0.1, 0.1, 0.1, 0.1, 0.1, 0.8, 0.8};
		double marketDays [] 	= {0.0, 0.0, 0.0, 0.9, 0.0, 0.0, 0.0};
		double sitcomDays1 [] 	= {0.8, 0.0, 0.0, 0.8, 0.0, 0.0, 0.0};
		double sitcomDays2 [] 	= {0.0, 0.8, 0.0, 0.0, 0.0, 0.8, 0.0};
		double shoppingDays [] 	= {0.8, 0.0, 0.0, 0.8, 0.0, 0.0, 0.0};

		//Weather Array
		double sunny []			= {1.0, 0.5, 0.0};
		double rainy []			= {0.0, 0.0, 1.0};
		double independent []	= {1.0, 1.0, 1.0};
		
		//Time Description Array
		double a101 [] = {0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.13333333333333333, 0.16666666666666669, 0.19999999999999998, 0.23333333333333328, 0.2666666666666667, 0.3, 0.33333333333333337, 0.3666666666666667, 0.4, 0.43333333333333335, 0.4666666666666667, 0.5, 0.5333333333333333, 0.5666666666666667, 0.6, 0.6333333333333333, 0.6666666666666666, 0.6999999999999998, 0.7333333333333332, 0.7666666666666667, 0.7999999999999998, 0.8333333333333334, 0.8666666666666666, 0.9, 0.9333333333333333, 0.9666666666666667, 1.0, 1.0333333333333334, 1.0666666666666667, 1.1, 1.1333333333333335, 1.1666666666666667, 1.2000000000000002, 1.2333333333333334, 1.2666666666666668, 1.3, 1.3333333333333335, 1.3666666666666667, 1.4000000000000001, 1.4333333333333333, 1.4666666666666668, 1.5, 1.5333333333333334, 1.5666666666666669, 1.6, 1.6333333333333333, 1.6666666666666667, 1.7000000000000002, 1.7333333333333334, 1.7666666666666666, 1.8, 1.8333333333333335, 1.8666666666666667, 1.9000000000000001, 1.9333333333333336, 1.9666666666666668, 2.0, 2.033333333333333, 2.066666666666667, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.13333333333333333, 0.16666666666666669, 0.19999999999999998, 0.23333333333333328, 0.2666666666666667, 0.3, 0.33333333333333337, 0.3666666666666667, 0.4, 0.43333333333333335, 0.4666666666666667, 0.5, 0.5333333333333333, 0.5666666666666667, 0.6, 0.6333333333333333, 0.6666666666666666, 0.6999999999999998, 0.7333333333333332, 0.7666666666666667, 0.7999999999999998, 0.8333333333333334, 0.8666666666666666, 0.9, 0.9333333333333333, 0.9666666666666667, 1.0, 1.0333333333333334, 1.0666666666666667, 1.1, 1.1333333333333335, 1.1666666666666667, 1.2000000000000002, 1.2333333333333334, 1.2666666666666668, 1.3, 1.3333333333333335, 1.3666666666666667, 1.4000000000000001, 1.4333333333333333, 1.4666666666666668, 1.5, 1.5333333333333334, 1.5666666666666669, 1.6, 1.6333333333333333, 1.6666666666666667, 1.7000000000000002, 1.7333333333333334, 1.7666666666666666, 1.8, 1.8333333333333335, 1.8666666666666667, 1.9000000000000001, 1.9333333333333336, 1.9666666666666668, 2.0, 2.033333333333333, 2.066666666666667, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.13333333333333333, 0.16666666666666669, 0.19999999999999998, 0.23333333333333328, 0.2666666666666667, 0.3, 0.33333333333333337, 0.3666666666666667, 0.4, 0.43333333333333335, 0.4666666666666667, 0.5, 0.5333333333333333, 0.5666666666666667, 0.6, 0.6333333333333333, 0.6666666666666666, 0.6999999999999998, 0.7333333333333332, 0.7666666666666667, 0.7999999999999998, 0.8333333333333334, 0.8666666666666666, 0.9, 0.9333333333333333, 0.9666666666666667, 1.0, 1.0333333333333334, 1.0666666666666667, 1.1, 1.1333333333333335, 1.1666666666666667, 1.2000000000000002, 1.2333333333333334, 1.2666666666666668, 1.3, 1.3333333333333335, 1.3666666666666667, 1.4000000000000001, 1.4333333333333333, 1.4666666666666668, 1.5, 1.5333333333333334, 1.5666666666666669, 1.6, 1.6333333333333333, 1.6666666666666667, 1.7000000000000002, 1.7333333333333334, 1.7666666666666666, 1.8, 1.8333333333333335, 1.8666666666666667, 1.9000000000000001, 1.9333333333333336, 1.9666666666666668, 2.0, 2.033333333333333, 2.066666666666667, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.13333333333333333, 0.16666666666666669, 0.19999999999999998, 0.23333333333333328, 0.2666666666666667, 0.3, 0.33333333333333337, 0.3666666666666667, 0.4, 0.43333333333333335, 0.4666666666666667, 0.5, 0.5333333333333333, 0.5666666666666667, 0.6, 0.6333333333333333, 0.6666666666666666, 0.6999999999999998, 0.7333333333333332, 0.7666666666666667, 0.7999999999999998, 0.8333333333333334, 0.8666666666666666, 0.9, 0.9333333333333333, 0.9666666666666667, 1.0, 1.0333333333333334, 1.0666666666666667, 1.1, 1.1333333333333335, 1.1666666666666667, 1.2000000000000002, 1.2333333333333334, 1.2666666666666668, 1.3, 1.3333333333333335, 1.3666666666666667, 1.4000000000000001, 1.4333333333333333, 1.4666666666666668, 1.5, 1.5333333333333334, 1.5666666666666669, 1.6, 1.6333333333333333, 1.6666666666666667, 1.7000000000000002, 1.7333333333333334, 1.7666666666666666, 1.8, 1.8333333333333335, 1.8666666666666667, 1.9000000000000001, 1.9333333333333336, 1.9666666666666668, 2.0, 2.033333333333333, 2.066666666666667, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
		//ID, NAME, DAYS, WEATHER, TIMEDESCRIPTIONARRAY, MINTIME,  
		//NEEDS, EFFECTS

		//Morning
		adl.put(101, new ADL (101, "Breakfast", days, independent, a101, 10,
				new ArrayList<String>(Arrays.asList("hunger")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hunger", -0.09), new ADLEffect("bladder", +0.03)))));
		adl.put(102, new ADL (102, "Sitcom", sitcomDays1, independent, a101, 50,
				new ArrayList<String>(Arrays.asList("comfort", "fun")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("comfort", -0.02), new ADLEffect("fun", -0.03)))));
		adl.put(103, new ADL (103, "Shopping", shoppingDays, independent, a101, 10,
				new ArrayList<String>(Arrays.asList("stock")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("energy", +0.02), new ADLEffect("hygiene", +0.03)))));
		adl.put(104, new ADL (104, "Garden", days, independent, a101, 10,
				new ArrayList<String>(Arrays.asList("fun")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hunger", +0.03), new ADLEffect("energy", +0.03), new ADLEffect("hygiene", +0.03)))));
		adl.put(105, new ADL (105, "Shower", days, independent, a101, 10,
				new ArrayList<String>(Arrays.asList("hygiene")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hygiene", -0.1)))));
		adl.put(106, new ADL (106, "TakeAWalk", days, independent, a101, 10,
				new ArrayList<String>(Arrays.asList("fun")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hunger", +0.03), new ADLEffect("energy", +0.03), new ADLEffect("hygiene", +0.03)))));
		adl.put(107, new ADL (107, "Launder", days, independent, a101, 10,
				new ArrayList<String>(Arrays.asList("dirtiness")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("energy", +0.02)))));
		adl.put(109, new ADL (109, "CleanUp", days, independent, a101, 10,
				new ArrayList<String>(Arrays.asList("dirtiness")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("energy", +0.02)))));
		adl.put(110, new ADL (110, "Toilet", days, independent, a101, 10,
				new ArrayList<String>(Arrays.asList("bladder")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("bladder", -0.1)))));
		adl.put(111, new ADL (111, "Toilet", days, independent, a101, 10,
				new ArrayList<String>(Arrays.asList("bladder")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("bladder", -0.1)))));		
		adl.put(112,new ADL (112, "Sleep", days, independent, a101, 10,
				new ArrayList<String>(Arrays.asList("energy")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("energy", -0.1), new ADLEffect("comfort", -0.1)))));
		adl.put(113, new ADL (113, "MARKET", days, independent, a101, 10,
				new ArrayList<String>(Arrays.asList("stock")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hunger", +0.01), new ADLEffect("energy", +0.03), new ADLEffect("hygiene", +0.01)))));
		adl.put(114, new ADL (114, "Phone", days, independent, a101, 10,
				new ArrayList<String>(Arrays.asList("fun")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("fun", -0.01)))));


		adl.put(100, new ADL (100, "Lunch", days, independent, a101, 10,
				new ArrayList<String>(Arrays.asList("hunger")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hunger", -0.08), new ADLEffect("energy", +0.01)))));
		//Afternoon		

		adl.put(201, new ADL (201, "Sitcom1", days, independent, a101, 10,
				new ArrayList<String>(Arrays.asList("comfort", "fun")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("comfort", -0.02), new ADLEffect("fun", -0.03)))));
		adl.put(202, new ADL (202, "Sitcom2", days, independent, a101, 10,
				new ArrayList<String>(Arrays.asList("comfort", "fun")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("comfort", -0.02), new ADLEffect("fun", -0.03)))));
		adl.put(203, new ADL (203, "Tea", days, independent, a101, 10,
				new ArrayList<String>(Arrays.asList("hunger", "energy")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hunger", -0.02), new ADLEffect("energy", -0.01)))));
		adl.put(204, new ADL (204, "Garden", days, independent, a101, 10,
				new ArrayList<String>(Arrays.asList("fun")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hunger", +0.03), new ADLEffect("energy", +0.03), new ADLEffect("hygiene", +0.03)))));
		adl.put(205, new ADL (205, "ReadBook", days, independent, a101, 10,
				new ArrayList<String>(Arrays.asList("comfort", "fun")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("comfort", -0.03), new ADLEffect("fun", -0.03)))));
		adl.put(206, new ADL (206, "Shower", days, independent, a101, 10,
				new ArrayList<String>(Arrays.asList("hygiene")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hygiene", -0.1)))));
		adl.put(207, new ADL (207, "WashingMachine", days, independent, a101, 10,
				new ArrayList<String>(Arrays.asList("dirtiness")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("energy", +0.02)))));
		adl.put(208, new ADL (208, "TakeAWalk", days, independent, a101, 10,
				new ArrayList<String>(Arrays.asList("fun")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hunger", +0.03), new ADLEffect("energy", +0.03), new ADLEffect("hygiene", +0.03)))));
		adl.put(209, new ADL (209, "CleanUp", days, independent, a101, 10,
				new ArrayList<String>(Arrays.asList("dirtiness")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("energy", +0.01),new ADLEffect("hygiene", +0.01)))));
		adl.put(210, new ADL (210, "Toilet", days, independent, a101, 10,
				new ArrayList<String>(Arrays.asList("bladder")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("bladder", -0.1)))));
		adl.put(211, new ADL (211, "Phone", days, independent, a101, 10,
				new ArrayList<String>(Arrays.asList("fun")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("fun", -0.01)))));

		adl.put(200, new ADL (200, "Dinner", days, independent, a101, 10,
				new ArrayList<String>(Arrays.asList("hunger")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hunger", -0.08), new ADLEffect("energy", +0.01)))));

		//Evening
		adl.put(301, new ADL (301, "Cinema", days, independent, a101, 10,
				new ArrayList<String>(Arrays.asList("comfort", "fun")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("comfort", -0.05), new ADLEffect("fun", -0.03)))));
		adl.put(302, new ADL (302, "EveningTV", days, independent, a101, 10,
				new ArrayList<String>(Arrays.asList("comfort", "fun")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("comfort", -0.05), new ADLEffect("fun", -0.03)))));
		adl.put(303, new ADL (303, "ReadBook", days, independent, a101, 10,
				new ArrayList<String>(Arrays.asList("comfort", "fun")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("comfort", -0.05), new ADLEffect("fun", -0.03)))));

		adl.put(300, new ADL (300, "Sleep", days, independent, a101, 10,
				new ArrayList<String>(Arrays.asList("energy")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("energy", -0.01), new ADLEffect("comfort", -0.01)))));
		return adl;
	}
}
