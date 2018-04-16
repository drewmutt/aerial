package aero.aerial.radiostation;

/**
 * Created by drewmutt on 5/25/15.
 */
public interface IRadioStation {
    void startStation();
    void stopStation();

    public float handleStationChangeReturnReception(int frequency);
    public void update();
    public void setVolumeFactor(float volumeFactor);

    public int getStationFrequency();
    public String getCurrentPlayingTitle();
    public String getStationName();

    public void shufflePlaylist();
}
