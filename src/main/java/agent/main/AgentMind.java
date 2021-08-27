package agent.main;

import Simulation.Environment;
import agent.codelets.behavioral.Forage;
import agent.codelets.behavioral.GoTo;
import agent.codelets.motor.HandsAction;
import agent.codelets.motor.LegsAction;
import agent.codelets.perceptual.JewelDetector;
import agent.codelets.sensory.FakeVision;
import agent.codelets.sensory.InnerSense;
import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryObject;
import br.unicamp.cst.core.entities.Mind;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Wandemberg Gibaut
 */
public class AgentMind extends Mind {
    private List<Codelet> behavioralCodelets = new ArrayList<>();

    public AgentMind(Environment env) {
        super();

        // Declare Memory Objects
        MemoryObject legsMO;
        MemoryObject legsForwardMO;
        //MemoryObject handsMO;
        MemoryObject visionMO;
        MemoryObject innerSenseMO;
        MemoryObject jewelDetectMO;

        //Initialize Memory Objects
        legsMO=createMemoryObject("LEGS", null);
        legsForwardMO=createMemoryObject("LEGS_FORWARD", null);
        //handsMO=createMemoryObject("HANDS", null);
        visionMO=createMemoryObject("FAKE_VISION",null);
        innerSenseMO=createMemoryObject("INNER", null);
        jewelDetectMO = createMemoryObject("JEWEL_DETECT", null);

        // Create Sensor Codelets
        FakeVision vision=new FakeVision("fake_vision", env.getCreature());
        vision.addOutput(visionMO);
        insertCodelet(vision); //Creates a vision sensor

        InnerSense innerSense=new InnerSense("inner_sense", env.getCreature());
        innerSense.addOutput(innerSenseMO);
        insertCodelet(innerSense); //A sensor for the inner state of the creature

        // Create Actuator Codelets
        LegsAction legs=new LegsAction("legs", env.getCreature());
        legs.addInput(legsMO);
        legs.addInput(legsForwardMO);
        insertCodelet(legs);

        //HandsAction hands=new HandsAction("hands", env.getCreature());
        //hands.addInput(handsMO);
        //insertCodelet(hands);

        // Create Perception Codelets
        JewelDetector jewelDetector = new JewelDetector("jewel_detector");
        jewelDetector.addInput(visionMO);
        jewelDetector.addInput(innerSenseMO);
        jewelDetector.addOutput(jewelDetectMO);
        insertCodelet(jewelDetector);

        // Create Behavior Codelets
        GoTo goTo = new GoTo("go_to");
        goTo.addInput(jewelDetectMO);
        goTo.addOutput(legsForwardMO);
        insertCodelet(goTo);

        Forage forage=new Forage("forage");
        forage.addInput(jewelDetectMO);
        forage.addOutput(legsMO);
        insertCodelet(forage);

        this.behavioralCodelets.add(forage);
        this.behavioralCodelets.add(goTo);

        // sets a time step for running the codelets to avoid heating too much your machine
        for (Codelet c : this.getCodeRack().getAllCodelets())
            c.setTimeStep(50);

        // Start Cognitive Cycle
        start();
    }

    public List<Codelet> getBehavioralCodelets(){
        return this.behavioralCodelets;
    }
}
