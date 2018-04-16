package aero.aerial.web.serializers;

import aero.aerial.radiostation.IRadioStation;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by andrewsimmons on 6/10/15.
 */
public class RadioStationSerializer implements JsonSerializer<IRadioStation> {

    @Override
    public JsonElement serialize(IRadioStation iRadioStation, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject root = new JsonObject();
        root.addProperty("frequency", iRadioStation.getStationFrequency());
        root.addProperty("title", iRadioStation.getStationName());
        root.addProperty("playing", iRadioStation.getCurrentPlayingTitle());
        root.addProperty("type", iRadioStation.getClass().getSimpleName());
        return root;
    }

}
