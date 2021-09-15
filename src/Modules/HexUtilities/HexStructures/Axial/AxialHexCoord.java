// By Iacon1
// Created 09/12/2021
// Axial coordinates as defined by https://www.redblobgames.com/grids/hexagons/#coordinates-axial

package Modules.HexUtilities.HexStructures.Axial;

import java.util.ArrayList;

import GameEngine.Point2D;
import Modules.HexUtilities.HexConfigManager;
import Modules.HexUtilities.HexDirection;
import Modules.HexUtilities.HexStructures.HexCoord;
import Modules.HexUtilities.HexStructures.HexCoordConverter;
import Utils.Logging;
import Utils.MiscUtils;

public class AxialHexCoord implements HexCoord
{
	public int q_; // "Column"
	public int r_; // "Row"
	public int s() // "Side"?
	{
		return -(q_ + r_);
	}

	public AxialHexCoord(int q, int r)
	{
		q_ = q;
		r_ = r;
	}

	@Override
	public AxialHexCoord rAdd(HexCoord delta)
	{
		AxialHexCoord deltaAxial = HexCoordConverter.convert(delta, delta.getClass(), AxialHexCoord.class);
		return new AxialHexCoord(q_ + deltaAxial.q_, r_ + deltaAxial.r_);
	}
	@Override
	public AxialHexCoord rMultiply(int factor)
	{
		return new AxialHexCoord(this.q_ * factor, this.r_ * factor);
	}

	@Override
	public AxialHexCoord getUnitVector(HexDirection dir) // https://www.redblobgames.com/grids/hexagons/#neighbors-axial
	{
		switch (dir)
		{
		case north: return new AxialHexCoord(0, -1);
		case northWest: return new AxialHexCoord(-1, 0);
		case northEast: return new AxialHexCoord(1, -1);
		
		case south: return new AxialHexCoord(0, 1);
		case southWest: return new AxialHexCoord(-1, 1);
		case southEast: return new AxialHexCoord(1, 0);
		default: return null;
		}
	}	
	@Override
	public AxialHexCoord getNeighbor(HexDirection dir) // https://www.redblobgames.com/grids/hexagons/#neighbors-axial
	{
		AxialHexCoord delta = getUnitVector(dir);
		return rAdd(delta);
	}

	@Override
	public int distance(HexCoord target) // https://www.redblobgames.com/grids/hexagons/#distances-cube
	{	
		AxialHexCoord targetAxial = HexCoordConverter.convert(target, target.getClass(), AxialHexCoord.class);
		int dQ = q_ - targetAxial.q_;
		int dR = r_ - targetAxial.r_;
		int dS = s() - targetAxial.s();
		return MiscUtils.multiMax(dQ, dR, dS);
	}
	
	@Override
	public ArrayList<AxialHexCoord> straightLine(HexCoord target) // https://www.redblobgames.com/grids/hexagons/#line-drawing
	{
		AxialHexCoord targetAxial = HexCoordConverter.convert(target, target.getClass(), AxialHexCoord.class);
		ArrayList<AxialHexCoord> line = new ArrayList<AxialHexCoord>();
		
		int dist = distance(targetAxial);
		
		float fAQ = (float) q_;
		float fAR = (float) r_;
		float fBQ = (float) targetAxial.q_;
		float fBR = (float) targetAxial.r_;
		for (int i = 0; i < dist; ++i)
		{
			float t = 1.0f / ((float) dist) * (float) i;
			int q = (int) MiscUtils.lerp(fAQ, fBQ, t);
			int r = (int) MiscUtils.lerp(fAR, fBR, t);
			
			line.add(new AxialHexCoord(q, r));
		}
		
		return line;
	}
	@Override
	public ArrayList<AxialHexCoord> withinDistance(int r) // https://www.redblobgames.com/grids/hexagons/#range
	{
		ArrayList<AxialHexCoord> within = new ArrayList<AxialHexCoord>();
		
		for (int i = -r; i <= r; ++i)
			for (int j = Math.max(-r, -(i + r)); j <= Math.min(r, r - i); ++j)
			{
				AxialHexCoord delta = new AxialHexCoord(i, j);
				within.add(rAdd(delta));
			}
		
		return within;
	}
	
	@Override
	public Point2D toPixel() // https://www.redblobgames.com/grids/hexagons/#hex-to-pixel
	{
		int x = (3 * HexConfigManager.getHexWidth()) / 4 * q_ + q_; // Last q fixes a off-by-one spacing issue
		// x = (3 / 2 * q * size) = (3 * width) / 4 * q
		int y = (HexConfigManager.getHexHeight() / 2) * q_ +  HexConfigManager.getHexHeight() * r_;
		// y = size * (sqrt3 / 2 * q + sqrt3 * r) = (height / 2 * q + height * r)

		// Using approximations somehow works better, yay
		return new Point2D(x, y);
	}
}