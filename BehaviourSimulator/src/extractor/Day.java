package extractor;


public class Day {
	private static Day instance;
	
	//TODO: Change distribution of conditions
	private Day(){
		this.weather = 1;
		this.weekDay = 1;		
	}

	private int weather;
	private int weekDay;
	
	
	public int getWeather() {
		return weather;
	}
	public int getWeekDay() {
		return weekDay;
	}
	public void nextDay() {
		weather = weather == 1 ? 3 			: 1;	//TODO: insert a more complex weather model
		weekDay = weekDay < 7  ? weekDay+1 	: 1;
	}
	
	public static synchronized Day getInstance() {

		if(instance==null) {
			instance=new Day();
		}
		return instance;
	}
}