package app;

import util.Globals;
import obj.Organization;
import java.util.Vector;
import java.util.Iterator;
import java.io.PrintWriter;
import java.util.Calendar;

import util.StatCalc;

public class Simulation {
	private static Vector<Organization> organizations; //= new Vector<Organization>();
	
	public static void main(String args[]) {
            long millisStart = Calendar.getInstance().getTimeInMillis();
           // System.out.println("system millisecond is "+millisStart);
		String configFile = setConfigFile(args);
		Globals.loadGlobals(configFile);
		if (Globals.debug) { System.out.println("configFile loaded"); }

		for (int j = Globals.startLandscapeID; j < Globals.numRuns; j++) {
			Globals.setRandomNumbers(j); //set the seed of rand to make results replicable
			// create landscape
			Globals.createLandscape(j);
			//Globals.landscape.printLandscapeFitness();
			if (Globals.debug) { System.out.println("landscape created at " + j); }
			
			organizations = new Vector<Organization>();
			// create numOrgs organizations
			
			for (int i = 0; i < Globals.numOrgs; i++) {
					organizations.add(new Organization(i));
			}
			if (Globals.debug) { System.out.println("orgs created for landscape " + j); }
			
			
			if (Globals.debug) { System.out.println("initialized for landscape " + j); }
                        
                        
			// //1: no overlap, 2: some overlap, 0: no temp diff
			if (Globals.temporalType == 1) {
				run1();
			} else if (Globals.temporalType == 2){
				run2();
			}else{
                                run3();
                        }
			if (Globals.debug) { System.out.println("finished running for landscape " + j); }
//			System.out.println("landscape:\t" + j + "\t" + Globals.landscape.getMaxFitness());
//			Globals.landscape.printLandscapeFitness();
			Globals.landscape = null;
                        long millisEnd = Calendar.getInstance().getTimeInMillis();
            //System.out.println("system millisecond is "+millisEnd);
		}
	}
	
	private static void run1() {
		for (int t = 0; t < Globals.periods + 1; t++) {
			if (Globals.debug) {
				System.out.println("Simulation.run()\tperiod:\t" + t);
			}
			for (Organization org : organizations) {
                            //System.out.println("\n\nRun 1: period is "+t);
//				org.run(t);
                                org.setPeriod(t);
				org.run1();
			}
			if (Globals.reportLevel.equals("details")) {
				reportDetails(Globals.out, t);
			} else if (Globals.reportLevel.equals("summary")) {
				reportSummary(Globals.out, t);
			}
		}
	}
        
        private static void run2() {
		for (int t = 0; t < Globals.periods + 1; t++) {
			if (Globals.debug) {
				System.out.println("Simulation.run()\tperiod:\t" + t);
			}
			for (Organization org : organizations) {
                            //System.out.println("\n\nRun 2: period is "+t);
//				org.run(t);
                                org.setPeriod(t);
				org.run2();
			}
			if (Globals.reportLevel.equals("details")) {
				reportDetails(Globals.out, t);
			} else if (Globals.reportLevel.equals("summary")) {
				reportSummary(Globals.out, t);
			}
		}
	}

        
        private static void run3() {
		for (int t = 0; t < Globals.periods + 1; t++) {
			if (Globals.debug) {
				System.out.println("Simulation.run()\tperiod:\t" + t);
			}
			for (Organization org : organizations) {
//				org.run(t);
                                org.setPeriod(t);
				org.run3();
			}
			if (Globals.reportLevel.equals("details")) {
				reportDetails(Globals.out, t);
			} else if (Globals.reportLevel.equals("summary")) {
				reportSummary(Globals.out, t);
			}
		}
	}
	
	private static boolean allEnded() {
		boolean retBool = true;
		for (Organization org : organizations) {
			if (!org.finished()) {
				retBool = false; 
				break;
			}
		}
		return retBool;
	}
	
	private static String setConfigFile(String[] args) {
		String retString = "";
		if (args.length > 1) {
			System.err.println("Need at most one argument (config file).  Try again.");
			System.exit(0);
		} else if (args.length == 0) {
			retString = "";
			
		} else {
			retString = args[0];
		}
		return retString;
	}
	
	private static void reportDetails(int period) {
		for (Organization org : organizations) {
			org.printDetails(period);
		}
	}
	
	private static void reportDetails(PrintWriter pw, int period) {
		for (Organization org : organizations) {
			org.printDetails(pw, period);
		}
	}

	private static void reportSummary(int period) {
		// calc average and report average for landscape
		StatCalc stat = new StatCalc();
		int completed = 0;
		for(Organization org : organizations) {
			stat.enter(org.getOrgFitness());
//			stat.enter(org.getFitness());
			if (org.finished()) { completed++; }
		}
                //System.out.println("jaha");
		//System.out.println(Globals.landscape.getLandscapeID() + "\t" + period + "\t" + completed + "\t" + stat.getMean() + "\t" + stat.getStandardDeviation() + "\t" + stat.getMin() + "\t" + stat.getMax());
	}

	private static void reportSummary(PrintWriter pw, int period) {
		// calc average and report average for landscape
		// calc average and report average for landscape
		StatCalc stat = new StatCalc();
		int completed = 0;
		for(Organization org : organizations) {
			stat.enter(org.getOrgFitness());
                        
//			stat.enter(org.getFitness());
			if (org.finished()) {  completed++;}
                        else{
                            //System.out.println("org "+org.index+"  fitnesss is "+org.getOrgFitness()+" period is "+period); org.printLocation();
                        }
		}
                 //System.out.println("completed is "+completed);
                 //System.out.println("stat.getMean() is "+stat.getMean());
		pw.println(Globals.landscape.getLandscapeID() + "\t" + period + "\t" + completed + "\t" + stat.getMean() + "\t" + stat.getStandardDeviation());
	}
        
        
}
