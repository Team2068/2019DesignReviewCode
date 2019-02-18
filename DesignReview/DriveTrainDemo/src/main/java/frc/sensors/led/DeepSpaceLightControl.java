package frc.sensors.led;

import edu.wpi.first.wpilibj.DriverStation;

public class DeepSpaceLightControl extends SerialLightControl {

    private boolean disabled = false;

    public DeepSpaceLightControl() {
        super();

        new Thread(() -> {
            while (!Thread.interrupted()) {
					if (DriverStation.getInstance().isDisabled()) {
                        if (disabled == false) {
                            disabled();
                        }
                        disabled = true;
                    } else {
                        disabled = false;
                    }
            }
        }).start();
    }

    public DeepSpaceLightControl(String port, int baud) {
        super(port, baud);

        new Thread(() -> {
            while (!Thread.interrupted()) {
					if (DriverStation.getInstance().isDisabled()) {
                        if (disabled == false) {
                            disabled();
                        }
                        disabled = true;
                    } else {
                        disabled = false;
                    }
            }
        }).start();
    }

    public void autonomous() {

    }

    public void driverControl() {

    }

    public void disabled() {

    }

    public void initial() {

    }
}