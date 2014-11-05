package br.fapesp.subspacestream;

import javax.management.RuntimeErrorException;

public class FilledSquare extends Square {

	public FilledSquare(int ndim) {
		super(true, ndim);
	}
	
	@Override
	public Point getInternalPoint() {
		throw new RuntimeException("Uninmplemented yet");
	}

}
