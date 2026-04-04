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
        super.leadMotor.setPosition(0);
    }

    public Command dropDozer() {
        return moveWrist(Constants.Intake.DOWN, 0.5);
    }

    public Command upDozer() {
        return moveWrist(0, 0.5);
    }


    public Command runIntakeAuto() {
        return moveWrist(Constants.Intake.DOWN, 10).andThen(runOnce(() -> leadSpinMotor.set(Constants.Intake.SPEED)));
    }

    // Do as i say, not as i do
    public double getIntakeMotorState() {
        return leadSpinMotor.get();
    }

    /**
     * Runs the intake :shock:
     * @return the Command that runs the intake
     */
    public Command runIntake() {
        return moveWrist(Constants.Intake.DOWN, 10).andThen(runOnce(() -> leadSpinMotor.set(Constants.Intake.SPEED)));
    }

    public Command upIntake() {
        return moveWrist(8*360, 10);
    }

    public Command downIntake() {
        return moveWrist(Constants.Intake.DOWN, 10);
    }

    public Command runSpin() {
        return runOnce(() -> leadSpinMotor.set(Constants.Intake.SPEED));
    }

    public Command revSpin() {
        return runOnce(() -> leadSpinMotor.set(-Constants.Intake.SPEED));
    }

    /**
     * Stops the indexer
     * @return the Command that stops the indexer
     */
    public Command stopIntake() {
        return moveWrist(0, 10).andThen(runOnce(() -> leadSpinMotor.set(0)));
    }

    /**
     * Runs the indexer on reverse
     * @return the Command that runs the indexer on reverse
     */
    public Command reverseIntake() {
        return moveWrist(Constants.Intake.DOWN, 10).andThen(runOnce(() -> leadSpinMotor.set(-Constants.Intake.SPEED)));
    }
    @Override
    public void periodic() {
        super.periodic();
        SmartDashboard.putNumber("Intake Motor Speed", leadSpinMotor.get());
    }
}