import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Agent
 */
public class Agent {
	
	//positions of agent
	private int x;
	private int y;

	//name and color of agent
	private String name;
	private Color color;
	
	//reference to MultiAgentSystem
	private MultiAgentSystem system;
	
	//is running?
	private boolean running = true;
	
	//not target got
	private int numTargets = 0;
	
	//private channel
	private List<Target> privateChannel = new ArrayList<>();
	
	//public channel
	private List<Target> publicChannel = new ArrayList<>();
	
	//radius of censor 
	private static final int RADIUS = 10;
	
	//number of steps
	private int steps = 0;
	
	//other targets memory
	private List<Target> otherTargetsMemory = new ArrayList<>();
	
	/**
	 * constructor
	 * 
	 * @param x
	 * @param y
	 * @param name
	 * @param color
	 */
	public Agent(MultiAgentSystem system, int x, int y, String name, Color color) {
		this.system = system;
		this.x = x;
		this.y = y;
		this.name = name;
		this.color = color;
	}
	
	/**
	 * @return the numTargets
	 */
	public int getNumTargets() {
		return numTargets;
	}

	/**
	 * @param numTargets the numTargets to set
	 */
	public void setNumTargets(int numTargets) {
		this.numTargets = numTargets;
	}
	
	//remove wrong target
	private void removeWrongTargets(List<Object> neighbors){
		
		for (int i = 0; i < publicChannel.size(); i++){
			Target t = publicChannel.get(i);
			double distance = Math.sqrt(Math.pow(t.getX() - x, 2) +
					Math.pow(t.getY() - y, 2));
			if (distance <= RADIUS){
				//must be in neighbors
				if (!isInNeighbors(neighbors, t)){
					publicChannel.remove(t);
				}
			}
		}
		for (int i = 0; i < privateChannel.size(); i++){
			Target t = privateChannel.get(i);
			double distance = Math.sqrt(Math.pow(t.getX() - x, 2) +
					Math.pow(t.getY() - y, 2));
			if (distance <= RADIUS){
				//must be in neighbors
				if (!isInNeighbors(neighbors, t)){
					privateChannel.remove(t);
				}
			}
		}
		
	}
	
	//check if target in the neighbors
	private boolean isInNeighbors(List<Object> neighbors, Target target){
		for (Object obj: neighbors){
			if (obj instanceof Target){
				Target t = (Target)obj;
				if (t.getX() == target.getX() && t.getY() == target.getY()){
					return true;
				}
			}
		}
		return false;
	}
	
	//move by sensor that check within circle
	private boolean moveBySensor(List<Object> neighbors){
		
		boolean done = false;
		Target myTarget = null;
		double minDistance = 0;
		
		//find my targets
		for (Object obj: neighbors){
			if (obj == this){
				continue;
			}
			if (obj instanceof Target){
				Target t = (Target)obj;
				
				//my target?
				if (t.getName().equals(name)){
					double distance = Math.sqrt(Math.pow(t.getX() - x, 2) +
							Math.pow(t.getY() - y, 2));
					if (myTarget == null || minDistance > distance){
						myTarget = t;
						minDistance = distance;
					}
				}
			}
		}
		
		//target found and near me?
		if (myTarget != null && system.isAdjcency(x, y, myTarget.getX(), myTarget.getY())){
			system.gotTarget(this, myTarget);
			
			//remove from channel if any
			privateChannel.remove(myTarget);
			publicChannel.remove(myTarget);
			
			done = true;
		}
		
		//try to move to nearest target
		if (!done && myTarget != null){
			
			if (x == myTarget.getX() && y == myTarget.getY()){
				System.out.println("ERROR");
			}
			
			if (x == myTarget.getX()){
				if (y > myTarget.getY()){
					if (system.isEmpty(x, y - 1)){
						y--;
						done = true;
					}
				}else{
					if (system.isEmpty(x, y + 1)){
						y++;
						done = true;
					}
				}
			}else if (y == myTarget.getY()){
				if (x > myTarget.getX()){
					if (system.isEmpty(x - 1, y)){
						x--;
						done = true;
					}
				}else{
					if (system.isEmpty(x + 1, y)){
						x++;
						done = true;
					}
				}
			}
			//move direction y
			else if (Math.abs(x - myTarget.getX()) < Math.abs(y - myTarget.getY())){
				if (y > myTarget.getY()){
					if (system.isEmpty(x, y - 1)){
						y--;
						done = true;
					}
				}else{
					if (system.isEmpty(x, y + 1)){
						y++;
						done = true;
					}
				}
			}else{//move direction x
				if (x > myTarget.getX()){
					if (system.isEmpty(x - 1, y)){
						x--;
						done = true;
					}
				}else{
					if (system.isEmpty(x + 1, y)){
						x++;
						done = true;
					}
				}
			}
		}
		return done;
	}
	
