// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
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
    // TODO: create the Pivot object
    Pivot pivot = Pivot.getInstance();
    public RobotContainer() {
        // Configure the trigger bindings
        swerve.setDefaultCommand(new TeleopSwerveDrive(swerve, controller));
        configureBindings();
    }

    private void configureBindings() {
        /**
         * TODO: button bind the Flywheel like so:
         * Run at 1000 RPM: Bind to Right Trigger on Driver Controller
         * Run at -1000 RPM: Bind to Left Trigger on Driver Controller
         * Releasing the Trigger should stop the flywheel (0 RPM) Hint: use .onFalse(Command)
         * Hint #2: start with controller.getDriverTrigger(XboxController.Axis.kRightTrigger)
         */
        controller.getDriverTrigger(XboxController.Axis.kRightTrigger.value).onTrue(flywheel.updateSetpointCommand(1000)).onFalse(flywheel.updateSetpointCommand(0));
        controller.getDriverTrigger(XboxController.Axis.kLeftTrigger.value).onTrue(flywheel.updateSetpointCommand(-1000)).onFalse(flywheel.updateSetpointCommand(0));
    



        /**
         * TODO: button bind the Pivot like so:
         * Move up by 1: Bind to Y Button on Driver Controller
         * Move down by 1: Bind to A Button on Driver Controller
         * Hint #2: start with controller.getDriverButton(XboxController.Button.kY)
         */
        controller.getDriverButton(XboxController.Button.kY.value).onTrue(Pivot.getInstance().moveWristBy(1, 0.5));
        controller.getDriverButton(XboxController.Button.kA.value).onTrue(Pivot.getInstance().moveWristBy(-1, 0.5));

        controller.getOperatorButton(XboxController.Button.kA.value).onTrue(indexer.runIndexer()).onFalse(indexer.stopIndexer());
        controller.getOperatorButton(XboxController.Button.kB.value).onTrue(indexer.reverseIndexer()).onFalse(indexer.stopIndexer());

        controller.getDriverButton(XboxController.Button.kLeftBumper.value).onTrue(turret.moveWristBy(-100,5)).onFalse(turret.moveWristBy(0, 5));
        controller.getDriverButton(XboxController.Button.kRightBumper.value).onTrue(turret.moveWristBy(100,5)).onFalse(turret.moveWristBy(0, 5));
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        // An example command will be run in autonomous
        return null;
    }
}
