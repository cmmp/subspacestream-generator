package br.fapesp.subspacestream;

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
	
	

}
