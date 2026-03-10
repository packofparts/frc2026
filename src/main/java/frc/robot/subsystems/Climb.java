// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.Constants.Climb.CLIMB_SETPOINT;
import poplib.control.FFConfig;
import poplib.subsystems.pivot.TalonPivot;

public class Climb extends TalonPivot {
  private static Climb instance;

   public static Climb getInstance() {
    if (instance == null) {
            instance = new Climb();
        }

        return instance;
    }

  public Climb() {
    super(Constants.Climb.MOTOR_CONFIG, 
    6, 
    1, 
    new FFConfig(0), 
    Constants.Climb.TUNING_MODE, 
    "Climb");
  }

  public Command setSetpoint(CLIMB_SETPOINT climbSetpointGiven) {
    return runOnce(() -> {this.setpoint.setDefault(climbSetpointGiven.getSetpoint());});
  }


  public Command extendClimb() {
    return moveWrist(Constants.Climb.CLIMB_SETPOINT.L1.getSetpoint(), 0.1);
  }

  public Command unextendClimb() {
    return moveWrist(Constants.Climb.CLIMB_SETPOINT.IDLE.getSetpoint(), 0.1);
  }

  @Override
  public void periodic() {
    super.periodic();
  }
}
