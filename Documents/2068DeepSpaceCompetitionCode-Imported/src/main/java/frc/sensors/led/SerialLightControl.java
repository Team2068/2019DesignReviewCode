package frc.sensors.led;

import edu.wpi.first.wpilibj.SerialPort;

public class SerialLightControl {
    private static final SerialPort.Port DEFAULT_PORT = SerialPort.Port.kUSB;
    protected static final int DEFAULT_BAUDRATE = 9600;

    private int baud = DEFAULT_BAUDRATE;
    protected SerialPort serial;

    public SerialLightControl(SerialPort.Port port, int baud) {
        this.baud = baud;

        serial = new SerialPort(baud, port);
        serial.enableTermination('\n');
        //serial.setTimeout(0.1);
    }

    public SerialLightControl() {
        this(DEFAULT_PORT, DEFAULT_BAUDRATE);
    }

    public void setAnimation(Integer strip, int anim) {
        //System.out.println("CHANGING ANIMATION");
        String stripCode = (strip == null)? "" : strip.toString();
        serial.writeString("A" + stripCode + "=" + anim);
        timer();
    }

    public void setAnimation(int anim) {
        setAnimation(null, anim);
    }

    public void setPalette(Integer strip, int pal) {
        //System.out.println("CHANGING PALETTE");
        String stripCode = (strip == null)? "" : strip.toString();
        serial.writeString("P" + stripCode + "=" + pal);
        timer();
    }

    public void setPalette(int pal) {
        setPalette(null, pal);
    }

    public void setColor(Integer strip, String color) {
        //System.out.println("CHANGING COLOR");
        String stripCode = (strip == null)? "" : strip.toString();
        serial.writeString("C" + stripCode + "=" + color);
        timer();
    }

    public void setColor(String color) {
        setColor(null, color);
    }

    public void setDelay(int delay) {
        
        //System.out.println("CHANGING DELAY");
        serial.writeString("D=" + delay);
        timer();
    }

    public void setDelay(Integer strip, int delay) {
        //System.out.println("CHANGING DURATION");
        String stripCode = (strip == null)? "" : strip.toString();
        serial.writeString("D" + stripCode + "=" + delay);
    }

    public void setBrightness(Integer strip, int brightness) {
        //System.out.println("CHANGING BRIGHTNESS");
        String stripCode = (strip == null)? "" : strip.toString();
        serial.writeString("B" + stripCode + "=" + brightness);
        timer();
    }

    public void setBrightness(int brightness) {
        setBrightness(null, brightness);
    }

    public int getBaud() {
        return baud;
    }

    public void timer() {
        int count = 0;
        while (!serial.readString(1).equals("#")) {
            count++;
        }
    }

}