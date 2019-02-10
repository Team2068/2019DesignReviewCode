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
    Solenoid suction1, suction2, airOutake, vacuumControl;
    DoubleSolenoid piston;
    Timer timer1 = new Timer();
    public PneumaticsControl(Solenoid suction1, Solenoid suction2, Solenoid airOutake, Solenoid vacuumControl,  DoubleSolenoid piston )
    
    {
        this.suction1 = suction1;
        this.suction2 = suction2;
        this.vacuumControl = vacuumControl;
        this.airOutake = airOutake;
        this.piston = piston;

        
    
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
        vacuumControl.set(on);
    }
    public void intakeHatch()
    {
        piston.set(DoubleSolenoid.Value.kForward);
        airOutake.set(false);
        wait(.25);
        suctionControl(true);
        wait(.25);
        piston.set(DoubleSolenoid.Value.kReverse);

        

    }
    public void outakeHatch()
    {
        piston.set(DoubleSolenoid.Value.kForward);
        wait(.25);
        suctionControl(false);
        wait(.25);
        airOutake.set(true);
        wait(.25);
        piston.set(DoubleSolenoid.Value.kReverse);
    }
}