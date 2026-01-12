// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Swerve;
import poplib.controllers.io.XboxIO;
import poplib.swerve.commands.TeleopSwerveDrive;

public class RobotContainer {

    // TODO: create the Flywheel object
    Swerve swerve = Swerve.getInstance();
    XboxIO controller = XboxIO.getInstance();
    // TODO: create the Pivot object

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

        /**
         * TODO: button bind the Pivot like so:
         * Move up by 1: Bind to Y Button on Driver Controller
         * Move down by 1: Bind to A Button on Driver Controller
         * Hint #2: start with controller.getDriverButton(XboxController.Button.kY)
         */
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
