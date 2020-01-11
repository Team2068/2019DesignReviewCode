package frc.sensors;

import edu.wpi.first.wpilibj.DigitalInput;

public class LimitSwitch extends DigitalInput {

    public LimitSwitch(int channel) {
        super(channel);
    }

    public boolean isPressed() {
        return get();
    }
}