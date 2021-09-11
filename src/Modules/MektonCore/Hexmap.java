// By
// Iacon1
// Created 04/25/2021
//

package Modules.MektonCore;

import java.util.ArrayList;

import GameEngine.GameCanvas;
import GameEngine.Configurables.ConfigManager;
import GameEngine.EntityTypes.GameEntity;
import GameEngine.EntityTypes.SpriteEntity;
import GameEngine.Managers.GraphicsManager;
import Utils.Instancer;
import Utils.Logging;
import Utils.MiscUtils;

public class Hexmap<T extends HexData> extends GameEntity
{	
	private ArrayList<ArrayList<ArrayList<T>>> hexes_; // A set of rows (x/width) of columns (y/length) of pillars (z/height)
	private Instancer<T> instancer_;
	private int cZ_;
	
	public Hexmap(T hexTemplate)
	{
		super();
		hexes_ = new ArrayList<ArrayList<ArrayList<T>>>();
		instancer_ = new Instancer<T>(hexTemplate);
	}

	public void setDimensions(int x, int y, int z) // Sets new dimensions for map
	{
		hexes_ = MiscUtils.resizeArrayList(hexes_, x);
		for (int i = 0; i < x; ++i)
		{
			if (hexes_.get(i) == null) hexes_.set(i, new ArrayList<ArrayList<T>>());
			ArrayList<ArrayList<T>> xSlice = hexes_.get(i); // A slice along the x axis; Only has y & z axes
			xSlice = MiscUtils.resizeArrayList(xSlice, y);
			
			for (int j = 0; j < y; ++j)
			{
				if (xSlice.get(j) == null) xSlice.set(j, new ArrayList<T>());
				ArrayList<T> ySlice = xSlice.get(j); // A slice along the y axis; Only has z axis
				ySlice = MiscUtils.resizeArrayList(ySlice, z);
				
				for (int k = 0; k < z; ++k)
				{
					try {ySlice.set(k, instancer_.getInstance());}
					catch (Exception e) {Logging.logException(e);}
				}

				xSlice.set(j,  ySlice);
			}
			
			hexes_.set(i,  xSlice);
		}
	}
	public int getMapWidth() // Get map size in x-direction
	{
		return hexes_.size();
	}	
	public int getMapLength() // Get map size in y-direction
	{
		return hexes_.get(0).size();
	}
	public int getMapHeight() // Get map size in z-direction
	{
		return hexes_.get(0).get(0).size();
	}
	
	public void setHex(int x, int y, int z, T data) // Sets the hex data at a coordinate
	{
		ArrayList<ArrayList<T>> xSlice = hexes_.get(x); // A slice along the x axis; Only has y & z axes
		ArrayList<T> ySlice = xSlice.get(y); // A slice along the y axis; Only has z axis
		T zSlice = ySlice.get(z); // A slice along the x axis; Only has y & z axes
		
		zSlice = data;
		ySlice.set(z, zSlice);
		xSlice.set(y,  ySlice);
		hexes_.set(x,  xSlice);
	}
	public T getHex(int x, int y, int z) // Gets the hex data @ (x, y, z)
	{
		ArrayList<ArrayList<T>> xSlice = hexes_.get(x); // A slice along the x axis; Only has y & z axes
		ArrayList<T> ySlice = xSlice.get(y); // A slice along the y axis; Only has z axis
		T zSlice = ySlice.get(z); // A slice along the x axis; Only has y & z axes
		
		return zSlice;
	}
	public SpriteEntity findEntity(int x, int y, int z) // returns a game instance at that position if available
	{
		for (int i = 0; i < childrenIds_.size(); ++i)
		{
			HexEntity obj = (HexEntity) getChild(i); // Please only put physical objects in here
			if (obj.getX() == x && obj.getY() == y && obj.getZ() == z)
				return obj;
		}
		
		return null;
	}
	public static int GX2SX(int gX, int hexWidth) // gX relative to corner-of-screen
	{
		return (int) Math.round(gX * 0.75 * hexWidth);
	}
	public static int GY2SY(int gY, int hexHeight, boolean shift) // gY relative to corner-of-screen
	{
		if (shift)
			return (int) Math.round(gY * hexHeight + 0.5 * hexHeight);
		else
			return (int) Math.round(gY * hexHeight);
	}
	
	@Override
	public String getName()
	{
		return "Hexmap"; // TODO
	}
	
	public void setCameraHeight(int z) // Set camera height in hexes
	{
		cZ_ = z;
	}
	@Override
	public void render(GameCanvas canvas)
	{
		int hexWidth = ConfigManager.getHexWidth(); // Hex width
		int hexHeight = ConfigManager.getHexHeight(); // Hex height	
		
		for (int k = 0; k < cZ_; ++k)
		{
			boolean shift = false; // Shift down on y-axis by half-column?
			for (int i = 0; i < getMapWidth(); ++i)
			{
				for (int j = 0; j < getMapLength(); ++j)
				{
					T hex = getHex(i, j, k);
					int hX = GX2SX(i, hexWidth);
					int hY = GY2SY(j, hexHeight, shift);
					int cTX = hex.tX_ * hexWidth;
					int cTY = hex.tY_ * hexHeight;
					
					canvas.drawImageScaled(GraphicsManager.getImage(hex.tileset_), canvas.cX_ + hX, canvas.cY_ + hY, cTX, cTY, hexWidth, hexHeight);
				}
				shift = !shift;
			}
			
			for (int t = 0; t < getChildren().size(); ++t)
			{
				HexEntity entity = (HexEntity) getChildren().get(t);
			
				if (entity.getZ() == k) entity.render(canvas);
			}
		}
	}
}
