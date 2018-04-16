package aero.aerial.web;

import aero.aerial.Radio;
import aero.aerial.radiostation.IRadioStation;
import aero.aerial.radiostation.SoundCloudRadioStation;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import java.io.IOException;

/**
 * Created by andrewsimmons on 6/25/15.
 */
public class PopulateStationWithSoundcloudPlaylist extends ServerResource {

    @Get
    public Representation acceptRepresentation(Representation entity) throws ResourceException
    {
        String username = "";
        JSONObject request = null;

        int frequency = Integer.parseInt(getQueryValue("frequency"));
        int playlistId = Integer.parseInt(getQueryValue("playlistId"));

        SoundCloudRadioStation station = new SoundCloudRadioStation(playlistId, frequency);
        Radio.getInstance().replaceStationAtFrequency(frequency, station);

        return new StringRepresentation("{\"success\":true}", MediaType.TEXT_PLAIN);
    }
}