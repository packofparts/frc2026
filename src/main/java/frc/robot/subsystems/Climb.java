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
  private TalonFX climbMotor;
  private static Climb instance;
  private TunableNumber setpoint;
  private final PositionDutyCycle position;

   public static Climb getInstance() {
    if (instance == null) {
            instance = new Climb();
        }

        return instance;
    }

  public Climb() {
    climbMotor = Constants.Climb.MOTOR_CONFIG.createTalon();
    position = new PositionDutyCycle(0.0).
    withSlot(climbMotor.getClosedLoopSlot().getValue());
    setpoint = new TunableNumber("climbSP", 0, false);
  }

  public Command setSetpoint(CLIMB_SETPOINT climbSetpointGiven) {
    return runOnce(() -> {this.setpoint.setDefault(climbSetpointGiven.getHi());});
  }

  public boolean centerAtSetpoint() {
    return (Math.abs(setpoint.get() - climbMotor.getPosition().getValueAsDouble()) < 0.1);
  }

  public Command extendClimb() {
    return runOnce(() -> setpoint.setDefault(setpoint.get() + 1));
  }

  public Command unextendClimb() {
    return runOnce(() -> setpoint.setDefault(setpoint.get() - 1));
  }

  public Command climbTo(CLIMB_SETPOINT setpoint) {
    return runOnce(() -> 
      setSetpoint(setpoint))
      .until(this::centerAtSetpoint);
  }

  @Override
  public void periodic() {
    climbMotor.setControl(position.withPosition(setpoint.get()));
  }
}
