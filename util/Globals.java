package util;

import util.MersenneTwisterFast;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Random;

import obj.InfluenceMatrix;
import obj.Landscape;

public class Globals {
	/**
	 * simulation parameters: default values
	 */
    public static int temporalType = 0;//1: no overlap, 2: some overlap, 0: no temp diff
    public static int spatialType = 0;//1: with spatial diff, 0: no spatial diff
    public static double lossPossibility=0.25;
    public static double distortionPossibility=0.25;
    public static double weight=1.0;
    
    
	public static int N = 8;
//	public static int K = 2; // no need
	public static int periods = 20;
//	private static String outfilename = "results/test.txt";
	private static String outfilename = "results/joint_n16k0_0.txt";
	private static String influenceMatrixFile = "conf/n16k0.txt";
//	public static int numOrgs = 10;
	public static int numOrgs = 100;
		public static Landscape landscape;
//	public static String reportLevel = "details";
	public static String reportLevel = "summary";
	public static boolean debug = false;
	public static boolean replicate = false; 
//	private static long seed = 1261505528597l;
	public static double landscapeMax;
	public static int numRuns = 1;
	public static int startLandscapeID;
        public static int numSubOrgs = 2;
	/**
	 * utils
	 */
	public static long runID = System.currentTimeMillis(); // need?
//	private static long runID = 1261505528597l;
	public static PrintWriter out;
	public static MersenneTwisterFast rand = new MersenneTwisterFast();
//	public static MersenneTwisterFast nkrnd = new MersenneTwisterFast(seed);
	public static Random random = new Random();

	public static void loadGlobals(String configFile) {
		if (!configFile.equals("")) {
			Properties p = new Properties();
			try {
				p.load(new FileInputStream(configFile));
				// simulation parameters
//				seed = Long.parseLong(p.getProperty("seed"));
                                
                                
                                temporalType=Integer.parseInt(p.getProperty("temporalType"));
                                spatialType=Integer.parseInt(p.getProperty("spatialType"));
                                lossPossibility=Double.parseDouble(p.getProperty("lossPossibility"));
                                distortionPossibility=Double.parseDouble(p.getProperty("distortionPossibility"));
                                weight=Double.parseDouble(p.getProperty("alignment"));
                                
                                
				periods = Integer.parseInt(p.getProperty("periods"));
				numRuns = Integer.parseInt(p.getProperty("runs"));
				outfilename = p.getProperty("outfile");
				influenceMatrixFile = p.getProperty("influenceMatrix");
				numOrgs = Integer.parseInt(p.getProperty("numOrgs"));
//				overlap = Integer.parseInt(p.getProperty("overlap"));
				N = Integer.parseInt(p.getProperty("N"));
                                numSubOrgs = Integer.parseInt(p.getProperty("numSubOrgs"));
				// [added 3/24/12]
				String startLandscapeIDStr = p.getProperty("startLandscapeID");
				if (startLandscapeIDStr == null) {
					startLandscapeID = 0;
				} else {
					startLandscapeID = Integer.parseInt(startLandscapeIDStr);
				}
				// [end add]
				reportLevel = p.getProperty("reportLevel");
				String debugString = p.getProperty("debug");
				if (debugString.equals("true") || debugString.equals("1")) { debug = true; }
				
			} catch (Exception e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
			} // END try..catch

			// calculate derived values if any
//			numAlternatives = (int)(N / numSubOrgs);

		}  // end if confFile
		
		try {
			// create output printwriter
			out = new PrintWriter(new FileOutputStream(outfilename, true), true);
		} catch (IOException io) {
			System.err.println(io.getMessage());
			io.printStackTrace();
		}
	}
	
	public static void createLandscape(int id) {
		landscape  = new Landscape(id, new InfluenceMatrix(influenceMatrixFile));
		landscapeMax = landscape.getMaxFitness();
	}
	
	public static void setRandomNumbers(int intRunID) {
		long runID;
		if (replicate) { runID = (long)intRunID;
		} else { runID = System.currentTimeMillis(); }
		rand = new MersenneTwisterFast(runID);
	} 
	

	public static void main(String[] args) {
//		long runID = 1261505528597l;
		long runID = Long.parseLong(args[0]);
		System.out.println(runID);
	}
}
