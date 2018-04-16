package aero.aerial.web;

import aero.aerial.Radio;
import aero.aerial.radiostation.BeepingToneRadioStation;
import aero.aerial.radiostation.SoundAssetRadioStation;
import aero.aerial.radiostation.SoundCloudRadioStation;
import aero.aerial.radiostation.ToneRadioStation;
import aero.aerial.web.serializers.RadioStationSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Options;
import org.restlet.resource.ServerResource;

/**
 * Created by andrewsimmons on 6/11/15.
 */
public class GetCurrentFrequency  extends ServerResource
{
    @Get
    public Representation getCurrentFrequency() {
        getResponse().setAccessControlAllowOrigin("*");
        return new StringRepresentation(String.valueOf(Radio.getInstance().getCurrentStationFreq()), MediaType.TEXT_PLAIN);
    }


    @Options
    public void doOptions(Representation entity) {
        getResponse().setAccessControlAllowOrigin("*");
    }
}
