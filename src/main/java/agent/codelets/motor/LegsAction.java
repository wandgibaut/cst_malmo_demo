package agent.codelets.motor;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import com.microsoft.msr.malmo.AgentHost;
import java.util.List;

/**
 *
 * @author Wandemberg Gibaut
 */
public class LegsAction extends Codelet {
    AgentHost agentHost;

    private Memory moveMotorMO;
    private Memory motorMO;

    public LegsAction(String name, AgentHost agentHost){
        super();
        this.agentHost = agentHost;
        setName(name);
    }

    @Override
    public void accessMemoryObjects() {
        this.motorMO = this.getInput("LEGS");
        this.moveMotorMO = this.getInput("LEGS_FORWARD");
    }

    @Override
    public void calculateActivation() {

    }

    @Override
    public void proc() {

        if (motorMO.getI() != null && moveMotorMO.getI() != null) {
            Double turn_activation = motorMO.getEvaluation();
            Double move_activation = moveMotorMO.getEvaluation();
            List commandList;
            if (move_activation >= turn_activation){
                commandList = (List) moveMotorMO.getI();

            }
            else{
                commandList = (List) motorMO.getI();
            }

            for (Object command : commandList){
                agentHost.sendCommand((String) command);
                try {
                    Thread.sleep((long) 10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
