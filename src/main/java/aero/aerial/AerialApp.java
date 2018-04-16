package aero.aerial;
import aero.aerial.integrations.SoundCloudManager;
import aero.aerial.radiostation.BeepingToneRadioStation;
import aero.aerial.radiostation.SoundAssetRadioStation;
import aero.aerial.radiostation.ToneRadioStation;
import aero.aerial.util.JSONConfigRadioStationPopulator;
import aero.aerial.util.RadioUtil;
import aero.aerial.util.UDPFindMeServer;
import aero.aerial.web.RestletServer;
import aero.aerial.web.socket.SocketServer;
import ddf.minim.AudioOutput;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import ddf.minim.analysis.FFT;
import ddf.minim.effects.BandPass;
import ddf.minim.signals.PinkNoise;
import ddf.minim.ugens.Waves;
import jssc.*;

import javax.sound.sampled.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by andrewsimmons on 6/2/15.
 */
public class AerialApp implements SerialPortEventListener {

    SerialPort _serialPort;  // The serial port
    boolean _activatedSerialPort = false;
    Minim minim;

    AudioPlayer sound;

    BandPass bpf;
    FFT fft;
    float centerFreq;
    float bandwidth;

    float gain = 100; // in dB

    int spectrumScale = 2; // pixels per FFT bin
    int pipeAscii = 124;

    PinkNoise wn;
    AudioOutput out;

    float currentFrequency;
    private boolean _isTesting;
    private SocketServer _socketServer;

