package test;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.util.ContextUtils;
import repast.simphony.valueLayer.GridValueLayer;
import repastsimphony.common.Constants;

public class HabitatCell {
	private static final double maximumFoodProductionRate = 0.01;

	private final int x, y;
	private double foodAvailability = 0.0;

	public HabitatCell(final int x, final int y) {
		this.x = x;
		this.y = y;
	}

	public double getFoodAvailability() {
		return foodAvailability;
	}

	
	@ScheduledMethod(start =1, interval =1, priority =1)
	public void growFood() {
		foodAvailability += RandomHelper.nextDoubleFromTo(0.0,
				maximumFoodProductionRate);

		final GridValueLayer foodValueLayer = (GridValueLayer) ContextUtils
				.getContext(this).getValueLayer(Constants.FOOD_VALUE_LAYER_ID);

		foodValueLayer.set(getFoodAvailability(), x, y);
	}

	public void foodConsumed(final double eatenFood) {
		foodAvailability -= eatenFood;
	}

	@Override
	public String toString() {
		return String.format(
				"HabitatCell @ location (%d, %d), foodAvailability=%f", x, y,
				foodAvailability);
	}
}