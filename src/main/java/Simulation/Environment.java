/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Simulation;

import java.util.logging.Logger;

import SoarBridge.SoarBridge;
import com.microsoft.msr.malmo.AgentHost;
import support.MindView;
import tempUtils.Mission;


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
