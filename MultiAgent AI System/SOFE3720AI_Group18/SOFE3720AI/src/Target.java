import java.awt.Color;

/**
 * Target
 */
public class Target {
	
	//positions of target
	private int x;
	private int y;
	
	//name and color of agent
	private String name;
	private Color color;
	
	/**
	 * constructor
	 * 
	 * @param x
	 * @param y
	 * @param name
	 * @param color
	 */
	public Target(int x, int y, String name, Color color) {
		this.x = x;
		this.y = y;
		this.name = name;
		this.color = color;
	}
	
	/**
	 * 
	 * @return the x
	 */
	public int getX() {
		return x;
	}
	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}
	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}
	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}
	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	//copy this target
	public Target copy(){
		return new Target(x, y, name, color);
	}
	
	@Override
	public boolean equals(Object obj) {
		
		Target other = (Target) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	
	
}
