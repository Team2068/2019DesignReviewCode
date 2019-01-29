package frc.sensors.led;

import java.util.Properties;

import com.fazecast.jSerialComm.SerialPort;
import arduino.Arduino;
import java.io.FileInputStream;
import java.io.IOException;

public class SerialLightControl {
    private static final int DEFAULT_BAUDRATE = 9600;
    private static final String DEFAULT_PORT = SerialPort.getCommPorts()[0].getSystemPortName();
    private static final String ANIMATIONS_FILE_NAME = "./animations.conf";
    private static final String PALETTES_FILE_NAME = "./palettes.conf";
    private static final String COLORS_FILE_NAME = "./colors.conf";


    private int baud = DEFAULT_BAUDRATE;
    private String port = DEFAULT_PORT;

    private Arduino arduino;

    private Properties animations;
    private Properties palettes;
    private Properties colors;

    public SerialLightControl(String port, int baud) {
        this.baud = baud;
        this.port = port;

        arduino = new Arduino(port, baud);

        arduino.openConnection();

        animations = new Properties();
        palettes = new Properties();
        colors = new Properties();

        try {
            animations.load(new FileInputStream(ANIMATIONS_FILE_NAME));
            palettes.load(new FileInputStream(PALETTES_FILE_NAME));
            colors.load(new FileInputStream(COLORS_FILE_NAME));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SerialLightControl() {
        this(DEFAULT_PORT, DEFAULT_BAUDRATE);
    }

    public void setAnimation(Integer strip, String anim) {
        String stripCode = (strip == null)? "" : strip.toString();
        arduino.serialWrite("A" + stripCode + "=" + (animations.getProperty(anim)));
    }

    public void setAnimation(String anim) {
        setAnimation(null, anim);
    }

    public void setPalette(Integer strip, String pal) {
        String stripCode = (strip == null)? "" : strip.toString();
        arduino.serialWrite("P" + stripCode + "=" + (palettes.getProperty(pal)));
    }

    public void setPalette(String pal) {
        setPalette(null, pal);
    }

    public void setColor(Integer strip, String color) {
        String stripCode = (strip == null)? "" : strip.toString();
        String colorCode = (colors.containsKey(color))? colors.getProperty(color) : color;
        arduino.serialWrite("C" + stripCode + "=" + colorCode);
    }

    public void setColor(String color) {
        setColor(null, color);
    }

    public void setDelay(int delay) {
        arduino.serialWrite("D=" + delay);
    }

    public void setBrightness(Integer strip, int brightness) {
        String stripCode = (strip == null)? "" : strip.toString();
        arduino.serialWrite("B" + stripCode + "=" + brightness);
    }

    public void setBrightness(int brightness) {
        setBrightness(null, brightness);
    }

    public String getPort() {
        return port;
    }

    public int getBaud() {
        return baud;
    }

}