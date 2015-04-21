package obj;

import java.util.Collections;
import java.util.Random;
import java.util.Vector;
import util.Globals;

public class DMU {
	protected String DMUType;
	protected Location globalLocation;
        protected int siteNum;
        protected String status="normal";
        protected int period;

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }


	private Vector<Location> neighbors;
	private Location moveTo;
        
	public DMU() {
		
	}
	
	public DMU(int siteNumber, Location loc) {
            String[] retStringArray = loc.getLocation();
            globalLocation=new Location();
            globalLocation.setLocation(retStringArray);
            
             siteNum=siteNumber;
	}

	/**
	 * accessors
	 */
	
	public Location getLocation() {
		return globalLocation;
	}
	
	public String[] getLocationString() {
		return globalLocation.getLocation();
	}
	
	public void setLocation(String[] locationStr) {
		globalLocation = new Location(locationStr);
	}
        
        public void transferLocation(int siteNumber, Location loc){
            globalLocation.setLocation(loc);
            if(Globals.spatialType==1){//if there is spatial diff
            Random randomGenerator = new Random();
             int randomInt = randomGenerator.nextInt(100);
             double randomDouble=randomInt/100.0;
             //System.out.println("randomDouble is "+randomDouble);
             siteNum=siteNumber;
             if(randomDouble<=(Globals.distortionPossibility+Globals.lossPossibility)){
                 if(randomDouble<=Globals.distortionPossibility){          //distorted at bitNum
                     int bitNum=randomGenerator.nextInt(Globals.N/2)+Math.abs(siteNum-1)*Globals.N/2; //abs to convert site 0 to 1 and site 1 to 0, ultimate purpose is to get the bit 
                     if(globalLocation.getLocationAt(bitNum).equals("0")){
                         globalLocation.setLocationAt(bitNum, "1");
                     }else{
                         globalLocation.setLocationAt(bitNum, "0");
                     }
                     //System.out.println("globalLocation after information distortion is "+globalLocation);
                     status="distortion";
                 }else{  //loss at bitNum
                         int bitNum=randomGenerator.nextInt(Globals.N/2)+Math.abs(siteNum-1)*Globals.N/2; //abs to convert site 0 to 1 and site 1 to 0, ultimate purpose is to get the bit 
                         //System.out.println("site number is "+siteNumber+" bitNum is "+bitNum);
                         globalLocation.setLocationAt(bitNum, " "); 
                         
                         //System.out.println("globalLocation after information loss is "+globalLocation);
                         status="loss";
                 }
             }
            }
        }
	
	public double getFitness() {
		return Globals.landscape.getFitness(globalLocation);
	}
        
	// SEARCH
	public Location search() {
		moveTo = null;
//		boolean success = false;
		int numRemainingNeighbors = neighbors.size();
		int r = Globals.rand.nextInt(numRemainingNeighbors);
		Location neighbor = (Location)neighbors.remove(r); // need to find global location for neighbor as well
		String[] neighborGlobalLocString = new String[Globals.N];
		for (int i = 0; i < Globals.N; i++) {
			 if (neighbor.getLocationAt(i).equals(" ")) {
				 neighborGlobalLocString[i] = globalLocation.getLocationAt(i);
			 } else {
				 neighborGlobalLocString[i] = neighbor.getLocationAt(i);
			 }
		}
		Location neighborGlobalLocation = new Location(neighborGlobalLocString);
		

                
		double localFitness = 0d;
		double neighborFitness = 0d;
                
			localFitness = Globals.landscape.getFitness(globalLocation, period, siteNum);
			neighborFitness = Globals.landscape.getFitness(neighbor, period, siteNum);

                //System.out.println("Current location in search is "+globalLocation+" and fitness is "+localFitness +" and real fitness is "+Globals.landscape.getFitness(globalLocation));
                //System.out.println("neighbour location is "+neighbor+" and fitness is "+neighborFitness +" and real fitness is "+Globals.landscape.getFitness(neighbor));

		if (neighborFitness > localFitness) {
			// replace localLoc with neighbor & reset tried vector (no need to create new Location object)
//			localLoc.setLocation(neighbor); // set it now or later?
			// since moveTo was null before, we need to initialize it first and then set it's location
			moveTo = new Location(neighbor);
                       // System.out.println("Location changed to "+moveTo+"\n\n");
//			moveTo.setLocation(neighbor);
			resetSearchHistory();
//			success = true;
		}
		return moveTo;
	}

	public void resetHistory() {
		resetSearchHistory();
	}

	protected void resetSearchHistory() {
		neighbors = new Vector<Location>();
                setNeighbors();
//		printNeighbors();
	}
        
        
	
	private void setNeighbors() {
            
            if((Globals.temporalType==0)||(Globals.temporalType==2&&period%3==1)){
                for (int i = 0; i < Globals.N; i++) {
			String[] neighborLocString = new String[Globals.N];
			boolean add = false;
			for (int j = 0; j < Globals.N; j++) {
				if (i == j) {
					if (globalLocation.getLocationAt(j).equals("1")) {
						neighborLocString[j] = "0"; add = true;
					} else if (globalLocation.getLocationAt(j).equals("0")) {
						neighborLocString[j] = "1"; add = true;
					} // else locationAt is blank so do nothing
				} else { // all other i != j
					neighborLocString[j] = globalLocation.getLocationAt(j);
				}
			}
			if (add) { neighbors.add(new Location(neighborLocString)); }
		}
            }else{
            if(siteNum==0){
		for (int i = 0; i < Globals.N/2; i++) {//only set N/2 neighbours for first site
			String[] neighborLocString = new String[Globals.N];
			boolean add = false;
			for (int j = 0; j < Globals.N; j++) {
				if (i == j) {
					if (globalLocation.getLocationAt(j).equals("1")) {
						neighborLocString[j] = "0"; add = true;
					} else if (globalLocation.getLocationAt(j).equals("0")) {
						neighborLocString[j] = "1"; add = true;
					} // else locationAt is blank so do nothing
				} else { // all other i != j
					neighborLocString[j] = globalLocation.getLocationAt(j);
				}
			}
			if (add) { neighbors.add(new Location(neighborLocString)); }
		}
            }else{
                for (int i = Globals.N/2; i < Globals.N; i++) {
			String[] neighborLocString = new String[Globals.N];
			boolean add = false;
			for (int j = 0; j < Globals.N; j++) {
				if (i == j) {
					if (globalLocation.getLocationAt(j).equals("1")) {
						neighborLocString[j] = "0"; add = true;
					} else if (globalLocation.getLocationAt(j).equals("0")) {
						neighborLocString[j] = "1"; add = true;
					} // else locationAt is blank so do nothing
				} else { // all other i != j
					neighborLocString[j] = globalLocation.getLocationAt(j);
				}
			}
			if (add) { neighbors.add(new Location(neighborLocString)); }
                    }
                }
            }
            
		//Collections.shuffle(neighbors);  // shuffle so that order of retrieval is randomized
		
		if (Globals.debug) { System.out.println("Neighbors for " + DMUType); printNeighbors(); }
	}
        
        

//	public boolean hasMoved() {
//		return move;
//	}
	
	public boolean isLocalOptimum() {
		return neighbors.isEmpty();
	}
	

	private void printNeighbors() {
		for (Location neighbor : neighbors) {
			System.out.println(neighbor.toString());
		}
	}
	
	// debug only
	public void start() {
		resetSearchHistory();
	}

	
		
	public void printAllNeighbors() {
		printNeighbors();
	}

	// for debug purposes only
	public static void main(String args[]) {
		Globals.createLandscape(0);
		Location l = new Location();
		System.out.println(l.toString());
	}
}
