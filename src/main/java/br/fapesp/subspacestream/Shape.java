package br.fapesp.subspacestream;

public abstract class Shape {
	
	public double[] center;
	public boolean filled;
	
	/**
	 * radius for circles and half the length of the side for a square
	 */
	public double radius;

	public abstract Point getSidePoint();
	
	public boolean isFilled() {
		return filled;
	}
	
}
