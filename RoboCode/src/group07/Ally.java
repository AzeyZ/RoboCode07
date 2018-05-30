
package group07;

/**
 * Ally: Info about allied robots.
 */
public class Ally {
	private String name;
	private double x;
	private double y;
	private long tick;

	/**
	 * Ally: Creates an Ally object.
	 * 
	 * @param name
	 *            Name of the ally we are creating a object for.
	 */
	public Ally(String name) {
		this.name = name;
	}

	/**
	 * update: Updating the saved info about our ally.
	 * 
	 * @param x
	 *            Xpos.
	 * @param y
	 *            Ypos.
	 * @param tick
	 *            The turn which the info was gathered.
	 */
	public void update(double x, double y, long tick) {
		this.x = x;
		this.y = y;
		this.tick = tick;

	}

	/**
	 * isMrRobot: Checks if this ally is MrRobot.
	 * 
	 * @return boolean If it´s a MrRobot true, otherwise false.
	 */
	public boolean isMrRobot() {
		return this.name.contains("MrRobot");
	}

	/**
	 * 
	 * @return String Name of the Ally.
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return double X value for the ally.
	 */
	public double getX() {
		return x;
	}

	/**
	 * 
	 * @return double Y value for the ally.
	 */
	public double getY() {
		return y;
	}

	/**
	 * 
	 * @return long The turn the information about the ally was last updated.
	 */
	public long getTick() {
		return tick;
	}

}
