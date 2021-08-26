package agent.codelets.behavioral;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;

import java.util.Arrays;
import java.util.List;

public class GoTo extends Codelet {
    Memory jewelDetection;
    Memory motorMO;

    @Override
    public void accessMemoryObjects() {
        this.jewelDetection = this.getInput("JEWEL_DETECT");
        this.motorMO = this.getOutput("LEGS_FORWARD");
    }

    @Override
    public void calculateActivation() {

    }

    @Override
    public void proc() {
        if (jewelDetection.getI() != null ){//&& appleDetection.getI() != null){
            boolean detected = (boolean) jewelDetection.getI();
            List<String> commands;
            if(detected){
                commands = Arrays.asList("move 0.1","turn 0.0");
                motorMO.setI(commands);
                motorMO.setEvaluation(1.0);
            }
            else{
                commands = Arrays.asList("move 0.0","turn 0.0");
                motorMO.setI(commands);
                motorMO.setEvaluation(0.0);
            }
        }
    }
}
