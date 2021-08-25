/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package SoarBridge;

import Simulation.Environment;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.microsoft.msr.malmo.AgentHost;
import org.jsoar.kernel.Agent;
import org.jsoar.kernel.Phase;
import org.jsoar.kernel.RunType;
import org.jsoar.kernel.memory.Wme;
import org.jsoar.kernel.memory.Wmes;
import org.jsoar.kernel.symbols.DoubleSymbol;
import org.jsoar.kernel.symbols.Identifier;
import org.jsoar.kernel.symbols.StringSymbol;
import org.jsoar.kernel.symbols.Symbol;
import org.jsoar.kernel.symbols.SymbolFactory;
import org.jsoar.runtime.ThreadedAgent;
import org.jsoar.util.commands.SoarCommands;

import tempUtils.MalmoUtils;


/**
 *
 * @author Danilo Lucentini, Ricardo Gudwin and Wandemberg Gibaut
 */
public class SoarBridge
{
    // Log Variable
    Logger logger = Logger.getLogger(SoarBridge.class.getName());

    // SOAR Variables
    Agent soarAgent = null;
    public Identifier inputLink = null;

    // Entity Variables
    Identifier creature;
    Identifier creatureSensor;
    Identifier creatureParameters;
    Identifier creaturePosition;
    Identifier creatureMemory;
    
    Environment env;
    public AgentHost c;
    public String input_link_string = "";
    public String output_link_string = "";

    /**
     * Constructor class
     * @param _e Environment
     * @param path Path for Rule Base
     * @param startSOARDebugger set true if you wish the SOAR Debugger to be started
     */
    public SoarBridge(Environment _e, String path, Boolean startSOARDebugger) 
    {
        env = _e;
        c = env.getCreature();
        try
        {
            ThreadedAgent tag = ThreadedAgent.create();
            soarAgent = tag.getAgent();
            SoarCommands.source(soarAgent.getInterpreter(), path);
            inputLink = soarAgent.getInputOutput().getInputLink();

            // Initialize entities
            creature = null;

            // Debugger line
            if (startSOARDebugger)
            {
                soarAgent.openDebugger();
            }
        }
        catch (Exception e)
        {
            logger.severe("Error while creating SOAR Kernel");
            e.printStackTrace();
        }
    }

    private Identifier CreateIdWME(Identifier id, String s) {
        SymbolFactory sf = soarAgent.getSymbols();
        Identifier newID = sf.createIdentifier('I');
        soarAgent.getInputOutput().addInputWme(id, sf.createString(s), newID);
        return(newID);
    }
    
    private void CreateFloatWME(Identifier id, String s, double value) {
        SymbolFactory sf = soarAgent.getSymbols();
        DoubleSymbol newID = sf.createDouble(value);
        soarAgent.getInputOutput().addInputWme(id, sf.createString(s), newID);
    }
    
    private void CreateStringWME(Identifier id, String s, String value) {
        SymbolFactory sf = soarAgent.getSymbols();
        StringSymbol newID = sf.createString(value);
        soarAgent.getInputOutput().addInputWme(id, sf.createString(s), newID);
    }
    
