package repastsimphony.style;

import java.awt.Color;





import repast.simphony.random.RandomHelper;
import repast.simphony.valueLayer.ValueLayer;
import repast.simphony.visualizationOGL2D.ValueLayerStyleOGL;

public class HomeStructureLayerStyleOGL2D implements ValueLayerStyleOGL {
		private ValueLayer layer = null;
		

		@Override
		public void init(final ValueLayer layer) {
			this.layer = layer;
		}

		@Override
		public float getCellSize() {
			return 15.0f;
		}
/*
		@Override
		public Color getColor(final double... coordinates) {
			final double food = layer.get(coordinates);
			System.out.println("GETCOLOROK");
			final double colorTile = RandomHelper.nextDoubleFromTo(0.0, 1.0);
			final int strength = (int) Math.min(200 * colorTile, 255);
			return new Color(0, strength, 0); // 0x000000 - black,
							  // 0x00FF00 - green
		}
	*/	
		
		@Override
		public Color getColor(final double... coordinates) {
			final double food = layer.get(coordinates);

			if (food < 0) {
				throw new IllegalStateException(
						String.format(
								"A cell's food availability property should be non-negative, but its current value is %f.",
								food));
			}

			final int strength = (int) Math.min(50+(200 * food), 255);
			return new Color(0, strength, 0); // 0x000000 - black,
												// 0x00FF00 - green
		}
}

