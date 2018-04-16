package aero.aerial.soundasset;

import aero.aerial.Radio;
import ddf.minim.AudioPlayer;

/**
 * Created by andrewsimmons on 6/9/15.
 */
public class MP3SoundAsset implements ISoundAsset {
    private String _mp3;
    private String _title;
    private String _artist;

    public String getTitle() {
        return _title;
    }

    public void setTitle(String title) {
        _title = title;
    }

    public String getArtist() {
        return _artist;
    }

    public void setArtist(String artist) {
        _artist = artist;
    }

    public MP3SoundAsset(String mp3File) {
        this._mp3 = mp3File;
        _title = mp3File;
        _artist = "";
    }

    public AudioPlayer createAudioPlayer()
    {
        return Radio.getInstance().getMinim().loadFile(_mp3);
    }

    @Override
    public String toString() {
        return super.toString() + " [" + _mp3 + "]";
    }

}