    private String getItemType(JsonObject sight) {
         return sight.get("type").getAsString();
    }
    
    
    /**
     * Create the WMEs at the InputLink of SOAR
     */
    private void prepareInputLink() 
    {
        //SymbolFactory sf = soarAgent.getSymbols();
        AgentHost c = env.getCreature();
        JsonObject perceptions = MalmoUtils.getPerceptions(c.peekWorldState());

        inputLink = soarAgent.getInputOutput().getInputLink();
        try
        {
            if (soarAgent != null && perceptions.has("XPos"))
            {
              //SimulationCreature creatureParameter = (SimulationCreature)parameter;
              // Initialize Creature Entity
              creature = CreateIdWME(inputLink, "CREATURE");
              // Initialize Creature Memory
              creatureMemory = CreateIdWME(creature, "MEMORY");
              // Set Creature Parameters
              Calendar lCDateTime = Calendar.getInstance();
              creatureParameters = CreateIdWME(creature, "PARAMETERS");
              CreateFloatWME(creatureParameters, "MINFUEL", 400);
              CreateFloatWME(creatureParameters, "TIMESTAMP", lCDateTime.getTimeInMillis());
              // Setting creature Position
              creaturePosition = CreateIdWME(creature, "POSITION");
              CreateFloatWME(creaturePosition, "X", MalmoUtils.getStat(perceptions,"XPos").getAsDouble());
              CreateFloatWME(creaturePosition, "Y", MalmoUtils.getStat(perceptions,"YPos").getAsDouble());
              // Set creature sensors
              creatureSensor = CreateIdWME(creature, "SENSOR");
              // Create Fuel Sensors
              Identifier fuel = CreateIdWME(creatureSensor, "FUEL");
              CreateFloatWME(fuel, "VALUE", MalmoUtils.getStat(perceptions,"Food").getAsDouble());
              // Create Visual Sensors
              Identifier visual = CreateIdWME(creatureSensor, "VISUAL");

              //TODO: generalize

                JsonObject sight = perceptions.get("LineOfSight").getAsJsonObject();
                JsonArray inventory = perceptions.get("inventory").getAsJsonArray();

                Identifier entity = CreateIdWME(visual, "ENTITY");
                CreateFloatWME(entity, "DISTANCE", sight.get("distance").getAsDouble());
                CreateFloatWME(entity, "X", sight.get("x").getAsDouble());
                CreateFloatWME(entity, "Y", sight.get("y").getAsDouble());
                CreateStringWME(entity, "Type", sight.get("hitType").getAsString());
                CreateStringWME(entity, "NAME", sight.get("type").getAsString());

                //inventory

                Identifier inventoryId = CreateIdWME(creature,"INVENTORY");

                for(JsonElement item : inventory){
                    JsonObject itemObject = item.getAsJsonObject();

                    Identifier itemId = CreateIdWME(inventoryId,"ITEM");
                    CreateStringWME(itemId,"Type", itemObject.get("type").getAsString());
                    CreateStringWME(itemId,"INDEX", itemObject.get("index").getAsString());
                    CreateStringWME(itemId,"QUANTITY", itemObject.get("quantity").getAsString());
                }

            }
        }
        catch (Exception e)
        {
            logger.severe("Error while Preparing Input Link");
            e.printStackTrace();
        }
    }

    private double GetGeometricDistanceToCreature(double x1, double y1, double x2, double y2, double xCreature, double yCreature)
    {
          float squared_dist = 0.0f;
          double maxX = Math.max(x1, x2);
          double minX = Math.min(x1, x2);
          double maxY = Math.max(y1, y2);
          double minY = Math.min(y1, y2);

          if(xCreature > maxX)
          {
            squared_dist += (xCreature - maxX)*(xCreature - maxX);
          }
          else if(xCreature < minX)
          {
            squared_dist += (minX - xCreature)*(minX - xCreature);
          }

          if(yCreature > maxY)
          {
            squared_dist += (yCreature - maxY)*(yCreature - maxY);
          }
          else if(yCreature < minY)
          {
            squared_dist += (minY - yCreature)*(minY - yCreature);
          }

          return Math.sqrt(squared_dist);
    }

    private void resetSimulation() {
        soarAgent.initialize();
    }
    
    /**
     * Run SOAR until HALT
     */
    private void runSOAR() 
    {
        soarAgent.runForever(); 
    }
    
    private int stepSOAR() {
        soarAgent.runFor(1, RunType.PHASES);
        Phase ph = soarAgent.getCurrentPhase();
        if (ph.equals(Phase.INPUT)) return(0);
        else if (ph.equals(Phase.PROPOSE)) return(1);
        else if (ph.equals(Phase.DECISION)) return(2);
        else if (ph.equals(Phase.APPLY)) return(3);
        else if (ph.equals(Phase.OUTPUT)) {
            if (soarAgent.getReasonForStop() == null) return(4);
            else return(5);
        }
        else return(6);
    }

