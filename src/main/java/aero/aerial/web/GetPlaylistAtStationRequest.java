package aero.aerial.web;

import aero.aerial.Radio;
import aero.aerial.radiostation.*;
import aero.aerial.soundasset.ISoundAsset;
import aero.aerial.soundasset.MP3SoundAsset;
import aero.aerial.web.serializers.RadioStationSerializer;
import aero.aerial.web.serializers.SoundAssetSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import java.util.ArrayList;

/**
 * Created by andrewsimmons on 6/26/15.
 */
public class GetPlaylistAtStationRequest extends ServerResource
{
    @Get
    public Representation acceptRepresentation(Representation entity) throws ResourceException
    {
        getResponse().setAccessControlAllowOrigin("*");


        int frequency = Integer.parseInt(getQueryValue("frequency"));

        IRadioStation station = Radio.getInstance().getStationAtFrequency(frequency);

        if(station instanceof SoundAssetRadioStation)
        {
            SoundAssetRadioStation saStation = (SoundAssetRadioStation) station;

            GsonBuilder gson = new GsonBuilder();
            gson.registerTypeAdapter(MP3SoundAsset.class, new SoundAssetSerializer());
            Gson parser = gson.create();
            class CurrentPlayLocation
            {
                public int playIndex;
            }
            CurrentPlayLocation curr = new CurrentPlayLocation();
            curr.playIndex = saStation.getCurrentPlayingIndex();
            String jsonOut = parser.toJson(saStation.getSoundAssetPlaylist());
            return new StringRepresentation(jsonOut, MediaType.APPLICATION_JSON);
        }

        return new StringRepresentation("[{}]", MediaType.TEXT_PLAIN);

    }
}
