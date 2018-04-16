package aero.aerial;

import aero.aerial.integrations.SoundCloudManager;
import aero.aerial.radiostation.IRadioStation;
import ddf.minim.AudioOutput;
import ddf.minim.Minim;

import java.util.ArrayList;

/**
 * Created by drewmutt on 5/25/15.
 */
public class Radio {
    private SoundCloudManager _soundCloudManager;
    private AerialApp _app;
    private ArrayList<IRadioStation> _stations;
    private StaticManager _staticManager;
    private RadioSettings _settings;
    private Minim _minim;
    private static Radio instance = null;
    private AudioOutput _lineOut;
    private int _currentStationFreq;

    public int getCurrentStationFreq() {
        return _currentStationFreq;
    }

    public ArrayList<IRadioStation> getStations() {
        return _stations;
    }

    public Radio()
    {
        _stations = new ArrayList<IRadioStation>();
        _staticManager = new StaticManager();
        _settings = new RadioSettings();
        _settings.setStaticVolMax(.125f);
        _settings.setStaticVolMin(0f);
        _settings.setReceptionFreqDistance(20);
        _settings.setReceptionLockDistance(5);
        _settings.setBandpassBWMax(10000);
    }

    public void setSoundCloudManager(SoundCloudManager soundCloudManager) {
        this._soundCloudManager = soundCloudManager;
    }

    public SoundCloudManager getSoundCloudManager() {
        return _soundCloudManager;
    }


    public String getFullPathForMedia(String fileName)
    {
        return _app.soundsPath(fileName);
    }
    public static Radio getInstance()
    {
        if(instance == null)
            instance = new Radio();

        return instance;
    }

    public RadioSettings getSettings()
    {
        return _settings;
    }

    public void startAllStations() {
        for (IRadioStation station : _stations)
        {
            station.startStation();
            station.setVolumeFactor(0);
        }
    }
    public void addRadioStation(IRadioStation station)
    {
        _stations.add(station);
    }

    public void updateAllStations()
    {
        _staticManager.update();
        for (IRadioStation station : _stations)
        {
            station.update();
        }
    }
    public void changeFrequency(int freq)
    {
        float reception = 0;
        for (IRadioStation station : _stations)
        {
            float stationReception = station.handleStationChangeReturnReception(freq);
            if(stationReception > reception)
                reception = stationReception;
        }
        _staticManager.handleStaticWithStationReception(reception);
        _currentStationFreq = freq;
    }

    public Minim getMinim() {
        return _minim;
    }

    public void setMinim(Minim _minim) {
        this._minim = _minim;
    }

    public StaticManager getStaticManager()
    {
        return this._staticManager;
    }


    public AudioOutput getLineOut() {
        return _lineOut;
    }

    public void setLineOut(AudioOutput _lineOut) {
        this._lineOut = _lineOut;
    }


    public AerialApp getApp() {
        return _app;
    }

    public void setApp(AerialApp App) {
        this._app = App;
    }

    public IRadioStation getStationAtFrequency(int frequency)
    {
        for (IRadioStation station : _stations)
        {
            if(station.getStationFrequency() == frequency)
                return station;
        }
        return null;
    }

    public void replaceStationAtFrequency(int frequency, IRadioStation radioStation)
    {
        IRadioStation displacedStation = null;
        for (IRadioStation station : _stations)
        {
            if(station.getStationFrequency() == frequency)
                displacedStation = station;
        }

        if(displacedStation != null)
        {
            displacedStation.stopStation();
            _stations.remove(displacedStation);
        }

        _stations.add(radioStation);
        radioStation.startStation();
    }
}
