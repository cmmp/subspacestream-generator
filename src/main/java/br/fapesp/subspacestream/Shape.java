package br.fapesp.subspacestream;

import java.util.ArrayList;

import javax.management.RuntimeErrorException;

import br.fapesp.myutils.MyUtils;

public abstract class Shape {
	
	/**
	 * min radius value
	 */
	private static final double MIN_R = 0.1;
	
	/**
	 * max radius value
	 */
	private static final double MAX_R = 0.3;
	
	/**
	 * min value for generating center
	 */
	private static final double MIN_V = 0.0;
	
	/**
	 * max value for generating center
	 */
	private static final double MAX_V = 1.0;
	
	public double[] center;
	
	public boolean filled;
	
	public boolean[] prefDims;
	
	/**
	 * radius for circles and half the length of the side for a square
	 */
	public double radius;
	
	public Shape(boolean filled, int ndim) {
		this.filled = filled;
		this.center = new double[] {
				SubspaceStreamGenerator.RNG.nextUniform(MIN_V, MAX_V),
				SubspaceStreamGenerator.RNG.nextUniform(MIN_V, MAX_V),
		};
		ArrayList<Integer> dims = new ArrayList<Integer>();
		for(int i = 0; i < ndim; i++) dims.add(i);
		Object[] sel = SubspaceStreamGenerator.RNG.nextSample(dims, 2);
		prefDims = new boolean[ndim];
		for (int i = 0; i < ndim; i++)
			if (i == (Integer) sel[0] || i == (Integer) sel[1])
				prefDims[i] = true;
		this.radius = SubspaceStreamGenerator.RNG.nextUniform(MIN_R, MAX_R);
	}
	

	public abstract Point getSidePoint();
	public abstract Point getInternalPoint();
	
	public boolean isFilled() {
		return filled;
	}
	
	@Override
	public String toString() {
		return "Shape with center (" + 
					String.format("%.3f", center[0]) + "," + 
				  String.format("%.3f", center[1]) + ") and filled: " + filled + 
				  " prefdims : " + MyUtils.arrayToString(prefDims) +
				  " radius: " + String.format("%.3f", radius); 
	}
	
	public abstract double getShapeRadius();
	
	public boolean intersectsWith(Shape s) {
		ArrayList<Integer> intersectDims = getIntersectionDims(this, s);
		
		// the maximum intersection is 2, as the objects always reside
		// in a 2-d manifold, i.e., they always prefer only two dimensions.
		if (intersectDims.size() > 2)
			throw new RuntimeException("Got more than 2 intersecting dimensions!");
				
		// if they intersect in only one dimension, 
		// then they will at most touch on a line, 
		// but will be separated by the other dimension.
		if (intersectDims.size() <= 1) 
			return false;
		
		// if there are 2 intersecting dimensions, compute the euclidean distance
		// between their centers and check if it is greater than
		// the sum of both shapes' radiuses.
		if (SubspaceStreamGenerator.euclideanDistance(this.center, s.center) > (this.getShapeRadius() + s.getShapeRadius()))
			return false;
		
		return true;
	}

	private static ArrayList<Integer> getIntersectionDims(Shape s1, Shape s2) {
		ArrayList<Integer> intersection = new ArrayList<Integer>();
		for (int i = 0; i < s1.prefDims.length; i++)
			if (s1.prefDims[i] && s2.prefDims[i])
				intersection.add(i);
		return intersection;
	}
	
}
