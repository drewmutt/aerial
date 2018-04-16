package aero.aerial.radiostation;

import aero.aerial.Radio;
import aero.aerial.util.RadioUtil;
import ddf.minim.ugens.Oscil;
import ddf.minim.ugens.Waves;
import ddf.minim.ugens.Wavetable;

/**
 * Created by drewmutt on 5/27/15.
 */
public class ToneRadioStation extends AbstractRadioStation implements IRadioStation {
    protected Oscil _wave;
    protected float _waveAmp;

    protected float _volumeMax = .1f;
    protected float _toneFreqMax = 1000;
    protected float _toneFreqMin = 500;
    protected float _toneFreqDistance = 50;
    protected Wavetable _toneType = Waves.SINE;
    protected float _volumeFactor;

    public Wavetable getToneType() {
        return _toneType;
    }

    public void setToneType(Wavetable _toneType) {
        this._toneType = _toneType;
    }

    public float getVolumeMax() {
        return _volumeMax;
    }

    public void setVolumeMax(float _volumeMax) {
        this._volumeMax = _volumeMax;
    }

    public float getToneFreqMax() {
        return _toneFreqMax;
    }

    public void setToneFreqMax(float _toneFreqMax) {
        this._toneFreqMax = _toneFreqMax;
    }

    public float getToneFreqMin() {
        return _toneFreqMin;
    }

    public void setToneFreqMin(float _toneFreqMin) {
        this._toneFreqMin = _toneFreqMin;
    }

    public float getToneFreqDistance() {
        return _toneFreqDistance;
    }

    public void setToneFreqDistance(float _toneFreqDistance) {
        this._toneFreqDistance = _toneFreqDistance;
    }

    public ToneRadioStation(int stationFreq, float _volumeMax, float _toneFreqMin, float _toneFreqMax, float _toneFreqDistance) {
        this(stationFreq);
        this._volumeMax = _volumeMax;
        this._toneFreqMax = _toneFreqMax;
        this._toneFreqMin = _toneFreqMin;
        this._toneFreqDistance = _toneFreqDistance;
        setStationName("Tone Station");
    }


    public ToneRadioStation(int stationFreq) {
        _stationFreq = stationFreq;
    }

    @Override
    public void startStation() {
        _wave = new Oscil( 440, 0.1f, _toneType);
        // patch the Oscil to the output
        _wave.patch( Radio.getInstance().getLineOut() );
    }

    @Override
    public float handleStationChangeReturnReception(int frequency) {
        float freqDistance = Math.abs(frequency - _stationFreq);
        float freq = RadioUtil.map(freqDistance, 0, _toneFreqDistance, _toneFreqMin, _toneFreqMax);
        if(freq > _toneFreqMax)
            freq = _toneFreqMax;
        if(freq < _toneFreqMin)
            freq = _toneFreqMin;

        _wave.setFrequency(freq);
        float amp = RadioUtil.map(freqDistance, 0, _toneFreqDistance, _volumeMax, 0);
        if(amp < 0)
            amp = 0;

        _waveAmp = amp;
        _wave.setAmplitude(_waveAmp * _volumeFactor);



        return 0f;
    }

    @Override
    public void setVolumeFactor(float volumeFactor) {
        _volumeFactor = volumeFactor;
        _wave.setAmplitude(_waveAmp * _volumeFactor);
    }

    @Override
    public String getCurrentPlayingTitle() {
        return "Sine Wave";
    }


}
