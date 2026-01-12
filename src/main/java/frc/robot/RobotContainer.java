// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Swerve;
import poplib.controllers.io.XboxIO;
import poplib.swerve.commands.TeleopSwerveDrive;

public class RobotContainer {

    Swerve swerve = Swerve.getInstance();
    XboxIO controller = XboxIO.getInstance();

    public RobotContainer() {
        // Configure the trigger bindings
        swerve.setDefaultCommand(new TeleopSwerveDrive(swerve, controller));
        configureBindings();
    }

    private void configureBindings() {

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
