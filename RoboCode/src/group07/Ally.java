
package group07;

public class Ally {
	private String name;
	private int X;
	private int Y;
	//etc
	
	public Ally(String name) {
		this.name = name;
	}
		
	public boolean isSame() {
		return this.name.contains("Robot07");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getX() {
		return X;
	}

	public void setX(int x) {
		X = x;
	}

	public int getY() {
		return Y;
	}

	public void setY(int y) {
		Y = y;
	}
}
