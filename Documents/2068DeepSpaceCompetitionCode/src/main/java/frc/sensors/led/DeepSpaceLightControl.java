package frc.sensors.led;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SerialPort;

public class DeepSpaceLightControl extends SerialLightControl {

    private boolean disabled = false;

    private static final int UNDERLIGHTING = 0;
    private static final int SPONSORS = 1;
    private static final int SIDES = 2;


    public DeepSpaceLightControl(SerialPort.Port port, int baud) {
        super(port, baud);
    }

    public void autonomous() {
        if(DriverStation.getInstance().getAlliance() == DriverStation.Alliance.Blue) {
            serial.writeString("B");
        } else {
            serial.writeString("C");
        }
    }

    public void driverControl() {
        if(DriverStation.getInstance().getAlliance() == DriverStation.Alliance.Blue) {
            serial.writeString("D");
        } else {
            serial.writeString("E");
        }
    }

    public void disabled() {
        serial.writeString("F");
    }

    public void test() {
        serial.writeString("R");
    }

    public void init() {
        serial.writeString("A");
    }
}