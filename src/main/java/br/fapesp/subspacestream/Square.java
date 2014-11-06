package br.fapesp.subspacestream;

import java.util.ArrayList;

public class Square extends Shape {
	
	private static final double SQRT2 = Math.sqrt(2.0);
	
	public Square(int ndim) {
		super(false, ndim);
	}
	
	public Square(boolean filled, int ndim) {
		super(filled, ndim);
	}

	@Override
	public Point getSidePoint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getShapeRadius() {
		return SQRT2 * super.radius;
	}

	@Override
	public Point getInternalPoint() {
		throw new RuntimeException("This is not a filled square!");
	}

	@Override
	public String getClassVal() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
