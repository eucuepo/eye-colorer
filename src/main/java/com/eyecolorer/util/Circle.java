package com.eyecolorer.util;

import java.util.Comparator;

public class Circle {
	public static final Circle INFINITY = new Circle(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);

	public double[] coord; // coord[0] = x, coord[1] = y

	public Circle(double x, double y) {
		coord = new double[] { x, y };
	}

	public double getX() {
		return coord[0];
	}

	public double getY() {
		return coord[1];
	}

	public double distance(Circle p) {
		double dX = getX() - p.getX();
		double dY = getY() - p.getY();
		return Math.sqrt(dX * dX + dY * dY);
	}

	public boolean equals(Circle p) {
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