    private String GetParameterValue(String par) {
        List<Wme> Commands = Wmes.matcher(soarAgent).filter(soarAgent.getInputOutput().getOutputLink());
        List<Wme> Parameters = Wmes.matcher(soarAgent).filter(Commands.get(0));
        String parvalue = "";
        for (Wme w : Parameters) 
           if (w.getAttribute().toString().equals(par)) parvalue = w.getValue().toString();
        return(parvalue);
    }
    
    
    /**
     * Process the OutputLink given by SOAR and return a list of commands to WS3D
     * @return A List of SOAR Commands
     */
    private ArrayList<Command> processOutputLink() 
    {
        ArrayList<Command> commandList = new ArrayList<Command>();

        try
        {
            if (soarAgent != null)
            {
                List<Wme> Commands = Wmes.matcher(soarAgent).filter(soarAgent.getInputOutput().getOutputLink());

                for (Wme com : Commands)
                {
                    String name  = com.getAttribute().asString().getValue();
                    Command.CommandType commandType = Enum.valueOf(Command.CommandType.class, name);
                    Command command = null;

                    switch(commandType)
                    {
                        case MOVE:
                            Float rightVelocity = null;
                            Float leftVelocity = null;
                            Float linearVelocity = null;
                            Float xPosition = null;
                            Float yPosition = null;
                            Float moveSpeed = null;
                            rightVelocity = tryParseFloat(GetParameterValue("VelR"));
                            leftVelocity = tryParseFloat(GetParameterValue("VelL"));
                            linearVelocity = tryParseFloat(GetParameterValue("Vel"));
                            xPosition = tryParseFloat(GetParameterValue("X"));
                            yPosition = tryParseFloat(GetParameterValue("Y"));
                            moveSpeed = tryParseFloat(GetParameterValue("MoveSpeed"));
                            command = new Command(Command.CommandType.MOVE);
                            CommandMove commandMove = (CommandMove)command.getCommandArgument();
                            if (commandMove != null)
                            {
                                if (rightVelocity != null) commandMove.setRightVelocity(rightVelocity);
                                if (leftVelocity != null)  commandMove.setLeftVelocity(leftVelocity);
                                if (linearVelocity != null) commandMove.setLinearVelocity(linearVelocity);
                                if (xPosition != null) commandMove.setX(xPosition);
                                if (yPosition != null) commandMove.setY(yPosition);
                                if (moveSpeed != null) commandMove.setMoveSpeed(moveSpeed);
                                commandList.add(command);
                            }
                            else
                            {
                                logger.severe("Error processing MOVE command");
                            }
                            break;

                        case GET:
                            String thingNameToGet = null;
                            command = new Command(Command.CommandType.GET);
                            CommandGet commandGet = (CommandGet)command.getCommandArgument();
                            if (commandGet != null)
                            {
                                thingNameToGet = GetParameterValue("Name");
                                if (thingNameToGet != null) commandGet.setThingName(thingNameToGet);
                                commandList.add(command);
                            }
                            break;

                        case EAT:
                            String thingNameToEat = null;
                            command = new Command(Command.CommandType.EAT);
                            CommandEat commandEat = (CommandEat)command.getCommandArgument();
                            if (commandEat != null)
                            {
                                thingNameToEat = GetParameterValue("Name");
                                if (thingNameToEat != null) commandEat.setThingName(thingNameToEat);
                                commandList.add(command);
                            }
                            break;

                        default:
                            break;
                    }   
                }
            }
        }
        catch (Exception e)
        {
            logger.severe("Error while processing commands");
            e.printStackTrace();
        }

        return ((commandList.size() > 0) ? commandList : null);
    }
    
    /**
     * Perform a complete SOAR step
     *
     */
    public void step() {
        if (phase != -1) finish_msteps();
        resetSimulation();
        //c.getWorldState();
        prepareInputLink();
        input_link_string = stringInputLink();
        //printInputWMEs();
        runSOAR();
        output_link_string = stringOutputLink();
        //printOutputWMEs();
        List<Command> commandList = processOutputLink();
        processCommands(commandList);
        //resetSimulation();
    }
    
    
    public void prepare_mstep() {
        resetSimulation();
        c.getWorldState();
        prepareInputLink();
        input_link_string = stringInputLink();
    }
    
    public int phase=-1;
    public void mstep() {
        if (phase == -1) prepare_mstep();
        phase = stepSOAR();
        if (phase == 5) {
            post_mstep();
            phase = -1;
        }
    }
    
    public void finish_msteps() {
        while (phase != -1) mstep();
    }
    
    public void post_mstep() {
        output_link_string = stringOutputLink();
        //printOutputWMEs();
        List<Command> commandList = processOutputLink();
        processCommands(commandList);
        //resetSimulation();
    }

    private void processCommands(List<Command> commandList) {

        if (commandList != null)
        {
            for (Command command:commandList)
            {
                switch (command.getCommandType())
                {
                    case MOVE:
                        processMoveCommand((CommandMove)command.getCommandArgument());
                    break;

                    //case GET:
                    //    processGetCommand((CommandGet)command.getCommandArgument());
                    //break;

                    case EAT:
                        processEatCommand((CommandEat)command.getCommandArgument());
                    break;

                    default:System.out.println("Nenhum comando definido ...");
                        // Do nothing
                    break;
                }
            }
        }
        else System.out.println("comando nulo ...");
    }

    /**
     * Send Move Command to World Server
     * @param soarCommandMove Soar Move Command Structure
     */
    private void processMoveCommand(CommandMove soarCommandMove) {
        if (soarCommandMove != null)
        {
            if (soarCommandMove.getMoveSpeed() != null)
            {
                c.sendCommand("turn 0.0");
                c.sendCommand("move "+ soarCommandMove.getMoveSpeed());

                int jewels = MalmoUtils.getPerceptions(c.peekWorldState()).get("inventory").getAsJsonArray().get(0).getAsJsonObject().get("quantity").getAsInt();

                while(MalmoUtils.getPerceptions(c.peekWorldState()).get("inventory").getAsJsonArray().get(0).getAsJsonObject().get("quantity").getAsInt() == jewels){

                }

            }
            else
            {
                c.sendCommand("move 0.0");
                c.sendCommand("turn 0.1");
            }
        }
        else
        {
            logger.severe("Error processing processMoveCommand");
        }
    }

