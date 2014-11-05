package br.fapesp.subspacestream;

public class Circle extends Shape {

	public Circle(int ndim) {
		super(false, ndim);
	}
	
	public Circle(boolean filled, int ndim) {
		super(true, ndim);
	}
	
	@Override
	public Point getSidePoint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getShapeRadius() {
		return super.radius;
	}

	@Override
	public Point getInternalPoint() {
		throw new RuntimeException("This is not a filled circle!");
	}

}
