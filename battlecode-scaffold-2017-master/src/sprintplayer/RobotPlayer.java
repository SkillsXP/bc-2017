package sprintplayer;
import battlecode.common.*;

public strictfp class RobotPlayer {
	static RobotController rc;
	static Direction[] fourDir = new Direction [4];
	static Direction[] eightDir = new Direction[8];
	static Direction[] sixteenDir = new Direction[16];
	
	@SuppressWarnings("unused")
    public static void run(RobotController roco) throws GameActionException {
        RobotPlayer.rc = roco;
        initDirections();
        switch (rc.getType()) {
            case ARCHON:
                runArchon();
                break;
            case GARDENER:
                runGardener();
                break;
            case SCOUT:
            	runScout();
            	break;
            case SOLDIER:
                runSoldier();
                break;
            case LUMBERJACK:
                runLumberjack();
                break;
        }
	}

	private static void runArchon() {
		// TODO Auto-generated method stub
		if(rc.getRoundNum() == 1){
			try{
				//Broadcasts Archon locations
				MapLocation[] myArchons = rc.getInitialArchonLocations(rc.getTeam());
				for (int k = 0; k < myArchons.length; k++){
					MapLocation tempLoc = myArchons[k];
					int xco = Math.round(tempLoc.x);
					int yco = Math.round(tempLoc.y);
					rc.broadcast(2*k, xco);
					rc.broadcast((2*k)+1, yco);
				}
				MapLocation[] theirArchons = null;
				if (rc.getTeam() == Team.A){
					theirArchons = rc.getInitialArchonLocations(Team.B);
				}
				else{
					theirArchons = rc.getInitialArchonLocations(Team.A);
				}
				for (int k = 0; k < theirArchons.length; k++){
					MapLocation tempLoc = myArchons[k];
					int xco = Math.round(tempLoc.x);
					int yco = Math.round(tempLoc.y);
					rc.broadcast((2*k)+6, xco);
					rc.broadcast((2*k)+7, yco);
				}
				
				//Creates a gardener
				if (rc.getTeamBullets() > 299){
					for (int k = 0; k < sixteenDir.length; k++){
						if (rc.canHireGardener(sixteenDir[k])){
							rc.hireGardener(sixteenDir[k]);
							break;
						}
					}
				}
				
				//Donates 10 bullets to get 1 victory point to clutch a rare tie-breaker
				rc.donate(10);
				
				//Chooses an optimal rally tree
				TreeInfo[] treeTargets = rc.senseNearbyTrees(30);
				/*
				 * 1.) Iterate through trees and look for the following conditions in priority
				 * 2.) Empty surroundings
				 * 3.) Nearby
				 * 4.) Low enemies
				 */
				
			} catch(Exception e){
				System.out.println("Archon Exception");
				 e.printStackTrace();
			}
		}
		while(true){
			try{
				
			} catch(Exception e){
				System.out.println("Archon Exception");
				 e.printStackTrace();
			}
		}
	}
	
	private static void runGardener() {
		// TODO Auto-generated method stub
		while(true){
			try{
				
			} catch(Exception e){
				System.out.println("Gardener Exception");
				 e.printStackTrace();
			}
		}
	}
	
	private static void runScout() {
		// TODO Auto-generated method stub
		while(true){
			try{
				
			} catch(Exception e){
				System.out.println("Scout Exception");
				 e.printStackTrace();
			}			
		}
	}
	
	private static void runLumberjack() {
		// TODO Auto-generated method stub
		while(true){
			try{
				
			} catch(Exception e){
				System.out.println("Lumberjack Exception");
				 e.printStackTrace();
			}			
		}
	}

	private static void runSoldier() {
		// TODO Auto-generated method stub
		while(true){
			try{
				
			} catch(Exception e){
				System.out.println("Soldier Exception");
				 e.printStackTrace();
			}			
		}
	}
	
/****************************************************************/
	//Support Methods
	
	private static void initDirections() {
		///Generates direction lists
		for (int k = 0; k < 16; k++){
			float rad = (float)(-Math.PI + (2*Math.PI)/16);
			sixteenDir[k] = new Direction(rad);
			if (k % 2 == 0){
				eightDir[k/2] = new Direction(rad);
				if (k % 4 == 0){
					fourDir[k/4] = new Direction(rad);
				}
			}
		}
	}
	
	public static boolean warnav(MapLocation destiny){
		//navigates to a target while avoiding obstacles
		//fights enemies instead of fleeing
		return false;
	}
	
	public static boolean peacenav(MapLocation destiny){
		//navigates to a target while avoiding obstacles
		//avoids enemies instead of engaging
		return false;
	}
	
	public static boolean engagemelee(RobotInfo enemy){
		//Engages an enemy robot in melee combat
		return false;
	}
	
	public static boolean engagerange (RobotInfo enemy){
		//Engages an enemy robot in range combat
		return false;
	}
	
	public static boolean hunt (RobotInfo enemy){
		//Hunts target until target is dead
		return false;
	}
	
	public static void hide(){
		//Hides behind nearest tree to protect from other bots
	}
	
	public static boolean search(RobotType needle){
		//Searches for a specific type of robot, presumably an archon
		return false;
	}
		
	static boolean tryMove(Direction dir, float degreeOffset, int checksPerSide) throws GameActionException {
        // First, try intended direction
        if (rc.canMove(dir)) {
            rc.move(dir);
            return true;
        }

        // Now try a bunch of similar angles
        boolean moved = false;
        int currentCheck = 1;

        while(currentCheck<=checksPerSide) {
            // Try the offset of the left side
            if(rc.canMove(dir.rotateLeftDegrees(degreeOffset*currentCheck))) {
                rc.move(dir.rotateLeftDegrees(degreeOffset*currentCheck));
                return true;
            }
            // Try the offset on the right side
            if(rc.canMove(dir.rotateRightDegrees(degreeOffset*currentCheck))) {
                rc.move(dir.rotateRightDegrees(degreeOffset*currentCheck));
                return true;
            }
            // No move performed, try slightly further
            currentCheck++;
        }

        // A move never happened, so return false.
        return false;
   	}
	
	 static boolean willCollideWithMe(BulletInfo bullet) {
	        MapLocation myLocation = rc.getLocation();

	        // Get relevant bullet information
	        Direction propagationDirection = bullet.dir;
	        MapLocation bulletLocation = bullet.location;

	        // Calculate bullet relations to this robot
	        Direction directionToRobot = bulletLocation.directionTo(myLocation);
	        float distToRobot = bulletLocation.distanceTo(myLocation);
	        float theta = propagationDirection.radiansBetween(directionToRobot);

	        // If theta > 90 degrees, then the bullet is traveling away from us and we can break early
	        if (Math.abs(theta) > Math.PI/2) {
	            return false;
	        }

	        // distToRobot is our hypotenuse, theta is our angle, and we want to know this length of the opposite leg.
	        // This is the distance of a line that goes from myLocation and intersects perpendicularly with propagationDirection.
	        // This corresponds to the smallest radius circle centered at our location that would intersect with the
	        // line that is the path of the bullet.
	        float perpendicularDist = (float)Math.abs(distToRobot * Math.sin(theta)); // soh cah toa :)

	        return (perpendicularDist <= rc.getType().bodyRadius);
	}

	static boolean tryMove(Direction dir) throws GameActionException {
        return tryMove(dir,20,3);
   	}
	
	
	private static boolean trySideStep(BulletInfo bullet) throws GameActionException{
		Direction towards = bullet.getDir();
		MapLocation leftGoal = rc.getLocation().add(towards.rotateLeftDegrees(90));
		MapLocation rightGoal = rc.getLocation().add(towards.rotateRightDegrees(90));
		return tryMove(towards.rotateRightDegrees(90)) || tryMove(towards.rotateLeftDegrees(90));
	}
	
	private static void dodge() throws GameActionException{
		BulletInfo[] bullets = rc.senseNearbyBullets();
		for (BulletInfo bi: bullets){
			if(willCollideWithMe(bi)){
				trySideStep(bi);
			}
		}
	}
	private static void orbit(MapLocation center, float radians){ // 0 is east pi is west
		float radius = rc.getLocation().distanceTo(center); // distance from tree center to robot center is the radius 
		float x = (float) (radius*Math.cos(radians));
		float y = (float) (radius*Math.sin(radians));
		MapLocation loc = new MapLocation(center.x + x, center.y + y);
		Direction dir = new Direction(rc.getLocation(), loc);
		try { // idk why it makes me do this try catch also in get to tree
			if(tryMove(dir) == true){
				rc.move(loc);
			}
		} catch (GameActionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static float getRadianDegrees(Direction dir){
		return (float) (dir.getAngleDegrees()*Math.PI/180);
	}
	
	private static void getToTree(int x, int y){
		MapLocation loc = new MapLocation(x,y);
		//scan map location pass in accurate location of tree
		Direction dir = new Direction(rc.getLocation(), loc);
		float theta = getRadianDegrees(dir);
		float radius = rc.getLocation().distanceTo(loc);
		float shiftX = (float) (radius*Math.cos(theta)); 
		float shiftY = (float) (radius*Math.sin(theta)); 
		loc = new MapLocation(x + shiftX,y + shiftY);
		dir = new Direction(rc.getLocation(), loc); 
		try {
			if(tryMove(dir) == true){
				rc.move(loc);
			}
		} catch (GameActionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

