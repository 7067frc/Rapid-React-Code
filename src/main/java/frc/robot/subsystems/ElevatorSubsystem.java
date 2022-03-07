// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.cClimber;

public class ElevatorSubsystem extends SubsystemBase {
  WPI_TalonFX elevatorMotorM = new WPI_TalonFX(cClimber.climberMasterM);
  WPI_TalonFX elevatorMotorF = new WPI_TalonFX(cClimber.climberFollowM);
  boolean reseted = false;
  DigitalInput magneticSwitch = new DigitalInput(cClimber.magneticSwitch);

  /** Creates a new ClimbSubsystem. */
  public ElevatorSubsystem() {
    elevatorMotorF.configFactoryDefault();
    elevatorMotorM.configFactoryDefault();

    setBrake();
    elevatorMotorF.follow(elevatorMotorM);
    elevatorMotorM.setSelectedSensorPosition(0);
    elevatorMotorM.configForwardSoftLimitThreshold(132262);
    elevatorMotorM.configReverseSoftLimitThreshold(00);
    elevatorMotorM.configForwardSoftLimitEnable(true);
    elevatorMotorM.configReverseSoftLimitEnable(true);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    if (isElevatorDown())
      elevatorMotorM.setSelectedSensorPosition(0);
    SmartDashboard.putNumber("elevator", elevatorMotorM.getSelectedSensorPosition());
    if (!reseted)
      reseted = isElevatorDown();
  }

  public void elevatorUp() {
    if (reseted)
      elevatorMotorM.set(0.3);
    SmartDashboard.putBoolean("elevator stop", false);
  }

  public void elevatorDown() {
    if (reseted)
      elevatorMotorM.set(-0.3);
  }

  public void stop() {
    elevatorMotorM.set(0);
    SmartDashboard.putBoolean("elevator stop", true);
  }

  public void setBrake() {
    elevatorMotorM.setNeutralMode(NeutralMode.Brake);
    elevatorMotorF.setNeutralMode(NeutralMode.Brake);
  }

  public void setCoast() {
    elevatorMotorM.setNeutralMode(NeutralMode.Coast);
    elevatorMotorF.setNeutralMode(NeutralMode.Coast);
  }

  public boolean isElevatorDown() {
    return !magneticSwitch.get();
  }

  public boolean isElevatorUp() {
    return elevatorMotorM.getSelectedSensorPosition() >= 132262;
  }
}