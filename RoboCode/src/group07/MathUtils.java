package group07;

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

}
