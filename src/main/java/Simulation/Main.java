/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Simulation;

import support.MindView;
import support.NativeUtils;
import SoarBridge.SoarBridge;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wandemberg Gibaut
 */
public class Main {
    static
    {
        //System.setProperty("java.library.path","./libs");
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
            //NativeUtils.loadFileFromJar("/soar-rules.soar");
            //String soarRulesPath = "rules/soar-rules.soar";

            //Start enviroment data
            Environment e = new Environment(Boolean.FALSE);
            //SoarBridge soarBridge = new SoarBridge(e,soarRulesPath,false);
            //MindViewer mv = new MindViewer(soarBridge);
            //mv.setVisible(true);

            // Run Simulation until some criteria was reached
            Thread.sleep(3000);

            //run malmo and SOAR interpreter
            e.runSimulation();

        }
        catch (Exception ex)
        {
            System.out.println(ex.toString());
            ex.printStackTrace();
            logger.severe("Unknown error"+ex);
        }
    }

    public static void main(String[] args)
    {
        Main m = new Main();
    }


}
