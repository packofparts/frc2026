// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.controls.PositionDutyCycle;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.Climb.CLIMB_SETPOINT;
import poplib.smart_dashboard.TunableNumber;

public class Climb extends SubsystemBase {
  private TalonFX leadCenterMotor;
  @SuppressWarnings("unused")
  private TalonFX followerCenterMotor;
  private TalonFX leadOuterMotor;
  @SuppressWarnings("unused")
  private TalonFX followerOuterMotor;
  private static Climb instance;
  private TunableNumber centerSetpoint;
  private TunableNumber outerSetpoint;
  private double centerError;
  private double outerError;
  private final PositionDutyCycle position;

   public static Climb getInstance() {
    if (instance == null) {
            instance = new Climb();
        }

        return instance;
    }

  public Climb() {
    leadCenterMotor = Constants.Climb.leadCenterConfig.createTalon();
    followerCenterMotor = Constants.Climb.followerCenterConfig.createTalon();
    leadOuterMotor = Constants.Climb.leadOuterConfig.createTalon();
    followerOuterMotor = Constants.Climb.followerOuterConfig.createTalon();
    position = new PositionDutyCycle(0.0).
    withSlot(leadCenterMotor.getClosedLoopSlot().getValue());
    centerSetpoint = new TunableNumber("centerSPClimb", 0, false);
    outerSetpoint = new TunableNumber("outerSPClimb", 0, false);
  }

  public Command setCenterSetpoint(CLIMB_SETPOINT setpoint) {
    return runOnce(() -> {this.centerSetpoint.setDefault(setpoint.getHi());});
  }

  public Command setOuterSetpoint(CLIMB_SETPOINT setpoint) {
    return runOnce(() -> {this.outerSetpoint.setDefault(setpoint.getHi());});
  }

  public Command getCenterError(double setpoint) {
    return runOnce(() -> {centerError = Math.abs(setpoint - leadCenterMotor.getPosition().getValueAsDouble());});
  }

  public Command getOuterError(double setpoint) {
    return runOnce(() -> {outerError = Math.abs(setpoint - leadOuterMotor.getPosition().getValueAsDouble());});

  }

  public boolean centerAtSetpoint() {
    getCenterError(centerSetpoint.get());
    return (centerError < 0.1) ? true : false;
  }

  public boolean outerAtSetpoint() {
    getOuterError(outerSetpoint.get());
    return (outerError < 0.1) ? true : false;
  }

  public Command extendCenter() {
    return runOnce(() -> centerSetpoint.setDefault(centerSetpoint.get() + 1));
  }

  public Command unextendCenter() {
    return runOnce(() -> centerSetpoint.setDefault(centerSetpoint.get() - 1));
  }

  public Command extendOuter() {
    return runOnce(() -> outerSetpoint.setDefault(outerSetpoint.get() + 1));
  }

  public Command unextendOuter() {
    return runOnce(() -> outerSetpoint.setDefault(outerSetpoint.get() - 1));
  }

  public Command moveCenter(CLIMB_SETPOINT setpoint) {
    return runOnce(() -> 
      setCenterSetpoint(setpoint))
      .until(instance::centerAtSetpoint);
  }

  public Command moveOuter(CLIMB_SETPOINT setpoint) {
    return runOnce(() -> 
      setOuterSetpoint(setpoint))
      .until(instance::outerAtSetpoint);
  }

  @Override
  public void periodic() {
    leadCenterMotor.setControl(position.withPosition(centerSetpoint.get()));
    leadOuterMotor.setControl(position.withPosition(outerSetpoint.get()));
  }
  
}