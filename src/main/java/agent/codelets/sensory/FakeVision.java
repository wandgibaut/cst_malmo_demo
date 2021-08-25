package agent.codelets.sensory;

import Simulation.Environment;
import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.microsoft.msr.malmo.AgentHost;
import tempUtils.MalmoUtils;

import java.util.List;


public class FakeVision extends Codelet {
    AgentHost agentHost;

    private Memory sensoryMemory;
    private List<Memory> sensorInfoObjects;

    //private Memory floorSensoryMO = new MemoryObject();
    //private MemoryObject entitiesSensoryMO = new MemoryObject();
    //private MemoryObject commandSensoryMO = new MemoryObject();
    //private MemoryObject selfSensoryMO = new MemoryObject();

    //private Memory broadcastedActionMO;
    //private Memory deathMemory;

    public FakeVision(AgentHost agentHost){
        super();
        this.agentHost = agentHost;
    }

    @Override
    public void accessMemoryObjects() {
        this.sensoryMemory = this.getOutput("FAKE_VISION_MEMORY");

        //this.broadcastedActionMO = this.getBroadcast("NEXT_ACTION");
        //this.deathMemory = this.getOutput("DEATH");
    }

    @Override
    public void calculateActivation() {

    }

    @Override
    public void proc() {

        JsonObject perceptions = MalmoUtils.getPerceptions(agentHost.peekWorldState());

        JsonObject sight = perceptions.get("LineOfSight").getAsJsonObject();
        JsonArray inventory = perceptions.get("inventory").getAsJsonArray();


        //sight.get("distance").getAsDouble());
        //CreateFloatWME(entity, "X", sight.get("x").getAsDouble());
        //CreateFloatWME(entity, "Y", sight.get("y").getAsDouble());
        //CreateStringWME(entity, "Type", sight.get("hitType").getAsString());
        //CreateStringWME(entity, "NAME", sight.get("type").getAsString());


        sensoryMemory.setI(sight);
    }
}
