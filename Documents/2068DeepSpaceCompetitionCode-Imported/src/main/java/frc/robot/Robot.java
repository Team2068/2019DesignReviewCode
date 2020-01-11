/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.revrobotics.*;
import frc.sensors.*;
import frc.sensors.led.DeepSpaceLightControl;
import edu.wpi.first.wpilibj.*;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANSparkMax.IdleMode;

import frc.sensors.i2c.*;
//import frc.processes.LineFollower;

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
  private boolean frontPistonFlag = false;
  private boolean backPistonFlag = false;
  private CANSparkMax cargoIntakeMotor = new CANSparkMax(6, MotorType.kBrushed);
  private CANSparkMax frontLeft = new CANSparkMax(8, MotorType.kBrushless);
  private CANSparkMax frontRight = new CANSparkMax(1, MotorType.kBrushless);
  private CANSparkMax backLeft = new CANSparkMax(7, MotorType.kBrushless);
  private CANSparkMax backRight = new CANSparkMax(2, MotorType.kBrushless);
  private CANSparkMax drawBridge = new CANSparkMax(3, MotorType.kBrushless);
  private CANSparkMax liftMotor = new CANSparkMax(4, MotorType.kBrushless);
  private XboxController chassisJoystick = new XboxController(0);
  private XboxController mechanismController = new XboxController(1);
  private LimitSwitch motherSwitch = new LimitSwitch(9);
  private Lift lift = new Lift(liftMotor, mechanismController, new LimitSwitch(3), motherSwitch);
  private DriveTrain chassis = new DriveTrain(frontRight, backRight, frontLeft, backLeft, chassisJoystick);
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
  private int counter = 0;
  private boolean metTarget = false;
  // private LimitSwitch cargoSwitch = new LimitSwitch(1);
  // private LimitSwitch drawbridgeSwitch = new LimitSwitch(0);
  // private Solenoid suction1 = new Solenoid(3);
  // private Solenoid suction2 = new Solenoid(4); // cole said to change to port
  // three, was originally port four
  // private Solenoid suctionCups = new Solenoid(3);
  // private Solenoid airOutake = new Solenoid(2);
  // private DoubleSolenoid hatchPiston = new DoubleSolenoid(0,1);
  private DoubleSolenoid liftPistonBack = new DoubleSolenoid(4, 5); // lines 62-65 are likely wrong
  private DoubleSolenoid liftPistonFront = new DoubleSolenoid(6, 7);

  // private Solenoid vacuumControl = new Solenoid(5);
  // // private PneumaticsControl hatchIntake = new PneumaticsControl(suctionCups,
  // airOutake, hatchPiston, mechanismController);
  // private boolean hasHatch = false;
  private boolean testFlag = false;
  private CargoIntake cargoIntake = new CargoIntake(drawBridge, cargoIntakeMotor, mechanismController);
  private MechanismControl mechanismControl = new MechanismControl(cargoIntake, /* hatchIntake */ lift,
      mechanismController);
  private boolean eStop = false;
  private boolean forward = false;
  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  // line 79 is likely wrong... line 75 is the only one wrong?
  // private LiftPistonControl LiftPistonControl = new
  // LiftPistonControl(liftPistonFL, liftPistonBL, liftPistonBR, chassisJoystick);
  private LiftPistonControl liftPistonControl = new LiftPistonControl(liftPistonFront, liftPistonBack, chassisJoystick);

  // private DeepSpaceLightControl lighting = new
  // DeepSpaceLightControl(SerialPort.Port.kUSB, 9600);

  private int LEFT_COLOR_SENSOR_PORT = 6, RIGHT_COLOR_SENSOR_PORT = 7, LEFT_PROX_SENSOR_PORT = 3,
      RIGHT_PROX_SENSOR_PORT = 2;
  private int[] ports = { LEFT_COLOR_SENSOR_PORT, RIGHT_COLOR_SENSOR_PORT, LEFT_PROX_SENSOR_PORT,
      RIGHT_PROX_SENSOR_PORT };
  private MultiplexColorSensor mux;

  @Override
  public void robotInit() {
    // mux = new MultiplexColorSensor(ports, I2C.Port.kMXP);
    // lighting.init();
    cargoIntakeMotor.setIdleMode(IdleMode.kCoast);
    drawBridge.setIdleMode(IdleMode.kBrake);
    frontRight.setMotorType(MotorType.kBrushless);
    frontLeft.setMotorType(MotorType.kBrushless);
    backRight.setMotorType(MotorType.kBrushless);
    backLeft.setMotorType(MotorType.kBrushless);
    liftMotor.setMotorType(MotorType.kBrushless);
    drawBridge.setMotorType(MotorType.kBrushless);
    cargoIntakeMotor.setMotorType(MotorType.kBrushed);
    frontRight.setIdleMode(IdleMode.kBrake);
    frontLeft.setIdleMode(IdleMode.kBrake);
    backRight.setIdleMode(IdleMode.kBrake);
    backLeft.setIdleMode(IdleMode.kBrake);
    frontLeft.setInverted(false);
    backLeft.setInverted(false);
    frontRight.setInverted(true);
    backRight.setInverted(true);
    // hatchPiston.set(DoubleSolenoid.Value.kReverse);

    // maybe right???

    // liftPistonFront.set(DoubleSolenoid.Value.kReverse);
    // liftPistonBack.set(DoubleSolenoid.Value.kReverse);

  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for
   * items like diagnostics that you want ran during disabled, autonomous,
   * teleoperated and test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    double baseTemp = (frontRight.getMotorTemperature() + frontLeft.getMotorTemperature()
        + backLeft.getMotorTemperature() + backRight.getMotorTemperature()) / 4.0;
    mechanismControl.displayPositions();
    chassis.displayValues();
    SmartDashboard.putNumber("Right Stick", mechanismController.getY(GenericHID.Hand.kRight));
    SmartDashboard.putBoolean("Upper Limit", motherSwitch.get());
    SmartDashboard.putNumber("Drawbridge Encoder", drawBridge.getEncoder().getPosition());
    SmartDashboard.putNumber("Base Temp", baseTemp);
    SmartDashboard.putNumber("Lift Temp", liftMotor.getMotorTemperature());
    //// System.out.println("Is working");
    Camera c = new Camera();
    c.doCamera();
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable chooser
   * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
   * remove all of the chooser code and uncomment the getString line to get the
   * auto name from the text box below the Gyro
   *
   * <p>
   * You can add additional auto modes by adding additional comparisons to the
   * switch structure below with additional strings. If using the SendableChooser
   * make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {

    // autoSelected = SmartDashboard.getString("Auto Selector",
    // defaultAuto);
    // System.out.println("Auto selected: " + m_autoSelected);
    // lighting.autonomous();

  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    counter++;

    // System.out.println("Has worked for" + counter + "acts.");

    mechanismControl.baseControl();
    // System.out.println("Passed Mechanism control on loop" + counter);
    chassis.baseDrive();
    // System.out.println("Passed Chassis control on loop" + counter);
    if (lift.isRaised()) {
      chassis.setSpeedMod2(.5);
    } else {
      chassis.setSpeedMod2(1);
    }
    // System.out.println("Finished loop" + counter);

  }

  @Override
  public void teleopInit() {
    // lighting.driverControl();
    // liftPistonFront.set(DoubleSolenoid.Value.kReverse);
    // liftPistonBack.set(DoubleSolenoid.Value.kReverse);
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    counter++;
    SmartDashboard.putBoolean("Estop", eStop);
    // System.out.println("Has worked for" + counter + "acts.");

    /*
     * SmartDashboard.putNumber("LeftColorSensor",
     * mux.getGreyscale(LEFT_COLOR_SENSOR_PORT));
     * SmartDashboard.putNumber("RightColorSensor",
     * mux.getGreyscale(RIGHT_COLOR_SENSOR_PORT));
     * SmartDashboard.putNumber("LeftProxSensor",
     * mux.getGreyscale(LEFT_PROX_SENSOR_PORT));
     * SmartDashboard.putNumber("RightProxSensor",
     * mux.getGreyscale(RIGHT_PROX_SENSOR_PORT));
     */

    mechanismControl.baseControl();
    // cargoIntake.baseControl();
    // lift.totalLiftControl();
    // System.out.println("Passed Mechanism control on loop" + counter);
    chassis.baseDrive();
    // System.out.println("Passed Chassis control on loop" + counter);
    if (lift.isRaised()) {
      chassis.setSpeedMod2(.5);
    } else {
      chassis.setSpeedMod2(1);
    }
    // System.out.println("Finished loop" + counter);
    // chassis.baseDrive();
    if (chassisJoystick.getYButtonPressed()) {
      if (frontPistonFlag) {
        liftPistonFront.set(DoubleSolenoid.Value.kReverse);

      } else {
        liftPistonFront.set(DoubleSolenoid.Value.kForward);
      }

      frontPistonFlag = !frontPistonFlag;
    }

    if (chassisJoystick.getAButtonPressed()) {
      if (backPistonFlag) {
        liftPistonBack.set(DoubleSolenoid.Value.kReverse);
      } else {
        liftPistonBack.set(DoubleSolenoid.Value.kForward);
      }
      backPistonFlag = !backPistonFlag;
    }
    SmartDashboard.putString("Front Lift Piston", liftPistonFront.get().toString());
    SmartDashboard.putString("Back Lift Piston", liftPistonBack.get().toString());
  }
  // frontRight.set(1);
  //// System.out.println(frontRight.get());

  // private LineFollower follower;

  @Override
  public void testInit() {
    // lighting.test();
    mux = new MultiplexColorSensor(ports, I2C.Port.kMXP);
    // follower = new LineFollower(chassis, mux, ports);
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {

    // SmartDashboard.putNumber("LeftColorSensor",
    // mux.getGreyscale(LEFT_COLOR_SENSOR_PORT));
    // SmartDashboard.putNumber("RightColorSensor",
    // mux.getGreyscale(RIGHT_COLOR_SENSOR_PORT));
    // SmartDashboard.putNumber("LeftProxSensor",
    // mux.getGreyscale(LEFT_PROX_SENSOR_PORT));
    // SmartDashboard.putNumber("RightProxSensor",
    // mux.getGreyscale(RIGHT_PROX_SENSOR_PORT));

    /*
     * if(mechanismController.getXButtonPressed()) {
     * follower.moveForwardUntilLine(); } else
     * if(mechanismController.getYButtonPressed()) { follower.followLine(); } else
     * if(mechanismController.getBButtonPressed()) { //follower.straighten(); } else
     * if(mechanismController.getAButtonPressed()) {
     * lift.setCurrentPosition(lift.getCurrentPosition() + 1); }
     */

    if (mechanismController.getBackButtonPressed()) {
      liftPistonFront.set(DoubleSolenoid.Value.kForward);
      liftPistonBack.set(DoubleSolenoid.Value.kForward);
    } else if (mechanismController.getStartButtonPressed()) {
      liftPistonFront.set(DoubleSolenoid.Value.kReverse);
      liftPistonBack.set(DoubleSolenoid.Value.kReverse);
    }

    // chassis.baseDrive();

    if (chassisJoystick.getYButtonPressed()) {
      if (frontPistonFlag) {
        liftPistonFront.set(DoubleSolenoid.Value.kReverse);

      } else {
        liftPistonFront.set(DoubleSolenoid.Value.kForward);
      }
      frontPistonFlag = !frontPistonFlag;
    } else if (chassisJoystick.getAButtonPressed()) {
      if (backPistonFlag) {
        liftPistonBack.set(DoubleSolenoid.Value.kReverse);
      } else {
        liftPistonBack.set(DoubleSolenoid.Value.kForward);
      }
      backPistonFlag = !backPistonFlag;
    }
    // chassis.baseDrive();
  }

  @Override
  public void disabledInit() {
    // lighting.disabled();
  }
}
