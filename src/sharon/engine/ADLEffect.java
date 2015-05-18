package sharon.engine;

public class ADLEffect {
	private String name;
	private double effect;
	
	public ADLEffect(String name, double effect) {
		super();
		this.name = name;
		this.effect = effect;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getEffect() {
		return effect;
	}
	public void setEffect(double effect) {
		this.effect = effect;
	}

}
