package aero.aerial.radiostation;
import aero.aerial.Radio;
import aero.aerial.util.RadioUtil;
import aero.aerial.soundasset.ISoundAsset;
import aero.aerial.soundasset.MP3SoundAsset;
import ddf.minim.AudioPlayer;
import ddf.minim.effects.BandPass;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by drewmutt on 5/25/15.
 */
public class SoundAssetRadioStation extends AbstractRadioStation implements IRadioStation {
    private final boolean _shuffleSongStart;
    private boolean _stationLockEnabled;
    private float _receptionDistanceFactor;
    private BandPass bpf;
    private float _maxReceptionAmount;
    protected AudioPlayer _stream;

    protected ArrayList<ISoundAsset> _playlist;
    protected int _currentPlaylistLocation = 0;

    public SoundAssetRadioStation(String mp3file, int stationFreq, float minStaticAmount, float receptionDistanceFactor, boolean stationLockEnabled, boolean shuffleSongStart) {

        _playlist = new ArrayList<>();
        _stationFreq = stationFreq;

        if(mp3file != null)
            addSoundAssetToPlaylist(new MP3SoundAsset(mp3file));

//        System.out.println("Loading station [" + stationFreq + "] = " + mp3file);
        _maxReceptionAmount = minStaticAmount * 1.1f;
        _receptionDistanceFactor = receptionDistanceFactor * 1.3f;
        _stationLockEnabled = stationLockEnabled;
        _shuffleSongStart = shuffleSongStart;
    }

    public void addSoundAssetToPlaylist(ISoundAsset soundAsset)
    {
        _playlist.add(soundAsset);
    }

    public ArrayList<ISoundAsset> getSoundAssetPlaylist() {
        return _playlist;
    }

    public SoundAssetRadioStation(String mp3file, int stationFreq, float minStaticAmount, float receptionDistanceFactor, boolean stationLockEnabled)
    {
        this(mp3file, stationFreq, minStaticAmount, receptionDistanceFactor, stationLockEnabled, false);
    }

    @Override
    public void shufflePlaylist()
    {
        Collections.shuffle(_playlist);
    }

    public SoundAssetRadioStation(String mp3file, int stationFreq, float minStaticAmount, float receptionDistanceFactor)
    {
        this(mp3file, stationFreq, minStaticAmount, receptionDistanceFactor, true, false);
    }

    public SoundAssetRadioStation(String mp3file, int stationFreq)
    {
        this(mp3file, stationFreq, 1, 1);
    }

    public ISoundAsset getCurrentSoundAsset()
    {
        return _playlist.get(_currentPlaylistLocation);
    }

    @Override
    public void startStation()
    {
        if(_stream != null)
            _stream.close();

        try {

            _stream = getCurrentSoundAsset().createAudioPlayer();
            bpf = new BandPass(440, 20, _stream.sampleRate());
        } catch (Exception e) {
            e.printStackTrace();
        }

        _stream.addEffect(bpf);

        System.out.println("Loading station [" + _stationFreq + "] = " + getCurrentSoundAsset());

        int max = _stream.length();
        int min = 0;
        int range = (max - min) + 1;
        int randomMillis = (int)(Math.random() * ((float) range*.1)) + min;



        if(_shuffleSongStart)
        {
            System.out.println("Seeking to ..  " + ((randomMillis / 1000) / 60) + "min of " + (max / 1000 / 60) + "min...");
            _stream.cue(randomMillis);
        }

//        _sample();
        _stream.play();
//        System.out.println("Started station [" + _stationFreq + "] = " + _mp3File);
        handleStationChangeReturnReception(Radio.getInstance().getCurrentStationFreq());
    }




    @Override
    public float handleStationChangeReturnReception(int frequency) {
        float distanceFromFreq = frequency - _stationFreq;
        float receptionDistance = Radio.getInstance().getSettings().getReceptionFreqDistance() * _receptionDistanceFactor;
        if(Math.abs(distanceFromFreq) > receptionDistance)
        {
            _stream.mute();
//            _stream.pause();
            return 0;
        }
        else
        {
            _stream.unmute();
//            _stream.play();

            float frequencyDistABS = Math.abs(frequency - _stationFreq);

            if(_stationLockEnabled)
            {
                if (frequencyDistABS < (Radio.getInstance().getSettings().getReceptionLockDistance() * _receptionDistanceFactor))
                    if (_maxReceptionAmount == 1)
                        _stream.disableEffect(bpf);
                    else
                        _stream.enableEffect(bpf);
            }

            float maxBandW = 10000;
            float bandwidth = RadioUtil.map(frequencyDistABS, 0, receptionDistance, maxBandW, 50);
            bpf.setBandWidth(bandwidth);

            float centerFreq = RadioUtil.map(frequencyDistABS, 0, receptionDistance, (maxBandW / 2), maxBandW);
            bpf.setFreq(centerFreq);

            float receptionAmount = RadioUtil.map(frequencyDistABS, 0, receptionDistance, _maxReceptionAmount, 0);
            if(receptionAmount < 0)
                receptionAmount = 0;
            if(receptionAmount > 1)
                receptionAmount = 1;
            return receptionAmount;
        }
    }

    @Override
    public void setVolumeFactor(float volumeFactor)
    {
        _stream.setGain(volumeFactor);
    }


    @Override
    public void update()
    {
        if(_stream != null && !_stream.isPlaying())
        {
            System.out.println("Steam stopped.. playing next song.. ");
            playNextSong();
        }
    }

    public int getCurrentPlayingIndex()
    {
        return _currentPlaylistLocation;
    }

    protected void playNextSong()
    {
        _currentPlaylistLocation = (_currentPlaylistLocation + 1) % _playlist.size();
        startStation();
    }


    @Override
    public String getCurrentPlayingTitle() {
        return getCurrentSoundAsset().getArtist() + " - " + getCurrentSoundAsset().getTitle();
    }


    @Override
    public void stopStation() {
        _stream.close();
        _stream = null;
    }
}
