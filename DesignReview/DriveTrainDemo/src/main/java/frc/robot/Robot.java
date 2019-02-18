/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;

import com.revrobotics.*;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.drive.*;
import frc.sensors.*;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  private CANSparkMax cargoIntake = new CANSparkMax(6, MotorType.kBrushed);
  private CANSparkMax frontLeft = new CANSparkMax(8, MotorType.kBrushless);
  private CANSparkMax frontRight = new CANSparkMax(1, MotorType.kBrushless);
  private CANSparkMax backLeft = new CANSparkMax(7, MotorType.kBrushless);
  private CANSparkMax backRight = new CANSparkMax(2, MotorType.kBrushless);
  private CANSparkMax drawBridge = new CANSparkMax(3, MotorType.kBrushless);
  private CANSparkMax liftMotor = new CANSparkMax(4, MotorType.kBrushless);
  private XboxController chassisJoystick = new XboxController(0);
  private XboxController mechanismController = new XboxController(1);
  private Lift lift = new Lift(liftMotor, mechanismController, new LimitSwitch(3));
  private DriveTrain chassis = new DriveTrain(frontRight, backRight, frontLeft, backLeft, chassisJoystick );
  private CANEncoder frontLeftEncoder = frontLeft.getEncoder();//
  private CANEncoder frontRightEncoder = frontRight.getEncoder();//
  private CANEncoder backLeftEncoder = backLeft.getEncoder();
  private CANEncoder backRightEncoder = backRight.getEncoder();
  private double rightAverageStart = backRightEncoder.getPosition(); 
  private double leftAverageStart = backLeftEncoder.getPosition(); 
  private double rightAverageTrue = 0;
  private double leftAverageTrue = 0;
  private double speedMod = .1;
  private int directionMod = 1;
  private boolean metTarget = false;
  private LimitSwitch cargoSwitch = new LimitSwitch(1);
  private LimitSwitch drawbridgeSwitch = new LimitSwitch(0);
  private Solenoid suction1 = new Solenoid(3);
  private Solenoid suction2 = new Solenoid(4);
  private Solenoid airOutake = new Solenoid(2);
  private DoubleSolenoid hatchPiston = new DoubleSolenoid(0,1);
  //private Solenoid vacuumControl = new Solenoid(5);
  private PneumaticsControl hatchIntake = new PneumaticsControl(suction1, suction2, airOutake, hatchPiston, mechanismController);
  private boolean hasHatch = false;
  private boolean testFlag = false;
  

  
  private void updateEncoders()
  {
    double leftCur = backLeftEncoder.getPosition(); 
    double rightCur = backRightEncoder.getPosition(); 
    rightAverageTrue = -(rightCur - rightAverageStart);
    leftAverageTrue = (leftCur - leftAverageStart);
    
    System.out.println("Left Side: " + leftAverageTrue);
    System.out.println("Right SIde: " +rightAverageTrue);
  }
  private void resetEncoders()
  {
    rightAverageStart = backRightEncoder.getPosition();
    leftAverageStart = backLeftEncoder.getPosition(); 
  }
  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    cargoIntake.setIdleMode(IdleMode.kCoast);
    drawBridge.setIdleMode(IdleMode.kBrake);
    frontRight.setMotorType(MotorType.kBrushless);
    frontLeft.setMotorType(MotorType.kBrushless);
    backRight.setMotorType(MotorType.kBrushless);
    backLeft.setMotorType(MotorType.kBrushless);
    liftMotor.setMotorType(MotorType.kBrushless);
    drawBridge.setMotorType(MotorType.kBrushless);
    cargoIntake.setMotorType(MotorType.kBrushed);
    frontRight.setIdleMode(IdleMode.kBrake);
    frontLeft.setIdleMode(IdleMode.kBrake);
    backRight.setIdleMode(IdleMode.kBrake);
    backLeft.setIdleMode(IdleMode.kBrake);
    frontLeft.setInverted(true);
    backLeft.setInverted(true);
    frontRight.setInverted(false);
    backRight.setInverted(false);
    hatchPiston.set(DoubleSolenoid.Value.kReverse);

    
    

  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic()
  {
    hatchIntake.displayPositions();
    lift.displayValues();
    chassis.displayValues();
    SmartDashboard.putBoolean("Cargo Switch", cargoSwitch.get());
    SmartDashboard.putBoolean("Drawbridge Switch", drawbridgeSwitch.get());
    SmartDashboard.putNumber("Drawbridge Motor", drawBridge.get());
    SmartDashboard.putNumber("Cargo Intake Motor", cargoIntake.get());

  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // autoSelected = SmartDashboard.getString("Auto Selector",
    // defaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
    //backRight.setInverted(false);
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() 
    {
        double targetRevs = 32.286;
        chassis.autoDrive(1, 1, targetRevs, targetRevs);
    
    }
  

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    

    chassis.baseDrive();
    if(lift.isRaised())
    {
      chassis.setSpeedMod2(.2);
    }
    else
    {
      chassis.setSpeedMod2(1);
    }
    
      lift.steppingLiftControl();
      if(mechanismController.getTriggerAxis(GenericHID.Hand.kRight) >.25)
      {
        cargoIntake.set(mechanismController.getTriggerAxis(GenericHID.Hand.kRight));
      }
      else if(mechanismController.getTriggerAxis(GenericHID.Hand.kLeft) > .25)// && !cargoSwitch.get())
      {
        cargoIntake.set(-mechanismController.getTriggerAxis(GenericHID.Hand.kLeft));
      }
      else
      {
        cargoIntake.set(0);
      }
     
    
      if(mechanismController.getY(GenericHID.Hand.kRight) > .75)
      {
        drawBridge.set(mechanismController.getY(GenericHID.Hand.kRight) /2);
      }
      else if(mechanismController.getY(Hand.kRight) < -0.75 && !drawbridgeSwitch.get() )
      {
        drawBridge.set(mechanismController.getY(Hand.kRight) /2);
      }
      else
      {
        drawBridge.set(0);
      }
      
      if(mechanismController.getBButtonPressed() ) //&& drawbridgeSwitch.get() == true)
      {
        if(hasHatch)
        {
          hatchIntake.outakeHatch();
          hasHatch = false;
          //mechanismController.setRumble(RumbleType.kRightRumble, 1);
        }
        else
        {
          hatchIntake.intakeHatch();
          hasHatch = true;
          
        }
      }
      hatchIntake.displayPositions();
      SmartDashboard.putBoolean("hasHatch", hasHatch);
    
    
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() 
  {
    /*System.out.println(testFlag);
    if(mechanismController.getBButton() && !testFlag)
    {
      hatchIntake.testOpenClose(true);
      System.out.println("Open");
      testFlag = true;
    }
    else if(!mechanismController.getBButton() && testFlag)
    {
        hatchIntake.testOpenClose(false);
        System.out.println("Closed");
        testFlag = false;
    }
    if(mechanismController.getXButtonPressed())
    {
        hatchIntake.pistonControl(false);
    }
    else if(mechanismController.getAButtonPressed())
    {
        hatchIntake.pistonControl(true);
    }
    
  }
  */
    //hatchIntake.independentControl();
    //hatchIntake.displayPositions();
  if(testFlag)
  {
    lift.calibrateSensors();
    testFlag = true;
  }
  
  

}
}
    