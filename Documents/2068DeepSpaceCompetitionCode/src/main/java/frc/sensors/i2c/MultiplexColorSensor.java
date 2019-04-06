package frc.sensors.i2c;

import edu.wpi.first.wpilibj.I2C;
import java.util.function.Supplier;

public class MultiplexColorSensor extends I2C {
    private int mask = 0b0;


    private I2CColorSensor sensor;

    public MultiplexColorSensor(int [] ports, I2C.Port bus) {
        super(bus, 0x70);

        for (int i = 0; i < ports.length; i++) {
            mask = mask | (1 << ports[i]);
        }
        //mask = 0b1111111;

        write(0x00, mask);

        sensor = new I2CColorSensor(bus);
    }

    public short getRed(int port) {
        return returnValue(port, sensor::getRed);
    }

    public short getGreen(int port) {
        return returnValue(port, sensor::getGreen);
    }

    public short getBlue(int port) {
        return returnValue(port, sensor::getBlue);
    }

    public short getGreyscale(int port) {
        return returnValue(port, sensor::getGreyscale);
    }

    public short getProximity(int port) {
        return returnValue(port, sensor::getProximity);
    }

    private short returnValue(int port, Supplier<Short> fun) {
        write(0x00, 1 << port);
        return fun.get();
    }

    public void setProximitySensitivity(int sensitivity) {
        write(0x00, mask);
        sensor.setProximitySensitivity(sensitivity);
    }

    public void setColorSensitivity(int sensitivity) {
        write(0x00, mask);
        sensor.setColorSensitivity(sensitivity);
    }

    public boolean isOnLine(int port) {
        return getGreyscale(port) < sensor.getThreshold();
    }
}