// By Iacon1
// Created 09/14/2021
// A simple timer

package Utils;

public class SimpleTimer
{
	private int startTime_ = -1; // -1 means stopped.
	
	/** Starts the timer
	 * 
	 */
	public void start()
	{
		startTime_ = (int) System.currentTimeMillis();
	}
	
	/** Checks the timer to see if current time is greater than threshold;
	 *  If it is then returns true.
	 *  
	 *  @param threshold Threshold in milliseconds.
	 */
	public boolean checkTime(int threshold)
	{
		if (startTime_ == -1) return false;
		int currTime = (int) System.currentTimeMillis() - startTime_;
		if (currTime >= threshold)
		{
			startTime_ = -1;
			return true;
		}
		else return false;
	}
	
	public int stopTime()
	{
		int totalTime = (int) System.currentTimeMillis() - startTime_;
		startTime_ = -1;
		return totalTime;
	}
}
