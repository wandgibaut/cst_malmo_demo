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
public class InnerSense extends Codelet {
    AgentHost agentHost;
    Memory sensoryMemory;

    public InnerSense(String name, AgentHost agentHost){
        super();
        this.agentHost = agentHost;
        setName(name);
    }
    @Override
    public void accessMemoryObjects() {
        this.sensoryMemory = this.getOutput("INNER");
    }

    @Override
    public void calculateActivation() {}

    @Override
    public void proc() {
        JsonObject inner = MalmoUtils.getPerceptions(agentHost.peekWorldState());
        if (inner.has("LineOfSight")){
            inner.remove("LineOfSight");
            sensoryMemory.setI(inner);
        }
    }



}
