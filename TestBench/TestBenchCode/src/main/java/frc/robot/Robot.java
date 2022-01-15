// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  WPI_TalonSRX brushed1 = new WPI_TalonSRX(1);
  WPI_VictorSPX brushed2 = new WPI_VictorSPX(2);
  WPI_VictorSPX brushed3 = new WPI_VictorSPX(3);
  WPI_VictorSPX brushed4 = new WPI_VictorSPX(4);

  TalonFX falcon1 = new TalonFX(5);
  TalonFX falcon2 = new TalonFX(6);
  TalonFX falcon3 = new TalonFX(7);
  TalonFX falcon4 = new TalonFX(8);

  TalonFX left1 = new TalonFX(9);
  TalonFX left2 = new TalonFX(10);
  TalonFX right1 = new TalonFX(11);
  TalonFX right2 = new TalonFX(12);

  Joystick stick = new Joystick(0);

  Compressor comp1 = new Compressor(50, PneumaticsModuleType.CTREPCM);

  DigitalInput compressorSwitch = new DigitalInput(0);


  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {}

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /** This function is called periodically during autonomous. */
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

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
    SmartDashboard.putNumber("Target RPM", (6380*2));

  }

  /** This function is called periodically during operator control. */
  int iteration = 1;
  double power = 0;
  double steering = 0;
  double left = power + steering;
  double right = power - steering;

  @Override
  public void teleopPeriodic() {
    if(!(compressorSwitch.get())) {
      comp1.enableDigital();;
    } else {
      comp1.disable();;
    }
    //System.out.println("Throttle = " + stick.getThrottle());
    System.out.println("RPM = " + (falcon1.getSelectedSensorVelocity()/2048*1200));
    SmartDashboard.putNumber("Falcon 1 RPM", falcon1.getSelectedSensorVelocity()/2048*1200);
    SmartDashboard.putNumber("Falcon 2 RPM", falcon2.getSelectedSensorVelocity()/2048*1200);
    SmartDashboard.putNumber("Falcon 3 RPM", falcon3.getSelectedSensorVelocity()/2048*1200);
    SmartDashboard.putNumber("Falcon 4 RPM", falcon4.getSelectedSensorVelocity()/2048*1200);

    left = power + steering;
    right = power - steering;
    iteration = 1;
    for(iteration = 1; iteration<=8; iteration++){
      if(stick.getRawButton(iteration)) {
        switch(iteration) {
          case 1:
            brushed1.set(ControlMode.PercentOutput, -(stick.getThrottle()));
          break;
          case 2:
            brushed2.set(ControlMode.PercentOutput, -(stick.getThrottle()));
          break;
          case 3:
            brushed3.set(ControlMode.PercentOutput, -(stick.getThrottle()));
          break;
          case 4:
            brushed4.set(ControlMode.PercentOutput, -(stick.getThrottle()));
          break;
          case 5:
            falcon1.set(ControlMode.PercentOutput, -SmartDashboard.getNumber("Target RPM", (6380*2))/(6380*2));
          break;
          case 6:
            falcon2.set(ControlMode.PercentOutput, -SmartDashboard.getNumber("Target RPM", (6380*2))/(6380*2));
          break;
          case 7:
            falcon3.set(ControlMode.PercentOutput, -(stick.getThrottle()));
          break;
          case 8:
            falcon4.set(ControlMode.PercentOutput, -(stick.getThrottle()));
          break;
        }
      } else {
        switch(iteration) {
          case 1:
            brushed1.set(ControlMode.PercentOutput, 0);
          break;
          case 2:
            brushed2.set(ControlMode.PercentOutput, 0);
          break;
          case 3:
            brushed3.set(ControlMode.PercentOutput, 0);
          break;
          case 4:
            brushed4.set(ControlMode.PercentOutput, 0);
          break;
          case 5:
            falcon1.set(ControlMode.PercentOutput, 0);
          break;
          case 6:
            falcon2.set(ControlMode.PercentOutput, 0);
          break;
          case 7:
            falcon3.set(ControlMode.PercentOutput, 0);
          break;
          case 8:
            falcon4.set(ControlMode.PercentOutput, 0);
          break;
        }
      }
    }

    power = stick.getY();
    steering = stick.getX();
    left1.set(ControlMode.PercentOutput, left);
    left2.set(ControlMode.PercentOutput, left);
    right1.set(ControlMode.PercentOutput, right);
    right2.set(ControlMode.PercentOutput, right);

  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}
