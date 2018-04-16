package aero.aerial;

/**
 * Created by drewmutt on 5/25/15.
 */
public class RadioSettings {
    private float _receptionFreqDistance;
    private float _receptionLockDistance;
    private float _bandpassBWMax;
    private float _staticVolMin;
    private float _staticVolMax;
    private float _radioFreqMin;
    private float _radioFreqMax;

    public RadioSettings() {
        _radioFreqMin = 500;
        _radioFreqMax = 1600;
    }

    public float getReceptionFreqDistance() {
        return _receptionFreqDistance;
    }

    public void setReceptionFreqDistance(float _receptionFreqDistance) {
        this._receptionFreqDistance = _receptionFreqDistance;
    }

    public float getBandpassBWMax() {
        return _bandpassBWMax;
    }

    public void setBandpassBWMax(float _bandpassBWMax) {
        this._bandpassBWMax = _bandpassBWMax;
    }

    public float getStaticVolMin() {
        return _staticVolMin;
    }

    public void setStaticVolMin(float _staticVolMin) {
        this._staticVolMin = _staticVolMin;
    }

    public float getStaticVolMax() {
        return _staticVolMax;
    }

    public void setStaticVolMax(float _staticVolMax) {
        this._staticVolMax = _staticVolMax;
    }

    public float getRadioFreqMin() {
        return _radioFreqMin;
    }

    public float getRadioFreqMax() {
        return _radioFreqMax;
    }

    public float getReceptionLockDistance() {
        return _receptionLockDistance;
    }

    public void setReceptionLockDistance(float receptionLockDistance) {
        this._receptionLockDistance = receptionLockDistance;
    }
}
