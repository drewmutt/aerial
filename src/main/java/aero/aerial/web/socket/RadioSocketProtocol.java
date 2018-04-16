package aero.aerial.web.socket;

import aero.aerial.Radio;

import java.util.Objects;

/**
 * Created by andrewsimmons on 6/30/15.
 */
public class RadioSocketProtocol {
    public String processInput(String input) {
        if(input.equals("f"))
            return String.valueOf(Radio.getInstance().getCurrentStationFreq());

        return null;
    }
}
