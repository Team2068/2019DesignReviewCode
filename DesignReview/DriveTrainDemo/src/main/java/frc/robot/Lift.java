package frc.robot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.GenericHID.Hand;

import com.revrobotics.*;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.drive.*;

public class Lift
{
    private CANSparkMax motor;
    private XboxController controller;
    private CANEncoder encoder;
    private double trueTicks = 0;
    private double startTicks = 0;
    private boolean hatchMode = false;
    private double[] encoderHeights = {0,33.14,47.193,122.639,131.67,201.2,213.532};
    private int curPosition = 0;
    public Lift(CANSparkMax motor,XboxController controller)
    {
        this.motor = motor;
        this.controller= controller;
        encoder = motor.getEncoder();
    }
    public void baseLiftControl()
    {
        if(controller.getAButtonPressed())
        {
            System.out.println(trueTicks);
        }
        if(controller.getTriggerAxis(GenericHID.Hand.kRight) > .25)
        {
            motor.set(controller.getTriggerAxis(GenericHID.Hand.kRight));
        }
        else if(controller.getTriggerAxis(GenericHID.Hand.kLeft) > .25)
        {
            motor.set(-controller.getTriggerAxis(GenericHID.Hand.kLeft));
        }
        else if(controller.getYButtonPressed())
        {
            resetEncoder();
        }
        else
        {
            motor.set(0);
        }
        updateEncoder();
    }
    private void updateEncoder()
    {
        trueTicks = startTicks - encoder.getPosition();
    }
    private void resetEncoder()
    {
        startTicks = encoder.getPosition();
    }
    public void steppingLiftControl()
    {
        if(controller.getAButtonPressed())
        {
            curPosition--;
        }
        else if(controller.getYButtonPressed())
        {
            curPosition++;
        }
        else if(controller.getXButtonPressed())
        {

            hatchMode = !hatchMode;
            
        }
        double targetRevs = encoderHeights[curPosition];
        //if(!hatchSwitch.get())
        
        if(trueTicks < targetRevs)
        {
            
            motor.set(-.75);
            updateEncoder();
            
        }
        else if(trueTicks > targetRevs)
        {
            
            motor.set(.75);
            updateEncoder();
            
        }
        else
        {
            motor.set(0);
            updateEncoder();
        }
        //TODO: Add Reset Functionality upon limit switch press
    }
    
}