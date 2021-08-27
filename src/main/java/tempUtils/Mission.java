package tempUtils;

import com.google.gson.JsonObject;
import com.microsoft.msr.malmo.AgentHost;
import com.microsoft.msr.malmo.MissionRecordSpec;
import com.microsoft.msr.malmo.MissionSpec;
import com.microsoft.msr.malmo.WorldState;
import java.io.File;
import java.util.Random;

import static org.apache.commons.io.FileUtils.readFileToString;


/**
 *
 * @author Wandemberg Gibaut
 */
public class Mission {

    private MissionSpec my_mission = new MissionSpec();// = null;
    private MissionRecordSpec my_mission_record = null;

    public Mission(){
        File file = new File("./resources/mission_test.xml");
        String xmlString ="";
        try{
            xmlString = readFileToString(file, "UTF-8");
        }
        catch(Exception e){
            e.printStackTrace();
        }

        try {
            this.my_mission = new MissionSpec(xmlString,true);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        this.my_mission.drawItem(5, 227, 2, "emerald");
        this.my_mission.setViewpoint(1);
        this.my_mission.timeLimitInSeconds(40);
        this.my_mission.requestVideo(320, 240);


        populate();

        this.my_mission_record = configureMissionRecord();

    }

    public Mission(String mission_xml, int timeLimitInSeconds, boolean shouldRequestVideo, boolean shouldPopulate){

        File file = new File(mission_xml);
        String xmlString ="";
        try{
            xmlString = readFileToString(file, "UTF-8");
        }
        catch(Exception e){
            e.printStackTrace();
        }

        try {
            this.my_mission = new MissionSpec(xmlString,true);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        this.my_mission.timeLimitInSeconds(timeLimitInSeconds);
        if(shouldRequestVideo) this.my_mission.requestVideo(320, 240);


        if(shouldPopulate) populate();

        this.my_mission_record = configureMissionRecord();

    }

    public MissionRecordSpec configureMissionRecord(){
        MissionRecordSpec my_mission_record = new MissionRecordSpec("./saved_data.tgz");
        my_mission_record.recordCommands();
        my_mission_record.recordMP4(20, 400000);
        my_mission_record.recordRewards();
        my_mission_record.recordObservations();
        return my_mission_record;
    }

    public MissionRecordSpec configureMissionRecord(boolean recordCommands, boolean recordRewards,
                                                    boolean recordObservations, boolean recordVideo){
        MissionRecordSpec my_mission_record = new MissionRecordSpec("./saved_data.tgz");

        if(recordCommands) my_mission_record.recordCommands();
        if(recordVideo) my_mission_record.recordMP4(20, 400000);
        if(recordRewards) my_mission_record.recordRewards();
        if(recordObservations) my_mission_record.recordObservations();
        return my_mission_record;
    }

    public void startMission(AgentHost agent_host){

        try{
            agent_host.startMission(my_mission, my_mission_record);
        }
        catch(Exception e){
            System.out.println("Error starting mission: " + e.getMessage());
            System.exit(1);
        }

        System.out.print("Waiting for the mission to start");

        WorldState world_state = agent_host.getWorldState();

        do{
            System.out.print(".");
            try{
                Thread.sleep(100);
            }
            catch(Exception eX){
                System.out.println("User interrupted while waiting for mission to start.");
                return;
            }
            world_state = agent_host.getWorldState();


        }while(!world_state.getIsMissionRunning());
        System.out.println("");

        //Main loop
        //agent_host.sendCommand("turn 0.1");
        do{

            world_state = agent_host.peekWorldState();
            System.out.println("system is running");
            //soarBridge.step();
            //grow();
            growTorch();

            //
            try{
                Thread.sleep(100);
            }
            catch(InterruptedException ex){
                System.out.println("User interrupted while mission was running.");
                return;
            }
        }while(world_state.getIsMissionRunning());

        System.out.println("End of Simulation!");
        System.exit(0);

    }




    MissionSpec getMission(){
        return this.my_mission;
    }

    MissionRecordSpec getMissionRecord(){
        return this.my_mission_record;
    }

    private void populate(){
        Random rand = new Random();
        int signal = 1;
        for(int i =1; i < 50; i++){
            if(!rand.nextBoolean()) signal *= -1;
            this.my_mission.drawItem(signal*rand.nextInt(5), 227, signal*rand.nextInt(5), "emerald");
        }

        this.my_mission.drawItem(7, 227, 3, "emerald");

        if(!rand.nextBoolean()) signal *= -1;
        this.my_mission.drawItem(signal*rand.nextInt(5), 227, signal*rand.nextInt(5), "apple");

        if(!rand.nextBoolean()) signal *= -1;
        this.my_mission.drawItem(signal*rand.nextInt(5), 227, signal*rand.nextInt(5), "apple");



    }

    private void grow(){
        Random rand = new Random();
        int signal = 1;
        for(int i =1; i < 5; i++){
            if(!rand.nextBoolean()) signal *= -1;
            this.my_mission.drawItem(signal*rand.nextInt(20), 227, signal*rand.nextInt(20), "emerald");
        }

        if(!rand.nextBoolean()) signal = -1;
        this.my_mission.drawItem(signal*rand.nextInt(20), 227, signal*rand.nextInt(20), "apple");
    }

    private void growTorch(){
        Random rand = new Random();
        int signal = 1;
        for(int i =1; i < 5; i++){
            if(!rand.nextBoolean()) signal *= -1;
            this.my_mission.drawItem(signal*rand.nextInt(20), 227, signal*rand.nextInt(20), "torch");
        }

    }
}
