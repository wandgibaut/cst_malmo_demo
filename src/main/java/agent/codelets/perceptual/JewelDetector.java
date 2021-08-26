package agent.codelets.perceptual;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import com.google.gson.JsonObject;

import java.util.List;

public class JewelDetector extends Codelet {
    Memory visionDataMemory;
    Memory innerDataMemory;
    Memory outputJewelPerceptMemory;
    List<Memory> sensorDataMemories;
    List<Memory> perceptsMO;


    @Override
    public void accessMemoryObjects() {
        this.visionDataMemory = this.getInput("FAKE_VISION");
        this.innerDataMemory = this.getInput("INNER");
        this.outputJewelPerceptMemory = this.getOutput("JEWEL_DETECT");
    }

    @Override
    public void calculateActivation() {

    }

    @Override
    public void proc() {
        if(visionDataMemory.getI() != null && innerDataMemory.getI() != null){//&& inputDataMemory.getEvaluation() > lastPerceptionReceivedID){
            JsonObject sight = (JsonObject) visionDataMemory.getI();
            //JsonObject inner = (JsonObject) innerDataMemory.getI();
            boolean detect = false;

            if(sight.get("type").getAsString().equals("emerald")){
               detect = true;

            }
            outputJewelPerceptMemory.setI(detect);
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

    //public boolean containsNameOnJson(final JsonObject json, final String name){
    //    return json.stream().filter(o -> o.getName().equals(name)).findFirst().isPresent();
    //}

    public boolean containsName(final List<Memory> memoryList, final String name){
        return memoryList.stream().filter(o -> o.getName().equals(name)).findFirst().isPresent();
    }
}