	//move by channel (private or public)
	private boolean moveByChannel(List<Target> channel){
		
		boolean done = false;
		Target myTarget = null;
		double minDistance = 0;
		
		//find my targets
		for (Target t: channel){				
			//my target?
			if (t.getName().equals(name)){
				double distance = Math.sqrt(Math.pow(t.getX() - x, 2) +
						Math.pow(t.getY() - y, 2));
				if (myTarget == null || minDistance > distance){
					myTarget = t;
					minDistance = distance;
				}
			}
		}
		
		//try to move to nearest target
		if (!done && myTarget != null){
			if (x == myTarget.getX()){
				if (y > myTarget.getY()){
					if (system.isEmpty(x, y - 1)){
						y--;
						done = true;
					}
				}else{
					if (system.isEmpty(x, y + 1)){
						y++;
						done = true;
					}
				}
			}else if (y == myTarget.getY()){
				if (x > myTarget.getX()){
					if (system.isEmpty(x - 1, y)){
						x--;
						done = true;
					}
				}else{
					if (system.isEmpty(x + 1, y)){
						x++;
						done = true;
					}
				}
			}
			//move direction y
			else if (Math.abs(x - myTarget.getX()) < Math.abs(y - myTarget.getY())){
				if (y > myTarget.getY()){
					if (system.isEmpty(x, y - 1)){
						y--;
						done = true;
					}
				}else{
					if (system.isEmpty(x, y + 1)){
						y++;
						done = true;
					}
				}
			}else{//move direction x
				if (x > myTarget.getX()){
					if (system.isEmpty(x - 1, y)){
						x--;
						done = true;
					}
				}else{
					if (system.isEmpty(x + 1, y)){
						x++;
						done = true;
					}
				}
			}
		}
		return done;
	}
	/**
	 * move
	 */
	public void move(){
		
		//running?
		if (!running){
			return;
		}
		
		boolean done = false; //done this move
		
		List<Object> neighbors = system.getObjects(x, y, RADIUS);
		
		//find target by censor
		done = moveBySensor(neighbors);
		
		//remove wrong targets
		removeWrongTargets(neighbors);
		
		//find target from private channel
		if (!done){
			done = moveByChannel(privateChannel);
		}
		//find target from public channel
		if (!done){
			done = moveByChannel(publicChannel);
		}
		
		//move x or y direction random
		for (int i = 0; !done && i < 10; i++){
			int random = (int)(Math.random() * 4);
			//System.out.println(random);
			switch (random){
			case 0:
				if (system.isEmpty(x - 1, y)){
					x--;
					done = true;
				}
				break;
			case 1:
				if (system.isEmpty(x + 1, y)){
					x++;
					done = true;
				}
				break;
			case 2:
				if (system.isEmpty(x, y - 1)){
					y--;
					done = true;
				}
				break;
			default:
				if (system.isEmpty(x, y + 1)){
					y++;
					done = true;
				}
				break;
			}			
		}
		
		//got other targets?
		for (Object obj: neighbors){
			if (obj instanceof Target){
				Target t = ((Target)obj);
				if (!t.getName().equals(name)){
					if (!otherTargetsMemory.contains(t)){
						otherTargetsMemory.add(t);
					}
				}
			}
		}
		
		//post to public channel
		for (Target target: otherTargetsMemory){
			
			Target t = ((Target)target).copy();
			if (Math.random() < 0.5){//send or not
				//send
				if (Math.random() < 0.5){//send wrong info
					t.setX((int)(Math.random() * 100));
					t.setY((int)(Math.random() * 100));
				}
				
				for (Object objAgent: neighbors){
					
					if (objAgent instanceof Agent){
						if (Math.random() < 0.5){//send or not
							((Agent)objAgent).receiveToPublicChannel(t);
						}
					}
				}				
			}
		}
		
		//post to private channel
		if (system.getScenario() == 2 || system.getScenario() == 3){
			for (Target t: otherTargetsMemory){
				
				if (Math.random() < 0.5){//send or not
					
					for (Object objAgent: neighbors){
						if (objAgent instanceof Agent){
							Agent a = (Agent)objAgent;
							if (a != this && a.getName().equals(t.getName())){
								a.receiveToPrivateChannel(t);	
							}							
						}
					}
				}
			}
		}
		
		steps++;
	}
	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}
	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}
	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}
	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @return the running
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * @param running the running to set
	 */
	public void setRunning(boolean running) {
		this.running = running;
	}
	
	/**
	 * receive target to private channel
	 * @param target
	 */
	public void receiveToPrivateChannel(Target target){
		privateChannel.add(target);
	}
	
	/**
	 * receive target to public channel
	 * @param target
	 */
	public void receiveToPublicChannel(Target target){
		publicChannel.add(target);
	}

	/**
	 * @return the steps
	 */
	public int getSteps() {
		return steps;
	}
	
	
}
