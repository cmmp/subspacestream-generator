package br.fapesp.subspacestream;


public class FilledSquare extends Square {

	public FilledSquare(int ndim) {
		super(true, ndim);
	}
	
	@Override
	public Point getInternalPoint() {
		Point p = new Point();
		
		double minX = center[0] - radius;
		double maxX = center[0] + radius;
		double minY = center[1] - radius;
		double maxY = center[1] + radius;
		
		p.x = SubspaceStreamGenerator.RNG.nextUniform(minX, maxX);
		p.y = SubspaceStreamGenerator.RNG.nextUniform(minY, maxY);
		
		return p;
	}

}
