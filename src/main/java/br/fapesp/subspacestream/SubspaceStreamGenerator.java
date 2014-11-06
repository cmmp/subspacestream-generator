package br.fapesp.subspacestream;

import java.util.ArrayList;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.math3.random.RandomDataGenerator;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import br.fapesp.myutils.MyUtils;

/**
 * This program creates a sequence of geometric objects embedded in subspaces of a 
 * high dimensional data stream.
 * 
 * @author Cássio M. M. Pereira
 *
 */
public class SubspaceStreamGenerator {
	
	private boolean verbose;
	private int nactive;
	private int ndim;
	private ArrayList<Shape> activeShapes = new ArrayList<Shape>();
	private ArrayList<Shape> futureShapes = new ArrayList<Shape>();
	private double time = 0;
	private double wActive = 0, wFuture = 0;
	private double lambda;
	private long seed;
	private String outFileName;
	public static RandomDataGenerator RNG = new RandomDataGenerator();
	private int N;
	private Instances dataset;
	
	public static double euclideanDistance(double[] p1, double[] p2) {
		if (p1.length != p2.length)
			throw new RuntimeException("arrays have different length!");
		
		double sum = 0;
		for (int i = 0; i < p1.length; i++)
			sum += (p1[i] - p2[i]) * (p1[i] - p2[i]);
		return Math.sqrt(sum);
	}
	
	public SubspaceStreamGenerator(boolean verbose, int nactive, double lambda, long seed,
			String outFileName, int ndim, int N) {
		this.verbose = verbose;
		this.nactive = nactive;
		this.lambda = lambda;
		this.seed = seed;
		SubspaceStreamGenerator.RNG.reSeed(seed);
		this.outFileName = outFileName;
		this.ndim = ndim;
		this.N = N;
	}
	
	public static void main(String[] args) {
		Options options = new Options();
		
		options.addOption("v", false, "Verbose");
		options.addOption("h", false, "Display help");
		
		options.addOption("a", true, "Number of active shapes (default: 4)");
		options.addOption("s", true, "Seed for the RNG (default: 1234)");
		options.addOption("o", true, "Output file (default: subspace_stream.arff)");
		options.addOption("d", true, "Number of dimensions (default: 4)");
		options.addOption("N", true, "Number of points (default: 10000)");
		options.addOption("l", true, "Lambda for weight function (default 0.0006)");
		
		CommandLineParser parser = new BasicParser();
		CommandLine cmd = null;
		
		HelpFormatter formatter = new HelpFormatter();
		
		try {
			cmd = parser.parse(options, args);
		} catch(ParseException e) {
			formatter.printHelp("SubspaceStreamGenerator", options);
		}
		
		if (cmd.hasOption("h")) {
			formatter.printHelp("SubspaceStreamGenerator", options);
			System.exit(0);
		}
		
		SubspaceStreamGenerator sub = new SubspaceStreamGenerator(
				cmd.hasOption("v"), 
				cmd.hasOption("a") ? Integer.parseInt(cmd.getOptionValue("a")) : 4, 
				cmd.hasOption("l") ? Double.parseDouble(cmd.getOptionValue("l")) : 0.0006,
				cmd.hasOption("s") ? Long.parseLong(cmd.getOptionValue("s")) : 1234,		
				cmd.hasOption("o") ? cmd.getOptionValue("o") : "subspace_stream.arff",
				cmd.hasOption("d") ? Integer.parseInt(cmd.getOptionValue("d")) : 4,
				cmd.hasOption("N") ? Integer.parseInt(cmd.getOptionValue("N")) : 10000);
		sub.genDataSet();
	}
	
	private double weightDecayFunction(double time) {
		return Math.pow(2.0, -this.lambda * time);
	}

