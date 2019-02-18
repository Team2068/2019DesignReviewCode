package frc.robot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.GenericHID.Hand;

import com.revrobotics.*;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.drive.*;
import frc.sensors.*;
public class PneumaticsControl
{
    Solenoid suction1, suction2, airOutake;
    DoubleSolenoid piston;
    Timer timer1 = new Timer();
    XboxController controller;
    public PneumaticsControl(Solenoid suction1, Solenoid suction2, Solenoid airOutake,  DoubleSolenoid piston, XboxController controller )
    
    {
        this.suction1 = suction1;
        this.suction2 = suction2;
        this.controller = controller;
        this.airOutake = airOutake;
        this.piston = piston;
        piston.set(DoubleSolenoid.Value.kOff);
        
    
    }

    private void wait(double time)
    {
        timer1.reset();
        timer1.start();
        while(timer1.get() < time)
        {

        }
        timer1.stop();
    }
    private void suctionControl(boolean on)
    {
        suction1.set(on);
        suction2.set(on);
        
    }
    public void intakeHatch()
    {
        piston.set(DoubleSolenoid.Value.kForward);
        wait(.1);
        
        airOutake.set(true);
        suctionControl(true);
        wait(.25);
        suctionControl(false);
        wait(.5);
        airOutake.set(false);
        wait(.25);
        

        

    }
    public void testOpenClose(boolean open)
    {
        if(open)
        {
            suctionControl(true);
            airOutake.set(true);
        }
        else
        {
            suctionControl(false);
            wait(.1);
            airOutake.set(false);
        }
    }
    public void pistonControl(boolean forward)
    {
        if(forward)
        {
            piston.set(DoubleSolenoid.Value.kForward);
        }
        else
        {
            piston.set(DoubleSolenoid.Value.kOff);
        }
    }
    public void outakeHatch()
    {
        suctionControl(true);
        wait(.25);
        suctionControl(false);
        piston.set(DoubleSolenoid.Value.kReverse);
    }
    public void displayPositions()
    {
        SmartDashboard.putBoolean("Suction1", suction1.get());
        SmartDashboard.putBoolean("Suction2", suction2.get());
        SmartDashboard.putBoolean("Air Outake", airOutake.get());
        SmartDashboard.putString("Piston", piston.get().toString());
    }
    public void independentControl()
    {
        if(controller.getAButtonPressed())
        {
            piston.set(DoubleSolenoid.Value.kForward);
        }
        else if(controller.getBButtonPressed())
        {
            piston.set(DoubleSolenoid.Value.kReverse);
        }
        else if(controller.getXButtonPressed())
        {
            piston.set(DoubleSolenoid.Value.kOff);
        }
        if(controller.getYButtonPressed())
        {
            airOutake.set(!airOutake.get());
        }
        if(controller.getBumperPressed(GenericHID.Hand.kRight))
        {
            suctionControl(true);

        }
        else if(controller.getBumperPressed(GenericHID.Hand.kLeft))
        {
            suctionControl(false);
        }

    }
}