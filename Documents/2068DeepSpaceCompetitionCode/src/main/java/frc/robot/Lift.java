package frc.robot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import frc.sensors.*;

import javax.lang.model.util.ElementScanner6;

import com.revrobotics.*;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.drive.*;

public class Lift
{
    private CANSparkMax motor;
    private XboxController controller;
    private VirtualCANEncoder encoder;
    private LimitSwitch lowerSwitch, upperSwitch;
    private double trueTicks = 0;
    private double startTicks = 0;
    private boolean hatchMode = false;
    private boolean targetMet = false;
    private boolean manualMode = false;
    private double[] hatchHeights = {0, 47.193, 131, 208};
    private double[] cargoHeights = {0, 28, 112, 183};
    private int curPosition = 0;
    public Lift(CANSparkMax motor,XboxController controller, LimitSwitch lowerSwitch, LimitSwitch upperSwitch)
    {
        this.motor = motor;
        this.controller= controller;
        this.upperSwitch = upperSwitch;
        encoder = new VirtualCANEncoder(motor);
        this.lowerSwitch = lowerSwitch;
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
        if(controller.getAButtonPressed()   )
        {
            if(curPosition == 0)
            {
                curPosition = curPosition;
                targetMet = true;
            }
            else if(MechanismControl.getHasHatch() && curPosition == 1)
            {
                curPosition = curPosition;
                targetMet = true;
            }
            else
            {
                curPosition--;
                targetMet = false;
            }
        }
        else if(controller.getYButtonPressed() )
        {
            if(  curPosition >= selectedHeights.length-1)
            {
                curPosition = curPosition;
                targetMet = true;
            }
            else
            {
                curPosition++;
                targetMet = false;
            }
        }
        else if(controller.getXButtonPressed())
        {
            if(MechanismControl.getHasHatch())
            {
                hatchMode = true;
                curPosition = 1;
                targetMet = false;

            }
            else
            {

                hatchMode = !hatchMode;
                curPosition = 0;
                targetMet = false;
            }
            
            
        }
        if(curPosition < 0)
        {
            curPosition = 0;
            targetMet = true;
        }
        if(curPosition > selectedHeights.length-1)
        {
            curPosition = selectedHeights.length-1;
            targetMet = true;
        }
        //This is the code that controls physical motion of the lift
        double encoderPosition = encoder.getPosition();
        if( curPosition == 0)
        {

                if(!lowerSwitch.get() && encoderPosition > -10 )
                {
                    motor.set(-.8);
                    
                }
               else
               {
                    motor.set(0);
                    encoder.reset();
                    targetMet = true;
               }
               
                
                
        }
        else if(encoderPosition < selectedHeights[curPosition] + 5 && !targetMet)
            {
                if(encoderPosition < selectedHeights[curPosition]  && !upperSwitch.get() && !(encoderPosition > selectedHeights[selectedHeights.length-1] + 10) )

                {
                    motor.set(.8);
                }
                else
                {
                    motor.set(.05);
                    targetMet = true;
                    //System.out.println("Hit Target");
                }
            }
        else if(encoderPosition > selectedHeights[curPosition] - 5 && !targetMet )
            {
                if(encoderPosition > selectedHeights[curPosition] && !lowerSwitch.get() && !(encoderPosition < -10))
                {
                    motor.set(-.8);
                }
                else
                {
                    //System.out.println("Hit target");
                    motor.set(.05);
                    targetMet = true;
                }
            }
            else 
            {
                motor.set(.05);
            }
            //System.out.println(curPosition);
            //System.out.println("Target Met: " + targetMet);
            //System.out.println(lowerSwitch.get());
            SmartDashboard.putNumber("Lift Encoder", encoderPosition);
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
        while(!lowerSwitch.get())
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
        
        if(controller.getBumper(GenericHID.Hand.kRight))
        {
            //System.out.println("Starting Stepping Lift Control");
            baseLiftControl();
            //System.out.println("Ending Stepping Lift Control");
        }
        else
        {
            steppingLiftControl();
            
        }
        displayValues();
        SmartDashboard.putBoolean("Manual Mode", manualMode);
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
        SmartDashboard.putBoolean("Bottom Limit", lowerSwitch.get());
        SmartDashboard.putNumber("Lift Motor", motor.get());
        SmartDashboard.putBoolean("Upper Limit", upperSwitch.get());
        SmartDashboard.putBoolean("Hatch Mode", hatchMode);

    }
    public boolean getManualMode()
    {
        return(manualMode);
    }
    public void liftToLevel(int level)
    {
        setCurrentPosition(level);
        while(!targetMet)
        {
            steppingLiftControl();
        }
    }
    
} 