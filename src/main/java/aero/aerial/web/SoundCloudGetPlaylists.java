package aero.aerial.web;

import aero.aerial.Radio;
import aero.aerial.integrations.SoundCloudPlaylist;
import aero.aerial.radiostation.IRadioStation;
import aero.aerial.radiostation.SoundCloudRadioStation;
import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by andrewsimmons on 6/8/15.
 */
public class SoundCloudGetPlaylists extends ServerResource {
    @Override
    @Get
    public String toString() {
        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">" +
                "<form> <input name='username'/></form></html>";
        return html;
    }

    @Post
    public Representation acceptRepresentation(Representation entity) throws ResourceException
    {
        String username = "";
        try {
            username = entity.getText();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Radio.getInstance().getSoundCloudManager().setUserIDFromUsername(username);
        ArrayList<SoundCloudPlaylist> playlistObjects = Radio.getInstance().getSoundCloudManager().getPlaylistsForCurrentUser();

        JSONObject responseObject = new JSONObject();
        JSONArray playlists = new JSONArray();
        responseObject.append("playlists", playlists);

        ArrayList<IRadioStation> stations = Radio.getInstance().getStations();


        for (int i = 0; i < playlistObjects.size(); i++)
        {
            JSONObject playlist = new JSONObject();
            playlists.put(playlist);

            SoundCloudPlaylist playlistObject = playlistObjects.get(i);
            playlist.append("title", playlistObject.getTitle());

            int freq = 0;
            for (IRadioStation station : stations)
            {
                if(station instanceof SoundCloudRadioStation)
                {
                    SoundCloudRadioStation scStation = (SoundCloudRadioStation) station;
                    if(scStation.getSoundCloudPlaylist().getPlaylistID() == playlistObject.getPlaylistID())
                    {
                        freq = scStation.getStationFrequency();
                    }
                }

            }

            playlist.append("freq", freq);

        }

        return new StringRepresentation(responseObject.toString(), MediaType.TEXT_PLAIN);
    }
}
