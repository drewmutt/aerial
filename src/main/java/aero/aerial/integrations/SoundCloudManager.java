package aero.aerial.integrations;

import aero.aerial.Radio;
import aero.aerial.util.RadioUtil;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;
import org.urbanstew.soundcloudapi.SoundCloudAPI;
import sun.net.www.http.HttpClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by andrewsimmons on 6/9/15.
 */
public class SoundCloudManager
{
    private int _currentUserID;
    SoundCloudAPI _api;

    ArrayList <SoundCloudPlaylist> _cachedUserPlaylists;

    public SoundCloudManager(int _currentUserID)
    {
        this._currentUserID = _currentUserID;
        _api = new SoundCloudAPI("eec82872ebe1fbc7e90ef15e7e923e4a", "31003279f7b5e660d47931837643c55b", SoundCloudAPI.USE_PRODUCTION.with(SoundCloudAPI.OAuthVersion.V2_0));
    }

    /*
    public SoundCloudPlaylist getPlaylistByID(int playlistID)
    {
        ArrayList <SoundCloudPlaylist> playlists = getPlaylistsForCurrentUser();

        for (SoundCloudPlaylist playlist : playlists) {
            if(playlist.getPlaylistID() == playlistID)
                return playlist;
        }
        return null;
    }
*/
    public void setUserIDFromUsername(String username)
    {
        String responseString;
        HttpResponse response = null;

        try
        {
            response = _api.get("users?q=" + username);
            if(response.getStatusLine().getStatusCode() == 200)
            {
                InputStream content = response.getEntity().getContent();
                responseString = RadioUtil.convertStreamToString(content);
                responseString = "{ \"response\":"+responseString + "}";
                JSONObject myObject = new JSONObject(responseString);
                JSONArray jsonResponse = (JSONArray) myObject.get("response");
                JSONObject userData = jsonResponse.getJSONObject(0);
                _currentUserID = (Integer) userData.get("id");
                System.out.println(responseString);
                _cachedUserPlaylists = null;
            }
            else
            {
                System.out.println("Got non 200 from SoundCloud: " + response.getStatusLine().getStatusCode());
            }
        } catch (OAuthMessageSignerException | OAuthCommunicationException | IOException | OAuthExpectationFailedException e) {
            e.printStackTrace();
        }
    }


