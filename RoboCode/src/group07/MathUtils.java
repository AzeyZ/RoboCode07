package group07;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

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

	public static double toRadians(double x) {
		return (x * Math.PI) / 180;
	}


	public static double bulletVelocity(double power) {
		return 20.0D - 3.0D * power;
	}

	public static Point2D project(Point2D sourceLocation, double angle, double length) {
		return new Point2D.Double(sourceLocation.getX() + Math.sin(angle) * length,
				sourceLocation.getY() + Math.cos(angle) * length);
	}

	public static double absoluteBearing(Point2D source, Point2D target) {
		return Math.atan2(target.getX() - source.getX(), target.getY() - source.getY());
	}

	public static int sign(double v) {
		return v < 0.0D ? -1 : 1;
	}

	public static int minMax(int v, int min, int max) {
		return Math.max(min, Math.min(max, v));
	}

	public static double distance(double x1, double y1, double x2, double y2) {
		return Math.hypot(x2 - x1, y2 - y1);
	}

	public static double distance(Point2D point1, Point2D point2) {
		return Math.hypot(point2.getX() - point1.getX(), point2.getY() - point1.getY());
	}

}
