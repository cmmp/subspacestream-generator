package br.fapesp.subspacestream;

public class FilledCircle extends Circle {

	public FilledCircle(int ndim) {
		super(true, ndim);
	}
	
	@Override
	public Point getInternalPoint() {
		throw new RuntimeException("Uninmplemented yet");
	}

	
}
