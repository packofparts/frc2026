// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Indexer extends SubsystemBase {
    private static Indexer instance;
    private final SparkMax motor;

    public static Indexer getInstance() {
        if (instance == null) {
            instance = new Indexer();
        }

        return instance;
    }

    private Indexer() {
        motor = Constants.Indexer.MOTOR_CONFIG.createSparkMax();
    }

    /**
     * Runs the indexer :shock:
     * @return the Command that runs the indexer
     */
    public Command runIndexer() {
        return run(() -> motor.set(Constants.Indexer.SPEED));
    }

    /**
     * Stops the indexer
     * @return the Command that stops the indexer
     */
    public Command stopIndexer() {
        return run(() -> motor.set(0.0));
    }

    /**
     * Runs the indexer on reverse
     * @return the Command that runs the indexer on reverse
     */
    public Command reverseIndexer() {
        return run(() -> motor.set(-Constants.Indexer.SPEED));
    }

    @Override
    public void periodic() {
        
    }
}