package aero.aerial.radiostation;

/**
 * Created by drewmutt on 5/27/15.
 */
public class BeepingToneRadioStation extends ToneRadioStation implements IRadioStation {

    private long _lastUpdateTime;
    private int _updatePeriodMSec;
    private boolean _isOn;

    public BeepingToneRadioStation(int stationFreq, float toneFreqMult)
    {
        this(stationFreq);
        _toneFreqMin *= toneFreqMult;
        _toneFreqMax *= toneFreqMult;
        setStationName("BPTone Station");
    }

    public BeepingToneRadioStation(int stationFreq) {
        super(stationFreq);
        _updatePeriodMSec = 50;
        _lastUpdateTime = System.currentTimeMillis();
        _toneFreqMax = 5000;
        _toneFreqMin = 4000;
        _volumeMax = .05f;
        setStationName("BPTone Station");
    }

    @Override
    public void update() {
        if(!_isOn)
            _wave.setAmplitude(0);
        else
            _wave.setAmplitude(_waveAmp * _volumeFactor);

        if( (_lastUpdateTime + _updatePeriodMSec) < System.currentTimeMillis()) {
            if ((Math.random() * 100) > 90)
            {
//                Radio.getInstance().getStaticManager().setVolumeFactorForAllExcept(this, 0.8f);
                _isOn = true;
            }
            else
            {
//                Radio.getInstance().getStaticManager().setVolumeFactorForAllExcept(this, 1f);
                _isOn = false;
            }
//            System.out.println(_isOn);
            _lastUpdateTime = System.currentTimeMillis();
        }
    }

    @Override
    public String getCurrentPlayingTitle() {
        return "Beep Station";
    }

    @Override
    public float handleStationChangeReturnReception(int frequency) {
        float receptionAmount = super.handleStationChangeReturnReception(frequency);
//        if(_isOn)
//            return .2f;
//        else
//            return 0;
        return receptionAmount;
    }
}
