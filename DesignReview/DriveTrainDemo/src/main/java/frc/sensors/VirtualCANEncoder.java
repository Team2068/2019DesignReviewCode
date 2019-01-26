/*package frc.sensors;
import edu.wpi.first.wpilibj.*;
import com.revrobotics.*;

public class VirtualCANEncoder extends CANEncoder {
    private double offset;

    public VirtualCANEncoder(CANSparkMax spark) {
        super(spark);
        offset = super.getPosition();
    }

    @Override
    public double getPosition() {
        return super.getPosition() - offset;
    }

    public void reset() {
        offset = super.getPosition();
    }
}*/