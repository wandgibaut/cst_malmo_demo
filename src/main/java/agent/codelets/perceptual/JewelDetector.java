package agent.codelets.perceptual;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import com.google.gson.JsonObject;

/**
 *
 * @author Wandemberg Gibaut
 */
public class JewelDetector extends Codelet {
    Memory visionDataMemory;
    Memory innerDataMemory;
    Memory outputJewelPerceptMemory;

    public JewelDetector(String name){
        super();
        setName(name);
    }

    @Override
    public void accessMemoryObjects() {
        this.visionDataMemory = this.getInput("FAKE_VISION");
        this.innerDataMemory = this.getInput("INNER");
        this.outputJewelPerceptMemory = this.getOutput("JEWEL_DETECT");
    }

    @Override
    public void calculateActivation() {}

    @Override
    public void proc() {
        if(visionDataMemory.getI() != null && innerDataMemory.getI() != null){//&& inputDataMemory.getEvaluation() > lastPerceptionReceivedID){
            JsonObject sight = (JsonObject) visionDataMemory.getI();

            boolean detect = sight.get("type").getAsString().equals("emerald");

            outputJewelPerceptMemory.setI(detect);
        }
    }
}