    public SoundCloudPlaylist getPlaylistById(int playlistId)
    {
        try {
            HttpResponse response = _api.get("playlists/" + playlistId);
            if(response.getStatusLine().getStatusCode() == 200) {
                InputStream content = response.getEntity().getContent();
                String responseString = RadioUtil.convertStreamToString(content);
//                responseString = "{ \"response\":" + responseString + "}";
                JSONObject responseObject = new JSONObject(responseString);
//                JSONArray playlistResponse = (JSONArray) responseObject.get("response");

                SoundCloudPlaylist playlist = new SoundCloudPlaylist(responseObject.getString("title"), responseObject.getInt("id"));
//                userPlaylists.add(playlist);


                JSONArray tracksObject = (JSONArray) responseObject.get("tracks");
                for (int j = 0; j < tracksObject.length(); j++)
                {
                    JSONObject trackResponse = tracksObject.getJSONObject(j);
                    String trackTitle = trackResponse.getString("title");
                    String trackArtist = trackResponse.getJSONObject("user").getString("username");
                    if(trackResponse.has("stream_url"))
                    {
                        SoundCloudTrack track = new SoundCloudTrack(trackResponse.getInt("id"), trackTitle, trackArtist, trackResponse.getString("stream_url"));
                        playlist.addTrack(track);
                    }
                }
                return playlist;
            }
        } catch (OAuthMessageSignerException | OAuthCommunicationException | IOException | OAuthExpectationFailedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<SoundCloudPlaylist> getPlaylistsForCurrentUser()
    {
        if(_cachedUserPlaylists != null)
            return _cachedUserPlaylists;

        ArrayList <SoundCloudPlaylist> userPlaylists = new ArrayList<>();
        try
        {
            HttpResponse response = _api.get("users/" + _currentUserID + "/playlists");
            if(response.getStatusLine().getStatusCode() == 200)
            {
                InputStream content = response.getEntity().getContent();
                String responseString = RadioUtil.convertStreamToString(content);
                responseString = "{ \"response\":"+responseString + "}";
                JSONObject responseObject2 = new JSONObject(responseString);
                JSONArray playlistsResponse = (JSONArray) responseObject2.get("response");

                for (int i = 0; i < playlistsResponse.length(); i++) {

                    JSONObject playlistResponse = playlistsResponse.getJSONObject(i);

                    SoundCloudPlaylist playlist = new SoundCloudPlaylist(playlistResponse.getString("title"), playlistResponse.getInt("id"));
                    userPlaylists.add(playlist);


                    JSONArray tracksObject = (JSONArray) playlistResponse.get("tracks");
                    for (int j = 0; j < tracksObject.length(); j++)
                    {
                        JSONObject trackResponse = tracksObject.getJSONObject(j);
                        String trackTitle = trackResponse.getString("title");
                        String trackArtist = trackResponse.getJSONObject("user").getString("username");
                        if(trackResponse.has("stream_url"))
                        {
                            SoundCloudTrack track = new SoundCloudTrack(trackResponse.getInt("id"), trackTitle, trackArtist, trackResponse.getString("stream_url"));
                            playlist.addTrack(track);
                        }
                    }
                }

                _cachedUserPlaylists = userPlaylists;
                return userPlaylists;
            }
        } catch (OAuthMessageSignerException | OAuthCommunicationException | IOException | OAuthExpectationFailedException e) {
            e.printStackTrace();
        }
            return null;
        }

    public boolean isTrackDownloaded(SoundCloudTrack track)
    {
        File f = new File(Radio.getInstance().getFullPathForMedia(track.getLocalFileName()));
        return f.exists();
    }

    public String getMP3URLFromSSLStreamURL(String stream) throws OAuthCommunicationException, OAuthExpectationFailedException, OAuthMessageSignerException, IOException {
        DefaultHttpClient client = new DefaultHttpClient();
        client = new DefaultHttpClient(new ThreadSafeClientConnManager(client.getParams(), client.getConnectionManager().getSchemeRegistry()), client.getParams());
        String signedResource = _api.signStreamUrl(stream);
        HttpGet get = new HttpGet(signedResource);
        get.getParams().setBooleanParameter("http.protocol.handle-redirects", false);
        HttpResponse resp = client.execute(get);

//        System.out.println("> Got response... " + track.getLocalFileName() + " | " + track.getStreamURL());
        if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY) {
            final Header location = resp.getFirstHeader("Location");
            if (location != null && location.getValue() != null) {
                return location.getValue();
            }
        }

        return null;
    }

    public void downloadSoundCloudTrackIfNeeded(SoundCloudTrack track)
    {
        if(isTrackDownloaded(track))
        {
            System.out.println("Already have track downloaded.. " + track.getLocalFileName());
            return;
        }

        try
        {
            System.out.println("> Requesting Track Data... " + track.getLocalFileName() + " | " + track.getStreamURL());

            String redirectedStreamUrl = getMP3URLFromSSLStreamURL(track.getStreamURL());

            if(redirectedStreamUrl == null)
            {
                System.out.println(" --- Couldn't get stream url -- " + track.getLocalFileName());
            }

            String saveLocation = Radio.getInstance().getFullPathForMedia(track.getLocalFileName());
            System.out.println("Saving to... " + saveLocation + " from " + redirectedStreamUrl);
            RadioUtil.saveUrl(saveLocation, redirectedStreamUrl);
            System.out.println("Saved");

        } catch (OAuthMessageSignerException | OAuthCommunicationException | IOException | OAuthExpectationFailedException e) {
            e.printStackTrace();
        }
    }
}
