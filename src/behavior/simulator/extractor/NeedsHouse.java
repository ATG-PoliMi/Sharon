package behavior.simulator.extractor;



public class NeedsHouse {
	private static NeedsHouse instance;
	
	private double stock;
	private double dirtiness;



	private NeedsHouse(){
		this.stock 		= 	0.0;
		this.dirtiness 	= 	0.0;
	}

	public static void setInstance(NeedsHouse instance) {
		NeedsHouse.instance = instance;
	}

	public static synchronized NeedsHouse getInstance() {

		if(instance==null) {
			instance = new NeedsHouse();
		}
		return instance;
	}
	public double getStock() {
		return stock;
	}

	public void setStock(double stock) {
		this.stock = stock;
	}

	public double getDirtiness() {
		return dirtiness;
	}

	public void setDirtiness(double dirtiness) {
		this.dirtiness = dirtiness;
	}
}