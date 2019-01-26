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
    private boolean hatchMode = true;
    
    private double[] hatchHeights = {47.193, 131.67, 213.532};
    private double[] cargoHeights = {33.14, 122.639, 201.2};
    private int curPosition = 0;
    public Lift(CANSparkMax motor,XboxController controller, LimitSwitch cargoSwitch)
    {
        this.motor = motor;
        this.controller= controller;
        encoder = new VirtualCANEncoder(motor);
        this.cargoSwitch = cargoSwitch;
        motor.setInverted(true);
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
        }
        else if(controller.getYButtonPressed() && curPosition < selectedHeights.length-1)
        {
            curPosition++;
        }
        else if(controller.getXButtonPressed())
        {

            hatchMode = !hatchMode;
            
        }
        if(!hatchMode && curPosition == 0)
        {
            
                if(!cargoSwitch.get())
                {
                    motor.set(-.75);
                }
                encoder.reset();
                
        }
            if(encoder.getPosition() < selectedHeights[curPosition])
            {
                if(encoder.getPosition() < selectedHeights[curPosition])
                {
                    motor.set(1);
                }
            }
            else if(encoder.getPosition() > selectedHeights[curPosition])
            {
                if(encoder.getPosition() > selectedHeights[curPosition])
                {
                    motor.set(-1);
                }
            }
            System.out.println("Target Position: " + selectedHeights[curPosition]);
            System.out.println("Current Position: " + encoder.getPosition());
            
    }  
} 