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

import com.revrobotics.*;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.drive.*;


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

  //private CANSparkMax frontLeft = new CANSparkMax(10, MotorType.kBrushless);
  //private CANSparkMax frontRight = new CANSparkMax(11, MotorType.kBrushless);
  private CANSparkMax backLeft = new CANSparkMax(12, MotorType.kBrushless);
  private CANSparkMax backRight = new CANSparkMax(13, MotorType.kBrushless);
  private double speedMod = .5;
  //private SpeedControllerGroup leftSide = new SpeedControllerGroup(frontLeft, backLeft);
  //private SpeedControllerGroup rightSide = new SpeedControllerGroup(frontRight, backRight);

  //private DifferentialDrive chassis = new DifferentialDrive(backLeft, backRight);

  private XboxController chassisJoystick = new XboxController(0);

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    //frontLeft.setIdleMode(IdleMode.kBrake);
    //frontRight.setIdleMode(IdleMode.kBrake);
    backLeft.setIdleMode(IdleMode.kCoast);
    backRight.setIdleMode(IdleMode.kCoast);
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
  public void robotPeriodic() {
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
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    //chassis.tankDrive(chassisJoystick.getY(Hand.kLeft), chassisJoystick.getY(Hand.kLeft), true);
    backLeft.set(chassisJoystick.getY(GenericHID.Hand.kLeft)*speedMod);
    //frontRight.set(chassisJoystick.getY(GenericHID.Hand.kLeft));
    //frontRight.set(chassisJoystick.getY(GenericHID.Hand.kRight));
    backRight.set(chassisJoystick.getY(GenericHID.Hand.kRight)*speedMod);
    if(chassisJoystick.getYButtonPressed() && speedMod < 1)
    {
      speedMod+= .1;
    }
    else if(chassisJoystick.getAButtonPressed() && speedMod > 0)
    {
      speedMod-= .1;
    }

  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
    