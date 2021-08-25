package agent.main;

import Simulation.Environment;
import agent.codelets.behavioral.EatApple;
import agent.codelets.behavioral.Forage;
import agent.codelets.behavioral.GoTo;
import agent.codelets.motor.HandsAction;
import agent.codelets.motor.LegsAction;
import agent.codelets.perceptual.AppleDetector;
import agent.codelets.perceptual.JewelDetector;
import agent.codelets.sensory.FakeVision;
import agent.codelets.sensory.InnerSense;
import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryObject;
import br.unicamp.cst.core.entities.Mind;
import br.unicamp.cst.util.MindViewer;

import java.util.Collections;

public class AgentMind extends Mind {

    public AgentMind(Environment env) {
        super();

        // Declare Memory Objects
        MemoryObject legsMO;
        MemoryObject handsMO;
        MemoryObject visionMO;
        MemoryObject innerSenseMO;
        MemoryObject closestAppleMO;
        MemoryObject knownApplesMO;

        //Initialize Memory Objects
        legsMO=createMemoryObject("LEGS", "");
        handsMO=createMemoryObject("HANDS", "");
        //List<Thing> vision_list = Collections.synchronizedList(new ArrayList<Thing>());
        visionMO=createMemoryObject("FAKE_VISION_MEMORY","");
        InnerSense cis = new InnerSense(env.getCreature());
        innerSenseMO=createMemoryObject("INNER", cis);
        //Thing closestApple = null;
        closestAppleMO=createMemoryObject("CLOSEST_APPLE", "");
        //List<Thing> knownApples = Collections.synchronizedList(new ArrayList<Thing>());
        knownApplesMO=createMemoryObject("KNOWN_APPLES", "");

        // Create and Populate MindViewer
        //MindViewer mv = new MindViewer(Mind mind, "MindView", List<Codelet> behavioralCodelets);
        //mv.addMO(knownApplesMO);
        //mv.addMO(visionMO);
        //mv.addMO(closestAppleMO);
        //mv.addMO(innerSenseMO);
        //mv.addMO(handsMO);
        //mv.addMO(legsMO);
        //mv.StartTimer();
        //mv.setVisible(true);

        // Create Sensor Codelets
        FakeVision vision=new FakeVision(env.getCreature());
        vision.addOutput(visionMO);
        insertCodelet(vision); //Creates a vision sensor

        InnerSense innerSense=new InnerSense(env.getCreature());
        innerSense.addOutput(innerSenseMO);
        insertCodelet(innerSense); //A sensor for the inner state of the creature

        // Create Actuator Codelets
        LegsAction legs=new LegsAction(env.getCreature());
        legs.addInput(legsMO);
        insertCodelet(legs);

        HandsAction hands=new HandsAction(env.getCreature());
        hands.addInput(handsMO);
        insertCodelet(hands);

        // Create Perception Codelets
        Codelet ad = new AppleDetector();
        ad.addInput(visionMO);
        ad.addOutput(knownApplesMO);
        insertCodelet(ad);

        JewelDetector closestAppleDetector = new JewelDetector();
        closestAppleDetector.addInput(knownApplesMO);
        closestAppleDetector.addInput(innerSenseMO);
        closestAppleDetector.addOutput(closestAppleMO);
        insertCodelet(closestAppleDetector);

        // Create Behavior Codelets
        GoTo goToClosestApple = new GoTo(/*creatureBasicSpeed,reachDistance*/);
        goToClosestApple.addInput(closestAppleMO);
        goToClosestApple.addInput(innerSenseMO);
        goToClosestApple.addOutput(legsMO);
        insertCodelet(goToClosestApple);

        EatApple eatApple=new EatApple(/*reachDistance*/);
        eatApple.addInput(closestAppleMO);
        eatApple.addInput(innerSenseMO);
        eatApple.addOutput(handsMO);
        eatApple.addOutput(knownApplesMO);
        insertCodelet(eatApple);

        Codelet forage=new Forage();
        forage.addInput(knownApplesMO);
        forage.addOutput(legsMO);
        insertCodelet(forage);

        // sets a time step for running the codelets to avoid heating too much your machine
        for (Codelet c : this.getCodeRack().getAllCodelets())
            c.setTimeStep(200);

        // Start Cognitive Cycle
        start();
    }
}
