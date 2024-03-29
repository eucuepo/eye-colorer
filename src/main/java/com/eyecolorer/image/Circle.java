package com.eyecolorer.image;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;

import com.eyecolorer.util.KDTree;

/**
 * Class that models a circle to be used in detection algorithms A circle is
 * defined by its center coordinates and the radius
 * 
 * @author ecuevas
 * 
 */
public class Circle {

	private static Logger log = Logger.getLogger(Circle.class.getName());

	private int centerX;
	private int centerY;
	private int radius;
	public static final Circle INFINITY = new Circle(Integer.MAX_VALUE, Integer.MAX_VALUE, 0);

	public int[] coord; // coord[0] = x, coord[1] = y

	public Circle(int x, int y, int radius) {
		this.centerX = x;
		this.centerY = y;
		this.radius = radius;
		this.coord = new int[] { x, y };
	}

	public int getX() {
		return coord[0];
	}

	public int getY() {
		return coord[1];
	}

	public int getCenterX() {
		return centerX;
	}

	public void setCenterX(int centroX) {
		this.centerX = centroX;
		this.coord[0] = centroX;
	}

	public int getCenterY() {
		return centerY;
	}

	public void setCenterY(int centroY) {
		this.centerY = centroY;
		this.coord[1] = centroY;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radio) {
		this.radius = radio;
	}

	/**
	 * Checks if b is inside a
	 * 
	 * @param a
	 *            Circle
	 * @param b
	 *            Circle
	 * @return
	 */
	private static boolean isCircleInCircle(Circle a, Circle b) {
		// the circle is inside if the distance between the centre is less than
		// the difference in the radius
		int dx = a.getX() - b.getX();
		int dy = a.getY() - b.getY();
		int radiusDifference = a.getRadius() - b.getRadius();
		double centreDistanceSquared = dx * dx + dy * dy;
		return radiusDifference * radiusDifference > centreDistanceSquared;
	}

	public static List<Circle> getConcentricCircunferencia(List<Circle> circleList) {
		if (circleList.size() == 1) {
			return circleList;
		}
		HashSet<Circle> toReturn = new HashSet<Circle>();
		List<Circle> aux = new ArrayList<Circle>();
		for (Circle circle : circleList) {
			for (Circle toCheck : circleList) {
				// if the circles are not the same and one is inside another,
				if (!toCheck.equals(circle) && isCircleInCircle(circle, toCheck)) {
					// if they are not intersected
					toReturn.add(circle);
					toReturn.add(toCheck);
				}
			}
		}
		aux.addAll(toReturn);

		return aux;
	}

	/**
	 * Algorithm used to get the closest centers in a list of circles The two
	 * closest are supposed to be iris an pupil
	 * 
	 * @param points
	 * @return
	 */
	public static Circle[] getClosestCenters(List<Circle> points) {
		Circle[] eye = new Circle[2];

		// only 2, perfect match
		if (points.size() == 2) {
			eye[1] = points.get(0).getRadius() > points.get(1).getRadius() ? points.get(0) : points.get(1);
			eye[0] = points.get(0).getRadius() <= points.get(1).getRadius() ? points.get(0) : points.get(1);
			return eye;
		}
		double distance = Double.POSITIVE_INFINITY;

		Circle[] closest = new Circle[points.size()];

		// init the KDTree
		KDTree tree = new KDTree(points, 0);

		// find the closest to each circle
		for (int i = 0; i < points.size(); i++) {
			closest[i] = tree.findClosest(points.get(i));
		}

		// get the two closest
		for (int i = 0; i < points.size(); i++) {
			double calculatedDistance = points.get(i).distance(closest[i]);
			if (calculatedDistance < distance) {
				distance = calculatedDistance;

				if (closest[i].getRadius() > points.get(i).getRadius()) {
					eye[0] = points.get(i); // pupil
					eye[1] = closest[i]; // iris
				} else if (closest[i].getRadius() != points.get(i).getRadius()) {
					eye[0] = closest[i]; // iris
					eye[1] = points.get(i); // pupil
				}
			}

			log.debug(points.get(i) + " is closest to " + closest[i] + " the distance is" + calculatedDistance);
		}
		return eye;
	}

	/**
	 * Gets the distance between this circle and the circle provided
	 * 
	 * @param p
	 *            The circle to compare
	 * @return
	 */
	public double distance(Circle p) {
		double dX = getX() - p.getX();
		double dY = getY() - p.getY();
		return Math.sqrt(dX * dX + dY * dY);
	}

	/**
	 * Check if two points are the same
	 * 
	 * @param p
	 * @return
	 */
	public boolean equalsPoint(Circle p) {
		return (getX() == p.getX()) && (getY() == p.getY());
	}

	/**
	 * String representation of the circle
	 */
	public String toString() {
		return "(" + getX() + ", " + getY() + " , " + radius + ")";
	}

	/**
	 * Comparator class that compares circles
	 * 
	 */
	public static class PointComp implements Comparator<Circle> {
		int d; // the dimension to compare in (0 => x, 1 => y)

		public PointComp(int dimension) {
			d = dimension;
		}

		public int compare(Circle a, Circle b) {
			return (int) (a.coord[d] - b.coord[d]);
		}
	}

}
