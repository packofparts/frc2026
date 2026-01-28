// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Flywheel;
import frc.robot.subsystems.Swerve;
import frc.robot.subsystems.Turret;
import poplib.controllers.io.XboxIO;
import poplib.swerve.commands.TeleopSwerveDrive;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Pivot;
import frc.robot.subsystems.Intake;



public class RobotContainer {

    Flywheel flywheel = Flywheel.getInstance();
    Swerve swerve = Swerve.getInstance();
    XboxIO controller = XboxIO.getInstance();
    Indexer indexer = Indexer.getInstance();
    Turret turret = Turret.getInstance();
    Pivot pivot = Pivot.getInstance();
    Intake intake = Intake.getInstance();


    public RobotContainer() {
        // Configure the trigger bindings
        swerve.setDefaultCommand(new TeleopSwerveDrive(swerve, controller));
        NamedCommands.registerCommand("Climb L1", ClimbL1());
        NamedCommands.registerCommand("Climb L2", ClimbL2());
        NamedCommands.registerCommand("Climb L3", ClimbL3());
        NamedCommands.registerCommand("Shoot Fuel", ShootFuel());
        NamedCommands.registerCommand("Collect Fuel", CollectFuel());
        configureBindings();
    }

    private void configureBindings() {
        controller.getOperatorTrigger(XboxController.Axis.kRightTrigger.value).onTrue(flywheel.updateSetpointCommand(1000)).onFalse(flywheel.updateSetpointCommand(0));
        controller.getOperatorTrigger(XboxController.Axis.kLeftTrigger.value).onTrue(flywheel.updateSetpointCommand(-1000)).onFalse(flywheel.updateSetpointCommand(0));

        controller.getOperatorTrigger(XboxController.Axis.kLeftX.value).onTrue(pivot.moveWristBy(-controller.getRawAxis(XboxController.Axis.kLeftX.value, controller.getOperatorController()), 0.1));
        controller.getOperatorTrigger(XboxController.Axis.kRightY.value).onTrue(turret.turnTurretBy(controller.getRawAxis(XboxController.Axis.kRightY.value, controller.getOperatorController()), 0.1));

        controller.getOperatorButton(XboxController.Button.kX.value).onTrue(indexer.runIndexer()).onFalse(indexer.stopIndexer());
        controller.getOperatorButton(XboxController.Button.kB.value).onTrue(indexer.reverseIndexer()).onFalse(indexer.stopIndexer());    

        //controller.getOperatorButton(XboxController.Button.k.value).onTrue(intake.runIntake()).onFalse(intake.stopIntake());
        //controller.getOperatorButton(XboxController.Button.k.value).onTrue(intake.reverseIntake()).onFalse(intake.stopIntake());    
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
    public Command ClimbL1() {
        // An example command will be run in autonomous
        return null;
    }
    public Command ClimbL2() {
        // An example command will be run in autonomous
        return null;
    }
    public Command ClimbL3() {
        // An example command will be run in autonomous
        return null;
    }
    public Command ShootFuel() {
        // An example command will be run in autonomous
        return null;
    }
    public Command CollectFuel() {
        // An example command will be run in autonomous
        return null;
    }


}
