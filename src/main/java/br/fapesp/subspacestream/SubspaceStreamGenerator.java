package br.fapesp.subspacestream;

import java.util.ArrayList;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.math3.random.RandomDataGenerator;

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
	private ArrayList<Shape> activeShapes = new ArrayList<Shape>();
	private ArrayList<Shape> futureShapes = new ArrayList<Shape>();
	private double time = 0;
	private double wActive = 0, wFuture = 0;
	private double lambda;
	private long seed;
	private String outFileName;
	public static RandomDataGenerator RNG = new RandomDataGenerator();
	
	public SubspaceStreamGenerator(boolean verbose, int nactive, double lambda, long seed,
			String outFileName) {
		this.verbose = verbose;
		this.nactive = nactive;
		this.lambda = lambda;
		this.seed = seed;
		SubspaceStreamGenerator.RNG.reSeed(seed);
		this.outFileName = outFileName;
	}
	
	public static void main(String[] args) {
		Options options = new Options();
		
		options.addOption("v", false, "Verbose");
		options.addOption("h", false, "Display help");
		
		options.addOption("a", true, "Number of active shapes (default: 4)");
		options.addOption("s", true, "Seed for the RNG (default: 1234)");
		options.addOption("o", true, "Output file (default: subspace_stream.arff)");
		
		Option optLambda = OptionBuilder.withArgName("arg").hasArg().withDescription("Lambda for weight function (default 0.1)").create("l");
		optLambda.setRequired(false);
		options.addOption(optLambda);
		
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
				cmd.hasOption("l") ? Double.parseDouble(cmd.getOptionValue("l")) : 0.1,
				cmd.hasOption("s") ? Long.parseLong(cmd.getOptionValue("s")) : 1234,		
				cmd.hasOption("o") ? cmd.getOptionValue("o") : "subspace_stream.arff");
		sub.genDataSet();
	}
	
	private double weightDecayFunction(double time) {
		return Math.pow(2.0, - this.lambda * time);
	}

	private void genDataSet() {
		if (verbose) {
			MyUtils.printRep('-', 80);
			System.out.println("Starting data generation...");
			MyUtils.printTimestamp();
		}
		
		// create the active shapes:
		
		
		
		
		if (verbose) {
			MyUtils.printRep('-', 80);
			System.out.println("Ended data generation...");
			MyUtils.printTimestamp();
			MyUtils.printRep('-', 80);
		}
	}

}
