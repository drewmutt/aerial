package aero.aerial.soundasset;

import ddf.minim.AudioPlayer;

/**
 * Created by andrewsimmons on 6/9/15.
 */
public interface ISoundAsset {
    public AudioPlayer createAudioPlayer();
    public String getTitle();
    public String getArtist();

}
