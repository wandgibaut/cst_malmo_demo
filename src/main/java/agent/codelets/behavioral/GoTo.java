package agent.codelets.behavioral;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Wandemberg Gibaut
 */
public class GoTo extends Codelet {
    Memory jewelDetection;
    Memory motorMO;

    public GoTo(String name){
        super();
        setName(name);
    }

    @Override
    public void accessMemoryObjects() {
        this.jewelDetection = this.getInput("JEWEL_DETECT");
        this.motorMO = this.getOutput("LEGS_FORWARD");
    }

    @Override
    public void calculateActivation() {}

    @Override
    public void proc() {
        if (jewelDetection.getI() != null ){//&& appleDetection.getI() != null){
            boolean detected = (boolean) jewelDetection.getI();
            List<String> commands;
            double level;
            if(detected){
                commands = Arrays.asList("move 0.8","turn 0.0");
                level = 1.0;
            }
            else{
                commands = Arrays.asList("move 0.0","turn 0.0");
                level = 0.0;
            }
            motorMO.setI(commands);
            motorMO.setEvaluation(level);

            try {
                this.setActivation(level);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
