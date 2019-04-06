package frc.robot;
import com.revrobotics.*;
import frc.sensors.*;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
public class CargoIntake
{
    private CANSparkMax drawbridgeMotor, intakeMotor;
   
    private XboxController controller;

    public CargoIntake(CANSparkMax drawbridgeMotor, CANSparkMax intakeMotor, XboxController controller)
    {
        this.drawbridgeMotor = drawbridgeMotor;
        this.intakeMotor = intakeMotor;
        
        this.controller = controller;
    }

    public void baseControl()
    {
        if(controller.getY(GenericHID.Hand.kRight) > .25  )
        {
            drawbridgeMotor.set(controller.getY(GenericHID.Hand.kRight)*.6);
        }
        else if(controller.getY(GenericHID.Hand.kRight) < -.25)// && !drawbridgeSwitch.get())
        {
            drawbridgeMotor.set(controller.getY(GenericHID.Hand.kRight)*.6);
        }
        else
        {
            drawbridgeMotor.set(0);
        }
        if(controller.getTriggerAxis(GenericHID.Hand.kRight) > .25)
        {
            intakeMotor.set(controller.getTriggerAxis(GenericHID.Hand.kRight) * .75);
        }
        else if(controller.getTriggerAxis(GenericHID.Hand.kLeft) > .25)
        {
            intakeMotor.set(-controller.getTriggerAxis(GenericHID.Hand.kLeft) * .75);
        }
        else 
        {
            intakeMotor.set(0);
        }

    }
    public boolean getLimitSwitch()
    {
        return(false);
    }
    public void displayPositions()
    {
        SmartDashboard.putBoolean("Drawbridge Switch", getLimitSwitch());
        SmartDashboard.putNumber("Drawbridge Motor", drawbridgeMotor.get());
        SmartDashboard.putNumber("Intake Motor", intakeMotor.get());
        SmartDashboard.putNumber("Stick", controller.getY(GenericHID.Hand.kRight));
    }
    
}