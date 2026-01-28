// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;
import com.pathplanner.lib.util.PathPlannerLogging;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Constants.CLIMBING_SETPOINTS;
import frc.robot.subsystems.Flywheel;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Pivot;
import frc.robot.subsystems.Swerve;
import frc.robot.subsystems.Turret;
import poplib.controllers.io.XboxIO;
import poplib.swerve.commands.TeleopSwerveDrive;


public class RobotContainer {

    Flywheel flywheel = Flywheel.getInstance();
    Swerve swerve = Swerve.getInstance();
    XboxIO controller = XboxIO.getInstance();
    Indexer indexer = Indexer.getInstance();
    Turret turret = Turret.getInstance();
    Pivot pivot = Pivot.getInstance();
    Intake intake = Intake.getInstance();
    private final SendableChooser<Command> autoChooser;
    private final SendableChooser<edu.wpi.first.math.geometry.Translation2d> climbing;



    public RobotContainer() {
        // Configure the trigger bindings
        autoChooser = AutoBuilder.buildAutoChooser();    
        climbing = new SendableChooser<>();

        climbing.setDefaultOption("Idle", CLIMBING_SETPOINTS.IDLE);
        climbing.addOption("L1", CLIMBING_SETPOINTS.L1);
        climbing.addOption("L2", CLIMBING_SETPOINTS.L2);
        climbing.addOption("L3", CLIMBING_SETPOINTS.L3);

        swerve.setDefaultCommand(new TeleopSwerveDrive(swerve, controller));
        NamedCommands.registerCommand("Climb L1", ClimbL1());
        NamedCommands.registerCommand("Climb L2", ClimbL2());
        NamedCommands.registerCommand("Climb L3", ClimbL3());
        NamedCommands.registerCommand("Shoot Fuel", ShootFuel());
        NamedCommands.registerCommand("Collect Fuel", CollectFuel());

        autoChooser.addOption("none", new InstantCommand(() -> {}));
        autoChooser.setDefaultOption("LeftL1", new PathPlannerAuto("LeftL1"));
        autoChooser.addOption("LeftL1Shoot", new PathPlannerAuto("LeftL1Shoot"));
        autoChooser.addOption("LeftL2", new PathPlannerAuto("LeftL2"));
        autoChooser.addOption("LeftL2Shoot", new PathPlannerAuto("LeftL2Shoot"));
        autoChooser.addOption("LeftL3", new PathPlannerAuto("LeftL3"));
        autoChooser.addOption("LeftL3Shoot", new PathPlannerAuto("LeftL3Shoot"));
        autoChooser.addOption("LeftNeutralShoot", new PathPlannerAuto("LeftNeutralShoot"));

        autoChooser.addOption("RightL1", new PathPlannerAuto("RightL1"));
        autoChooser.addOption("RightL1Shoot", new PathPlannerAuto("RightL1Shoot"));
        autoChooser.addOption("RightL2", new PathPlannerAuto("RightL2"));
        autoChooser.addOption("RightL2Shoot", new PathPlannerAuto("RightL2Shoot"));
        autoChooser.addOption("RightL3", new PathPlannerAuto("RightL3"));
        autoChooser.addOption("RightL3Shoot", new PathPlannerAuto("RightL3Shoot"));
        autoChooser.addOption("RightNeutralShoot", new PathPlannerAuto("RightNeutralShoot"));

        autoChooser.addOption("MidL1", new PathPlannerAuto("MidL1"));
        autoChooser.addOption("MidL2", new PathPlannerAuto("MidL2"));
        autoChooser.addOption("MidL3", new PathPlannerAuto("MidL3"));
        autoChooser.addOption("MidL1Shoot", new PathPlannerAuto("MidL1Shoot"));
        autoChooser.addOption("MidL2Shoot", new PathPlannerAuto("MidL2Shoot"));
        autoChooser.addOption("MidL3Shoot", new PathPlannerAuto("MidL3Shoot"));

        SmartDashboard.putData("Climbing level", climbing);
        SmartDashboard.putData("Auto Chooser", autoChooser);

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
        return autoChooser.getSelected();

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
