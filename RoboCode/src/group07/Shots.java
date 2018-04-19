package group07;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

//Used to warn allies when we shot at them.
public class Shots {
	double x;
	double y;
	long tick;
	public Shots(double x , double y , long tick)
	{
		this.x = x;
		this.y = y;
		this.tick = tick;
	}
	public double getX()
	{
		return x;
	}
	public double getY()
	{
		return y;
		
	}
	public long getTick()
	{
		return tick;
	}
}
