package br.fapesp.subspacestream;

import java.util.ArrayList;
import java.util.UUID;

import br.fapesp.myutils.MyUtils;

public abstract class Shape {
	
	/**
	 * extra separation besides radius
	 */
	private static final double EXTRA_SEP = 5;
	
	/**
	 * max border value
	 */
	protected static final double MAX_B = 0.5;
	
	/**
	 * min radius value
	 */
	private static final double MIN_R = 3; //0.05;
	
	/**
	 * max radius value
	 */
	private static final double MAX_R = 5; //0.2;
	
	/**
	 * min value for generating center
	 */
	private static final double MIN_V = 1; //0.0;
	
	/**
	 * max value for generating center
	 */
	private static final double MAX_V = 150; //1.0;
	
	public double[] center;
	
	public boolean filled;
	
	public boolean[] prefDims;
	
	public UUID uniqueID;
	
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
		this.uniqueID = UUID.randomUUID();
	}
	
	public boolean isFilled() {
		return filled;
	}
	
	@Override
	public String toString() {
		StringBuffer sh = new StringBuffer("[");
		int npref = 0;
		for (int i = 0; i < prefDims.length; i++) if (prefDims[i]) sh.append(i + ",");
		sh.deleteCharAt(sh.length() - 1);
		sh.append("]");
		
		
		return "Shape with center (" + 
					String.format("%.3f", center[0]) + "," + 
				  String.format("%.3f", center[1]) + ") and filled: " + filled + 
				  " prefdims : " + sh.toString() +
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

	/**
	 * method the main generator should use to get a point.
	 * it should already check that the point does not 
	 * intersect with other objects in other dimensions.
	 * the point should already have the noise dimensions
	 * added.
	 * @param futureShapes 
	 * @param activeShapes 
	 * @return
	 */
	public double[] getPoint(ArrayList<Shape> activeShapes, ArrayList<Shape> futureShapes) {
		
		boolean notOkPoint = true;
		double[] retP = new double[prefDims.length];
		
		Point p = null;
		
		if (isFilled()) {
			if (SubspaceStreamGenerator.coinflip()) {
				// get an internal point
				p = this.getInternalPoint();
			}
		}
		
		// get a side point
		if (p == null)
			p = this.getSidePoint();
		
		// check that p does not intersect with any other objects in other dimensions.
		// we already know that shapes do not intersect, so we must only check the 
		// noise dimensions against the preferred dimensions of other shapes.
	
		while (notOkPoint) {
//			System.out.println("trying...");
			int[] prefs = getPrefDims();
			retP[prefs[0]] = p.x; retP[prefs[1]] = p.y;
			
			for (int i = 0; i < prefDims.length; i++)
				if (!prefDims[i])
					retP[i] = SubspaceStreamGenerator.RNG.nextUniform(MIN_V, MAX_V);
			
			notOkPoint = false;
			
			// check the active shapes list:
			for (int i = 0; i < activeShapes.size(); i++) {
				if (activeShapes.get(i) == this) continue;
				prefs = activeShapes.get(i).getPrefDims();
				double[] x1 = new double[] {retP[prefs[0]], retP[prefs[1]]};
				if (SubspaceStreamGenerator.euclideanDistance(x1, activeShapes.get(i).center) <= (activeShapes.get(i).getShapeRadius() + EXTRA_SEP)) {
//					System.out.println("x1: " + MyUtils.arrayToString(x1) + " center: " + MyUtils.arrayToString(activeShapes.get(i).center) + " dist: " + SubspaceStreamGenerator.euclideanDistance(x1,
//							activeShapes.get(i).center) + " radius: " + activeShapes.get(i).getShapeRadius());
					notOkPoint = true;
					break;
				}
			}
			
			if (notOkPoint) continue;
			
			// check the future shapes list:
			for (int i = 0; i < futureShapes.size(); i++) {
				if (futureShapes.get(i) == this) continue;
				prefs = futureShapes.get(i).getPrefDims();
				double[] x1 = new double[] { retP[prefs[0]], retP[prefs[1]] };
				if (SubspaceStreamGenerator.euclideanDistance(x1,
						futureShapes.get(i).center) <= (futureShapes.get(i)
						.getShapeRadius() + EXTRA_SEP)) {
//					System.out.println("x1: " + MyUtils.arrayToString(x1) + " center: " + MyUtils.arrayToString(futureShapes.get(i).center) + " dist: " + SubspaceStreamGenerator.euclideanDistance(x1,
//							futureShapes.get(i).center) + " radius: " + futureShapes.get(i).getShapeRadius());
					notOkPoint = true;
					break;
				}
			}						
		}
		
		return retP;
	}

	/**
	 * get the class: empty or filled object
	 * @return
	 */
	public String getClassVal() {
		return this.uniqueID.toString(); // isFilled() ? "Filled" : "Empty";
	}
	
	protected abstract Point getSidePoint();
	
	protected abstract Point getInternalPoint();
	
	/**
	 * get an array with two elements: the preferred
	 * dimensions of this shape.
	 * 
	 * @return
	 */
	public int[] getPrefDims() {
		int[] pref = new int[2];
		int j = 0;
		for (int i = 0; i < prefDims.length; i++)
			if (prefDims[i])
				pref[j++] = i;
		return pref;
	}

	
}
