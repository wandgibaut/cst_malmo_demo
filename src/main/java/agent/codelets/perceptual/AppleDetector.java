package agent.codelets.perceptual;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.representation.owrl.AbstractObject;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class AppleDetector extends Codelet {
    Memory visionDataMemory;
    Memory innerDataMemory;
    Memory outputApplePerceptMemory;
    List<Memory> sensorDataMemories;
    List<Memory> perceptsMO;

    @Override
    public void accessMemoryObjects() {
        this.visionDataMemory = this.getInput("FAKE_VISION");
        this.innerDataMemory = this.getInput("INNER");
        this.outputApplePerceptMemory = this.getOutput("APPLE_DETECT");
        //this.perceptsMO = (List<Memory>)this.getOutput("PERCEPTUAL_MEMORY");
        //this.outputPerceptualMemory = this.getOutput("PERCEPTUAL_MEMORY");
    }

    @Override
    public void calculateActivation() {

    }

    @Override
    public void proc() {
        if(visionDataMemory.getI() != null && innerDataMemory.getI() != null){//&& inputDataMemory.getEvaluation() > lastPerceptionReceivedID){
            JsonObject sight = (JsonObject) visionDataMemory.getI();
            JsonObject inner = (JsonObject) innerDataMemory.getI();
            boolean detect = false;

            if(sight.get("type").getAsString().equals("apple")){
                detect = true;

            }
            outputApplePerceptMemory.setI(detect);
            //sensorDataMemories = (List<Memory>)inputDataMemory.getI();

            /*if(outputApplePerceptMemory.getI() != null){
                perceptsMO = (List<Memory>) outputApplePerceptMemory.getI();
            }
            else{
                perceptsMO = new ArrayList<>();
            }*/

            /*
            AbstractObject self = new AbstractObject("self");

            AbstractObject commands = (AbstractObject)sensorDataMemories.get(2).getI();
            AbstractObject  stats = (AbstractObject)sensorDataMemories.get(3).getI();
            */
            //initActiveCommands();
            //reinicia se tiver morrido


            /*
            if((double)stats.getProperties().get(1).getQualityDimensions().get(0).getValue() < lastTimeAlive){
                initActiveCommands();
            }
            lastTimeAlive = (double)stats.getProperties().get(1).getQualityDimensions().get(0).getValue();
            updateActiveCommands(commands);

            //agora self tem 2 partes
            self.addCompositePart(stats);
            self.addCompositePart(commands);
            //self.addCompositePart(createActiveCommandsStruct());

            outputSelfPercept.setI(self);

            lastPerceptionReceivedID = inputDataMemory.getEvaluation();
            outputSelfPercept.setEvaluation(lastPerceptionReceivedID);
            */
        }
    }
}
