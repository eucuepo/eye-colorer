package com.eyecolorer.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.eyecolorer.image.Circle;

/**
 * Implementation of the K-D-Tree algorithm http://en.wikipedia.org/wiki/K-d_tree
 * Used to look for the closest circles (iris and pupil center) in a circle vector
 * @author ecuevas
 *
 */
public class KDTree {
	// 2D k-d tree
	private KDTree childA, childB;
	private Circle point; // defines the boundary
	private int d; // dimension: 0 => left/right split, 1 => up/down split

	public KDTree(List<Circle> points, int depth) {
		childA = null;
		childB = null;
		d = depth % 2;

		// find median by sorting in dimension 'd' (either x or y)
		Comparator<Circle> comp = new Circle.PointComp(d);
		Collections.sort(points, comp);

		int median = (points.size() - 1) / 2;
		point = points.get(median);

		// Create childA and childB recursively.
		// WARNING: subList() does not create a true copy,
		// so the original will get modified.
		if (median > 0) {
			childA = new KDTree(points.subList(0, median), depth + 1);
		}
		if (median + 1 < points.size()) {
			childB = new KDTree(points.subList(median + 1, points.size()), depth + 1);
		}
	}

	/**
	 * Find closest circle given another
	 * @param target
	 * @return
	 */
	public Circle findClosest(Circle target) {
		Circle closest = point.equalsPoint(target) ? Circle.INFINITY : point;
		double bestDist = closest.distance(target);
		double spacing = target.coord[d] - point.coord[d];
		KDTree rightSide = (spacing < 0) ? childA : childB;
		KDTree otherSide = (spacing < 0) ? childB : childA;

		/*
		 * The 'rightSide' is the side on which 'target' lies and the
		 * 'otherSide' is the other one. It is possible that 'otherSide' will
		 * not have to be searched.
		 */

		if (rightSide != null) {
			Circle candidate = rightSide.findClosest(target);
			if (candidate.distance(target) < bestDist) {
				closest = candidate;
				bestDist = closest.distance(target);
			}
		}

		if (otherSide != null && (Math.abs(spacing) < bestDist)) {
			Circle candidate = otherSide.findClosest(target);
			if (candidate.distance(target) < bestDist) {
				closest = candidate;
				bestDist = closest.distance(target);
			}
		}

		return closest;
	}
}