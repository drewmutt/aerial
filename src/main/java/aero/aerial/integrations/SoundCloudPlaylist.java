package aero.aerial.integrations;

import java.util.ArrayList;

/**
 * Created by andrewsimmons on 6/9/15.
 */
public class SoundCloudPlaylist {

    private final int _playlistID;
    ArrayList<SoundCloudTrack> _trackList;
    private String _title;

    public int getPlaylistID() {
        return _playlistID;
    }

    public String getTitle() {
        return _title;
    }

    public void setTitle(String name) {
        this._title = name;
    }

    public ArrayList<SoundCloudTrack> getTrackList() {
        return _trackList;
    }

    public void setTrackList(ArrayList<SoundCloudTrack> trackList) {
        _trackList = trackList;
    }

    public SoundCloudPlaylist(String title, int playlistId)
    {
        _title = title;
        _trackList = new ArrayList<>();
        _playlistID = playlistId;
    }

    public void addTrack(SoundCloudTrack track) {
        _trackList.add(track);
    }
}
