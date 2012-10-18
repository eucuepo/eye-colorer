package com.eyecolorer.gui;

import java.util.ArrayList;
import java.util.List;

import com.eyecolorer.util.Circle;

public class Program {
	public static void main(String[] args) {
		
		//TODO: List of points a lista de circunferencias
		List<Circle> points = generatePoints();
		Circle[] closest = new Circle[points.size()];

		//KDTree tree = new KDTree(points, 0); // WILL MODIFY 'points'

		for (int i = 0; i < points.size(); i++) {
			//closest[i] = tree.findClosest(points.get(i));
		}

		for (int i = 0; i < points.size(); i++) {
			System.out.println(points.get(i) + " is closest to " + closest[i]+" the distance is"+ points.get(i).distance(closest[i]));
		}
	}

	private static List<Circle> generatePoints() {
		ArrayList<Circle> points = new ArrayList<Circle>();
//		circunferencia 1: centro X: 210 centroY: 51 radio: 21
//		circunferencia 2: centro X: 185 centroY: 121 radio: 47
//		circunferencia 3: centro X: 187 centroY: 53 radio: 13
		
		points.add(new Circle(210,51));
		points.add(new Circle(185,121));
		points.add(new Circle(187,53));

		return points;
	}
}



