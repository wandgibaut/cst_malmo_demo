package Simulation;

import agent.main.AgentMind;
import java.util.logging.Level;
import java.util.logging.Logger;
import br.unicamp.cst.util.MindViewer;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wandemberg Gibaut
 */
public class Main {
    static
    {
        System.loadLibrary("MalmoJava"); // attempts to load MalmoJava.dll (on Windows) or libMalmoJava.so (on Linux)
    }


    Logger logger = Logger.getLogger(Main.class.getName());
        
    private void SilenceLoggers() {
        ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger("org.jsoar")).setLevel(ch.qos.logback.classic.Level.OFF);
        Logger.getLogger("Simulation").setLevel(Level.SEVERE);
    }

    public Main() {
        //SilenceLoggers();
        try
        {
            //Start enviroment data
            Environment e = new Environment(Boolean.FALSE);

            // Run Simulation until some criteria was reached
            Thread.sleep(3000);

            //run malmo and CST agent
            AgentMind agentMind = new AgentMind(e);

            // Create and Populate MindViewer
            MindViewer mv = new MindViewer(agentMind, "Arthur Simpson", agentMind.getBehavioralCodelets());
            mv.setVisible(true);

            e.runSimulation();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.severe("Unknown error"+e);
        }
    }

    public static void main(String[] args)
    {
        Main m = new Main();
    }

}
