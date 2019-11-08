import java.awt.Graphics;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Board extends JPanel{
	
	//reference to MultiAgentSystem
	private MultiAgentSystem system;
		
	/**
	 * constructor
	 * @param system
	 */
	public Board(MultiAgentSystem system) {
		this.system = system;
	}

	@Override
	public void paintComponent(Graphics g) {
		
		super.paintComponents(g);
		
		//clear
		g.clearRect(0, 0, getWidth(), getHeight());
		
		//draw agents
		for (Agent agent: system.getAgents()){
			g.setColor(agent.getColor());
			g.fillOval(agent.getX() * 8 - 4, agent.getY() * 8 - 4, 8, 8);
		}
		
		//draw targets
		for (int i = 0; i < system.getTargets().length; i++){
			Target target = system.getTargets()[i];
			if (target != null){
				g.setColor(target.getColor());
				g.fillRect(target.getX() * 8 - 4, target.getY() * 8 - 4, 8, 8);
			}			
		}
	}	
}
