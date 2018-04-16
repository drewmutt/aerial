package aero.aerial.util;

import aero.aerial.Radio;
import aero.aerial.radiostation.SoundAssetRadioStation;
import aero.aerial.soundasset.MP3SoundAsset;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Created by andrewsimmons on 6/15/15.
 */
public class JSONConfigRadioStationPopulator {

    public void populateFromJSON(String aerialConfigFilePath) throws FileNotFoundException {
        JSONTokener tokener = new JSONTokener(new FileInputStream(new File(aerialConfigFilePath)));
        JSONObject jsonObject = new JSONObject(tokener);
        JSONArray array = (JSONArray) jsonObject.get("stations");
        for (int i = 0; i < array.length(); i++)
        {
            JSONObject stationObject = array.getJSONObject(i);
            int frequency = stationObject.getInt("frequency");
            String stationTitle = stationObject.getString("title");
            JSONArray soundAssetsObject = stationObject.getJSONArray("soundAssets");
            SoundAssetRadioStation station = new SoundAssetRadioStation(soundAssetsObject.getJSONObject(0).getString("fileName"), frequency);
            station.setStationName(stationTitle);

            //Already added the first one in the constructor
            for (int j = 1; j < soundAssetsObject.length(); j++)
            {
                JSONObject soundAssetObject = soundAssetsObject.getJSONObject(j);
                MP3SoundAsset soundAsset = new MP3SoundAsset(soundAssetObject.getString("fileName"));
                station.addSoundAssetToPlaylist(soundAsset);
            }

            Radio.getInstance().addRadioStation(station);

            boolean shufflePlaylist = stationObject.getBoolean("shufflePlaylist");

            if(shufflePlaylist)
                station.shufflePlaylist();
        }


    }
}
