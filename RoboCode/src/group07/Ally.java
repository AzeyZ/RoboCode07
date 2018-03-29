
package group07;

public class Ally {
	private String name;
	private double x;
	private double y;
	//etc
	
	public Ally(String name) {
		this.name = name;
	}
	public void update(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
		
	public boolean isMrRobot() {
		return this.name.contains("Robot07"); //Behövs ändras som vi bytar namn till MrRobot
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
	
	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
}
