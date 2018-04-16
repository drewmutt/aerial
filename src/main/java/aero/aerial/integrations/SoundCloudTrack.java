package aero.aerial.integrations;

import aero.aerial.soundasset.MP3SoundAsset;

/**
 * Created by andrewsimmons on 6/9/15.
 */
public class SoundCloudTrack {
    private int _trackID;
    private MP3SoundAsset _soundAsset;
    private String _streamURL;

    public SoundCloudTrack(int trackID, String title, String artist, String streamURL)
    {
        _trackID = trackID;
        _soundAsset = new MP3SoundAsset(getLocalFileName());
        _soundAsset.setTitle(title);
        _soundAsset.setArtist(artist);
        _streamURL = streamURL;
    }

    public String getStreamURL() {
        return _streamURL;
    }

    public String getLocalFileName()
    {
        return _trackID + ".mp3";
    }


    public int getTrackID() {
        return _trackID;
    }

    public MP3SoundAsset getSoundAsset() {
        return _soundAsset;
    }
}
