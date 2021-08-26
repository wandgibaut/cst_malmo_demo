package agent.codelets.sensory;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.microsoft.msr.malmo.AgentHost;
import tempUtils.MalmoUtils;

import java.util.List;

public class InnerSense extends Codelet {
    AgentHost agentHost;
    Memory sensoryMemory;

    public InnerSense(AgentHost agentHost){
        super();
        this.agentHost = agentHost;
    }
    @Override
    public void accessMemoryObjects() {
        this.sensoryMemory = this.getOutput("INNER");
    }

    @Override
    public void calculateActivation() {

    }

    @Override
    public void proc() {
        JsonObject perceptions = MalmoUtils.getPerceptions(agentHost.peekWorldState());

        JsonObject sight = perceptions.get("LineOfSight").getAsJsonObject();
        JsonArray inventory = perceptions.get("inventory").getAsJsonArray();


        JsonObject inner = perceptions;
        inner.remove("LineOfSight");

        //sight.get("distance").getAsDouble());
        //CreateFloatWME(entity, "X", sight.get("x").getAsDouble());
        //CreateFloatWME(entity, "Y", sight.get("y").getAsDouble());
        //CreateStringWME(entity, "Type", sight.get("hitType").getAsString());
        //CreateStringWME(entity, "NAME", sight.get("type").getAsString());


        sensoryMemory.setI(inner);
    }



}
