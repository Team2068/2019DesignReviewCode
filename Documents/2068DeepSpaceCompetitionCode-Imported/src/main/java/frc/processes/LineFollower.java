/*package frc.processes;

import frc.robot.DriveTrain;
import frc.sensors.i2c.*;
//import edu.wpi.first.wpilibj.DriverStation;


public class LineFollower {

    private static final int TAPE_THRESHOLD = 33;
    private static final int DISTANCE = 3;

    private static final double LINE_UP_SPEED = 0.3;
    private static final double MOVE_SPEED = 0.6;
    private static final double SLOW_SPEED = 0.4;

    //private boolean hasHitLine = false;
    private boolean fromLeft = false;

    private DriveTrain chassis;

    private int leftColorSensor, rightColorSensor, leftProximitySensor, rightProximitySensor;

    private MultiplexColorSensor sensor;

    public LineFollower(DriveTrain chassis, MultiplexColorSensor sensor, int [] ports ) {
       // hasHitLine = false;
        fromLeft = false;

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

        //score
        return true;
    }

    public void moveForwardUntilLine() {
        while (!isOnLineLeft() || !isOnLineRight()) {
            chassis.set(LINE_UP_SPEED);
        }
        // Both of the sensors is now on the line
        chassis.set(0);
    }

    /*public void turnUntilLine() {
        while (!(sensor.getGreyscale(leftColorSensor) < TAPE_THRESHOLD &&     
                sensor.getGreyscale(rightColorSensor) < TAPE_THRESHOLD)) {
                
                if(sensor.getGreyscale(leftColorSensor)) {

                } else if (sensor.getGreyscale(rightColorSensor)){
                    
                }
                chassis.setLeft(LINE_UP_SPEED);
                chassis.setRight(-LINE_UP_SPEED);
        }
    }*/
/*
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

    private boolean isOnLineLeft() {
        return sensor.getGreyscale(leftColorSensor) > TAPE_THRESHOLD;
    }

    private boolean isOnLineRight() {
        return sensor.getGreyscale(rightColorSensor) > TAPE_THRESHOLD;
    }

} */