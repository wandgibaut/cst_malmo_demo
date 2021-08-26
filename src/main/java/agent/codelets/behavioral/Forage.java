package agent.codelets.behavioral;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

public class Forage extends Codelet {
    Memory jewelDetection;
    Memory motorMO;

    @Override
    public void accessMemoryObjects() {
        this.jewelDetection = this.getInput("JEWEL_DETECT");
        //this.perceptsOutMO = (MemoryObject)this.getOutput("PERCEPTS");
        //this.turnMotorMC = (MemoryContainer)this.getOutput("TURN_MOTOR_CONTAINER");
        this.motorMO = this.getOutput("LEGS");
    }

    @Override
    public void calculateActivation() {

    }

    @Override
    public void proc() {
        if (jewelDetection.getI() != null ){//&& appleDetection.getI() != null){
            boolean detected = (boolean) jewelDetection.getI();
            List<String> commands;
            if(!detected){
                commands = Arrays.asList("move 0.0","turn 0.1");
                motorMO.setI(commands);
                motorMO.setEvaluation(1.0);
            }
            else{
                commands = Arrays.asList("move 0.0","turn 0.0");
                motorMO.setI(commands);
                motorMO.setEvaluation(0.0);
            }

            // se n detectar, procure

            //if (!detected){
            //    turn = true
            //}
        }
    }
}
