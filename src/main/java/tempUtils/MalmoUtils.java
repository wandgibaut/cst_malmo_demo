package tempUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.microsoft.msr.malmo.TimestampedStringVector;
import com.microsoft.msr.malmo.WorldState;

/**
 *
 * @author Wandemberg Gibaut
 */
public class MalmoUtils {


    public static JsonObject getPerceptions(WorldState worldState){
        TimestampedStringVector observations = worldState.getObservations();
        Gson gson = new Gson();



        if(!observations.isEmpty()){
            JsonElement jelem = gson.fromJson(observations.get(0).getText(), JsonElement.class);
            JsonObject json = jelem.getAsJsonObject();

            //prettyPrint(json);
            return json;
        }
        else{
            return new JsonObject();
        }
    }

    public static JsonElement getStat(JsonObject perception, String stat){

        if(perception.has(stat)) {
            JsonElement jsonStat = perception.get(stat);
            return jsonStat;
        }
        return null;
    }
}
