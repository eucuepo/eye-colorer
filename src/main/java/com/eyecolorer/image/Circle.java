package com.eyecolorer.image;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import com.eyecolorer.util.KDTree;

public class Circle {

	private int centroX;
	private int centroY;
	private int radio;
	public static final Circle INFINITY = new Circle(Integer.MAX_VALUE,
			Integer.MAX_VALUE, 0);

	public int[] coord; // coord[0] = x, coord[1] = y

	public Circle(int x, int y, int radio) {
		this.centroX = x;
		this.centroY = y;
		this.radio = radio;
		this.coord = new int[] { x, y };
	}

	public int getX() {
		return coord[0];
	}

	public int getY() {
		return coord[1];
	}

	public int getCentroX() {
		return centroX;
	}

	public void setCentroX(int centroX) {
		this.centroX = centroX;
		this.coord[0] = centroX;
	}

	public int getCentroY() {
		return centroY;
	}

	public void setCentroY(int centroY) {
		this.centroY = centroY;
		this.coord[1] = centroY;
	}

	public int getRadio() {
		return radio;
	}

	public void setRadio(int radio) {
		this.radio = radio;
	}

	/**
	 * Checks if b is inside a
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	private static boolean isCircleInCircle(Circle a, Circle b) {
		// the circle is inside if the distance between the centre is less than
		// the difference in the radius
		int dx = a.getX() - b.getX();
		int dy = a.getY() - b.getY();
		int radiusDifference = a.getRadio() - b.getRadio();
		double centreDistanceSquared = dx * dx + dy * dy;
		return radiusDifference * radiusDifference > centreDistanceSquared;
	}

	public static double distanceTo(Circle a, Circle b) {
		return Math.sqrt((a.centroX - b.centroX) * (a.centroX - b.centroX)
				+ (a.centroY - b.centroY) * (a.centroY - b.centroY));
	}

	public static List<Circle> getConcentricCircunferencia(
			List<Circle> circleList) {
		if (circleList.size() == 1) {
			return circleList;
		}
		HashSet<Circle> toReturn = new HashSet<Circle>();
		List<Circle> aux = new ArrayList<Circle>();
		for (Circle circle : circleList) {
			for (Circle toCheck : circleList) {
				// if the circles are not the same and one is inside another,
				if (!toCheck.equals(circle)
						&& isCircleInCircle(circle, toCheck)) {
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
	 * Get the closest centers
	 */
	public static Circle[] getClosestCenters(List<Circle> points) {
		// TODO: List of points a lista de circunferencias
		// List<Circulo> aux = new ArrayList<Circulo>();
		Circle[] eye = new Circle[2];

		// solo hay dos, perfect match
		if (points.size() == 2) {
			System.out.println("Bingo!");
			eye[1] = points.get(0).getRadio() > points.get(1).getRadio() ? points
					.get(0) : points.get(1);
			eye[0] = points.get(0).getRadio() <= points.get(1).getRadio() ? points
					.get(0) : points.get(1);
			return eye;
		}
		double distance = Double.POSITIVE_INFINITY;
		// TODO: Quitar esta mierda

		// for (Circulo circunferencia : points) {
		// aux.add(new Circulo(circunferencia.centroX, circunferencia.centroY,
		// circunferencia.radio));
		// }

		Circle[] closest = new Circle[points.size()];

		KDTree tree = new KDTree(points, 0); // WILL MODIFY 'points'

		for (int i = 0; i < points.size(); i++) {
			closest[i] = tree.findClosest(points.get(i));
		}

		for (int i = 0; i < points.size(); i++) {
			double calculatedDistance = points.get(i).distance(closest[i]);
			if (calculatedDistance < distance) {
				distance = calculatedDistance;

				if (closest[i].getRadio() > points.get(i).getRadio()) {
					eye[0] = points.get(i); // pupil
					eye[1] = closest[i]; // iris
				} else if (closest[i].getRadio() != points.get(i).getRadio()) {
					eye[0] = closest[i]; // iris
					eye[1] = points.get(i); // pupil
				}
			}

			System.out.println(points.get(i) + " is closest to " + closest[i]
					+ " the distance is" + calculatedDistance);
		}
		return eye;
	}

	public double distance(Circle p) {
		double dX = getX() - p.getX();
		double dY = getY() - p.getY();
		return Math.sqrt(dX * dX + dY * dY);
	}

	public boolean equalsPoint(Circle p) {
		return (getX() == p.getX()) && (getY() == p.getY());
	}

	public String toString() {
		return "(" + getX() + ", " + getY() + ")";
	}

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
