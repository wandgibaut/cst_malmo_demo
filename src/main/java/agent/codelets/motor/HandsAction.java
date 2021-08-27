package agent.codelets.motor;

import br.unicamp.cst.core.entities.Codelet;
import com.microsoft.msr.malmo.AgentHost;

/**
 *
 * @author Wandemberg Gibaut
 */
public class HandsAction extends Codelet {
    AgentHost agentHost;

    public HandsAction(String name, AgentHost agentHost){
        super();
        this.agentHost = agentHost;
        setName(name);
    }
    @Override
    public void accessMemoryObjects() {

    }

    @Override
    public void calculateActivation() {

    }

    @Override
    public void proc() {

    }
}
