package frc.robot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import frc.sensors.*;
import com.revrobotics.*;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.drive.*;

public class Lift
{
    private CANSparkMax motor;
    private XboxController controller;
    private VirtualCANEncoder encoder;
    private LimitSwitch cargoSwitch, upperSwitch;
    private double trueTicks = 0;
    private double startTicks = 0;
    private boolean hatchMode = false;
    private boolean targetMet = false;
    
    private double[] hatchHeights = {0, 47.193, 131.67, 209.532};
    private double[] cargoHeights = {0, 33.14, 122.639, 201.2};
    private int curPosition = 0;
    public Lift(CANSparkMax motor,XboxController controller, LimitSwitch cargoSwitch, LimitSwitch upperSwitch)
    {
        this.motor = motor;
        this.controller= controller;
        this.upperSwitch = upperSwitch;
        encoder = new VirtualCANEncoder(motor);
        this.cargoSwitch = cargoSwitch;
        motor.setInverted(true);
        motor.setIdleMode(IdleMode.kBrake);
    }
    public void baseLiftControl()
    {
        if(controller.getAButtonPressed())
        {
            System.out.println(trueTicks);
        }
        if(controller.getTriggerAxis(GenericHID.Hand.kRight) > .25)
        {
            motor.set(controller.getTriggerAxis(GenericHID.Hand.kRight)*.25);
        }
        else if(controller.getTriggerAxis(GenericHID.Hand.kLeft) > .25)
        {
            motor.set(-controller.getTriggerAxis(GenericHID.Hand.kLeft)*.25);
        }
        else if(controller.getYButtonPressed())
        {
            encoder.getPosition();
        }
        else
        {
            motor.set(0);
        }
        System.out.println(motor.get());
    }
    
    public void steppingLiftControl()
    {
        
        double[] selectedHeights = hatchHeights;
        
        if(!hatchMode)
        {
            selectedHeights = cargoHeights;
        }
        if(controller.getAButtonPressed() && curPosition > 0)
        {
            curPosition--;
            targetMet = false;
        }
        else if(controller.getYButtonPressed() && curPosition < selectedHeights.length-1)
        {
            curPosition++;
            targetMet = false;
        }
        else if(controller.getXButtonPressed())
        {

            hatchMode = !hatchMode;
            curPosition = 0;
            targetMet = false;
            while(!cargoSwitch.get())
            {
                motor.set(-1);
            }
            encoder.reset();
            motor.set(0);
            
        }
        if( curPosition == 0)
        {
            
                while(!cargoSwitch.get())
                {
                    motor.set(-1);
                }
               
                motor.set(0);
                encoder.reset();
                
                
                
        }
            else if(-encoder.getPosition() < selectedHeights[curPosition]+5 && !targetMet)
            {
                if(-encoder.getPosition() < selectedHeights[curPosition] && !upperSwitch.get())
                {
                    motor.set(1);
                }
                else
                {
                    motor.set(.05);
                    targetMet = true;
                    System.out.println("Hit Target");
                }
            }
            else if(-encoder.getPosition() > selectedHeights[curPosition]-5 && !targetMet)
            {
                if(-encoder.getPosition() > selectedHeights[curPosition])
                {
                    motor.set(-1);
                }
                else
                {
                    System.out.println("Hit target");
                    motor.set(.05);
                    targetMet = true;
                }
            }
            else 
            {
                motor.set(.05);
            }
            //System.out.println(curPosition);
            System.out.println("Target Met: " + targetMet);
            System.out.println(cargoSwitch.get());
            SmartDashboard.putNumber("Lift Encoder", encoder.getPosition());
            
    } 
    public void calibrateSensors()
    {
        encoder.reset();
        int counter = 0;
        while(counter < hatchHeights.length)
        {
            motor.set(controller.getY(GenericHID.Hand.kRight)* .5);
            if(controller.getBButtonPressed())
            {
                hatchHeights[counter] = encoder.getPosition();
                counter++;
            }
        }
        while(!cargoSwitch.get())
        {
            motor.set(-.5);
        }
        encoder.reset();
        counter = 0;
        while(counter < cargoHeights.length)
        {
            motor.set(controller.getY(GenericHID.Hand.kRight)* .5);
            if(controller.getBButtonPressed())
            {
                cargoHeights[counter] = encoder.getPosition();
                counter++;
            } 
        }
        
    } 
    public boolean isRaised()
    {
        if(curPosition > 1)
        {
            return(true);
        }
        else
        {
            return(false);   
        }
    }
    public void displayValues()
    {
        SmartDashboard.putNumber("Current Position", curPosition);
        SmartDashboard.putNumber("Lift Encoder", encoder.getPosition());
        SmartDashboard.putBoolean("Bottom Limit", cargoSwitch.get());
        SmartDashboard.putNumber("Lift Motor", motor.get());

    }
    
} 