package group07;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import robocode.HitByBulletEvent;
import robocode.TeamRobot;
import robocode.util.Utils;

public class SurfMovement {
	private static int BINS = 47;
	private static double _surfStats[] = new double[BINS];
	private Point2D.Double _myLocation;     // our bot's location
	private Point2D.Double _enemyLocation;  // enemy bot's location

	private ArrayList _enemyWaves;
	private ArrayList _surfDirections;
	private ArrayList _surfAbsBearings;

	public static double _oppEnergy = 100.0;

	public static Rectangle2D.Double _fieldRect = new java.awt.geom.Rectangle2D.Double(18, 18, 1164, 1164);
	public static double WALL_STICK = 160;

	public SurfMovement() {
		_enemyWaves = new ArrayList();
		_surfDirections = new ArrayList();
		_surfAbsBearings = new ArrayList();
	}
	
	public void updateSurf(TeamRobot r, EnemyTracker t) {
		_myLocation = new Point2D.Double(r.getX(), r.getY());
		double lateralVelocity = r.getVelocity()*Math.sin(t.getTarget().getBearing()*Math.PI/180);
		double absBearing = t.getTarget().getBearing()*Math.PI/180 + r.getHeadingRadians();
		r.setTurnRadarRightRadians(Utils.normalRelativeAngle(absBearing - r.getRadarHeadingRadians()) * 2);
		_surfDirections.add(0, new Integer((lateralVelocity >= 0) ? 1 : -1));
	    _surfAbsBearings.add(0, new Double(absBearing + Math.PI));
	    double bulletPower = _oppEnergy - t.getTarget().getEnergy();
	    
	    if (bulletPower < 3.01 && bulletPower > 0.09
	            && _surfDirections.size() > 2) {
	            EnemyWave ew = new EnemyWave();
	            ew.fireTime = r.getTime() - 1;
	            ew.bulletVelocity = bulletVelocity(bulletPower);
	            ew.distanceTraveled = bulletVelocity(bulletPower);
	            ew.direction = ((Integer)_surfDirections.get(2)).intValue();
	            ew.directAngle = ((Double)_surfAbsBearings.get(2)).doubleValue();
	            ew.fireLocation = (Point2D.Double)_enemyLocation.clone(); // last tick
	 
	            _enemyWaves.add(ew);
	            _oppEnergy = t.getTarget().getEnergy();
	            
	            _enemyLocation = project(_myLocation, absBearing, t.getTarget().getDistance());
	            
	            updateWaves(r);
	            doSurfing(r);
	        }
	}
	
	public double checkDanger(EnemyWave surfWave, int direction, TeamRobot r) {
        int index = getFactorIndex(surfWave,
            predictPosition(surfWave, direction,r));
 
        return _surfStats[index];
    }
 
    public void doSurfing(TeamRobot r) {
        EnemyWave surfWave = getClosestSurfableWave();
 
        if (surfWave == null) { return; }
 
        double dangerLeft = checkDanger(surfWave, -1,r);
        double dangerRight = checkDanger(surfWave, 1,r);
 
        double goAngle = absoluteBearing(surfWave.fireLocation, _myLocation);
        if (dangerLeft < dangerRight) {
            goAngle = wallSmoothing(_myLocation, goAngle - (Math.PI/2), -1);
        } else {
            goAngle = wallSmoothing(_myLocation, goAngle + (Math.PI/2), 1);
        }
 
        setBackAsFront(r, goAngle);
    }
	
	public Point2D.Double predictPosition(EnemyWave surfWave, int direction, TeamRobot r) {
        Point2D.Double predictedPosition = (Point2D.Double)_myLocation.clone();
        double predictedVelocity = r.getVelocity();
        double predictedHeading = r.getHeadingRadians();
        double maxTurning, moveAngle, moveDir;
 
        int counter = 0; // number of ticks in the future
        boolean intercepted = false;
 
        do {    // the rest of these code comments are rozu's
            moveAngle =
                wallSmoothing(predictedPosition, absoluteBearing(surfWave.fireLocation,
                predictedPosition) + (direction * (Math.PI/2)), direction)
                - predictedHeading;
            moveDir = 1;
 
            if(Math.cos(moveAngle) < 0) {
                moveAngle += Math.PI;
                moveDir = -1;
            }
 
            moveAngle = Utils.normalRelativeAngle(moveAngle);
 
            // maxTurning is built in like this, you can't turn more then this in one tick
            maxTurning = Math.PI/720d*(40d - 3d*Math.abs(predictedVelocity));
            predictedHeading = Utils.normalRelativeAngle(predictedHeading
                + limit(-maxTurning, moveAngle, maxTurning));
 
            // this one is nice ;). if predictedVelocity and moveDir have
            // different signs you want to breack down
            // otherwise you want to accelerate (look at the factor "2")
            predictedVelocity +=
                (predictedVelocity * moveDir < 0 ? 2*moveDir : moveDir);
            predictedVelocity = limit(-8, predictedVelocity, 8);
 
            // calculate the new predicted position
            predictedPosition = project(predictedPosition, predictedHeading,
                predictedVelocity);
 
            counter++;
 
            if (predictedPosition.distance(surfWave.fireLocation) <
                surfWave.distanceTraveled + (counter * surfWave.bulletVelocity)
                + surfWave.bulletVelocity) {
                intercepted = true;
            }
        } while(!intercepted && counter < 500);
 
        return predictedPosition;
    }
	
