package repastsimphony.agent;


import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.util.ContextUtils;
import repast.simphony.valueLayer.GridValueLayer;
import repastsimphony.common.Map;
import utils.Constants;


public class Wall {
	private final int x, y;

	Map m;
	private int worldMap[][];

	/**
	 * Creates a new instance of <code>HabitatCell</code>.
	 * 
	 * @param x
	 *            the specified <code>x</code> coordinate; <i>must be
	 *            non-negative</i>
	 * @param y
	 *            the specified <code>y</code> coordinate; <i>must be
	 *            non-negative</i>
	 */
	public Wall(final int x, final int y) {
		if (x < 0) {
			throw new IllegalArgumentException(String.format(
					"Coordinate x = %d < 0.", x));
		}

		if (y < 0) {
			throw new IllegalArgumentException(String.format(
					"Coordinate y = %d < 0.", y));
		}

		this.x = x;
		this.y = y;
		m = Map.getInstance();
		worldMap = m.getWorldMap();	
	}
	
	@ScheduledMethod(start = 1, interval = 1, priority = 1)
	public void wall(){
	final GridValueLayer ValueLayer = (GridValueLayer) ContextUtils
				.getContext(this).getValueLayer(Constants.STRUCTURE_LAYER_ID);

		if (null == ValueLayer) {
			throw new IllegalStateException(
					"Cannot locate value layer with ID="
							+ Constants.STRUCTURE_LAYER_ID + ".");
		}
		//ValueLayer.set(1, x, y);
	}
}