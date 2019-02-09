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
    private LimitSwitch cargoSwitch;
    private double trueTicks = 0;
    private double startTicks = 0;
    private boolean hatchMode = false;
    private boolean targetMet = false;
    
    private double[] hatchHeights = {0, 47.193, 131.67, 209.532};
    private double[] cargoHeights = {0, 33.14, 122.639, 201.2};
    private int curPosition = 0;
    public Lift(CANSparkMax motor,XboxController controller, LimitSwitch cargoSwitch)
    {
        this.motor = motor;
        this.controller= controller;
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
            motor.set(controller.getTriggerAxis(GenericHID.Hand.kRight));
        }
        else if(controller.getTriggerAxis(GenericHID.Hand.kLeft) > .25)
        {
            motor.set(-controller.getTriggerAxis(GenericHID.Hand.kLeft));
        }
        else if(controller.getYButtonPressed())
        {
            encoder.getPosition();
        }
        else
        {
            motor.set(0);
        }
        
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
                if(-encoder.getPosition() < selectedHeights[curPosition])
                {
                    motor.set(1);
                }
                else
                {
                    motor.set(0);
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
                    motor.set(0);
                    targetMet = true;
                }
            }
            else 
            {
                motor.set(0);
            }
            //System.out.println(curPosition);
            System.out.println("Target Met: " + targetMet);
            System.out.println(cargoSwitch.get());
            
    }  
    
} 