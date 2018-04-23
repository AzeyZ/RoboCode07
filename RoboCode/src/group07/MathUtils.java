package group07;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

public class MathUtils {

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

	// Calculate the future X value
	public static double getFutureLinearX(double x, double heading, double velocity, double time) {
		x += Math.sin(Math.toRadians(heading)) * velocity * time;
		return x;
	}

	// Calculate the future Y value
	public static double getFutureLinearY(double y, double heading, double velocity, double time) {
		y += Math.sin(Math.toRadians(heading)) * velocity * time;
		return y;
	}

	static double bulletVelocity(double power)
	{
		return 20.0D - 3.0D * power;
	}

	static Point2D project(Point2D sourceLocation, double angle, double length)
	{
		return new Point2D.Double(sourceLocation.getX() + Math.sin(angle) * length, sourceLocation.getY() + Math.cos(angle) * length);
	}

	static double absoluteBearing(Point2D source, Point2D target)
	{
		return Math.atan2(target.getX() - source.getX(), target.getY() - source.getY());
	}

	static int sign(double v)
	{
		return v < 0.0D ? -1 : 1;
	}

	static int minMax(int v, int min, int max)
	{
		return Math.max(min, Math.min(max, v));
	}

}