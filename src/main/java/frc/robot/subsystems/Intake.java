// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Intake extends SubsystemBase {
    private static Intake instance;
    private final SparkMax motor;
    private final DigitalInput beamBreak;

    public static Intake getInstance() {
        if (instance == null) {
            instance = new Intake();
        }

        return instance;
    }

    private Intake() {
        motor = Constants.Intake.MOTOR_CONFIG.createSparkMax();
        beamBreak = new DigitalInput(Constants.Intake.BEAMBRAKE_ID);
    }

    /**
     * Runs the indexer :shock:
     * @return the Command that runs the indexer
     */
    public Command runIntake() {
        return run(() -> motor.set(Constants.Intake.SPEED));
    }

    /**
     * Stops the indexer
     * @return the Command that stops the indexer
     */
    public Command stopIntake() {
        return run(() -> motor.set(0.0));
    }

    /**
     * Runs the indexer on reverse
     * @return the Command that runs the indexer on reverse
     */
    public Command reverseIntake() {
        return run(() -> motor.set(-Constants.Intake.SPEED));
    }

    public boolean beamBreakTriggered() {
        return !beamBreak.get();
    }

    @Override
    public void periodic() {
        super.periodic();
        SmartDashboard.putNumber("Motor Speed", motor.get());
        SmartDashboard.putBoolean("Intake Beam Break Triggered", beamBreakTriggered());
    }
}