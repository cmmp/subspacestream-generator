package br.fapesp.subspacestream;

import java.util.ArrayList;

public class Circle extends Shape {

	public Circle(int ndim) {
		super(false, ndim);
	}
	
	public Circle(boolean filled, int ndim) {
		super(true, ndim);
	}
	
	@Override
	public Point getSidePoint() {
		double angle = SubspaceStreamGenerator.RNG.nextUniform(0, 2 * Math.PI);
		double border = SubspaceStreamGenerator.RNG.nextUniform(0, Shape.MAX_B);
		
		Point p = new Point();
		p.x = center[0] + Math.cos(angle) * (radius - border);
		p.y = center[1] + Math.sin(angle) * (radius - border);
		return p;
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
