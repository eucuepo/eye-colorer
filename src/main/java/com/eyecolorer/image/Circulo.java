package com.eyecolorer.image;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import com.eyecolorer.util.KDTree;

import jiputil.Circunferencia;

public class Circulo extends Circunferencia {

	public static final Circulo INFINITY = new Circulo(Integer.MAX_VALUE, Integer.MAX_VALUE);

	public int[] coord; // coord[0] = x, coord[1] = y

	public Circulo(int x, int y) {
		super();
		super.centroX = x;
		super.centroY = y;
		coord = new int[] { x, y };
	}

	public Circulo(int x, int y, int radio) {
		super();
		super.centroX = x;
		super.centroY = y;
		super.radio = radio;
		coord = new int[] { x, y };
	}

	public int getX() {
		return coord[0];
	}

	public int getY() {
		return coord[1];
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
	private static boolean isCircleInCircle(Circulo a, Circulo b) {
		// the circle is inside if the distance between the centre is less than
		// the difference in the radius
		int dx = a.getX() - b.getX();
		int dy = a.getY() - b.getY();
		int radiusDifference = a.getRadio() - b.getRadio();
		double centreDistanceSquared = dx * dx + dy + dy;
		return radiusDifference * radiusDifference > centreDistanceSquared;
	}

	public static List<Circulo> getConcentricCircles(List<Circulo> circleList) {
		HashSet<Circulo> toReturn = new HashSet<Circulo>();
		List<Circulo> aux = new ArrayList<Circulo>();
		for (Circulo circle : circleList) {
			for (Circulo toCheck : circleList) {
				// if the circles are not the same and one is inside another,
				if (!toCheck.equals(circle) && isCircleInCircle(circle, toCheck)) {
					// add both to the hashset
					aux.add(circle);
					aux.add(toCheck);
				}
			}
		}
		aux.addAll(toReturn);

		return aux;
	}

	public static double distanceTo(Circunferencia a, Circunferencia b) {
		return Math.sqrt((a.centroX - b.centroX) * (a.centroX - b.centroX) + (a.centroY - b.centroY) * (a.centroY - b.centroY));
	}

	private static boolean isCircleInCircle(Circunferencia a, Circunferencia b) {
		// the circle is inside if the distance between the centre is less than
		// the difference in the radius
		double dx = a.centroX - b.centroX;
		double dy = a.centroY - b.centroY;
		double radiusDifference = a.radio - b.radio;
		double centreDistanceSquared = dx * dx + dy + dy;
		return radiusDifference * radiusDifference > centreDistanceSquared;
	}

	public static List<Circunferencia> getConcentricCircunferencia(List<Circunferencia> circleList) {
		HashSet<Circunferencia> toReturn = new HashSet<Circunferencia>();
		List<Circunferencia> aux = new ArrayList<Circunferencia>();
		for (Circunferencia circle : circleList) {
			for (Circunferencia toCheck : circleList) {
				// if the circles are not the same and one is inside another,
				if (!toCheck.equals(circle) && isCircleInCircle(circle, toCheck)) {
					// add both to the hashset
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
	public List<Circunferencia> getClosestCenters(List<Circunferencia> points) {
		// TODO: List of points a lista de circunferencias
		List<Circulo> toReturn = new ArrayList<Circulo>();
		for (Circunferencia circunferencia : points) {
			toReturn.add(new Circulo(circunferencia.centroX, circunferencia.centroY));
		}

		Circulo[] closest = new Circulo[toReturn.size()];

		KDTree tree = new KDTree(toReturn, 0); // WILL MODIFY 'points'

		for (int i = 0; i < toReturn.size(); i++) {
			closest[i] = tree.findClosest(toReturn.get(i));
		}

		for (int i = 0; i < toReturn.size(); i++) {
			System.out.println(toReturn.get(i) + " is closest to " + closest[i] + " the distance is" + toReturn.get(i).distance(closest[i]));
		}
		// FIXME: Anadir casting
		return points;

	}

	public double distance(Circulo p) {
		double dX = getX() - p.getX();
		double dY = getY() - p.getY();
		return Math.sqrt(dX * dX + dY * dY);
	}

	public boolean equals(Circulo p) {
		return (getX() == p.getX()) && (getY() == p.getY());
	}

	public String toString() {
		return "(" + getX() + ", " + getY() + ")";
	}

	public static class PointComp implements Comparator<Circulo> {
		int d; // the dimension to compare in (0 => x, 1 => y)

		public PointComp(int dimension) {
			d = dimension;
		}

		public int compare(Circulo a, Circulo b) {
			return (int) (a.coord[d] - b.coord[d]);
		}
	}

}
