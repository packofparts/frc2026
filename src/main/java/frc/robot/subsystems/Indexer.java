// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Indexer extends SubsystemBase {
    private static Indexer instance;
    private final TalonFX spindexerMotor;
    private final TalonFX handoffMotor;
    private final DigitalInput beamBreak;

    public static Indexer getInstance() {
        if (instance == null) {
            instance = new Indexer();
        }

        return instance;
    }

    private Indexer() {
        spindexerMotor = Constants.Indexer.MOTOR_CONFIG.createTalon();
        handoffMotor = Constants.Indexer.HANDOFF_CONFIG.createTalon();
        beamBreak = new DigitalInput(Constants.Indexer.BEAMBREAK_ID);
    }

    public Command runSpindexer() {
        return runOnce(() -> spindexerMotor.set(Constants.Indexer.SPEED));
    }

    public Command stopSpindexer() {
        return runOnce(() -> spindexerMotor.set(0.0));
    }

    public Command reverseSpindexer() {
        return runOnce(() -> spindexerMotor.set(-Constants.Indexer.SPEED));
    }

    public Command runHandoff() {
        return runOnce(() -> handoffMotor.set(Constants.Indexer.SPEED));
    }

    public Command stopHandoff() {
        return runOnce(() -> handoffMotor.set(0.0));
    }

    public Command reverseHandoff() {
        return runOnce(() -> handoffMotor.set(-Constants.Indexer.SPEED));
    }

    public boolean ballInHandoff() {
        return !beamBreak.get();
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Spindexer Speed", spindexerMotor.get());
        SmartDashboard.putNumber("Handoff Speed", handoffMotor.get());
        SmartDashboard.putNumber("Handoff Voltage", handoffMotor.getMotorVoltage().getValueAsDouble());
        SmartDashboard.putBoolean("Ball in Handoff", !beamBreak.get());
    }
}