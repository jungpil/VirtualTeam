package obj;

import java.io.PrintWriter;

import util.Globals;

public class Organization {
//	private int period;  // NO NEED ANYMORE
	public int index; 
	protected String orgType;
	protected Location location; 
	protected DMU[] units = new DMU[Globals.numSubOrgs];  // initialize number of DMUs to Globals.numSubOrgs
	protected int[] searchStatus = new int[Globals.numSubOrgs]; // -2 for not started; -1 for local optimum; 0 for failed search; 1 for moved
	protected boolean completed;
	protected int lastDMU;
	protected int next = -1; // focal DMU (whose turn is it to search)?
	protected boolean lastPrinted = false;
        
        protected int period;



	
	public Organization(int idx) {
		index = idx;
		// orgType = "whatever"; set by subclass
		location = new Location(); // random location to start with
		// [changed 3/24/12]
		units[0] = new DMU(0, location);
		// [end change]
		if (Globals.debug) { System.out.println(" DMU 1 " + idx + " created"); }
		searchStatus[0] = -2;   // DO I NEED THIS?
		units[1] = new DMU(1, location);
		if (Globals.debug) { System.out.println(" DMU 2 " + idx + " created"); }
		searchStatus[1] = -2;   // DO I NEED THIS?
		
		/* period = 0; */
		completed = false;
		lastDMU = -2;
	}
	
	public boolean finished() {
		return completed;
	}

	
        
        public void run1(){
            units[0].setPeriod(period);
            units[1].setPeriod(period);
            //System.out.println("Current location is "+location);
               if(period%4==0||period%4==1){
                   //System.out.println("\n\n\nRun1 for the first site, period is "+period);
                   units[0].transferLocation(0,location);
                   units[0].resetHistory();
                   location = searchUntilResult(0);
                   units[0].setLocation(location.getLocation());
               }else{
                   //System.out.println("\n\n\nRun1 for the second site, period is "+period);
                   units[1].transferLocation(1,location);
                   units[1].resetHistory();
                   location = searchUntilResult(1);
                   units[1].setLocation(location.getLocation());
               }            
        }
        
        public void run2(){
            units[0].setPeriod(period);
            units[1].setPeriod(period);
            if(period%4==0){
                //System.out.println("\n\n\nRun2 for the first site, period is "+period);
                   units[0].transferLocation(0,location);
                   units[0].resetHistory();
                   location = searchUntilResult(0);
                   units[0].setLocation(location.getLocation());
            }else if(period%4==1){
                //System.out.println("\n\n\nRun2 for the both sites, period is "+period);
                   units[0].transferLocation(0,location);
                   units[0].resetHistory();
                   units[1].transferLocation(1,location);
                   units[1].resetHistory();
                   
                   Location first_half=searchUntilResult(0);
                   Location second_half=searchUntilResult(1);
                   updateLocation(first_half, second_half);
            }else if(period%4==2){
                //System.out.println("\n\n\nRun2 for the second sites, period is "+period);
                   units[1].transferLocation(1,location);
                   units[1].resetHistory();
                   location = searchUntilResult(1);
                   units[1].setLocation(location.getLocation());
            }else{
                
            }
        }
        
        public void run3(){
            units[0].setPeriod(period);
            units[1].setPeriod(period);
            if(period%4==0||period%4==1){
                //System.out.println("\n\n\nRun3 for the both sites, period is "+period);
                   units[0].transferLocation(0,location);
                   units[0].resetHistory();
                   units[1].transferLocation(1,location);
                   units[1].resetHistory();
                   
                   //System.out.println("location now is "+location);
                   Location first_half=searchUntilResult(0);
                   //System.out.println("first half is "+first_half+" and the current location is "+location+"  and current org is "+index);
                   Location second_half=searchUntilResult(1);
                   //System.out.println("second half is "+second_half+" and the current location is "+location+"  and current org is "+index);
                   updateLocation(first_half, second_half);
            }else{
                
            }
        }
        
