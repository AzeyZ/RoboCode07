package group07;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

/**
 * Class for our mathemathical calculations
 * 
 */
public class MathUtils {

	private MathUtils() {

	}

	// Ger en vinkel mellan -180 och 180
	public static double normalizeBearing(double angle) {
		while (angle > 180)
			angle -= 360;
		while (angle < -180)
			angle += 360;
		return angle;
	}

	// computes the absolute bearing between two points
	public static double absoluteBearing(double x1, double y1, double x2, double y2) {
		double xo = x2 - x1;
		double yo = y2 - y1;
		double hyp = Math.sqrt(Math.pow(xo, 2) + Math.pow(yo, 2));
		double arcSin = Math.toDegrees(Math.asin(xo / hyp));
		double bearing = 0;

		if (xo > 0 && yo > 0) { // both pos: lower-Left
			bearing = arcSin;
		} else if (xo < 0 && yo > 0) { // x neg, y pos: lower-right
			bearing = 360 + arcSin; // arcsin is negative here, actuall 360 - ang
		} else if (xo > 0 && yo < 0) { // x pos, y neg: upper-left
			bearing = 180 - arcSin;
		} else if (xo < 0 && yo < 0) { // both neg: upper-right
			bearing = 180 - arcSin; // arcsin is negative here, actually 180 + ang
		}

		return bearing;
	}

	/**
	 * 
	 * @param angle
	 *            in degrees
	 * @return angle in radians
	 */
	public static double toRadians(double x) {
		return (x * Math.PI) / 180;
	}

	/**
	 * Calculates velocity of with given power
	 * 
	 * @param power
	 * @return velocity
	 */
	public static double bulletVelocity(double power) {
		return 20.0D - 3.0D * power;
	}

	/**
	 * Projecting our position in the angle and length given
	 * 
	 * @param sourceLocation
	 * @param angle
	 * @param length
	 * @return projected Position
	 */
	public static Point2D project(Point2D sourceLocation, double angle, double length) {
		return new Point2D.Double(sourceLocation.getX() + Math.sin(angle) * length,
				sourceLocation.getY() + Math.cos(angle) * length);
	}

	/**
	 * Calculates the absolute bearing to enemy from our location
	 * 
	 * @return angle to enemy
	 */
	public static double absoluteBearing(Point2D sourcelocation, Point2D targetlocation) {
		return Math.atan2(targetlocation.getX() - sourcelocation.getX(), targetlocation.getY() - sourcelocation.getY());
	}

	/**
	 * Calculates direction from angle
	 * 
	 * @return direction
	 */
	public static int sign(double angle) {
		return angle < 0.0D ? -1 : 1;
	}

	/**
	 * Checks if value is between min and max
	 * 
	 * @return returns value.
	 */
	public static int minMax(int value, int min, int max) {
		return Math.max(min, Math.min(max, value));
	}

	/**
	 * Calculates distance between point x1,y1 and x2,y2.
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return distance
	 */
	public static double distance(double x1, double y1, double x2, double y2) {
		return Math.hypot(x2 - x1, y2 - y1);
	}

	/**
	 * alculates distance between point1 and point2.
	 * 
	 * @param point1
	 * @param point2
	 * @return distance
	 */
	public static double distance(Point2D point1, Point2D point2) {
		return Math.hypot(point2.getX() - point1.getX(), point2.getY() - point1.getY());
	}

}
