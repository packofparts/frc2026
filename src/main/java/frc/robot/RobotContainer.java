// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants.Climb.CLIMB_SETPOINT;
import frc.robot.subsystems.Climb;
import frc.robot.subsystems.Flywheel;
import frc.robot.subsystems.Swerve;
import frc.robot.subsystems.Turret;
import poplib.controllers.io.XboxIO;
import poplib.swerve.commands.TeleopSwerveDrive;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Pivot;


public class RobotContainer {

    Flywheel flywheel = Flywheel.getInstance();
    Swerve swerve = Swerve.getInstance();
    XboxIO controller = XboxIO.getInstance();
    Indexer indexer = Indexer.getInstance();
    Turret turret = Turret.getInstance();
    Pivot pivot = Pivot.getInstance();
    Climb climb = Climb.getInstance();

    public RobotContainer() {
        // Configure the trigger bindings
        swerve.setDefaultCommand(new TeleopSwerveDrive(swerve, controller));
        configureBindings();
    }

    private void configureBindings() {
        controller.getOperatorTrigger(XboxController.Axis.kRightTrigger.value).onTrue(flywheel.updateSetpointCommand(1000)).onFalse(flywheel.updateSetpointCommand(0));
        controller.getOperatorTrigger(XboxController.Axis.kLeftTrigger.value).onTrue(flywheel.updateSetpointCommand(-1000)).onFalse(flywheel.updateSetpointCommand(0));

        controller.getOperatorTrigger(XboxController.Axis.kLeftX.value).onTrue(pivot.moveWristBy(controller.getRawAxis(XboxController.Axis.kLeftX.value, controller.getOperatorController()), 0.1));
        controller.getOperatorTrigger(XboxController.Axis.kRightY.value).onTrue(turret.turnTurretBy(controller.getRawAxis(XboxController.Axis.kRightY.value, controller.getOperatorController()), 0.1));

        controller.getOperatorButton(XboxController.Button.kX.value).onTrue(indexer.runIndexer()).onFalse(indexer.stopIndexer());
        controller.getOperatorButton(XboxController.Button.kB.value).onTrue(indexer.reverseIndexer()).onFalse(indexer.stopIndexer());    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        // An example command will be run in autonomous
        return null;
    }

    public Command L1() {
        return climb.moveCenter(CLIMB_SETPOINT.L1)
        .alongWith(climb.moveOuter(CLIMB_SETPOINT.L1))
        .andThen(climb.moveCenter(CLIMB_SETPOINT.IDLE))
        .alongWith(climb.moveOuter(CLIMB_SETPOINT.IDLE))
        .andThen(new WaitCommand(0.5))
        .andThen(climb.moveCenter(CLIMB_SETPOINT.L1))
        .alongWith(climb.moveOuter(CLIMB_SETPOINT.L1))
        .andThen(climb.moveCenter(CLIMB_SETPOINT.IDLE))
        .alongWith(climb.moveOuter(CLIMB_SETPOINT.IDLE));
    }

    public Command L3() {
        return climb.moveCenter(CLIMB_SETPOINT.L1)
        .alongWith(climb.moveOuter(CLIMB_SETPOINT.L1))
        .andThen(climb.moveCenter(CLIMB_SETPOINT.IDLE))
        .alongWith(climb.moveOuter(CLIMB_SETPOINT.IDLE))
        .andThen(climb.moveCenter(CLIMB_SETPOINT.L2))
        .andThen(climb.moveCenter(CLIMB_SETPOINT.IDLE))
        .andThen(climb.moveOuter(CLIMB_SETPOINT.L3))
        .andThen(climb.moveOuter(CLIMB_SETPOINT.IDLE));
    }
}
