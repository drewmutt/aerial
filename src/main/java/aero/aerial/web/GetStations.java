package aero.aerial.web;

import aero.aerial.Radio;
import aero.aerial.radiostation.*;
import aero.aerial.web.serializers.RadioStationSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.jndi.cosnaming.IiopUrl;
import org.restlet.Message;
import org.restlet.data.Form;
import org.restlet.data.Header;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Options;
import org.restlet.resource.ServerResource;
import org.restlet.util.Series;

import java.util.concurrent.ConcurrentMap;

/**
 * Created by andrewsimmons on 6/10/15.
 */
public class GetStations extends ServerResource {

    @Get
    public Representation getAllStations() {
//
        getResponse().setAccessControlAllowOrigin("*");

        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(SoundCloudRadioStation.class, new RadioStationSerializer());
        gson.registerTypeAdapter(ToneRadioStation.class, new RadioStationSerializer());
        gson.registerTypeAdapter(BeepingToneRadioStation.class, new RadioStationSerializer());
        gson.registerTypeAdapter(SoundAssetRadioStation.class, new RadioStationSerializer());

        Gson parser = gson.create();

        String jsonOut = parser.toJson(Radio.getInstance().getStations());
        return new StringRepresentation(jsonOut, MediaType.APPLICATION_JSON);
    }

    @Options
    public void doOptions(Representation entity) {
        getResponse().setAccessControlAllowOrigin("*");
    }
}
