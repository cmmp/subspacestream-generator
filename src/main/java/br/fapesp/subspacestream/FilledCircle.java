package br.fapesp.subspacestream;

public class FilledCircle extends Circle {

	public FilledCircle(int ndim) {
		super(true, ndim);
	}
	
	@Override
	public Point getInternalPoint() {
		double r = SubspaceStreamGenerator.RNG.nextUniform(0, radius);
		double angle = SubspaceStreamGenerator.RNG.nextUniform(0, 2 * Math.PI);
		Point p = new Point();
		p.x = center[0] + Math.cos(angle) * r;
		p.y = center[1] + Math.sin(angle) * r;
		return p;
	}

	
}
