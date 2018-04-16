
package aero.aerial;

import aero.aerial.radiostation.BeepingToneRadioStation;
import aero.aerial.radiostation.SoundAssetRadioStation;
import aero.aerial.radiostation.ToneRadioStation;
import aero.aerial.util.RadioUtil;
import ddf.minim.AudioOutput;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import ddf.minim.analysis.FFT;
import ddf.minim.effects.BandPass;
import ddf.minim.signals.PinkNoise;
import ddf.minim.ugens.Waves;
import processing.core.PApplet;
import processing.serial.Serial;

/**
 * Created by drewmutt on 5/25/15.
 */


public class ProcessingTest extends PApplet
{
    Serial myPort;  // The serial port
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

    public void setup()
    {
        size(512, 200);
        textFont(createFont("SanSerif", 35));
        textFont(createFont("SanSerif", 15));

        minim = new Minim(this);

        out = minim.getLineOut();
//        out.setVolume(1.5f);
        minim.debugOn();


        Radio.getInstance().setMinim(minim);
        Radio.getInstance().setLineOut(out);
        Radio.getInstance().setApplet(this);
        //Stations

        Radio.getInstance().addRadioStation(new SoundAssetRadioStation(dataPath("avalanchesradio.mp3"),   550, 1.0f, 1.0f, true, false));
        Radio.getInstance().addRadioStation(new SoundAssetRadioStation(dataPath("anotherday.mp3"),        600, 1.0f, 1.0f, false));
        Radio.getInstance().addRadioStation(new SoundAssetRadioStation(dataPath("warworldsradio.mp3"),    650, .9f, .8f, false));

        Radio.getInstance().addRadioStation(new SoundAssetRadioStation(dataPath("oldtimeymusic2.mp3"),    700, 1.0f, 1f, false));
        Radio.getInstance().addRadioStation(new SoundAssetRadioStation(dataPath("beatdrum.mp3"),          800, 1.0f, 1.0f, false));

        Radio.getInstance().addRadioStation(new SoundAssetRadioStation(dataPath("thatalife_am.mp3"),      900, 1.0f, .8f, false));
        Radio.getInstance().addRadioStation(new SoundAssetRadioStation(dataPath("numberstation.mp3"),     950, .9f, 1.8f, false));
        Radio.getInstance().addRadioStation(new SoundAssetRadioStation(dataPath("gunslinger.mp3"),        1000, 1.0f, 1.0f, false));
        Radio.getInstance().addRadioStation(new SoundAssetRadioStation(dataPath("onemoretime.mp3"),       1100, 1.0f, 1.0f, false));

        Radio.getInstance().addRadioStation(new SoundAssetRadioStation(dataPath("40watt.mp3"),            1150, 1.0f, 1.0f, false));
        Radio.getInstance().addRadioStation(new SoundAssetRadioStation(dataPath("1940sradiocuba.mp3"),    1200, 1f, .7f, false));
        Radio.getInstance().addRadioStation(new SoundAssetRadioStation(dataPath("midnightrough2.mp3"),    1250, 1f, .7f, false));
        Radio.getInstance().addRadioStation(new SoundAssetRadioStation(dataPath("grapevine2.mp3"),        1300, 1f, .7f, false));
        Radio.getInstance().addRadioStation(new SoundAssetRadioStation(dataPath("jackbennyradio.mp3"),    1350, 1f, .5f, false));
        Radio.getInstance().addRadioStation(new SoundAssetRadioStation(dataPath("bills.mp3"),             1400, 1f, 1f, false));
        Radio.getInstance().addRadioStation(new SoundAssetRadioStation(dataPath("sambaFun.mp3"),          1450, 1f, .5f, false));
        Radio.getInstance().addRadioStation(new SoundAssetRadioStation(dataPath("fiasco.mp3"),            1500, 1.0f, .6f, false));


        Radio.getInstance().addRadioStation(new SoundAssetRadioStation(dataPath("oldtimeymusic.mp3"),     1550, 1.0f, 1.2f, false));
//        Radio.getInstance().addRadioStation(new SoundAssetRadioStation(dataPath("oldtimeymusic.mp3"),     1550, 1.0f, 1.2f, false));

        ToneRadioStation tone1 = new ToneRadioStation(1120, .1f, 300, 500, 100);
        Radio.getInstance().addRadioStation(tone1);
        Radio.getInstance().getStaticManager().addBackgroundStation(tone1);

        ToneRadioStation tone2 = new ToneRadioStation(825);
        Radio.getInstance().addRadioStation(tone2);
        Radio.getInstance().getStaticManager().addBackgroundStation(tone2);

        ToneRadioStation hum = new ToneRadioStation(1000);
        hum.setToneFreqDistance(1500);
        hum.setToneFreqMax(65);
        hum.setToneFreqMin(60);
        hum.setVolumeMax(.08f);
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

        if(!_isTesting)
        {
            println(Serial.list());
            // Open the port you are using at the rate you want:
            String serialName = Serial.list()[3];
            println("Opening: " + serialName);
            myPort = new Serial(this, serialName, 115200);
        }

        currentFrequency = Radio.getInstance().getSettings().getRadioFreqMin();

//        wn = new PinkNoise((float) 0.01);
//        out.addSignal(wn);


    }

    public void mouseMoved()
    {

        // map the mouse position to the range [100, 10000], an arbitrary range of passBand frequencies
        if(_isTesting)
        {
            currentFrequency = (int) map(mouseX, 0, width, Radio.getInstance().getSettings().getRadioFreqMin(), Radio.getInstance().getSettings().getRadioFreqMax());
            Radio.getInstance().changeFrequency((int) currentFrequency);
        }
/*        bpf.setFreq(centerFreq);
        float bpfQ = 100 - map(mouseY, 0, height, 0, 98);  // lowest Q is 100-98 = 2;
        bandwidth = centerFreq/bpfQ;
        bpf.setBandWidth(bandwidth);
*/
    }


    public void draw()
    {
        Radio.getInstance().updateAllStations();

        if(!_isTesting) {
            String inBuffer = null;
            while (myPort.available() > 0) {
                inBuffer = myPort.readStringUntil(pipeAscii);
                if (inBuffer != null) {
                    if (inBuffer.length() < 5 && inBuffer.length() > 0) {
                        int value = parseInt(inBuffer.substring(0, inBuffer.length() - 1));

                        currentFrequency = map(value, 0, 624, Radio.getInstance().getSettings().getRadioFreqMin(), Radio.getInstance().getSettings().getRadioFreqMax());
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
                        */
                    }
                }
            }
        }


        background(0);

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

    }

    public void keyReleased()
    {
        // +/- used to adjust gain on the fly
        if (key == '+' || key == '=') {
            gain = (float) (gain + 3.0);
        } else if (key == '-' || key == '_') {
            gain = (float) (gain - 3.0);
        }
    }

    public void stop()
    {

        // always close Minim audio classes when you are done with them
//        sound.close();

        minim.stop();

        super.stop();
    }


}
