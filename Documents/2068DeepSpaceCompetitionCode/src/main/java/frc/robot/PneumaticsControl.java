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
    //Solenoid suction1, suction2, airOutake;
    public Solenoid suctionCups, airOutake;
    public DoubleSolenoid piston;
    Timer timer1 = new Timer();
    XboxController controller;
    //public PneumaticsControl(Solenoid suction1, Solenoid suction2, Solenoid airOutake,  DoubleSolenoid piston, XboxController controller )
    Timer timer2 = new Timer();
    private boolean hasHatch = false;
    private boolean flag1 = true;
    private boolean flag2 = true;
    public PneumaticsControl(Solenoid suctionCups, Solenoid airOutake,  DoubleSolenoid piston, XboxController controller)
    {
       // this.suction1 = suction1;
       // this.suction2 = suction2;
       this.suctionCups = suctionCups;
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
      //  suction1.set(on);
       // suction2.set(on);
       suctionCups.set(on);
        
    }
    public void intakeHatch()
    {
        piston.set(DoubleSolenoid.Value.kForward);
        wait(.1);
        
        airOutake.set(true);
        wait(0.5);
        suctionCups.set(true);
        wait(1.0);
        suctionCups.set(false);
        wait(.25);
        airOutake.set(false);
        piston.set(DoubleSolenoid.Value.kReverse);
       
        
        

        

    }
    public void venturiAlternator()
    {
        if(hasHatch)
        {
            if(timer2.get() > 2 && timer2.get() < 2.9 )
            {
                flag2 = true;
                if(flag1 )
                {
                    timer1.reset();
                    flag1 = false;
                }
                System.out.println("Debug: Turning off suction cups");
                
                
                suctionCups.set(false);
               
                
                    System.out.println("Debug: Turning off air outake");
                if(timer2.get() > 2.1)
                {
                airOutake.set(false);
                }
                
            }
            else if(timer2.get() > 3)
            {
                System.out.println("Debug: Turning on air outake");
                flag1 = true;
                if(flag2)
                {
                    timer1.reset();
                    flag2 = false;
                }
                timer2.reset();
                airOutake.set(true);
                
                
                
                    System.out.println("Debug: Turning on suction cups");
                    suctionCups.set(true);
                
            }
            SmartDashboard.putNumber("Timer 1", timer1.get());
            SmartDashboard.putNumber("Timer 2", timer2.get());
        }
        else
        {
            airOutake.set(false);
            System.out.println("Debug: No Hatch");
            SmartDashboard.putNumber("Timer 1", timer1.get());
            SmartDashboard.putNumber("Timer 2", timer2.get());
        }
        
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
            piston.set(DoubleSolenoid.Value.kReverse);
        }
    }
    public void outakeHatch()
    {   
        piston.set(DoubleSolenoid.Value.kForward);
        wait(.25);
        airOutake.set(false);
        suctionControl(true);
        wait(.25);
        suctionControl(false);
        piston.set(DoubleSolenoid.Value.kReverse);
        hasHatch = false;
        timer2.stop();
    }
    public void displayPositions()
    {
       // SmartDashboard.putBoolean("Suction1", suction1.get());
       // SmartDashboard.putBoolean("Suction2", suction2.get());
        SmartDashboard.putBoolean("suctionCups", suctionCups.get());
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
            suctionCups.set(true);

        }
        else if(controller.getBumperPressed(GenericHID.Hand.kLeft))
        {
           suctionCups.set(false);
        }
        

    }
    public void retractPiston()
    {
        piston.set(DoubleSolenoid.Value.kReverse);
    }

    /*public void liftPistonsControl(boolean forward) //literally praying that this works
    {
        if(forward)
        {
            liftPistonFL.set(DoubleSolenoid.Value.kForward);
            liftPistonBL.set(DoubleSolenoid.Value.kForward);
            liftPistonFR.set(DoubleSolenoid.Value.kForward);
            liftPistonBR.set(DoubleSolenoid.Value.kForward);
        }
        else
        {
            liftPistonFL.set(DoubleSolenoid.Value.kOff);
            liftPistonBL.set(DoubleSolenoid.Value.kOff);
            liftPistonFR.set(DoubleSolenoid.Value.kOff);
            liftPistonBR.set(DoubleSolenoid.Value.kOff);
        }
    }

    public void retractLiftPistons()
    {
        liftPistonFL.set(DoubleSolenoid.Value.kReverse);
        liftPistonBL.set(DoubleSolenoid.Value.kReverse);
        liftPistonFR.set(DoubleSolenoid.Value.kReverse);
        liftPistonBR.set(DoubleSolenoid.Value.kReverse);
    }

    public void platformClimbing()
    {
        if(controller.)
    }
    */
}
