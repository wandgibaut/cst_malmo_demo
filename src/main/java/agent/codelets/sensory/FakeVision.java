package agent.codelets.sensory;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import com.google.gson.JsonObject;
import com.microsoft.msr.malmo.AgentHost;
import tempUtils.MalmoUtils;

/**
 *
 * @author Wandemberg Gibaut
 */
public class FakeVision extends Codelet {
    private AgentHost agentHost;
    private Memory sensoryMemory;

    public FakeVision(String name, AgentHost agentHost){
        super();
        this.agentHost = agentHost;
        setName(name);
    }

    @Override
    public void accessMemoryObjects() {
        this.sensoryMemory = this.getOutput("FAKE_VISION");
    }

    @Override
    public void calculateActivation() {}

    @Override
    public void proc() {
        JsonObject perceptions = MalmoUtils.getPerceptions(agentHost.peekWorldState());
        if (perceptions.has("LineOfSight")){
            JsonObject sight = perceptions.get("LineOfSight").getAsJsonObject();
            sensoryMemory.setI(sight);
        }

    }
}
