package frc.robot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.GenericHID.Hand;

import com.revrobotics.*;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.drive.*;
import frc.sensors.*;
public class LiftPistonControl
{
    DoubleSolenoid liftPistonFront;
    DoubleSolenoid liftPistonBack;
   //DoubleSolenoid liftPistonFR;
    XboxController controller; 

    public LiftPistonControl(DoubleSolenoid liftPistonFront, DoubleSolenoid liftPistonBack, XboxController controller)
    {
        this.liftPistonFront = liftPistonFront; //I assume that these are right? -KS
        this.liftPistonBack = liftPistonBack;
       // this.liftPistonFR = liftPistonFR;
        liftPistonFront.set(DoubleSolenoid.Value.kOff);
        liftPistonBack.set(DoubleSolenoid.Value.kOff);
    }

    public void frontLiftPistonsFire(boolean forward) //literally praying that this works
    {
        if(!forward && controller.getYButtonPressed())
        {
            liftPistonFront.set(DoubleSolenoid.Value.kForward);
        }
        else if(forward && controller.getYButtonPressed())
        {
            liftPistonFront.set(DoubleSolenoid.Value.kReverse);
        }
        else
        {
            liftPistonFront.set(DoubleSolenoid.Value.kOff);
        }
    }

    public void backLiftPistonsFire(boolean forward)//not quite sure if the boolean should be there
    {
        if(!forward && controller.getAButtonPressed())
        {
            liftPistonBack.set(DoubleSolenoid.Value.kForward);
        }
        else if(forward && controller.getAButtonPressed())
        {
            liftPistonBack.set(DoubleSolenoid.Value.kReverse);
        }
        else
        {
            liftPistonBack.set(DoubleSolenoid.Value.kOff);
        }
    }

  
}   

