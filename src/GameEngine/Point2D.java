// By Iacon1
// Created 05/17/2022
//

package GameEngine;

public abstract class Point2D<T extends Number, P extends Point2D<T, P>>
{
	public T x;
	public T y;
	
	public abstract <M extends Number> P add(Point2D<M, ?> delta);
	public abstract <M extends Number> P multiply(M factor);
	public abstract <M extends Number> P subtract(Point2D<M, ?> delta);
	public abstract <M extends Number> P divide(M factor);
	
	@Override
	public int hashCode()
	{
		return (x.hashCode() + "." + y.hashCode()).hashCode();
	}
}
