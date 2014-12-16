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
			final double wall = layer.get(coordinates);

			if (wall < 0) {
				throw new IllegalStateException(
						String.format("Cell value can not be less than 0 "));
			}

			switch ((int)wall){
			case 0: 
				return new Color(255, 255, 255);				
			case 1: 				
				return new Color(0,0,0);				
			case 2:
				return new Color(255,0,0);
			default:
				return new Color(255,255,255);
			}
			
			
		}
}