        public Location searchUntilResult(int currentDMU){
            //System.out.println("\nsearchUntilResult for site "+currentDMU);
            Location moveTo = units[currentDMU].search(); 
            while(moveTo==null){
                if(units[currentDMU].isLocalOptimum()){
                   // System.out.println("Location is remain unchanged");
                    if(Globals.temporalType==0&&Globals.spatialType==0) {completed=true; }
                    else if(Globals.temporalType==2&&Globals.spatialType==0&&currentDMU%3==1){completed=true;}
                    else if(Globals.temporalType==1&&Globals.spatialType==0&&currentDMU%2==1){completed=true;}
                    moveTo=units[currentDMU].getLocation();
                }else{
                    moveTo = units[currentDMU].search();
                    
                }
            }
            
            
            
            
            	String[] newGlobalLocation = new String[Globals.N];
		if(currentDMU==0){
                  for (int i = 0; i < Globals.N; i++) {
		  	if (i<Globals.N/2) { 
		  		newGlobalLocation[i] = moveTo.getLocationAt(i);
                                                               // if(newGlobalLocation[i].equals(" ")) System.out.println("\n1"+i);
		  	} else {
		  		newGlobalLocation[i] = location.getLocationAt(i);
                                                               // if(newGlobalLocation[i].equals(" ")) System.out.println("\n2"+i);
		  	}
		  }
                }else{
                    for (int i = 0; i < Globals.N; i++) {
		  	if (i<Globals.N/2) { 
		  		newGlobalLocation[i] = location.getLocationAt(i);
                               // if(newGlobalLocation[i].equals(" ")) System.out.println("\n"+i);
		  	} else {
		  		newGlobalLocation[i] = moveTo.getLocationAt(i);
                                                              //  if(newGlobalLocation[i].equals(" ")) System.out.println("\n"+i);
		  	}
		  }
                }
                moveTo.setLocation(newGlobalLocation);
               // System.out.println("REAL LOCATION IS "+moveTo);
            return moveTo;
        }

	
        
        
	public double getOrgFitness() {
            double numerator =Globals.landscape.getFitness(location)-Globals.landscape.getMinFitness();
            double denominator=Globals.landscape.getMaxFitness()-Globals.landscape.getMinFitness();
            double orgFitness=numerator/denominator;
	           //System.out.println("numerator is "+numerator+"  denominator  is "+denominator+"   actual fintess is "+Globals.landscape.getFitness(location)+"   orgFitness after normalization is "+orgFitness);	
            return orgFitness;
	}

	protected void updateLocation(Location first_half, Location second_half) {
		
		String[] newGlobalLocation = new String[Globals.N];
		for (int i = 0; i < Globals.N; i++) {
			if (i<Globals.N/2) { 
				newGlobalLocation[i] = first_half.getLocationAt(i);
			} else {
				newGlobalLocation[i] = second_half.getLocationAt(i);
			}
		}
		location.setLocation(newGlobalLocation);	
	}
		
	// PRINTERS
	public void printDetails(int period) {
		double globalFitness = Globals.landscape.getFitness(location);
		double[] localFitness = new double[Globals.numSubOrgs];
		for (int i = 0; i < Globals.numSubOrgs; i++) {
			localFitness[i] = units[i].getFitness();
		}
		String searchStatusString = "";
		String localFitnessString = "";
		for (int i = 0; i < Globals.numSubOrgs; i++) {
			searchStatusString += searchStatus[i] + "\t";
			localFitnessString += localFitness[i] + "\t";
		}
		
		if (!completed) {
			//System.out.println(period + "\t" + index + "\t" + searchStatusString + next + "\t" + location.toString() + "\t" + localFitnessString + globalFitness);
		} else {
			if (!lastPrinted) {
				//System.out.println(period + "\t" + index + "\t" + searchStatusString + next + "\t" + location.toString() + "\t" + localFitnessString + globalFitness);
				lastPrinted = true;
			}
		}
	}
	
	public void printDetails(PrintWriter pw, int period) {
		double globalFitness = Globals.landscape.getFitness(location);
		double[] localFitness = new double[Globals.numSubOrgs];
		for (int i = 0; i < Globals.numSubOrgs; i++) {
			localFitness[i] = units[i].getFitness();
		}
		String searchStatusString = "";
		String localFitnessString = "";
		for (int i = 0; i < Globals.numSubOrgs; i++) {
			searchStatusString += searchStatus[i] + "\t";
			localFitnessString += localFitness[i] + "\t";
		}
		
		if (!completed) {
			pw.println(Globals.landscape.getLandscapeID() + "\t" + period + "\t" + index + "\t" + searchStatusString + next + "\t" + location.toString() + "\t" + localFitnessString + globalFitness);
		} else {
			if (!lastPrinted) {
				pw.println(Globals.landscape.getLandscapeID() + "\t" + period + "\t" + index + "\t" + searchStatusString + next + "\t" + location.toString() + "\t" + localFitnessString + globalFitness);
				lastPrinted = true;
			}
		}
	}
	
	public static void main(String args[]) {
		Globals.createLandscape(0);
//		Location l = new Location();
//		System.out.println("initial location: " + l.toString());
		Organization o = new Organization(0);
		o.printLocation();
		o.printDMUNeighbors(0);
		o.printDMUNeighbors(1);
		
	}
	
	public DMU getDMU(int i) {
		return units[i];
	}
	
	public void printDMUNeighbors(int i) {
		System.out.println("unit: " + i);
		units[i].printAllNeighbors();
	}
	
	public void printLocation() {
		System.out.println(location.toString());
	}
	
	public String toString() {
		String toString = orgType + "\t" + location.toString() + "\t" + Globals.landscape.getFitness(location);
		return toString;
	}
        
            public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }
        
}
