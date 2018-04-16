package aero.aerial.web.serializers;

import aero.aerial.radiostation.IRadioStation;
import aero.aerial.soundasset.ISoundAsset;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by andrewsimmons on 6/26/15.
 */
public class SoundAssetSerializer implements JsonSerializer<ISoundAsset>
{

    @Override
    public JsonElement serialize(ISoundAsset soundAsset, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject root = new JsonObject();
        root.addProperty("title", soundAsset.getTitle());
        root.addProperty("artist", soundAsset.getArtist());
        return root;
    }
}