	private void genDataSet() {
		if (verbose) {
			MyUtils.printRep('-', 80);
			System.out.println("Starting data generation...");
			MyUtils.printTimestamp();
			MyUtils.printRep('-', 80);
		}
		
		// create data set:
		ArrayList<Attribute> atts = new ArrayList<Attribute>();
		ArrayList<String> classValues = new ArrayList<String>();
		
		for (int i = 0; i < this.ndim; i++)
			atts.add(new Attribute("X" + (i + 1)));
		
		classValues.add("Filled");
		classValues.add("Empty");
		Attribute clazz = new Attribute("class", classValues);
		atts.add(clazz);
		
		this.dataset = new Instances("SubspaceTopologicalStream", atts, this.N);
		this.dataset.setClass(clazz);
		
		if (verbose) {
			System.out.println("filling active shapes:");
			MyUtils.printRep('-', 80);
		}
		
		fillShapes(activeShapes); // fill the active shapes list
		
		if (verbose) {
			MyUtils.printRep('-', 80);
			System.out.println("filling future shapes:");
		}
		
		fillShapes(futureShapes); // fill the future shapes list
		
		if (verbose)
			MyUtils.printRep('-', 80);
		
		ArrayList<Integer> actIds = new ArrayList<Integer>();
		ArrayList<Integer> futureIds = new ArrayList<Integer>();
		
		for (int i = 0; i < this.nactive; i++) {actIds.add(i); futureIds.add(i);}
		
		// MAIN LOOP - generate points:
		for (int i = 0; i < this.N; i++) {
			time++;
			
			wActive = weightDecayFunction(time);
			wFuture = 1.0 - wActive;
			
			if (wActive < 0.1) { 
				// current active models are very outdated,
				// exchange them for the future ones and replace
				// the future ones for a new batch of models.
				if (verbose) {
					MyUtils.printRep('-', 80);
					System.out.println("Making the switch of future models to a new batch at t = " + i + ":");
				}
				
				activeShapes.clear();
				activeShapes.addAll(futureShapes);
								
				fillShapes(futureShapes); // fill future shapes with a new batch
				
				if (verbose)
					MyUtils.printRep('-', 80);
				
				time = 0;
			}
			
			// generate the next point in the stream:
			
			double val = RNG.nextUniform(0, 1);
			double[] p;
			String classVal;
			
			if (wActive <= val) { // choose from active models
				int id = (Integer) RNG.nextSample(actIds, 1)[0];
				p = activeShapes.get(id).getPoint(activeShapes, futureShapes);
				classVal = activeShapes.get(id).getClassVal();
			} else { // choose from future models
				int id = (Integer) RNG.nextSample(futureIds, 1)[0];
				p = futureShapes.get(id).getPoint(activeShapes, futureShapes);
				classVal = futureShapes.get(id).getClassVal();
			}
			
			Instance inst = new DenseInstance(atts.size());
			inst.setDataset(this.dataset);
			for (int j = 0; j < this.ndim; j++)
				inst.setValue(j, p[j]);
			inst.setClassValue(classVal);
			
			this.dataset.add(inst);
			
		} // END MAIN POINT GENERATION LOOP
		
		// TODO save data set to disk:
		
		if (verbose) {
			MyUtils.printRep('-', 80);
			System.out.println("Ended data generation...");
			MyUtils.printTimestamp();
			MyUtils.printRep('-', 80);
		}
	}
	
	private void fillShapes(ArrayList<Shape> list) {
		
		list.clear();
		
		for (int i = 0; i < nactive; i++) {
			Shape next = null;
			while (true) {
				boolean noIntersect = true;
				if (coinflip()) { // create a square
					if (coinflip()) { // create an empty square
						next = new Square(ndim);
					} else { // create a filled square
						next = new FilledSquare(ndim);
					}
				} else { // create a circle
					if (coinflip()) { // create an empty circle
						next = new Circle(ndim);
					} else { // create a filled circle
						next = new FilledCircle(ndim);
					}
				}
				
				// check if this shape does not intersect with any other
				// in active or future
				for (int j = 0; j < activeShapes.size(); j++)
					if (next.intersectsWith(activeShapes.get(j))) {
						noIntersect = false;
						break;
					}
				if (noIntersect) {
					for (int j = 0; j < futureShapes.size(); j++)
						if (next.intersectsWith(futureShapes.get(j))) {
							noIntersect = false;
							break;
						}							
				}
								
				if (noIntersect) {
//					System.out.println("just created the following shape:");
					System.out.println(next);
					list.add(next);
					break;
				}
			}
		}
		
	}
	
	public static boolean coinflip() {
		return RNG.nextUniform(0, 1) >= 0.5 ? true : false;
	}

}
