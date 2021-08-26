package agent.codelets.motor;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import com.microsoft.msr.malmo.AgentHost;

import java.util.List;

public class LegsAction extends Codelet {
    AgentHost agentHost;

    private Memory moveMotorMO;
    private Memory turnMotorMO;
    private Memory motorMO;
    private Memory motorForwardMO;
    //private Memory nextActionMemory;

    private Double moveSpec;
    private String fullCommand = "";
    private String lastCommand = "move 0.0";
    private Double lastForage = 1.0;

    public LegsAction(AgentHost agentHost){
        super();
        this.agentHost = agentHost;
    }

    @Override
    public void accessMemoryObjects() {
        //this.moveMotorMO = this.getInput("MOVE");
        //this.turnMotorMO = this.getInput("TURN");
        this.motorMO = this.getInput("LEGS");
        this.motorForwardMO = this.getInput("LEGS_FORWARD");
    }

    @Override
    public void calculateActivation() {

    }

    @Override
    public void proc() {
        //if(moveMotorMO.getI() != null){
        //    moveSpec = moveMotorMO.getEvaluation();
        //    fullCommand = (String) moveMotorMO.getI() + " " + moveSpec.toString();
        //}
        if(true){//lastCommand.split(" ")[1].equals("0.0")){
            if (motorMO.getI() != null) {
                if (lastForage != motorMO.getEvaluation()){
                    lastForage = motorMO.getEvaluation();
                    for (Object command : (List) motorMO.getI()){
                        agentHost.sendCommand((String) command);
                        try {
                            Thread.sleep((long) 0.01);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }


                    //fullCommand = "turn 0.1";
                    //agent_host.sendCommand(fullCommand);

                }
            }
        }
        /*if(nextActionMemory.getI() != null){
            //String broadcastedAction = (String)nextActionMemory.getI();
            //if(broadcastedAction.split("\\s")[0].equals("turn")){
            if(isThereARelevantBroadcast("move")){
                fullCommand = getBroadcastCommand("move");
            }
        }*/


        //se existir e n estiver vazia
        //if(this.getBroadcast()!=null && !this.getBroadcast().isEmpty()){
        //o comando vindo da consciencia sobrepoe o "natural"
        //fullCommand = getBroadcastCommand("move");
        //}

        //if(!fullCommand.equals(lastCommand)){
        //    agentHost.sendCommand(fullCommand);
        //    lastCommand = fullCommand;
        //}

        //comando de enviar pro Malmo
    }
}
