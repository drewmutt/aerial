package aero.aerial;

import aero.aerial.radiostation.IRadioStation;
import aero.aerial.util.RadioUtil;
import ddf.minim.AudioOutput;
import ddf.minim.signals.WhiteNoise;

import java.util.ArrayList;


/**
 * Created by drewmutt on 5/25/15.
 */
public class StaticManager {

    private ArrayList<IRadioStation> backgroundStations;
    private AudioOutput _out;
    WhiteNoise _noise;
    private float _noiseVolumeFactor;
    private float _noiseVolume;

    public StaticManager()
    {
        _noise = new WhiteNoise(0);
        backgroundStations = new ArrayList<IRadioStation>();
    }

    public void addBackgroundStation(IRadioStation station)
    {
        backgroundStations.add(station);
    }

    public void update()
    {
        if(Math.random() > .98)
            _noiseVolumeFactor = (float) (Math.random() * .5) + 0.5f;

        if(_noiseVolumeFactor >= 1)
            _noiseVolumeFactor = 1;
        else
            _noiseVolumeFactor += .2;

        _noise.setAmp(_noiseVolume * _noiseVolumeFactor);
    }

/*
    public void setVolumeFactorForAllExcept(IRadioStation station, float volume)
    {
        _noise.setAmp(volume);
        for (IRadioStation backgroundStation : backgroundStations)
        {
            if(backgroundStation != station)
                backgroundStation.setVolumeFactor(volume);

        }
    }
    */
    public void handleStaticWithStationReception(float reception)
    {
        float noiseVol = RadioUtil.map(reception, 0, 1, Radio.getInstance().getSettings().getStaticVolMax(), Radio.getInstance().getSettings().getStaticVolMin());
        _noise.setAmp(noiseVol * _noiseVolumeFactor);
        _noiseVolume = noiseVol;
        for (IRadioStation backgroundStation : backgroundStations)
        {
            backgroundStation.setVolumeFactor(1-reception);
        }
    }

    public void setAudioOutput(AudioOutput out) {
        this._out = out;
        _out.addSignal(_noise);
    }
}
