package aero.aerial.radiostation;

/**
 * Created by drewmutt on 5/27/15.
 */
public class AbstractRadioStation {
    protected String _stationName;
    protected int _stationFreq;

    public void update()
    {

    }

    public int getStationFrequency()
    {
        return _stationFreq;
    }

    public String getStationName() {
        return _stationName;
    }

    public void setStationName(String stationName) {
        _stationName = stationName;
    }

    public void shufflePlaylist()
    {

    }

    public void stopStation()
    {

    }
}
