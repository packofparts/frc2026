// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import poplib.subsystems.pivot.TalonPivot;

public class Intake extends TalonPivot {
    private static Intake instance;
    private final TalonFX leadSpinMotor;
    @SuppressWarnings("unused")
    private final TalonFX followerMotor;
    @SuppressWarnings("unused")
    private final TalonFX intakeGuideFollower;
    public static Intake getInstance() {
        if (instance == null) {
            instance = new Intake();
        }

        return instance;
    }

    private Intake() {
        super(
            Constants.Intake.PIVOT_CONFIG, 
            7, 
            1, 
            Constants.Intake.FF, 
            Constants.Intake.TUNING_MODE, 
            "Intake"
        );
        leadSpinMotor = Constants.Intake.MOTOR_CONFIG.createTalon();
        followerMotor = Constants.Intake.FOLLOWER_CONFIG.createTalon();
        intakeGuideFollower = Constants.Intake.INTAKE_GUIDE_MOTOR.createTalon();
        super.leadMotor.setPosition(0);
    }

    /**
     * Runs the indexer :shock:
     * @return the Command that runs the indexer
     */
    public Command runIntake() {
        return runOnce(() -> leadSpinMotor.set(Constants.Intake.SPEED)).andThen(() -> intakeGuideFollower.set(Constants.Intake.SPEED)).andThen(moveWrist(Constants.Intake.DOWN, 0.5));
    }

    /**
     * Stops the indexer
     * @return the Command that stops the indexer
     */
    public Command stopIntake() {
        return runOnce(() -> leadSpinMotor.set(0)).andThen(() -> intakeGuideFollower.set(0)).andThen(moveWrist(0, 0.5));
    }

    /**
     * Runs the indexer on reverse
     * @return the Command that runs the indexer on reverse
     */
    public Command reverseIntake() {
        return runOnce(() -> leadSpinMotor.set(-Constants.Intake.SPEED)).andThen(() -> intakeGuideFollower.set(-Constants.Intake.SPEED)).andThen(moveWrist(Constants.Intake.DOWN, 0.5));
    }

    @Override
    public void periodic() {
        super.periodic();
        SmartDashboard.putNumber("Intake Motor Speed", leadSpinMotor.get());
    }
}