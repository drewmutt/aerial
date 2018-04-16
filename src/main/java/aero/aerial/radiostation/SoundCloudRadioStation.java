package aero.aerial.radiostation;

import aero.aerial.Radio;
import aero.aerial.integrations.SoundCloudPlaylist;
import aero.aerial.integrations.SoundCloudTrack;
import aero.aerial.soundasset.ISoundAsset;
import aero.aerial.soundasset.MP3SoundAsset;

/**
 * Created by andrewsimmons on 6/9/15.
 */
public class SoundCloudRadioStation extends SoundAssetRadioStation implements IRadioStation
{
    private SoundCloudPlaylist _playlist;
    private boolean _waitingForFirstTrackDownload;


    public SoundCloudRadioStation(int soundCloudPlaylistID, int stationFreq, float minStaticAmount, float receptionDistanceFactor, boolean stationLockEnabled, boolean shuffleSongStart)
    {
        super(null, stationFreq, minStaticAmount, receptionDistanceFactor, stationLockEnabled, shuffleSongStart);
        _playlist = Radio.getInstance().getSoundCloudManager().getPlaylistById(soundCloudPlaylistID);
        setStationName("SC: " + _playlist.getTitle());
        populateSoundAssetPlaylistWithSCPlaylist();
    }


    public SoundCloudPlaylist getSoundCloudPlaylist()
    {
        return _playlist;
    }

    private void populateSoundAssetPlaylistWithSCPlaylist() {
        for (SoundCloudTrack soundCloudTrack : _playlist.getTrackList())
        {
            addSoundAssetToPlaylist(soundCloudTrack.getSoundAsset());
        }
    }

    public SoundCloudRadioStation(int soundCloudPlaylistID, int stationFreq)
    {
        super(null, stationFreq);
        _playlist = Radio.getInstance().getSoundCloudManager().getPlaylistById(soundCloudPlaylistID);
        setStationName("SC: " + _playlist.getTitle());
        populateSoundAssetPlaylistWithSCPlaylist();
    }
    public SoundCloudRadioStation(String mp3file, int stationFreq, float minStaticAmount, float receptionDistanceFactor, boolean stationLockEnabled, boolean shuffleSongStart)
    {
        super(mp3file, stationFreq, minStaticAmount, receptionDistanceFactor, stationLockEnabled, shuffleSongStart);
    }

    class DownloadTask implements Runnable {
        SoundCloudTrack _track = null;
        SoundCloudRadioStation _station;
        public boolean isFirstTrack = false;

        public DownloadTask(SoundCloudTrack track, SoundCloudRadioStation station)
        {
            _station = station;
            _track  = track;
        }

        public void run()
        {
            System.out.println("Starting download.." + _track.getLocalFileName());
            Radio.getInstance().getSoundCloudManager().downloadSoundCloudTrackIfNeeded(_track);
            System.out.println("Download Complete.." + _track.getLocalFileName());
            if(isFirstTrack)
            {
                _station.firstTrackDownloadComplete();
            }
        }
    }

    @Override
    public void startStation() {

        if(!Radio.getInstance().getSoundCloudManager().isTrackDownloaded(getCurrentTrack()))
        {
            System.out.println("Current track not downloaded.. ");
            DownloadTask task = new DownloadTask(getCurrentTrack(), this);
            task.isFirstTrack = true;
            Thread t = new Thread(task);
            t.start();
            _waitingForFirstTrackDownload = true;
        }
        else
        {
            System.out.println("Downloading upcoming track..");
            Thread t2 = new Thread(new DownloadTask(getUpcomingTrack(), this));
            t2.start();
        }

        super.startStation();

//        Radio.getInstance().getSoundCloudManager().downloadSoundCloudTrackIfNeeded(getUpcomingTrack());
    }

    public void firstTrackDownloadComplete()
    {
        System.out.println("First track complete downloading.. starting station..");
        _waitingForFirstTrackDownload = false;

//        Thread t2 = new Thread(new DownloadTask(getUpcomingTrack(), this));
//        t2.start();

        //This should trigger the next song
        _currentPlaylistLocation--;
        _stream.close();
    }

    /*
    private static class DownloadSoundCloudTrackTask extends AsyncTask<String, String, String> {


        private final SoundCloudTrack _track;

        public DownloadSoundCloudTrackTask(SoundCloudTrack track) {
            _track = track;
        }

        @Override
        public void run() {
            // surround with try-catch if downloadFile() throws something
            Radio.getInstance().getSoundCloudManager().downloadSoundCloudTrackIfNeeded(_track);
        }
    }

*/
    private SoundCloudTrack getUpcomingTrack()
    {
        return _playlist.getTrackList().get((_currentPlaylistLocation + 1) % _playlist.getTrackList().size());
    }

    public SoundCloudTrack getCurrentTrack()
    {
        return _playlist.getTrackList().get(_currentPlaylistLocation);
    }

    @Override
    public ISoundAsset getCurrentSoundAsset() {
        if(_waitingForFirstTrackDownload)
            return new MP3SoundAsset("_holdloop.mp3");
        else
            return super.getCurrentSoundAsset();
    }

    @Override
    protected void playNextSong() {
        if(_waitingForFirstTrackDownload)
            super.startStation();
        else
            super.playNextSong();
    }

    @Override
    public void update() {
        super.update();
    }
}
