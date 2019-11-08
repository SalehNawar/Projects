import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * The program that simulates
 * the agents that move to reach the targets
 */
@SuppressWarnings("serial")
public class MultiAgentSystem extends JFrame{

	//group number
	private static final String GROUP_NUMER = "18";
	
	/**
	 * drawing panel
	 */
	private Board drawingPanel;
	
	/**
	 * button to start
	 */
	private JButton btnStart;
	
	/**
	 * button to exit
	 */
	private JButton btnExit;
	
	//array of agents
	private Agent[] agents;
	
	//array of targets
	private Target[] targets;
	
	//scenario
	private JTextField txtScenario;
	
	//iteration
	private JTextField txtIteration;
	
	///number of agents
	private static final int SIZE = 5;
	
	//sleep time
	private static final int SLEEP = 1;
	
	//colors
	private static final Color[] colors = {Color.BLUE, Color.RED, Color.MAGENTA, Color.BLACK, Color.GREEN};
	
	//names
	private static final String[] names = {"A", "B", "C", "D", "E"};
		
	//number of targets found
	private int numTargetsFound = 0;
	
	//scenario 1,2,3
	private int scenario;
	
	//running?
	private boolean running = true;	
	
	/**
	 * constructor
	 */
	public MultiAgentSystem(){
		
		agents = new Agent[SIZE];
		targets = new Target[SIZE * SIZE];
		
		//create agents and target
		createRandomAgentsTargets();
				
		//create drawing panel
		drawingPanel = new Board(this);
		drawingPanel.setPreferredSize(new Dimension(800, 800));
		add(drawingPanel, BorderLayout.CENTER);
		
		//button 
		JPanel southPanel = new JPanel();
		btnStart = new JButton("Start");
		btnExit = new JButton("Exit");
		txtScenario = new JTextField(10);
		txtScenario.setText("2");
		southPanel.add(new JLabel("Scenario (1/2/3): "));
		southPanel.add(txtScenario);
		
		southPanel.add(new JLabel("Iteration: "));
		txtIteration = new JTextField(10);
		txtIteration.setText("15");		
		southPanel.add(txtIteration);
		southPanel.add(btnStart);
		southPanel.add(btnExit);
		add(southPanel, BorderLayout.SOUTH);
		
		//add action listener
		btnStart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				move();				
			}
		});
		
		//add action listener
		btnExit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);				
			}
		});
		setTitle("Multi-agent system");
		
		pack();
		setLocationRelativeTo(null); //center frame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		
	}
	
	/**
	 * simulate
	 */
	private void move(){		
		scenario = Integer.parseInt(txtScenario.getText());
		(new MoveThread()).start();
	}
	
	//thread that draw the panel
	class MoveThread extends Thread{
		
		public void run(){
			
			int maxInteration = Integer.parseInt(txtIteration.getText());
			
			//total average happiness
			double totalAverageHappiness = 0;
			//total std happiness
			double totalSTDHappiness = 0;
			
			double maxHappiness = -1;
			double minHappiness = Double.MAX_VALUE;
			double totalHappiness = 0;
			double avgHappiness = 0;
			double stdHappiness = 0;
			
			//write to CSV file
			try (BufferedWriter writer = new BufferedWriter(
					new FileWriter("G" + GROUP_NUMER + "_1.csv", true))) {

				writer.write("a. Scenario number (1 2 or 3),");				
				writer.write("b. Iteration number,");
				writer.write("c. Agent number,");
				writer.write("d. Number of collected targets by the agent,");
				writer.write("e. Number of steps taken by the agent at the end of iteration,");
				writer.write("f. Agent happiness: d/(e+1),");
				writer.write("g. Maximum happiness in each iteration,");
				writer.write("h. Minimum happiness in each iteration,");
				writer.write("i. Average happiness in each iteration,");
				writer.write("j. Standard deviation of happiness in each iteration,");
				writer.write("k. Agent competitiveness: (f-h)/(g-h)\n");
				
				for (int iteration = 1; iteration <= maxInteration; iteration++){

					//run until finish
					while(running){
						//move agent
						for (int i = 0; i < SIZE; i++){
							if (agents[i] != null){
								agents[i].move();
							}
						}
						
						drawingPanel.repaint();
						
						try {
							Thread.sleep(SLEEP);
						} catch (InterruptedException e) {
							// ignore
						}
					}//end one iteration
					
					for (Agent a: agents){
						double happiness = (double)a.getNumTargets() / (double)(a.getSteps() + 1);
						if (maxHappiness < happiness){
							maxHappiness = happiness;
						}
						if (minHappiness > happiness){
							minHappiness = happiness;
						}
						totalHappiness += happiness;
					}
					avgHappiness = totalHappiness / (double)agents.length;
					
					//calculate std
					for (Agent a: agents){
						double happiness = (double)a.getNumTargets() / (double)(a.getSteps() + 1);
						stdHappiness +=  (happiness - avgHappiness) * (happiness - avgHappiness);						
					}
					stdHappiness = Math.sqrt(stdHappiness / (double)agents.length);
					
					totalAverageHappiness += avgHappiness;
					totalAverageHappiness += stdHappiness;
					
					for (Agent a: agents){
						double happiness = (double)a.getNumTargets() / (double)(a.getSteps() + 1);
						writer.write(scenario + ",");				
						writer.write(iteration + ",");
						writer.write(a.getName() + ",");
						writer.write(a.getNumTargets() + ",");
						writer.write(a.getSteps() + ",");
						writer.write(happiness + ",");
						writer.write(maxHappiness + ",");
						writer.write(minHappiness + ",");
						writer.write(avgHappiness + ",");
						writer.write(stdHappiness + ",");
						
						double competitiveness = 0;
						if (maxHappiness - minHappiness != 0){
							competitiveness = (double)(happiness - minHappiness) / (double)(maxHappiness - minHappiness);
						}
						writer.write(competitiveness + "\n");
					}
					
					//reset game
					running = true;
					//create agents and target
					createRandomAgentsTargets();
					
					numTargetsFound = 0;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			//write to CSV file
			try (BufferedWriter writer = new BufferedWriter(
					new FileWriter("G" + GROUP_NUMER + "_2.csv", true))) {
				
				writer.write("a. Scenario number (1 2 and 3),");
				writer.write("b. Average of column \"i\" for the iterations of same scenario,");
				writer.write("c. Average of column \"k\" for the iterations of same scenario\n");
				
				
				writer.write(scenario + ",");		
				writer.write(totalAverageHappiness / (double)maxInteration + ",");
				writer.write(totalSTDHappiness / (double)maxInteration + "\n");
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			JOptionPane.showMessageDialog(null, "The result has been written to CSV files");
		}
	}
	
	
	/**
	 * agent a got target t
	 * 
	 * @param a
	 * @param t
	 */
	public void gotTarget(Agent a, Target t){
		a.setX(t.getX());
		a.setY(t.getY());
		
		for (int i = 0; i < SIZE*SIZE; i++){
			if (targets[i] == t){
				targets[i] = null;
				break;
			}
		}
		
		//enough targets?
		a.setNumTargets(a.getNumTargets() + 1);
		
//		if (a.getNumTargets() == SIZE){
//			a.setRunning(false);
//		}
		
		if (scenario == 1 || scenario == 3){//Competition or Compassionate
			//The game will be over as soon as one of the agents collects all its targets.
			if (a.getNumTargets() == SIZE){
				running = false;	
			}
			
		}
		
		//done?
		numTargetsFound ++;
		if (numTargetsFound == SIZE * SIZE){
			for (Agent x: agents){
				x.setRunning(false);
			}
			running = false;
		}	
		
	}
	
	/**
	 * check if a point is empty, not object
	 * call by agent to find neighbor cells
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isEmpty(int x, int y){
		
		if (x < 0 || x >= 100 || y < 0 || y >= 100){
			return false; //border
		}
		
		for (Agent agent: agents){
			if (agent.getX() == x && agent.getY() == y){
				return false;
			}
		}
		for (Target target: targets){
			if (target != null){
				if (target.getX() == x && target.getY() == y){
					return false;
				}
			}
		}
		return true;
	}
	
	//check 2 points are adjacency
	public boolean isAdjcency(int x, int y, int x1, int y1){
		return (y == y1 && Math.abs(x - x1) <= 1)
				 || (x == x1 && Math.abs(y - y1) <= 1);
	}
	
	/**
	 * get object in the circle
	 * @param x
	 * @param y
	 * @param radius
	 * @return
	 */
	public List<Object> getObjects(int x0, int y0, int radius){
		List<Object> objects = new ArrayList<>();
		
		for (Agent agent: agents){
			
			double distance = Math.sqrt(Math.pow(agent.getX() - x0, 2) +
					Math.pow(agent.getY() - y0, 2));
			if (distance <= radius){
				objects.add(agent);
			}
		}
		
		for (Target target: targets){
			if (target != null){
				double distance = Math.sqrt(Math.pow(target.getX() - x0, 2) +
						Math.pow(target.getY() - y0, 2));
				if (distance <= radius){
					objects.add(target);
				}
			}			
		}
		
		return objects;
	}
	
	/**
	 * create random agents and targets
	 */
	private void createRandomAgentsTargets(){
		
		//create agents
		for (int i = 0; i < SIZE; i++){
			int x = (int)(Math.random() * 100);
			int y = (int)(Math.random() * 100);
			
			//check duplicate
			boolean duplicate = false;
			for (int j = 0; !duplicate && j < i; j++){
				if (agents[j].getX() == x && agents[j].getY() == y){
					duplicate = true;
				}
			}
			
			if (!duplicate){
				agents[i] = new Agent(this, x, y, names[i], colors[i]);
			}else{
				i--;
			}
		}
		
		//create targets
		int index = 0;
		while (index < SIZE * SIZE){
			int x = (int)(Math.random() * 100);
			int y = (int)(Math.random() * 100);
			
			//check duplicate
			boolean duplicate = false;
			for (int j = 0; !duplicate && j < SIZE; j++){
				if (agents[j].getX() == x && agents[j].getY() == y){
					duplicate = true;
				}
			}
			for (int j = 0; !duplicate && j < index; j++){
				if (targets[j].getX() == x && targets[j].getY() == y){
					duplicate = true;
				}
			}
			
			if (!duplicate){				
				targets[index] = new Target(x, y, names[index % SIZE],colors[index % SIZE]);
				index++;
			}
		}
		
		//test
		//int i = 0;
		//agents[i] = new Agent(this, 10, 10, names[i], colors[i]);		
		//targets[i] = new Target(99, 99, names[i],colors[i]);		
		//agents[i].receiveToPrivateChannel(targets[i]);
	}
	
	/**
	 * @return the scenario
	 */
	public int getScenario() {
		return scenario;
	}
	
	

	/**
	 * @return the agents
	 */
	public Agent[] getAgents() {
		return agents;
	}

	/**
	 * @return the targets
	 */
	public Target[] getTargets() {
		return targets;
	}

	/**
	 * main method to start java application
	 * @param args unused
	 */
	public static void main(String[] args) {
		
		MultiAgentSystem frame = new MultiAgentSystem();			
		frame.setVisible(true);

	}
}