	public void onHitByBulletSurf(HitByBulletEvent e) {
        // If the _enemyWaves collection is empty, we must have missed the
        // detection of this wave somehow.
        if (!_enemyWaves.isEmpty()) {
            Point2D.Double hitBulletLocation = new Point2D.Double(
                e.getBullet().getX(), e.getBullet().getY());
            EnemyWave hitWave = null;
 
            // look through the EnemyWaves, and find one that could've hit us.
            for (int x = 0; x < _enemyWaves.size(); x++) {
                EnemyWave ew = (EnemyWave)_enemyWaves.get(x);
 
                if (Math.abs(ew.distanceTraveled -
                    _myLocation.distance(ew.fireLocation)) < 50
                    && Math.abs(bulletVelocity(e.getBullet().getPower()) 
                        - ew.bulletVelocity) < 0.001) {
                    hitWave = ew;
                    break;
                }
            }
 
            if (hitWave != null) {
                logHit(hitWave, hitBulletLocation);
 
                // We can remove this wave now, of course.
                _enemyWaves.remove(_enemyWaves.lastIndexOf(hitWave));
            }
        }
    }
	
	// Given the EnemyWave that the bullet was on, and the point where we
    // were hit, update our stat array to reflect the danger in that area.
    public void logHit(EnemyWave ew, Point2D.Double targetLocation) {
        int index = getFactorIndex(ew, targetLocation);
 
        for (int x = 0; x < BINS; x++) {
            // for the spot bin that we were hit on, add 1;
            // for the bins next to it, add 1 / 2;
            // the next one, add 1 / 5; and so on...
            _surfStats[x] += 1.0 / (Math.pow(index - x, 2) + 1);
        }
    }
	
	 public static int getFactorIndex(EnemyWave ew, Point2D.Double targetLocation) {
	        double offsetAngle = (absoluteBearing(ew.fireLocation, targetLocation)
	            - ew.directAngle);
	        double factor = Utils.normalRelativeAngle(offsetAngle)
	            / maxEscapeAngle(ew.bulletVelocity) * ew.direction;
	 
	        return (int)limit(0,
	            (factor * ((BINS - 1) / 2)) + ((BINS - 1) / 2),
	            BINS - 1);
	    }
	 
	public EnemyWave getClosestSurfableWave() {
        double closestDistance = 50000; // I juse use some very big number here
        EnemyWave surfWave = null;
 
        for (int x = 0; x < _enemyWaves.size(); x++) {
            EnemyWave ew = (EnemyWave)_enemyWaves.get(x);
            double distance = _myLocation.distance(ew.fireLocation)
                - ew.distanceTraveled;
 
            if (distance > ew.bulletVelocity && distance < closestDistance) {
                surfWave = ew;
                closestDistance = distance;
            }
        }
 
        return surfWave;
    }
	
	public void updateWaves(TeamRobot r) {
        for (int x = 0; x < _enemyWaves.size(); x++) {
            EnemyWave ew = (EnemyWave)_enemyWaves.get(x);
 
            ew.distanceTraveled = (r.getTime() - ew.fireTime) * ew.bulletVelocity;
            if (ew.distanceTraveled >
                _myLocation.distance(ew.fireLocation) + 50) {
                _enemyWaves.remove(x);
                x--;
            }
        }
    } 
	
	public double wallSmoothing(Point2D.Double botLocation, double angle, int orientation) {
        while (!_fieldRect.contains(project(botLocation, angle, WALL_STICK))) {
            angle += orientation*0.05;
        }
        return angle;
    }
 
    public static Point2D.Double project(Point2D.Double sourceLocation,
        double angle, double length) {
        return new Point2D.Double(sourceLocation.x + Math.sin(angle) * length,
            sourceLocation.y + Math.cos(angle) * length);
    }
 
    public static double absoluteBearing(Point2D.Double source, Point2D.Double target) {
        return Math.atan2(target.x - source.x, target.y - source.y);
    }
 
    public static double limit(double min, double value, double max) {
        return Math.max(min, Math.min(value, max));
    }
 
    public static double bulletVelocity(double power) {
        return (20.0 - (3.0*power));
    }
 
    public static double maxEscapeAngle(double velocity) {
        return Math.asin(8.0/velocity);
    }
 
    public static void setBackAsFront(TeamRobot robot, double goAngle) {
        double angle =
            Utils.normalRelativeAngle(goAngle - robot.getHeadingRadians());
        if (Math.abs(angle) > (Math.PI/2)) {
            if (angle < 0) {
                robot.setTurnRightRadians(Math.PI + angle);
            } else {
                robot.setTurnLeftRadians(Math.PI - angle);
            }
            robot.setBack(100);
        } else {
            if (angle < 0) {
                robot.setTurnLeftRadians(-1*angle);
           } else {
                robot.setTurnRightRadians(angle);
           }
            robot.setAhead(100);
        }
    }
}