    public static void main(String[] args)
    {
        AerialApp app = new AerialApp();
        char fun = 0;

        while(fun != 'q')
        {
            try {
                fun = (char) System.in.read();
                if(fun == '1')
                    app.changeFrequency((int) app.currentFrequency - 10);
                if(fun == '2')
                    app.changeFrequency((int) app.currentFrequency + 10);

                if(fun != '\n')
                    System.out.println(app.currentFrequency + " KHz");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        app.cleanup();
    }

    public String dataPath(String s)
    {
        return soundsPath(s);
    }

    public String soundsPath(String fileName)
    {
        return System.getProperty("user.dir")+"/sounds/"+fileName;
    }

    public String configPath( String fileName )
    {
        return System.getProperty("user.dir")+"/config/"+fileName;
    }

    public InputStream createInput( String fileName )
    {
        try {
            return new FileInputStream(dataPath(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public AerialApp() {
        setup();
    }

    public void setup()
    {

        Radio.getInstance().setApp(this);
        Radio.getInstance().setSoundCloudManager(new SoundCloudManager(0));

//        size(512, 200);
//        textFont(createFont("SanSerif", 35));
//        textFont(createFont("SanSerif", 15));


        minim = new Minim(this);

        Mixer.Info[] mixerInfo;
        mixerInfo = AudioSystem.getMixerInfo();



        //IS ON PI,,
        if(true) {
            //RaspI = 0
            int selectedMixer = 0;
            for (int i = 0; i < mixerInfo.length; i++) {
                if (i == selectedMixer)
                    System.out.println("> " + i + ": " + mixerInfo[i].getName() + " " + mixerInfo[i].getDescription());
                else
                    System.out.println("  " + i + ": " + mixerInfo[i].getName() + " " + mixerInfo[i].getDescription());
            }
            Mixer mixer = AudioSystem.getMixer(mixerInfo[selectedMixer]);
            System.out.println(mixer);


            minim.setOutputMixer(mixer);
        }
        out = minim.getLineOut();




//        out.setVolume(1.5f);
//        minim.debugOn();


        Radio.getInstance().setMinim(minim);
        Radio.getInstance().setLineOut(out);

        //Stations
/*
        String fileName = "test.mp3";

        try {
            SoundCloudAPI api = new SoundCloudAPI("eec82872ebe1fbc7e90ef15e7e923e4a", "31003279f7b5e660d47931837643c55b", SoundCloudAPI.USE_PRODUCTION.with(SoundCloudAPI.OAuthVersion.V2_0));
//            api.obtainAccessToken("drewmutt", "*");
            HttpResponse response = api.get("users?q=drewmutt");

            if(response.getStatusLine().getStatusCode() == 200)
            {
                InputStream content = response.getEntity().getContent();
                String responseString = RadioUtil.convertStreamToString(content);
                responseString = "{ \"response\":"+responseString + "}";
                JSONObject myObject = new JSONObject(responseString);
                JSONArray jsonResponse = (JSONArray) myObject.get("response");
                JSONObject firstSong = jsonResponse.getJSONObject(1);
                Integer firstSongID = (Integer) firstSong.get("id");

                HttpResponse response2 = api.get("tracks/" + firstSongID);
                if(response.getStatusLine().getStatusCode() == 200)
                {
                    InputStream content2 = response2.getEntity().getContent();
                    String responseString2 = RadioUtil.convertStreamToString(content2);
                    JSONObject myObject2 = new JSONObject(responseString2);
                    String streamURL = (String) myObject2.get("stream_url");

                    String redirectedStreamUrl = api.getRedirectedStreamUrl(streamURL);

                    System.out.println("Saving to... " + soundsPath(fileName));
                    RadioUtil.saveUrl(soundsPath(fileName), redirectedStreamUrl);
                    System.out.println("Saved");
//                    AudioPlayer radio = minim.loadFile(redirectedStreamUrl, 2048);
//                    radio.play();
                    System.out.println(streamURL);
                }

                System.out.println(responseString);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OAuthExpectationFailedException e) {
            e.printStackTrace();
        } catch (OAuthCommunicationException e) {
            e.printStackTrace();
        } catch (OAuthMessageSignerException e) {
            e.printStackTrace();
        }
*/
        /*
        try{
            SoundCloudAPI api = new SoundCloudAPI("eec82872ebe1fbc7e90ef15e7e923e4a", "31003279f7b5e660d47931837643c55b", SoundCloudAPI.USE_PRODUCTION.with(SoundCloudAPI.OAuthVersion.V2_0));
            api.obtainAccessToken("drewmutt", "steeliron");
            HttpResponse response = api.get("me/favorites");
            if(response.getStatusLine().getStatusCode() == 200)
            {
                InputStream content = response.getEntity().getContent();
                String responseString = RadioUtil.convertStreamToString(content);
                responseString = "{ \"response\":"+responseString + "}";
                JSONObject myObject = new JSONObject(responseString);
                JSONArray jsonResponse = (JSONArray) myObject.get("response");
                JSONObject firstSong = jsonResponse.getJSONObject(1);
                Integer firstSongID = (Integer) firstSong.get("id");

                HttpResponse response2 = api.get("tracks/" + firstSongID);
                if(response.getStatusLine().getStatusCode() == 200)
                {
                    InputStream content2 = response2.getEntity().getContent();
                    String responseString2 = RadioUtil.convertStreamToString(content2);
                    JSONObject myObject2 = new JSONObject(responseString2);
                    String streamURL = (String) myObject2.get("stream_url");

                    String redirectedStreamUrl = api.getRedirectedStreamUrl(streamURL);

                    /*
                    HttpURLConnection.setFollowRedirects(false);

                    URL obj = new URL(streamURL);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                    con.setInstanceFollowRedirects(false);  //you still need to handle redirect manully.

                    // optional default is GET
                    con.setRequestMethod("GET");

                    //add request header
                    con.setRequestProperty("User-Agent", "Mozilla/5.0");

                    int responseCode = con.getResponseCode();
                    System.out.println("\nSending 'GET' request to URL : " + streamURL);
                    System.out.println("Response Code : " + responseCode);

                    String returnStuff = RadioUtil.convertStreamToString(con.getInputStream());

                    System.out.println("Saving to... " + soundsPath(fileName));
                    RadioUtil.saveUrl(soundsPath(fileName), redirectedStreamUrl);
                    System.out.println("Saved");
//                    AudioPlayer radio = minim.loadFile(redirectedStreamUrl, 2048);
//                    radio.play();
                    System.out.println(streamURL);
                }

                System.out.println(responseString);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OAuthExpectationFailedException e) {
            e.printStackTrace();
        } catch (OAuthCommunicationException e) {
            e.printStackTrace();
        } catch (OAuthMessageSignerException e) {
            e.printStackTrace();
        }




        ArrayList <SourceDataLine> lines = new ArrayList<>();
        try {
            for(int i = 0; i < 25; i++) {
                System.out.println("Opening " + i + "..");
                final AudioFormat audioFormat = new AudioFormat(44100, 16, 2, true, false); //new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 1, 4, 30, false);
                final DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat, 512);
                final SourceDataLine soundLine = (SourceDataLine) AudioSystem.getLine(info);
                soundLine.open(audioFormat);
                lines.add(soundLine);
            }
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

        System.exit(0);
*/
        /*
        Radio.getInstance().getSoundCloudManager().setUserIDFromUsername("drewmutt");
        ArrayList<SoundCloudPlaylist> playlistsForCurrentUser = Radio.getInstance().getSoundCloudManager().getPlaylistsForCurrentUser();
        SoundCloudPlaylist firstPlaylist = playlistsForCurrentUser.get(0);*/
//        SoundCloudTrack soundCloudTrack = playlistsForCurrentUser.get(0).getTrackList().get(0);
//        Radio.getInstance().getSoundCloudManager().downloadSoundCloudTrackIfNeeded(soundCloudTrack);
//        System.exit(0);

        JSONConfigRadioStationPopulator populator = new JSONConfigRadioStationPopulator();
        try {
            populator.populateFromJSON(configPath("aerialconfig.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        boolean enableStatic = true;
        if(enableStatic) {
            ToneRadioStation tone1 = new ToneRadioStation(1120, .1f, 300, 500, 100);
            Radio.getInstance().addRadioStation(tone1);
            Radio.getInstance().getStaticManager().addBackgroundStation(tone1);

            ToneRadioStation tone2 = new ToneRadioStation(825);
            Radio.getInstance().addRadioStation(tone2);
            Radio.getInstance().getStaticManager().addBackgroundStation(tone2);

            ToneRadioStation hum = new ToneRadioStation(1000);
            hum.setToneFreqDistance(1500);
            hum.setToneFreqMax(60);
            hum.setToneFreqMin(60);
            hum.setVolumeMax(.04f);
            hum.setToneType(Waves.SQUARE);
            Radio.getInstance().addRadioStation(hum);
            Radio.getInstance().getStaticManager().addBackgroundStation(hum);
//        Radio.getInstance().addRadioStation(new BeepingToneRadioStation(715));
            BeepingToneRadioStation beep1 = new BeepingToneRadioStation(1520, .8f);
            Radio.getInstance().addRadioStation(beep1);
            Radio.getInstance().getStaticManager().addBackgroundStation(beep1);

            BeepingToneRadioStation beep2 = new BeepingToneRadioStation(890, 1.1f);
            Radio.getInstance().addRadioStation(beep2);
            Radio.getInstance().getStaticManager().addBackgroundStation(beep2);

            BeepingToneRadioStation beep3 = new BeepingToneRadioStation(670, .3f);
            Radio.getInstance().addRadioStation(beep3);
            Radio.getInstance().getStaticManager().addBackgroundStation(beep3);

            Radio.getInstance().getStaticManager().setAudioOutput(out);

            Radio.getInstance().startAllStations();

        }


        /*
        println("Loading mp3: " + s);
        sound = minim.loadFile(dataPath("beatdrum.mp3")); //beatdrum.mp3");


        // make it repeat
        sound.loop();

        // add in the bpf to the input
        centerFreq = 440;
        bandwidth = 20;
        bpf = new BandPass(centerFreq, bandwidth, sound.sampleRate());
        sound.addEffect(bpf);

        // create an FFT object that has a time-domain buffer
        // the same size as line-in's sample buffer
        fft = new FFT(sound.bufferSize(), sound.sampleRate());
        // Tapered window important for log-domain display
        fft.window(FFT.HAMMING);


*/
        // List all the available serial ports:
        _isTesting = false;


//        String[] portNames2 = SerialPortList.getPortNames();
//        for(int i = 0; i < portNames2.length; i++){
//            System.out.println(portNames2[i]);
//        }

        boolean arduinoConnected = false;
        if(!_isTesting)
        {
            String[] portNames = SerialPortList.getPortNames();
            int serialPortIndex = -1;
            for(int i = 0; i < portNames.length; i++){
                if(portNames[i].contains("ttyACM") || portNames[i].contains("tty.usbmodem"))
                    serialPortIndex = i;

                if(serialPortIndex == i)
                    System.out.println(" > " + i + ": " + portNames[i]);
                else
                    System.out.println("   " + i + ": " + portNames[i]);
            }
//            System.out.println(Arrays.toString(Serial.list()));
            // Open the port you are using at the rate you want:
            if(serialPortIndex == -1)
            {
                System.out.println("Can't find good serial port containing Arduino.. bailing..");
//                System.exit(0);
            }
            else
            {
                String serialName = SerialPortList.getPortNames()[serialPortIndex];
                System.out.println("Opening: " + serialName);
                _serialPort = new SerialPort(serialName);
                try {
                    System.setProperty(SerialNativeInterface.PROPERTY_JSSC_NO_TIOCEXCL, "true");
                    _serialPort.openPort();//Open serial port
                    _serialPort.setParams(SerialPort.BAUDRATE_115200,
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);

                    int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;//Prepare mask
                    _serialPort.setEventsMask(mask);//Set mask
                    _serialPort.purgePort(SerialPort.PURGE_RXABORT | SerialPort.PURGE_RXCLEAR);
                    _serialPort.addEventListener(this);
                    arduinoConnected = true;
                } catch (SerialPortException e) {
                    e.printStackTrace();
                }
            }
        }


        if(arduinoConnected)
        {
            System.out.println("Sleeping for Arduino....");
            // Give arduino time to figure it's shit out
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                _serialPort.writeString("s|");
            } catch (SerialPortException e) {
                e.printStackTrace();
            }
        }
/*
        try {
            _serialPort.addEventListener(this);//Add SerialPortEventListener
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
        System.out.println("Listening to Arduino.. starting..");
*/
        changeFrequency((int) Radio.getInstance().getSettings().getRadioFreqMin());
//        wn = new PinkNoise((float) 0.01);
//        out.addSignal(wn);


        Timer _updateTimer = new Timer();

        _updateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Radio.getInstance().getApp().update();
            }
        }, 30, 30); //ms

        RestletServer.startServer();

        _socketServer = new SocketServer();
        _socketServer.startListening(8181);
//        UDPFindMeServer.sendFindMe();
    }

    private byte[] _sBuffer = new byte[10000];
    int _sBufferLoc = 0;
    public void serialEvent(SerialPortEvent event)
    {

        if(event.isRXCHAR()) {   //If data is available
            int byteAvail = event.getEventValue();
//            if(event.getEventValue() >= 5){//Check bytes count in the input buffer
            //Read data, if 10 bytes available
            try
            {
                byte payLoad[] = _serialPort.readBytes(event.getEventValue());
                for (int i = 0; i < byteAvail; i++) {
                    _sBuffer[_sBufferLoc] = payLoad[i];
                    _sBufferLoc++;
                    if (payLoad[i] == '|' && _sBufferLoc > 4) {
                        String stationString = new String(_sBuffer, _sBufferLoc - 5, 4);
                        int station = 0;
                        try {
                            station = Integer.parseInt(stationString, 10);
//                            System.out.print(station + " > ");
                            station = (int) RadioUtil.map(station, 0, 1024, Radio.getInstance().getSettings().getRadioFreqMin(), Radio.getInstance().getSettings().getRadioFreqMax());
                        } catch (NumberFormatException e) {
                            //Oh, just move on.. a lil' glitch is fun..
                        }
//                        System.out.println(station);
                        changeFrequency(station);
                        _sBufferLoc = 0;
                    }
                }
            } catch (SerialPortException ex) {
                System.out.println(ex);
            }
        }
    }

    public void changeFrequency(int freq)
    {
        currentFrequency = freq;
        Radio.getInstance().changeFrequency((int) currentFrequency);
//        System.out.println(freq + " KHz");
    }

    public void update()
    {

        Radio.getInstance().updateAllStations();
/*
        if(!_isTesting) {
            String inBuffer = null;
            while (_serialPort.available() > 0) {
                inBuffer = _serialPort.readStringUntil(pipeAscii);
                if (inBuffer != null) {
                    if (inBuffer.length() < 5 && inBuffer.length() > 0) {
                        int value = Integer.parseInt(inBuffer.substring(0, inBuffer.length() - 1));

                        currentFrequency = RadioUtil.map(value, 0, 624, Radio.getInstance().getSettings().getRadioFreqMin(), Radio.getInstance().getSettings().getRadioFreqMax());
                        System.out.println(value);
                        Radio.getInstance().changeFrequency((int) currentFrequency);

                        //centerFreq = sound.sampleRate() / 2; //map(value, 0, 1024, 0, sound.sampleRate());


                        //bpf.setFreq(centerFreq);

    /*
                        int sliderSpot = 512;

                        if(value > sliderSpot)
                            value = 1024 - value;

                        //Enable TunerLock
                        if( abs(value - sliderSpot) < 5)
                        {
                            sound.disableEffect(bpf);
                            value = sliderSpot;
                        }
                        else
                        {
                            sound.enableEffect(bpf);
                        }


                        float bpfQ = 100 - map(value, 0, sliderSpot, 0, 70);  // lowest Q is 100-98 = 2;
                        //println(bpfQ);
                        bandwidth = centerFreq/bpfQ;

                        float maxBandW = 10000;
                        bandwidth = map(value, 0, sliderSpot, 200, maxBandW);
                        bpf.setBandWidth(bandwidth);

                        centerFreq = map(value, 0, sliderSpot, 10000, (maxBandW/2));
                        bpf.setFreq(centerFreq);

                        float wnVol = map(value, 0, sliderSpot, (float) .1, 0);
                        wn.setAmp(wnVol);
                        bpf.setFreq(centerFreq);

                        //bpf.setBandWidth((float) (value*10));

                    }
                }
            }
        }
*/

//        background(0);

        /*
        fft.forward(sound.mix);
        fill(64,192,255);
        noStroke();
        for(int i = 0; i < fft.specSize(); i++)
        {
            // draw the line for frequency band i using dB scale
            float val = 2*(20*((float)Math.log10(fft.getBand(i))) + gain);
            rect(i*spectrumScale, height, spectrumScale, -Math.round(val));
        }

        // add band showing BPF
        fill(255, 0, 0, 80);
        rect(mouseX - bpf.getBandWidth()/20, 0, bpf.getBandWidth()/10, height);
        // report center frequency
        fill(255);*/

//        text("Center frequency="+Math.round(centerFreq)+" Hz bw="+Math.round(bandwidth), 5, 20);

        /*
        rotateX(50);
        textSize(35);
        fill(255,255,255);
        text(((int)currentFrequency) + " KHz", 5, 50);
        fill(64,192,255,100);
        noStroke();
        int tuneLineWidth = 2;
        int stepSize = 10;
        for(float freq = Radio.getInstance().getSettings().getRadioFreqMin(); freq < Radio.getInstance().getSettings().getRadioFreqMax(); freq+=stepSize)
        {
            float screenX = RadioUtil.convertFreqToScreenX(freq, width);
//            println(screenX);
            float lineHeight;
            if(freq % 100 == 0)
            {
                fill(255,255,255, 100);
                lineHeight = height * .5f;
                textSize(15);
                text((int)freq, screenX - 15, lineHeight + 15);
                fill(64,192,255,100);
            }
            else if(freq % 50 == 0)
                lineHeight = height * .4f;
            else
                lineHeight = height * .3f;
            rect(screenX, 0, tuneLineWidth, lineHeight);
        }

        fill(230, 100, 100,255);
        float screenX = RadioUtil.convertFreqToScreenX(currentFrequency, width);
        rect(screenX, 0, tuneLineWidth, height);
*/
    }



    public void cleanup()
    {
        // always close Minim audio classes when you are done with them
        minim.stop();
    }



}
