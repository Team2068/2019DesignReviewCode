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
public class DriveTrain
{
  //fields
  private CANSparkMax frontRight, frontLeft, backLeft, backRight;
  private I2CColorSensor rightColor, leftColor, rightDistance, leftDistance;
  private XboxController controller;
  private VirtualCANEncoder leftEncoder, rightEncoder;
  private double speedMod = 1;
  private double speedMod2 = 1;
  private double speedMod3 = .5;
  private boolean POVPressed = false;

  public DriveTrain(CANSparkMax frontRight, CANSparkMax backRight, CANSparkMax frontLeft, CANSparkMax backLeft , XboxController controller)
  {
    this.frontRight = frontRight;
    this.frontLeft = frontLeft;
    this.backLeft = backLeft;
    this.backRight =backRight;
    this.rightColor = rightColor;
    this.leftColor = leftColor;
    this.rightDistance = rightDistance;
    this.leftDistance = leftDistance;
    this.controller = controller;
    //frontRight.setInverted(true);
    //backRight.setInverted(true);
    leftEncoder = new VirtualCANEncoder(frontLeft);
    rightEncoder = new VirtualCANEncoder(frontRight);
    //backRight.follow(frontRight);
    //backLeft.follow(frontLeft);
    frontRight.setMotorType(MotorType.kBrushless);
    frontLeft.setMotorType(MotorType.kBrushless);
    backRight.setMotorType(MotorType.kBrushless);
    backLeft.setMotorType(MotorType.kBrushless);

  }
  public void baseDrive()
  {
      if(controller.getBumperPressed(GenericHID.Hand.kRight) && controller.getBumperPressed(GenericHID.Hand.kLeft))
      {
          speedMod3 = 1;
      }
      else
      {
         speedMod3 = .5;    
      }
      if(Math.abs(controller.getY(Hand.kLeft)) > .2 || Math.abs(controller.getY(Hand.kRight)) > .2)
      {
      frontLeft.set(-controller.getY(GenericHID.Hand.kLeft) * speedMod * speedMod2 * speedMod3);
      backLeft.set(-controller.getY(GenericHID.Hand.kLeft) * speedMod * speedMod2 * speedMod3);
      frontRight.set(-controller.getY(GenericHID.Hand.kRight) * speedMod * speedMod2 * speedMod3);
      backRight.set(-controller.getY(GenericHID.Hand.kRight) * speedMod * speedMod2 * speedMod3);
      } 
      else
      {
          frontLeft.set(0);
          backLeft.set(0);
          frontRight.set(0);
          backRight.set(0);
      }
      //System.out.println("Front Right: " + frontRight.get());
      //System.out.println("Back Right: " + backRight.get());
      //System.out.println("Front Left: " + frontLeft.get());
      //System.out.println("BackLeft: " + backLeft.get());
      speedModAdjust();
      brakeSystemAdjust();

  }
  public void brakeSystemAdjust()
  {
      if(controller.getAButtonPressed())
      {
          frontLeft.setIdleMode(IdleMode.kCoast);
          frontRight.setIdleMode(IdleMode.kCoast);
          backLeft.setIdleMode(IdleMode.kCoast);
          backRight.setIdleMode(IdleMode.kCoast);
      }
      else if(controller.getBButtonPressed())
      {
          frontLeft.setIdleMode(IdleMode.kBrake);
          frontRight.setIdleMode(IdleMode.kBrake);
          backLeft.setIdleMode(IdleMode.kBrake);
          backRight.setIdleMode(IdleMode.kBrake);
      }
  }
  public void speedModAdjust()
  {
      if(!POVPressed && controller.getPOV(0) > -1)
      {
        if(controller.getPOV(0) == 0 && speedMod < .99)
        {
            speedMod+= .1;
            POVPressed = true;
        }
        else if(controller.getPOV(0) == 180 && speedMod > .01)
        {
            speedMod-= .1;
            POVPressed = true;
        }
        
      }
      else if(POVPressed && controller.getPOV(0) == -1)
      {
          POVPressed = false;
      }
      //System.out.println(speedMod);

  }
  public void setSpeedMod2(double speedMod)
  {
        this.speedMod2 = speedMod;
  }
  public void resetEncoders()
  {
      rightEncoder.reset();
      leftEncoder.reset();
  }
  public void autoDrive(double rightSpeed, double leftSpeed, double targetRevsLeft, double targetRevsRight)
  {
      resetEncoders();
      while(leftEncoder.getPosition() < targetRevsLeft && rightEncoder.getPosition() < targetRevsRight)
      {
          if(leftEncoder.getPosition() < targetRevsLeft)
          {
              frontLeft.set(leftSpeed);
          }
          if(rightEncoder.getPosition() < targetRevsRight)
          {
              frontRight.set(rightSpeed);
          }
      }
      frontLeft.set(0);
      frontRight.set(0);
  }
  public void displayValues()
  {
      SmartDashboard.putNumber("Front Right", frontRight.get());
      SmartDashboard.putNumber("Back Right", backRight.get());
      SmartDashboard.putNumber("Front Left", frontLeft.get());
      SmartDashboard.putNumber("Back Left", backLeft.get());
      SmartDashboard.putNumber("Speed Mod 1", speedMod);
      SmartDashboard.putNumber("Speed Mod 2", speedMod2);
      SmartDashboard.putNumber("Speed Mod 3", speedMod3);
     
  }
}