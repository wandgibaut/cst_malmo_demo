package Simulation;

import com.microsoft.msr.malmo.AgentHost;
import tempUtils.Mission;
import java.util.logging.Logger;

/**
 *
 * @author Wander
 */
public class Environment
{
    Logger logger = Logger.getLogger(Environment.class.getName());
    AgentHost agentHost;// = null;
    Mission mission;


    public Environment(Boolean prepareEnviromentAndStartGame)
    {
        agentHost = new AgentHost();
        mission = new Mission();


    }
    public void runSimulation(){

        mission.startMission(agentHost);
    }

    public AgentHost getCreature(){ return this.agentHost;}

}
