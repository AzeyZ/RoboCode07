
package group07;

public class Ally {
	private String name;
	private double x;
	private double y;
	private long tick;
	//etc
	
	public Ally(String name) {
		this.name = name;
	}
	public void update(double x, double y, long tick)
	{
		this.x = x;
		this.y = y;
		this.tick = tick;
		
	}
		
	public boolean isMrRobot() {
		return this.name.contains("Robot07"); //Behövs ändras som vi bytar namn till MrRobot
	}

	public String getName() {
		return name;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
	public long getTick() {
		return tick;
	}
	
	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}
	public void setTick(long tick)
	{
		this.tick = tick;
	}
}
