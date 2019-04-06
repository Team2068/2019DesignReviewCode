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
    
    private double[] hatchHeights = {0, 47.193, 131, 208};
    private double[] cargoHeights = {0, 23, 112, 183};
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
        
        
        if(controller.getY(GenericHID.Hand.kLeft) < -.25 && !upperSwitch.get())
        {    
            motor.set(controller.getY(GenericHID.Hand.kLeft));
        }
        else if(controller.getY(GenericHID.Hand.kLeft) > .25 )
        {
            motor.set(controller.getY(GenericHID.Hand.kLeft));
        }
        else
        {
            motor.set(0);
        }

    
        
    }
    //Don't touch I don't know how it works but it does.
    // Make a new method if need be.
    public void steppingLiftControl()
    {
        
        double[] selectedHeights = hatchHeights;
        
        if(!hatchMode)
        {
            selectedHeights = cargoHeights;
        }
        if(controller.getAButtonPressed() && curPosition > 0 &&  !(MechanismControl.getHasHatch() && curPosition == 1) )
        {
            curPosition--;
            targetMet = false;
        }
        else if(controller.getYButtonPressed() && curPosition < selectedHeights.length-1 )
        {
            curPosition++;
            targetMet = false;
        }
        else if(controller.getXButtonPressed())
        {

            hatchMode = !hatchMode;
            curPosition = 0;
            targetMet = false;
            
            
        }
        if( curPosition == 0)
        {
            
                if(!cargoSwitch.get() )
                {
                    motor.set(-.8);
                    if(controller.getAButtonPressed())
                    {
                        curPosition++;
                        
                    }
                }
               else
               {
                    motor.set(0);
                    encoder.reset();
               }
               targetMet = true;
                
                
        }
            else if(encoder.getPosition() < selectedHeights[curPosition] + 5 && !targetMet)
            {
                if(encoder.getPosition() < selectedHeights[curPosition]  && !upperSwitch.get() )
                {
                    motor.set(.8);
                }
                else
                {
                    motor.set(.05);
                    targetMet = true;
                    System.out.println("Hit Target");
                }
            }
            else if(encoder.getPosition() > selectedHeights[curPosition] - 5 && !targetMet)
            {
                if(encoder.getPosition() > selectedHeights[curPosition])
                {
                    motor.set(-.8);
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
            SmartDashboard.putBoolean("Target Met", targetMet);
            
    } 
    public void calibrateSensors()
    {
        encoder.reset();
        int counter = 0;
        while(counter < hatchHeights.length)
        {
            motor.set(controller.getY(GenericHID.Hand.kRight)* .5);
            if(controller.getPOV(0) == 0) //POV originally B
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
            if(controller.getPOV(0) == 180) //POV originally B
            {
                cargoHeights[counter] = encoder.getPosition();
                counter++;
            } 
        }
        
    } 
    public int getCurrentPosition()
    {
        return(curPosition);
    }
    public void setCurrentPosition(int curPosition)
    {
        this.curPosition = curPosition;
        targetMet = false;
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
    public void totalLiftControl()
    {
        if(controller.getBumper(GenericHID.Hand.kLeft) && controller.getBumper(GenericHID.Hand.kRight))
        {
            System.out.println("Starting Base Lift Control");
            baseLiftControl();
            System.out.println("Finishing Base Lift Control");
        }
        else
        {
            System.out.println("Starting Stepping Lift Control");
            steppingLiftControl();
            System.out.println("Ending Stepping Lift Control");
        }
        displayValues();
    }
    public boolean getTargetMet()
    {
        return(targetMet);
    }
    public void setTargetMet(boolean targetMet)
    {
        this.targetMet = targetMet;
    }
    public void displayValues()
    {
        SmartDashboard.putNumber("Current Position", curPosition);
        SmartDashboard.putNumber("Lift Encoder", encoder.getPosition());
        SmartDashboard.putBoolean("Bottom Limit", cargoSwitch.get());
        SmartDashboard.putNumber("Lift Motor", motor.get());
        SmartDashboard.putBoolean("Upper Limit", upperSwitch.get());
        SmartDashboard.putBoolean("Hatch Mode", hatchMode);

    }
    
} 