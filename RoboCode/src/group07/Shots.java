package group07;
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