    /**
     * Send Get Command to World Server
     * @param soarCommandGet Soar Get Command Structure
     */
    /*private void processGetCommand(CommandGet soarCommandGet) throws CommandExecException
    {
        if (soarCommandGet != null)
        {
            c.putInSack(soarCommandGet.getThingName());
        }
        else
        {
            logger.severe("Error processing processMoveCommand");
        }
    }*/

     /**
     * Send Eat Command to World Server
     * @param soarCommandEat Soar Eat Command Structure
     */
    private void processEatCommand(CommandEat soarCommandEat)  {
        if (soarCommandEat != null)
        {
            c.sendCommand("hotbar.2 1");
            c.sendCommand("hotbar.2 0");
            c.sendCommand("use");
            //;c.eatIt(soarCommandEat.getThingName());
        }
        else
        {
            logger.severe("Error processing processMoveCommand");
        }
    }
    
    /**
     * Try Parse a Float Element
     * @param value Float Value
     * @return The Float Value or null otherwise
     */
    private Float tryParseFloat (String value)
    {
        Float returnValue = null;

        try
        {
            returnValue = Float.parseFloat(value);
        }
        catch (Exception ex)
        {
            returnValue = null;
        }

        return returnValue;
    }
    
    public void printWME(Identifier id) {
        printWME(id,0);
        
    }
    
    public void printWME(Identifier id, int level) {
        Iterator<Wme> It = id.getWmes();
        while (It.hasNext()) {
            Wme wme = It.next();
            Identifier idd = wme.getIdentifier();
            Symbol a = wme.getAttribute();
            Symbol v = wme.getValue();
            Identifier testv = v.asIdentifier();
            for (int i=0;i<level;i++) System.out.print("   ");
            if (testv != null) {
                System.out.print("("+idd.toString()+","+a.toString()+","+v.toString()+")\n");
                printWME(testv,level+1);
            }
            else System.out.print("("+idd.toString()+","+a.toString()+","+v.toString()+")\n");
        }   
    }
    
    public void printInputWMEs(){
        Identifier il = soarAgent.getInputOutput().getInputLink();
        System.out.println("Input --->");
        printWME(il);
    }
    
    public void printOutputWMEs(){
        Identifier ol = soarAgent.getInputOutput().getOutputLink();
        System.out.println("Output --->");
        printWME(ol);
    }
    
    public String stringWME(Identifier id) {
        String out = stringWME(id,0);
        return(out);
    }
    
    public String stringWME(Identifier id, int level) {
        String out="";
        Iterator<Wme> It = id.getWmes();
        while (It.hasNext()) {
            Wme wme = It.next();
            Identifier idd = wme.getIdentifier();
            Symbol a = wme.getAttribute();
            Symbol v = wme.getValue();
            Identifier testv = v.asIdentifier();
            for (int i=0;i<level;i++) out += "   ";
            if (testv != null) {
                out += "("+idd.toString()+","+a.toString()+","+v.toString()+")\n";
                out += stringWME(testv,level+1);
            }
            else out += "("+idd.toString()+","+a.toString()+","+v.toString()+")\n";
        }
       return(out); 
    }
    
    public String stringInputLink() {
        Identifier il = soarAgent.getInputOutput().getInputLink();
        String out = stringWME(il);
        return(out);
    }
    
    public String stringOutputLink() {
        Identifier ol = soarAgent.getInputOutput().getOutputLink();
        String out = stringWME(ol);
        return(out);
    }
    
    public Identifier getInitialState() {
        Set<Wme> allmem = soarAgent.getAllWmesInRete();
        for (Wme w : allmem) {
            Identifier id = w.getIdentifier();
            if (id.toString().equalsIgnoreCase("S1"))
                return(id);
        }
        return(null);
    }
    
    public List<Identifier> getStates() {
        List<Identifier> li = new ArrayList<Identifier>();
        Set<Wme> allmem = soarAgent.getAllWmesInRete();
        for (Wme w : allmem) {
            Identifier id = w.getIdentifier();
            if (id.isGoal()) {
                boolean alreadythere = false;
                for (Identifier icand : li)
                    if (icand == id) alreadythere = true;
                if (alreadythere == false) {
                    li.add(id);
                }
            }
        }
        return(li);
    }
    
    public Set<Wme> getWorkingMemory() {
        return(soarAgent.getAllWmesInRete());
    }
}
