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
		Point p = new Point();
		
		int side = SubspaceStreamGenerator.RNG.nextInt(1, 4);
		double minX, maxX, minY, maxY;
		
		switch (side) {
		case 1: // top
			p.y = center[1] + radius;
			minX = center[0] - radius;
			maxX = center[0] + radius;
			p.x = SubspaceStreamGenerator.RNG.nextUniform(minX, maxX);
			break;
		case 2: // bottom
			p.y = center[1] - radius;
			minX = center[0] - radius;
			maxX = center[0] + radius;
			p.x = SubspaceStreamGenerator.RNG.nextUniform(minX, maxX);
			break;
		case 3: // left
			p.x = center[0] - radius;
			minY = center[1] - radius;
			maxY = center[1] + radius;
			p.y = SubspaceStreamGenerator.RNG.nextUniform(minY, maxY);
			break;
		case 4: // right
			p.x = center[0] + radius;
			minY = center[1] - radius;
			maxY = center[1] + radius;
			p.y = SubspaceStreamGenerator.RNG.nextUniform(minY, maxY);
			break;
		}
		
		return p;
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
