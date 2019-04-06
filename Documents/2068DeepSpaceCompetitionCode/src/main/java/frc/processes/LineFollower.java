package frc.processes;

import frc.robot.DriveTrain;
import frc.sensors.i2c.*;
import edu.wpi.first.wpilibj.DriverStation;


public class LineFollower {

    private static final int TAPE_THRESHOLD = 33;
    private static final int DISTANCE = 3;

    private static final double LINE_UP_SPEED = 0.3;
    private static final double MOVE_SPEED = 0.6;
    private static final double SLOW_SPEED = 0.4;

    private DriveTrain chassis;

    private int leftColorSensor, rightColorSensor, leftProximitySensor, rightProximitySensor;

    private MultiplexColorSensor sensor;

    public LineFollower(DriveTrain chassis, MultiplexColorSensor sensor, int [] ports ) {

        this.chassis = chassis;

        this.sensor = sensor;

        leftColorSensor = ports[0];
        rightColorSensor = ports[1];

        leftProximitySensor = ports[2];
        rightProximitySensor = ports[3];
    }

    public boolean follow() {
        chassis.resetEncoders();

        //Attach to line
        moveForwardUntilLine();

        //Follow Line until too close
        followLine();

        //turn until straight
        straighten();

        //score
        return true;
    }

    public void moveForwardUntilLine() {
        while (sensor.getGreyscale(leftColorSensor) < TAPE_THRESHOLD && sensor.getGreyscale(rightColorSensor) < TAPE_THRESHOLD) {
                chassis.set(LINE_UP_SPEED);
        }
        chassis.set(0);
    }

    public void turnUntilLine() {
        while (sensor.getGreyscale(leftColorSensor) < TAPE_THRESHOLD &&     
                sensor.getGreyscale(rightColorSensor) < TAPE_THRESHOLD) {
                
                chassis.setLeft(LINE_UP_SPEED);
                chassis.setRight(-LINE_UP_SPEED);
        }
    }

    public void followLine() {
        // MAYBE CHANGE TO && FROM ||
        while (sensor.getGreyscale(leftProximitySensor) < DISTANCE ||
                sensor.getGreyscale(rightProximitySensor) < DISTANCE) {

                if (isOnLineLeft() && isOnLineRight()) {
                    chassis.set(MOVE_SPEED);
                } else if (isOnLineLeft() && !isOnLineRight()) {
                    chassis.setLeft(SLOW_SPEED);
                    chassis.setRight(MOVE_SPEED);
                } else if (!isOnLineLeft() && isOnLineRight()) {
                    chassis.setLeft(MOVE_SPEED);
                    chassis.setRight(SLOW_SPEED);
                } else {
                    moveForwardUntilLine();
                }
        }
        //chassis.set(0);
    }

    private Thread left = new Thread(() -> {
        while (sensor.getGreyscale(leftProximitySensor) < DISTANCE && !Thread.interrupted()) {
            chassis.setLeft(MOVE_SPEED);
        }
        chassis.setLeft(0);
    });

    private Thread right = new Thread(() -> {
        while(sensor.getGreyscale(rightProximitySensor) < DISTANCE && !Thread.interrupted()) {
            chassis.setRight(MOVE_SPEED);
        }
        chassis.setRight(0);
    });


    public void straighten() {
        left.run();
        right.run();
    }

    private boolean isOnLineLeft() {
        return sensor.getGreyscale(leftColorSensor) > TAPE_THRESHOLD;
    }

    private boolean isOnLineRight() {
        return sensor.getGreyscale(rightColorSensor) > TAPE_THRESHOLD;
    }

}